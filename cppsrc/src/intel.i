/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * SWIG interface file for plugin exports
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

%inline
%{
	typedef const regval_t &(idaapi *pc_get_operand_info_getreg_cb_t)(const char *name, const regval_t *regvalues);
%}
%ignore getreg_t;
//bool pc_get_operand_info(ea_t ea, int n, int tid, pc_get_operand_info_getreg_cb_t getreg, const regval_t *regvalues, idd_opinfo_t *opinf);
%ignore pc_get_operand_info;

%ignore getr;

%javaconstvalue(1000) pc_module_t::set_difbase; // Fix SWIG wrapping failure


//These didn't seem to be getting pulled out properly, even in after the above fixes
enum RegNo
{
  R_none = -1,
  R_ax = 0,
  R_cx = 1,
  R_dx = 2,
  R_bx = 3,
  R_sp = 4,
  R_bp = 5,
  R_si = 6,
  R_di = 7,
  R_r8 = 8,
  R_r9 = 9,
  R_r10 = 10,
  R_r11 = 11,
  R_r12 = 12,
  R_r13 = 13,
  R_r14 = 14,
  R_r15 = 15,

  R_al = 16,
  R_cl = 17,
  R_dl = 18,
  R_bl = 19,
  R_ah = 20,
  R_ch = 21,
  R_dh = 22,
  R_bh = 23,

  R_spl = 24,
  R_bpl = 25,
  R_sil = 26,
  R_dil = 27,

  R_ip = 28,

  R_es = 29,    // 0
  R_cs = 30,    // 1
  R_ss = 31,    // 2
  R_ds = 32,    // 3
  R_fs = 33,
  R_gs = 34,

  R_cf = 35,    // main cc's
  R_zf = 36,
  R_sf = 37,
  R_of = 38,

  R_pf = 39,    // additional cc's
  R_af = 40,
  R_tf = 41,
  R_if = 42,
  R_df = 43,

  R_efl = 44,   // eflags

  // the following registers will be used in the disassembly
  // starting from ida v5.7

  R_st0 = 45,   // floating point registers (not used in disassembly)
  R_st1 = 46,
  R_st2 = 47,
  R_st3 = 48,
  R_st4 = 49,
  R_st5 = 50,
  R_st6 = 51,
  R_st7 = 52,
  R_fpctrl = 53,// fpu control register
  R_fpstat = 54,// fpu status register
  R_fptags = 55,// fpu tags register

  R_mm0 = 56,   // mmx registers (not used in disassembly)
  R_mm1 = 57,
  R_mm2 = 58,
  R_mm3 = 59,
  R_mm4 = 60,
  R_mm5 = 61,
  R_mm6 = 62,
  R_mm7 = 63,

  R_xmm0 = 64,  // xmm registers (not used in disassembly)
  R_xmm1 = 65,
  R_xmm2 = 66,
  R_xmm3 = 67,
  R_xmm4 = 68,
  R_xmm5 = 69,
  R_xmm6 = 70,
  R_xmm7 = 71,
  R_xmm8 = 72,
  R_xmm9 = 73,
  R_xmm10 = 74,
  R_xmm11 = 75,
  R_xmm12 = 76,
  R_xmm13 = 77,
  R_xmm14 = 78,
  R_xmm15 = 79,
  R_mxcsr = 80,
};

inline bool is_segreg(int r) { return r >= R_es && r <= R_gs; }
inline bool is_mmxreg(int r) { return r >= R_mm0 && r <= R_mm7; }
inline bool is_xmmreg(int r) { return r >= R_xmm0 && r <= R_xmm15; }

#define sib             specflag2

inline RegNo sib_base(const op_t &x)                    // get extended sib base
{
  RegNo base = x.sib & 7;
  if ( cmd.rex & REX_B )
    base |= 8;
  return base;
}

inline RegNo sib_index(const op_t &x)                   // get extended sib index
{
  RegNo index = RegNo((x.sib >> 3) & 7);
  if ( cmd.rex & REX_X )
    index |= 8;
  return index;
}

inline int sib_scale(const op_t &x)
{
  int scale = (x.sib >> 6) & 3;
  return scale;
}

%include <intel.hpp>
