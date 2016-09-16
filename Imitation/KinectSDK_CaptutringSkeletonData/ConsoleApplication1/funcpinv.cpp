/*
	Name:funcpinv.cpp
	Version:MATLAB Coder version            : 2.6
	Data:C/C++ source code generated on  : 14-Nov-2015 13:49:53
	Dependence:rt_nonfinite.h、rtwtypes.h、funcpinv_types.h、funcpinv_initialize.h、funcpinv_terminate.h、rtGetInf.h、rtGetNaN.h
*/

#include "rt_nonfinite.h"
#include "funcpinv.h"

// Function Declarations
static void b_eml_xrot(double x[6], int ix0, int iy0, double c, double s);
static void b_eml_xscal(double a, double x[6], int ix0);
static void b_eml_xswap(double x[6], int ix0, int iy0);
static void c_eml_xscal(double a, double x[4], int ix0);
static double eml_div(double x, double y);
static void eml_xaxpy(int n, double a, int ix0, double y[6], int iy0);
static double eml_xdotc(int n, const double x[6], int ix0, const double y[6],
  int iy0);
static void eml_xgesvd(const double A[6], double U[6], double S[2], double V[4]);
static double eml_xnrm2(int n, const double x[6], int ix0);
static void eml_xrot(double x[4], int ix0, int iy0, double c, double s);
static void eml_xrotg(double *a, double *b, double *c, double *s);
static void eml_xscal(int n, double a, double x[6], int ix0);
static void eml_xswap(double x[4], int ix0, int iy0);

// Function Definitions

//
// Arguments    : double x[6]
//                int ix0
//                int iy0
//                double c
//                double s
// Return Type  : void
//
static void b_eml_xrot(double x[6], int ix0, int iy0, double c, double s)
{
  int ix;
  int iy;
  int k;
  double temp;
  ix = ix0 - 1;
  iy = iy0 - 1;
  for (k = 0; k < 3; k++) {
    temp = c * x[ix] + s * x[iy];
    x[iy] = c * x[iy] - s * x[ix];
    x[ix] = temp;
    iy++;
    ix++;
  }
}

//
// Arguments    : double a
//                double x[6]
//                int ix0
// Return Type  : void
//
static void b_eml_xscal(double a, double x[6], int ix0)
{
  int k;
  for (k = ix0; k <= ix0 + 2; k++) {
    x[k - 1] *= a;
  }
}

//
// Arguments    : double x[6]
//                int ix0
//                int iy0
// Return Type  : void
//
static void b_eml_xswap(double x[6], int ix0, int iy0)
{
  int ix;
  int iy;
  int k;
  double temp;
  ix = ix0 - 1;
  iy = iy0 - 1;
  for (k = 0; k < 3; k++) {
    temp = x[ix];
    x[ix] = x[iy];
    x[iy] = temp;
    ix++;
    iy++;
  }
}

//
// Arguments    : double a
//                double x[4]
//                int ix0
// Return Type  : void
//
static void c_eml_xscal(double a, double x[4], int ix0)
{
  int k;
  for (k = ix0; k <= ix0 + 1; k++) {
    x[k - 1] *= a;
  }
}

//
// Arguments    : double x
//                double y
// Return Type  : double
//
static double eml_div(double x, double y)
{
  return x / y;
}

//
// Arguments    : int n
//                double a
//                int ix0
//                double y[6]
//                int iy0
// Return Type  : void
//
static void eml_xaxpy(int n, double a, int ix0, double y[6], int iy0)
{
  int ix;
  int iy;
  int k;
  if ((n < 1) || (a == 0.0)) {
  } else {
    ix = ix0 - 1;
    iy = iy0 - 1;
    for (k = 0; k < n; k++) {
      y[iy] += a * y[ix];
      ix++;
      iy++;
    }
  }
}

