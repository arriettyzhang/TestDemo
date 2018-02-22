/******************************************************************************* 
** 程序名称：快速傅里叶变换(FFT)  
** 程序描述：本程序实现快速傅里叶变换  
*******************************************************************************/  

typedef double ElemType;    //原始数据序列的数据类型,可以在这里设置  
  
typedef struct              //定义复数结构体   
{  
    ElemType real,imag;  
}complex;  

//变址   
void ChangeSeat(complex *DataInput, int N);

//复数乘法   
complex XX_complex(complex a, complex b);
  
//FFT  
void FFT(complex *data_in, complex *data_out, int N);

//IFFT
void IFFT(complex *data_in, complex *data_out, int N);
