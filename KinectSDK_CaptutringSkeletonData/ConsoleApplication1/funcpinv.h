/*
	Name:funcpinv.h
	Version:MATLAB Coder version            : 2.6
	Data:C/C++ source code generated on  : 14-Nov-2015 13:49:53
	Description:MATLAB Coder生成函数pinv()用来求广义矩阵的逆，对外提供funcpinv(const double a[6], double b[6]);接口，广义矩阵必
	须是3*2的，最后求得的矩阵为2*3
	Dependence:rt_nonfinite.h、rtwtypes.h、funcpinv_types.h、funcpinv_initialize.h、funcpinv_terminate.h、rtGetInf.h、rtGetNaN.h
*/


#ifndef __FUNCPINV_H__
#define __FUNCPINV_H__

// Include files
#include <math.h>
#include <stddef.h>
#include <stdlib.h>
#include "rt_nonfinite.h"
#include "rtwtypes.h"
#include "funcpinv_types.h"

// Function Declarations
extern void funcpinv(const double a[6], double b[6]);

#endif

//
// File trailer for funcpinv.h
//
// [EOF]
//