//
// Arguments    : int n
//                const double x[6]
//                int ix0
//                const double y[6]
//                int iy0
// Return Type  : double
//
static double eml_xdotc(int n, const double x[6], int ix0, const double y[6],
  int iy0)
{
  double d;
  int ix;
  int iy;
  int k;
  d = 0.0;
  if (n < 1) {
  } else {
    ix = ix0;
    iy = iy0;
    for (k = 1; k <= n; k++) {
      d += x[ix - 1] * y[iy - 1];
      ix++;
      iy++;
    }
  }

  return d;
}

//
// Arguments    : const double A[6]
//                double U[6]
//                double S[2]
//                double V[4]
// Return Type  : void
//
static void eml_xgesvd(const double A[6], double U[6], double S[2], double V[4])
{
  double b_A[6];
  int i;
  double s[2];
  double Vf[4];
  int q;
  int qs;
  double ztest0;
  int m;
  double e[2];
  double rt;
  double ztest;
  int iter;
  double tiny;
  double snorm;
  boolean_T exitg3;
  boolean_T exitg2;
  double f;
  double varargin_1[5];
  double mtmp;
  boolean_T exitg1;
  double sqds;
  for (i = 0; i < 6; i++) {
    b_A[i] = A[i];
  }

  for (i = 0; i < 2; i++) {
    s[i] = 0.0;
  }

  for (i = 0; i < 6; i++) {
    U[i] = 0.0;
  }

  for (i = 0; i < 4; i++) {
    Vf[i] = 0.0;
  }

  for (q = 0; q < 2; q++) {
    qs = q + 3 * q;
    ztest0 = eml_xnrm2(3 - q, b_A, qs + 1);
    if (ztest0 > 0.0) {
      if (b_A[qs] < 0.0) {
        s[q] = -ztest0;
      } else {
        s[q] = ztest0;
      }

      eml_xscal(3 - q, eml_div(1.0, s[q]), b_A, qs + 1);
      b_A[qs]++;
      s[q] = -s[q];
    } else {
      s[q] = 0.0;
    }

    i = q + 2;
    while (i < 3) {
      if (s[q] != 0.0) {
        eml_xaxpy(3 - q, -eml_div(eml_xdotc(3 - q, b_A, qs + 1, b_A, q + 4),
                   b_A[q + 3 * q]), qs + 1, b_A, q + 4);
      }

      i = 3;
    }

    for (i = q; i + 1 < 4; i++) {
      U[i + 3 * q] = b_A[i + 3 * q];
    }
  }

  m = 1;
  e[0] = b_A[3];
  e[1] = 0.0;
  for (q = 1; q > -1; q += -1) {
    qs = q + 3 * q;
    if (s[q] != 0.0) {
      i = q + 2;
      while (i < 3) {
        eml_xaxpy(3 - q, -eml_div(eml_xdotc(3 - q, U, qs + 1, U, q + 4), U[qs]),
                  qs + 1, U, q + 4);
        i = 3;
      }

      for (i = q; i + 1 < 4; i++) {
        U[i + 3 * q] = -U[i + 3 * q];
      }

      U[qs]++;
      i = 1;
      while (i <= q) {
        U[3] = 0.0;
        i = 2;
      }
    } else {
      for (i = 0; i < 3; i++) {
        U[i + 3 * q] = 0.0;
      }

      U[qs] = 1.0;
    }
  }

  for (q = 1; q > -1; q += -1) {
    for (i = 0; i < 2; i++) {
      Vf[i + (q << 1)] = 0.0;
    }

    Vf[q + (q << 1)] = 1.0;
  }

  ztest0 = b_A[3];
  for (q = 0; q < 2; q++) {
    if (s[q] != 0.0) {
      rt = fabs(s[q]);
      ztest = eml_div(s[q], rt);
      s[q] = rt;
      if (q + 1 < 2) {
        ztest0 = eml_div(ztest0, ztest);
      }

      b_eml_xscal(ztest, U, 3 * q + 1);
    }

    if ((q + 1 < 2) && (ztest0 != 0.0)) {
      rt = fabs(ztest0);
      ztest = eml_div(rt, ztest0);
      ztest0 = rt;
      s[1] *= ztest;
      c_eml_xscal(ztest, Vf, 3);
    }

    e[0] = ztest0;
  }

  iter = 0;
  tiny = eml_div(2.2250738585072014E-308, 2.2204460492503131E-16);
  snorm = 0.0;
  for (i = 0; i < 2; i++) {
    ztest0 = fabs(s[i]);
    ztest = fabs(e[i]);
    if ((ztest0 >= ztest) || rtIsNaN(ztest)) {
    } else {
      ztest0 = ztest;
    }

    if ((snorm >= ztest0) || rtIsNaN(ztest0)) {
    } else {
      snorm = ztest0;
    }
  }

  while ((m + 1 > 0) && (!(iter >= 75))) {
    q = m;
    exitg3 = false;
    while (!(exitg3 || (q == 0))) {
      ztest0 = fabs(e[0]);
      if ((ztest0 <= 2.2204460492503131E-16 * (fabs(s[0]) + fabs(s[1]))) ||
          (ztest0 <= tiny) || ((iter > 20) && (ztest0 <= 2.2204460492503131E-16 *
            snorm))) {
        e[0] = 0.0;
        exitg3 = true;
      } else {
        q = 0;
      }
    }

    if (q == m) {
      i = 4;
    } else {
      qs = m + 1;
      i = m + 1;
      exitg2 = false;
      while ((!exitg2) && (i >= q)) {
        qs = i;
        if (i == q) {
          exitg2 = true;
        } else {
          ztest0 = 0.0;
          if (i < m + 1) {
            ztest0 = fabs(e[0]);
          }

          if (i > q + 1) {
            ztest0 += fabs(e[0]);
          }

          ztest = fabs(s[i - 1]);
          if ((ztest <= 2.2204460492503131E-16 * ztest0) || (ztest <= tiny)) {
            s[i - 1] = 0.0;
            exitg2 = true;
          } else {
            i--;
          }
        }
      }

      if (qs == q) {
        i = 3;
      } else if (qs == m + 1) {
        i = 1;
      } else {
        i = 2;
        q = qs;
      }
    }

    switch (i) {
     case 1:
      f = e[0];
      e[0] = 0.0;
      qs = m;
      while (qs >= q + 1) {
        ztest0 = s[0];
        eml_xrotg(&ztest0, &f, &ztest, &rt);
        s[0] = ztest0;
        eml_xrot(Vf, 1, (m << 1) + 1, ztest, rt);
        qs = 0;
      }
      break;

     case 2:
      f = e[q - 1];
      e[q - 1] = 0.0;
      for (qs = q; qs + 1 <= m + 1; qs++) {
        eml_xrotg(&s[qs], &f, &ztest, &rt);
        f = -rt * e[qs];
        e[qs] *= ztest;
        b_eml_xrot(U, 3 * qs + 1, 3 * (q - 1) + 1, ztest, rt);
      }
      break;

     case 3:
      varargin_1[0] = fabs(s[m]);
      varargin_1[1] = fabs(s[m - 1]);
      varargin_1[2] = fabs(e[m - 1]);
      varargin_1[3] = fabs(s[q]);
      varargin_1[4] = fabs(e[q]);
      i = 1;
      mtmp = varargin_1[0];
      if (rtIsNaN(varargin_1[0])) {
        qs = 2;
        exitg1 = false;
        while ((!exitg1) && (qs < 6)) {
          i = qs;
          if (!rtIsNaN(varargin_1[qs - 1])) {
            mtmp = varargin_1[qs - 1];
            exitg1 = true;
          } else {
            qs++;
          }
        }
      }

      if (i < 5) {
        while (i + 1 < 6) {
          if (varargin_1[i] > mtmp) {
            mtmp = varargin_1[i];
          }

          i++;
        }
      }

      f = eml_div(s[m], mtmp);
      ztest0 = eml_div(s[0], mtmp);
      ztest = eml_div(e[0], mtmp);
      sqds = eml_div(s[q], mtmp);
      rt = eml_div((ztest0 + f) * (ztest0 - f) + ztest * ztest, 2.0);
      ztest0 = f * ztest;
      ztest0 *= ztest0;
      ztest = 0.0;
      if ((rt != 0.0) || (ztest0 != 0.0)) {
        ztest = sqrt(rt * rt + ztest0);
        if (rt < 0.0) {
          ztest = -ztest;
        }

        ztest = eml_div(ztest0, rt + ztest);
      }

      f = (sqds + f) * (sqds - f) + ztest;
      ztest0 = sqds * eml_div(e[q], mtmp);
      while (q + 1 <= m) {
        eml_xrotg(&f, &ztest0, &ztest, &rt);
        f = ztest * s[0] + rt * e[0];
        e[0] = ztest * e[0] - rt * s[0];
        ztest0 = rt * s[1];
        s[1] *= ztest;
        eml_xrot(Vf, 1, 3, ztest, rt);
        s[0] = f;
        eml_xrotg(&s[0], &ztest0, &ztest, &rt);
        f = ztest * e[0] + rt * s[1];
        s[1] = -rt * e[0] + ztest * s[1];
        ztest0 = rt * e[1];
        e[1] *= ztest;
        b_eml_xrot(U, 1, 4, ztest, rt);
        q = 1;
      }

      e[0] = f;
      iter++;
      break;

     default:
      if (s[q] < 0.0) {
        s[q] = -s[q];
        c_eml_xscal(-1.0, Vf, (q << 1) + 1);
      }

      while ((q + 1 < 2) && (s[0] < s[1])) {
        rt = s[0];
        s[0] = s[1];
        s[1] = rt;
        eml_xswap(Vf, 1, 3);
        b_eml_xswap(U, 1, 4);
        q = 1;
      }

      iter = 0;
      m--;
      break;
    }
  }

  for (qs = 0; qs < 2; qs++) {
    S[qs] = s[qs];
    for (i = 0; i < 2; i++) {
      V[i + (qs << 1)] = Vf[i + (qs << 1)];
    }
  }
}

