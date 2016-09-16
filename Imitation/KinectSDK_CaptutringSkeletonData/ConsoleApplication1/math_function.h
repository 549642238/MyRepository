/*
	Name:math_function.h
	Data:2016.3.18 19:09
	Author:czh
	Description:提供基本的数学函数(求旋转矩阵，矩阵旋转、转置和叉乘以及向量大小)
	Dependence:definition.h
	Interface:对外提供val、val2、Rodrigues、rpy2rot、transposition、calcVWerr、cross接口
*/

#ifndef _MATH_FUNCTION_H_
#define _MATH_FUNCTION_H_

#include "definition.h"

class MathFunction{
public:
	MathFunction(){}
	static double val(vect v);
	static double** rodrigues(double* a,double q);
	static double** rpy2rot(double x,double y,double z);
	static double** transposition(double** R,int row,int column);
	static double val2(double* a);
	static double* calcVWerr(Joint Cref,Joint Cnow);
	double* cross(double* a,double* b);
};

#endif