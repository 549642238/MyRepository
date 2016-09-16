#include "math_function.h"

//求向量大小
double MathFunction::val(vect v){
	return sqrt(pow(v.x,2)+pow(v.y,2)+pow(v.z,2));
}

//三维空间点的大小
double MathFunction::val2(double* a){
	double x = sqrt(pow(a[0],2) + pow(a[1],2) + pow(a[2],2));
	return x;
}

//根据转轴和旋转角度求局部旋转矩阵，相对于母连杆的旋转矩阵
double** MathFunction::rodrigues(double* a,double q){
	double** R = (double**)malloc(sizeof(double*)*3);
	double** E = (double**)malloc(sizeof(double*)*3);
	double** A = (double**)malloc(sizeof(double*)*3);
	double** B = (double**)malloc(sizeof(double*)*3);
	double** C = (double**)malloc(sizeof(double*)*3);
	for(int i=0;i<3;i++){
		R[i] = (double*)malloc(sizeof(double)*3);
		E[i] = (double*)malloc(sizeof(double)*3);
		A[i] = (double*)malloc(sizeof(double)*3);
		B[i] = (double*)malloc(sizeof(double)*3);
		C[i] = (double*)malloc(sizeof(double)*3);
	}
	for(int i=0;i<3;i++){
		for(int j=0;j<3;j++){
			R[i][j] = E[i][j] = A[i][j] = B[i][j] = C[i][j] = 0;
		}
	}
	E[0][0] = E[1][1] = E[2][2] = 1;
	A[0][1] = -a[2];
	A[1][0] = a[2];
	A[0][2] = a[1];
	A[2][0] = -a[1];
	A[1][2] = -a[0];
	A[2][1] = a[0];
	for(int i=0;i<3;i++){
		for(int j=0;j<3;j++){
			B[i][j] = A[i][j]*sin(q);
			C[i][j] = (A[i][0]*A[0][j] + A[i][1]*A[1][j] + A[i][2]*A[2][j])*(1 - cos(q));
			R[i][j] = E[i][j] + B[i][j] + C[i][j];
		}
	}
	for(int i=0;i<3;i++){
		free(E[i]);
		free(A[i]);
		free(B[i]);
		free(C[i]);
	}
	free(A);
	free(B);
	free(C);
	free(E);
	return R;
}

//根据欧拉角求旋转矩阵
double** MathFunction::rpy2rot(double x,double y,double z){
	double** R = (double**)malloc(sizeof(double*)*3);
	for(int i=0;i<3;i++){
		R[i] = (double*)malloc(sizeof(double)*3);
	}
	R[0][0] = cos(z)*cos(y);
	R[0][1] = -sin(z)*cos(x)+cos(z)*sin(y)*sin(x);
	R[0][2] = sin(z)*sin(x)+cos(z)*sin(y)*cos(x);
	R[1][0] = sin(z)*cos(y);
	R[1][1] = cos(z)*cos(x)+sin(z)*sin(y)*sin(x);
	R[1][2] = -cos(z)*sin(x)+sin(z)*sin(y)*cos(x);
	R[2][0] = -sin(y);
	R[2][1] = cos(y)*sin(x);
	R[2][2] = cos(y)*cos(x);
	return R;
}

//矩阵转置
double** MathFunction::transposition(double** R,int row,int column){
	double** M = (double**)malloc(sizeof(double*)*column);
	for(int i=0;i<column;i++){
		M[i] = (double*)malloc(sizeof(double)*row);
	}
	for(int i=0;i<column;i++){
		for(int j=0;j<row;j++){
			M[i][j] = R[j][i];
		}
	}
	return M;
}

//计算和目标关节的偏差
double* MathFunction::calcVWerr(Joint Cref,Joint Cnow){
	double* err = (double*)malloc(sizeof(double)*3);
	for(int i=0;i<3;i++){
		err[i] = Cref.p[i] - Cnow.p[i];
	}
	return err;
}

//3维向量叉乘
double* MathFunction::cross(double* a,double* b){
	double* r = (double*)malloc(sizeof(double)*3);
	r[0] = a[1]*b[2] - a[2]*b[1];
	r[1] = a[2]*b[0] - a[0]*b[2];
	r[2] = a[0]*b[1] - a[1]*b[0];
	return r;
}