//
// Arguments    : int n
//                const double x[6]
//                int ix0
// Return Type  : double
//
static double eml_xnrm2(int n, const double x[6], int ix0)
{
  double y;
  double scale;
  int kend;
  int k;
  double absxk;
  double t;
  y = 0.0;
  if (n < 1) {
  } else if (n == 1) {
    y = fabs(x[ix0 - 1]);
  } else {
    scale = 2.2250738585072014E-308;
    kend = (ix0 + n) - 1;
    for (k = ix0; k <= kend; k++) {
      absxk = fabs(x[k - 1]);
      if (absxk > scale) {
        t = scale / absxk;
        y = 1.0 + y * t * t;
        scale = absxk;
      } else {
        t = absxk / scale;
        y += t * t;
      }
    }

    y = scale * sqrt(y);
  }

  return y;
}

//
// Arguments    : double x[4]
//                int ix0
//                int iy0
//                double c
//                double s
// Return Type  : void
//
static void eml_xrot(double x[4], int ix0, int iy0, double c, double s)
{
  int ix;
  int iy;
  int k;
  double temp;
  ix = ix0 - 1;
  iy = iy0 - 1;
  for (k = 0; k < 2; k++) {
    temp = c * x[ix] + s * x[iy];
    x[iy] = c * x[iy] - s * x[ix];
    x[ix] = temp;
    iy++;
    ix++;
  }
}

