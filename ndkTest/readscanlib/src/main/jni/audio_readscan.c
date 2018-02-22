/******************************************************************************* 
** 程序名称：计算给定声音文件的幅频响应曲线值
** 程序描述：
** 1. 寻找扫频信号的起始点和结束点
** 		1.1 计算扫频信号的能量。
** 		1.2 峰值二分法确定扫频信号的能量集中范围。
** 		1.3 从结束点朝前，一定时间确认起始点。
** 2. 取每一段信号的中间时间，计算每一段信号的频率响应，计算8192个点的FFT。
** 3. 给出频率响应图，保存到csv的文档当中。
**
** 作者：章浩
** 邮件：harryhao.zhang@oceanwing.com
**
*******************************************************************************/
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "audio_fft.h"
#include "audio_readscan.h"

//扫描频率个数
#define SCAN_N		20
//扫描时间
#define SCAN_T		20

//计算能量点数
#define SUM_N		1024
//计算fft点数
#define N			8192

int select_eq(char * file_path)
{
    int fd = 0;
	int out_fd = 0;

    short *start;
    struct stat st;

	int i = 0;
	int j = 0;
	int ii = 0;

	int fs = 0;
	int sampledata_i = 0;
	int sampledata_n = 0;

	int *sum_xx = NULL;
	int sum_xx_max_i = 0;
	int sum_xx_max = 0;

	int sum_min = 0;
	int sum_max_k = 0;

	int start_ii = 0;
	int stop_ii = 0;


	//SCAN_N*scan_m = SCAN_T*fs = 总时长的点数
	//SCAN_N 表示扫描频率的个数
	//scan_m 每个频率的点数
	//SCAN_T 扫频频率的时间
	//fs	 采样频率
	int scan_m = 0;


	complex x[N] = {{0,0}};
	complex y[N] = {{0,0}};



	int ret = 0;

	//int fc_center[SCAN_N] = {
	//50,   63,   80,   100,   126,     160,   201,   253,   320,   403,
	//507,  640,  806, 1015,  1280,    1612,  2031,  2560,  3225,  4063};

	//8192 5.38
	int fn_center[SCAN_N] = {
	10,   13,   16,   20,    25,       31,    38,    48,    60,    76,
	95,  120,  151,  190,   239,      300,   378,   477,   600,   756};

	double y_max[SCAN_N]={0};
	double y_max_temp[SCAN_N]={0};

	int y_max_i[SCAN_N] = {0};
	int y_max_i_temp[SCAN_N] = {0};

	//打开文件
	//"res/d/id1_80.wav"
    fd = open(file_path, O_RDONLY);

	//取得文件大小
    fstat(fd, &st);

	printf("%08x\r\n", fd);
	printf("st.st_size = %d\r\n", (int)st.st_size/4);

    start = mmap(NULL, st.st_size, PROT_READ, MAP_PRIVATE, fd, 0);

	//隐射是否成功
    if(start == MAP_FAILED)
	{
		printf("%s", "failed\r\n");
        return -1;
	}

	fs = (unsigned short)start[12] + ((unsigned short)start[13]<<16);
	printf("fs = %d\r\n", fs);
	//printf("fs = %04x %04x %04x %04x %04x %04x\r\n", start[10], start[11], (unsigned short)start[12], start[13], start[14], start[15]);

	//j=0;
	//for(i = 23; i<st.st_size; i+=2)
	//{
	//	if((j>500000)&&(j<500128))
	//	{
	//		printf("%06d ", start[i]);
	//		if(j%16 == 15)
	//			printf("\r\n");
	//		if(j%1024 == 1023)
	//			printf("\r\n");
	//	}
	//	j++;
	//
	//}


	//计算真实的信号长度
	sampledata_i = st.st_size/4-11;
	printf("total data = %d\r\n", sampledata_i);



	//1. 寻找扫频信号的起始点和结束点
	//1.1 计算扫频信号的能量。
	//1.2 峰值二分法确定扫频信号的能量集中范围。
	//1.3 从结束点朝前，一定时间确认起始点。
	//2. 计算每一段信号的频率响应，计算8192个点的FFT。
	//3. 给出频率响应图，保存到csv的文档当中。

	//1.1 计算扫频信号的能量。
	sampledata_n = sampledata_i/SUM_N;

	sum_xx = (int *)malloc(sampledata_n*sizeof(int));

	if(sum_xx == NULL)
	{
		printf("%s:malloc error!\r\n", __func__);
	}

	printf("sampledata_n = %d start i j:\r\n", sampledata_n);
	for(i=0; i<sampledata_n; i++)
	{

		sum_xx[i] = 0;
		for(j=0; j<SUM_N; j++)
		{
			sum_xx[i] = sum_xx[i] + start[(i*SUM_N + j)*2 + 23]*start[(i*SUM_N + j)*2 + 23]/32768;
		}

	}

	//printf("sum_xx:\r\n");
	//for(i=0; i<sampledata_n; i++)
	//{
	//	printf("%6d ", sum_xx[i]);
	//	if(j%16 == 15)
	//		printf("\r\n");
	//}
	//printf("\r\n");


	//1.2 峰值二分法确定扫频信号的能量集中范围。
	j=0;
	for(i=1; i<sampledata_n; i++)
	{
		if(sum_xx[i]>sum_xx[j])
			j = i;
	}
	sum_xx_max_i = j;
	sum_xx_max = sum_xx[j];
	printf("sum_xx_max_i = %d sum_xx_max = %d\r\n", sum_xx_max_i, sum_xx_max);


	sum_min = 0;
	sum_max_k = sum_xx_max/2;
	sum_min = sum_min + sum_max_k;

	for(j=0; j<16; j++)
	{
		//找起始位
		for(i=0; i<sampledata_n; i++)
		{
			if(sum_xx[i] > sum_min)
			{
				start_ii = i;
				break;
			}
		}

		//找结束位
		//结束位很重要，去掉结尾的脉冲影响，确保找到正确的结束位
		for(i=1; i<sampledata_n-1; i++)
		{
			if(sum_xx[sampledata_n - i - 1] > sum_min)
			{
				if((sum_xx[sampledata_n - i] > sum_min)
				&&(sum_xx[sampledata_n - i - 2] > sum_min))
				{
					stop_ii = sampledata_n - i - 1;
					break;
				}
			}
		}

		//减小步长
		sum_max_k = sum_max_k/2;

		//2分查找合适的峰值
		if(((stop_ii - start_ii) >(9*SCAN_T*fs/SUM_N/10))
			&&((stop_ii - start_ii) <=(SCAN_T*fs/SUM_N))
			&&(stop_ii >(SCAN_T*fs/SUM_N)))
		{
			break;
		}
		else if((stop_ii - start_ii) <(9*SCAN_T*fs/SUM_N/10))
		{
			sum_min = sum_min - sum_max_k;
		}
		else if((stop_ii - start_ii) >(SCAN_T*fs/SUM_N))
		{
			sum_min = sum_min + sum_max_k;
		}
		else if(stop_ii <=(SCAN_T*fs/SUM_N))
		{
			ret = -1;
			goto ret_error;
		}

	}
	start_ii = stop_ii - SCAN_T*fs/SUM_N;

	//包含起始位，以及之前的数，都不要
	printf("SCAN_T*fs/SUM_N = %d\r\n", SCAN_T*fs/SUM_N);
	printf("start_ii = %d stop_ii = %d\r\n", start_ii, stop_ii);
	printf("sum_min = %d sum_max_k = %d\r\n", sum_min, sum_max_k);

	//开始取计算fft的信号
	scan_m = SCAN_T*fs/SCAN_N;

	//int start_spot = 0;
	//start_spot = 0*scan_m + scan_m/2 + (start_ii+1) * SUM_N;
	//printf("start spot %d\r\n", start_spot);
	//
	//j=0;
	//for(i = 23; i<st.st_size; i+=2)
	//{
	//	if((j>=start_spot)&&(j<start_spot+128))
	//	{
	//		printf("%06d ", start[i]);
	//		if((j-start_spot)%16 == 15)
	//			printf("\r\n");
	//		if((j-start_spot)%1024 == 127)
	//			printf("\r\n");
	//	}
	//	j++;
	//
	//}


	//计算不同的频率的fft值
	for(ii=0; ii<SCAN_N; ii++)
	{
		//取SCAN_N段数据，每段N个数据进行fft
		for(j=0; j<N; j++)
		{
			x[j].real = start[(ii*scan_m + scan_m/2 + (start_ii+1) * SUM_N + j)*2+23];
			//x[j].real = start[(start_spot + ii*scan_m + j)*2+23];
			x[j].imag = 0;

			//if((ii == 0)&&(j<128))
			//{
			//	printf("%06f ", x[j].real);
			//	//printf("%06d ", start[(start_spot + ii*scan_m + j)*2+23]);
			//	if(j%16 == 15)
			//		printf("\r\n");
			//	if(j%1024 == 127)
			//		printf("\r\n");
			//}
		}

		//计算fft
		FFT(x, y, N);

		//for(j=0; j<N; j++)
		//{
		//
		//	if((ii == 0)&&(j<128))
		//	{
		//		printf("%06f ", y[j].real);
		//		//printf("%06d ", start[(start_spot + ii*scan_m + j)*2+23]);
		//		if(j%16 == 15)
		//			printf("\r\n");
		//		if(j%1024 == 127)
		//			printf("\r\n");
		//	}
		//
		//}
		j=0;
		y_max_i[ii] = fn_center[ii]-1;
		if(ii >0)
		{
			y_max_i_temp[ii] = fn_center[ii-1]-1;
		}
		else
		{
			y_max_i_temp[ii] = fn_center[ii]-1;
		}

		i = y_max_i[ii];
		y_max[ii] = 10*log10(y[i].real*y[i].real + y[i].imag*y[i].imag) - 90.309;

		//当结束点很小，找到的结束点提前时使用
		i = y_max_i_temp[ii];
		y_max_temp[ii] = 10*log10(y[i].real*y[i].real + y[i].imag*y[i].imag) - 90.309;

		//if(ii == 0)
		//{
				//20*log10(32768) = 90.309
				//printf("y_max[%d]:\r\n", ii);
				//printf("y_max[%d = %d] = %f \r\n",ii, y_max_i[ii], y_max[ii]);
				//printf("y_max_temp[%d = %d] = %f \r\n",ii,y_max_i_temp[ii], y_max_temp[ii]);
				//printf("%f + i%f\r\n", y[i].real, y[i].imag);
		//}

	}

	if(y_max_temp[SCAN_N-1]>y_max[SCAN_N-1]+3)
	{
		for(i=0; i<SCAN_N-1; i++)
		{
			y_max[i] = y_max_temp[i+1];
		}
		//最后一个值，给了假值，没去计算
		y_max[SCAN_N-1] = y_max_temp[SCAN_N-1];
	}

	char add_y_max[256]={0};
	if(access("res/out", 0))
		mkdir("res/out", 0777);

	out_fd = open("res/out/a.csv", O_RDWR|O_CREAT, S_IRUSR|S_IWUSR);

	if(out_fd == 0)
	{
		printf("open a.csv error!\r\n");
	}

	for(i=0; i<SCAN_N-1; i++)
	{
		sprintf(add_y_max, "%s%.3f,", add_y_max,y_max[i]);
	}
	sprintf(add_y_max, "%s%.3f\r\n",add_y_max, y_max[SCAN_N-1]);

	lseek(out_fd, 0, SEEK_END);

	write(out_fd, add_y_max, strlen(add_y_max));

	printf("%s", add_y_max);


	close(out_fd);






ret_error:

	//释放内存
	free(sum_xx);

	//解除映射
	munmap(start, st.st_size);

	//关闭文件
	close(fd);
	
	return ret;
}





