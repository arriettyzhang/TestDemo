/******************************************************************************* 
** 程序名称：快速傅里叶变换(FFT)  
** 程序描述：本程序实现快速傅里叶变换  
*******************************************************************************/  
#include <stdio.h>  
#include <math.h>  
#include "audio_fft.h"
  
#define PI 3.141592653589   //圆周率，12位小数   
//#define N 8                 //傅里叶变换的点数   
//#define M 3                 //蝶形运算的级数，N = 2^M  

#define MAX_N 8192			//最大处理fft的值，内存buf最大值

typedef double ElemType;    //原始数据序列的数据类型,可以在这里设置  
  
// typedef struct              //定义复数结构体   
// {  
    // ElemType real,imag;  
// }complex;

//extern complex data[N];            //定义存储单元，原始数据与负数结果均使用之   
//extern ElemType result[N];         //存储FFT后复数结果的模  
  
//变址
void ChangeSeat(complex *DataInput, int N)  
{  
    int nextValue,nextM,i,k,j=0;  
    complex temp;  
      
    nextValue=N/2;                  //变址运算，即把自然顺序变成倒位序，采用雷德算法  
    nextM=N-1;  
    for (i=0;i<nextM;i++)  
    {  
        if (i<j)                 //如果i<j,即进行变址  
        {  
            temp=DataInput[j];  
            DataInput[j]=DataInput[i];  
            DataInput[i]=temp;  
        }  
        k=nextValue;                //求j的下一个倒位序  
        while (k<=j)             //如果k<=j,表示j的最高位为1  
        {  
            j=j-k;                  //把最高位变成0  
            k=k/2;                  //k/2，比较次高位，依次类推，逐个比较，直到某个位为0  
        }  
        j=j+k;                      //把0改为1  
    }
}
/* 
//变址  
void ChangeSeat(complex *DataInput, int N) 
{ 
    complex Temp[N]; 
    int i,n,New_seat; 
    for(i=0; i<N; i++) 
    { 
        Temp[i].real = DataInput[i].real; 
        Temp[i].imag = DataInput[i].imag; 
    } 
    for(i=0; i<N; i++) 
    { 
        New_seat = 0; 
        for(n=0;n<M;n++) 
        { 
            New_seat = New_seat | (((i>>n) & 0x01) << (M-n-1)); 
        } 
        DataInput[New_seat].real = Temp[i].real; 
        DataInput[New_seat].imag = Temp[i].imag; 
    } 
} 
*/  
//复数乘法   
complex XX_complex(complex a, complex b)  
{  
    complex temp;  
      
    temp.real = a.real * b.real-a.imag*b.imag;  
    temp.imag = b.imag*a.real + a.imag*b.real;  
      
    return temp;  
}  
  
//FFT  
void FFT(complex *data_in, complex *data_out, int N) 
{  
    int L=0,B=0,J=0,K=0;  
    int step=0;  
	int M = 0;
	int i = 0;
	int j = 0;
	complex data[MAX_N] = {{0,0}};
	for(i=0; i<N; i++)
	{
		data[i].real = data_in[i].real;
		data[i].imag = data_in[i].imag;
	}
	
	for(i=N; i>=2; i/=2)
	{
		j++;
	}
	M = j;
	//printf("FFT N = %d M = %d\r\n", N, M);
	
    ElemType P=0;
    complex W,Temp_XX;  
    //ElemType TempResult[N];  
      
    ChangeSeat(data, N);  
    for(L=1; L<=M; L++)  
    {  
        B = 1<<(L-1);//B=2^(L-1)  
        for(J=0; J<=B-1; J++)  
        {  
            P = (1<<(M-L))*J;//P=2^(M-L) *J   
            step = 1<<L;//2^L  
            for(K=J; K<=N-1; K=K+step)  
            {  
                W.real =  cos(2*PI*P/N);  
                W.imag = -sin(2*PI*P/N);  
                  
                Temp_XX = XX_complex(data[K+B],W);  
                data[K+B].real = data[K].real - Temp_XX.real;  
                data[K+B].imag = data[K].imag - Temp_XX.imag;  
                  
                data[K].real = data[K].real + Temp_XX.real;  
                data[K].imag = data[K].imag + Temp_XX.imag;  
            }  
        }  
    }  
	
	
	for(i=0; i<N; i++)
	{
		data_out[i].real = data[i].real;
		data_out[i].imag = data[i].imag;
	}
	
	return;
}

void IFFT(complex *data_in, complex *data_out, int N)
{  
    int L=0,B=0,J=0,K=0;  
    int step=0;  
    ElemType P=0;
    complex W,Temp_XX;  
    //ElemType TempResult[N]; 
	
    int M = 0;
	int i = 0;
	int j = 0;
	complex data[MAX_N] = {{0,0}};
	for(i=0; i<N; i++)
	{
		data[i].real = data_in[i].real;
		data[i].imag = data_in[i].imag;
	}
	for(i=N; i>=2; i/=2)
	{
		j++;
	}
	M = j;
	printf("IFFT M = %d\r\n", M);
	
	
	
	
    ChangeSeat(data, N);  
    for(L=1; L<=M; L++)  
    {  
        B = 1<<(L-1);//B=2^(L-1)  
        for(J=0; J<=B-1; J++)  
        {  
            P = (1<<(M-L))*J;//P=2^(M-L) *J   
            step = 1<<L;//2^L  
            for(K=J; K<=N-1; K=K+step)  
            {  
                W.real =  cos(2*PI*P/N);  
                W.imag =  sin(2*PI*P/N);//逆运算，这里跟FFT符号相反   
                  
                Temp_XX = XX_complex(data[K+B],W);  
                data[K+B].real = data[K].real - Temp_XX.real;  
                data[K+B].imag = data[K].imag - Temp_XX.imag;  
                  
                data[K].real = data[K].real + Temp_XX.real;  
                data[K].imag = data[K].imag + Temp_XX.imag;  
            }  
        }  
    }  
	
	
	for(i=0; i<N; i++)
	{
		data_out[i].real = data[i].real;
		data_out[i].imag = data[i].imag;
	}
	
	
	return;
}

// int main(int argc, char *argv[])  
// {  
    // int i = 0;  
    // for(i=0; i<N; i++)//制造输入序列   
    // {  
        // data[i].real = sin(2*PI*i/N);  
        // printf("%lf ",data[i].real);  
    // }  
    // printf("\n\n");  
	
      
    // FFT();//进行FFT计算   
    // printf("\n\n");  
    // for(i=0; i<N; i++)  
        // {printf("%lf ",sqrt(data[i].real*data[i].real+data[i].imag*data[i].imag));}  
      
    // IFFT();//进行FFT计算   
    // printf("\n\n");  
    // for(i=0; i<N; i++)  
        // {printf("%lf ",data[i].real/N);}  
    // printf("\n");  
    // /*for(i=0; i<N; i++) 
        // {printf("%lf ",data[i].imag/N);} 
    // printf("\n");*/  
    // /*for(i=0; i<N; i++) 
        // {printf("%lf ",sqrt(data[i].real*data[i].real+data[i].imag*data[i].imag)/N);}*/  
	
	
	
	
	
	
	
	
	
	
    // return 0;  
// }  