//
// Arguments    : double *a
//                double *b
//                double *c
//                double *s
// Return Type  : void
//
static void eml_xrotg(double *a, double *b, double *c, double *s)
{
  double roe;
  double absa;
  double absb;
  double scale;
  double ads;
  double bds;
  roe = *b;
  absa = fabs(*a);
  absb = fabs(*b);
  if (absa > absb) {
    roe = *a;
  }

  scale = absa + absb;
  if (scale == 0.0) {
    *s = 0.0;
    *c = 1.0;
    ads = 0.0;
    scale = 0.0;
  } else {
    ads = absa / scale;
    bds = absb / scale;
    ads = scale * sqrt(ads * ads + bds * bds);
    if (roe < 0.0) {
      ads = -ads;
    }

    *c = *a / ads;
    *s = *b / ads;
    if (absa > absb) {
      scale = *s;
    } else if (*c != 0.0) {
      scale = 1.0 / *c;
    } else {
      scale = 1.0;
    }
  }

  *a = ads;
  *b = scale;
}

//
// Arguments    : int n
//                double a
//                double x[6]
//                int ix0
// Return Type  : void
//
static void eml_xscal(int n, double a, double x[6], int ix0)
{
  int i1;
  int k;
  i1 = (ix0 + n) - 1;
  for (k = ix0; k <= i1; k++) {
    x[k - 1] *= a;
  }
}

//
// Arguments    : double x[4]
//                int ix0
//                int iy0
// Return Type  : void
//
static void eml_xswap(double x[4], int ix0, int iy0)
{
  int ix;
  int iy;
  int k;
  double temp;
  ix = ix0 - 1;
  iy = iy0 - 1;
  for (k = 0; k < 2; k++) {
    temp = x[ix];
    x[ix] = x[iy];
    x[iy] = temp;
    ix++;
    iy++;
  }
}

//
// Arguments    : const double a[6]
//                double b[6]
// Return Type  : void
//
void funcpinv(const double a[6], double b[6])
{
  double X[6];
  int i0;
  double b_a[6];
  int k;
  double V[4];
  double s[2];
  double U[6];
  double S[4];
  double tol;
  int r;
  int vcol;
  int ar;
  int ic;
  int ib;
  int ia;
  for (i0 = 0; i0 < 6; i0++) {
    X[i0] = 0.0;
  }

  for (i0 = 0; i0 < 2; i0++) {
    for (k = 0; k < 3; k++) {
      b_a[k + 3 * i0] = a[i0 + (k << 1)];
    }
  }

  eml_xgesvd(b_a, U, s, V);
  for (i0 = 0; i0 < 4; i0++) {
    S[i0] = 0.0;
  }

  for (k = 0; k < 2; k++) {
    S[k + (k << 1)] = s[k];
  }

  tol = 3.0 * S[0] * 2.2204460492503131E-16;
  r = 0;
  k = 0;
  while ((k + 1 < 3) && (S[k + (k << 1)] > tol)) {
    r++;
    k++;
  }

  if (r > 0) {
    vcol = 0;
    for (ar = 0; ar + 1 <= r; ar++) {
      tol = 1.0 / S[ar + (ar << 1)];
      for (k = vcol; k + 1 <= vcol + 2; k++) {
        V[k] *= tol;
      }

      vcol += 2;
    }

    for (k = 0; k < 6; k += 2) {
      for (ic = k; ic + 1 <= k + 2; ic++) {
        X[ic] = 0.0;
      }
    }

    vcol = -1;
    for (k = 0; k < 6; k += 2) {
      ar = 0;
      vcol++;
      i0 = (vcol + 3 * (r - 1)) + 1;
      for (ib = vcol; ib + 1 <= i0; ib += 3) {
        if (U[ib] != 0.0) {
          ia = ar;
          for (ic = k; ic + 1 <= k + 2; ic++) {
            ia++;
            X[ic] += U[ib] * V[ia - 1];
          }
        }

        ar += 2;
      }
    }
  }

  for (i0 = 0; i0 < 2; i0++) {
    for (k = 0; k < 3; k++) {
      b[k + 3 * i0] = X[i0 + (k << 1)];
    }
  }
}

//
// File trailer for funcpinv.cpp
//
// [EOF]
//
