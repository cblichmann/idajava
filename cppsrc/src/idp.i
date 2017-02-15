/*
 * IdaJava version 0.3
 * Copyright (c)2007-2017 by Christian Blichmann
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

%ignore AbstractRegister; // Deprecated
%ignore WorkReg; // Deprecated
%ignore rginfo;
%ignore IDPOPT_STR;
%ignore IDPOPT_NUM;
%ignore IDPOPT_BIT;
%ignore IDPOPT_FLT;
%ignore IDPOPT_OK;
%ignore IDPOPT_BADKEY;
%ignore IDPOPT_BADTYPE;
%ignore IDPOPT_BADVALUE;
%ignore set_options_t; // Not exported in ida.lib
%ignore read_user_config_file; // Not exported in ida.lib
%ignore s_preline;
%ignore ca_operation_t;
%ignore _chkarg_cmd;
%ignore ENUM_SIZE;

%immutable instruc_t::name; // Avoid memory leak (warning 451)

%inline
%{
	typedef bool (idaapi* asm_t_checkarg_dispatch_cb)(void *a1, void *a2, uchar cmd);
	typedef void (idaapi *asm_t_func_cb)(func_t *);
	typedef ssize_t (idaapi *asm_t_get_type_name_cb)(flags_t flag, ea_t ea_or_id, char *buf, size_t bufsize);
%}
struct asm_t
{
	uint32 flag;
	uint16 uflag;
%immutable; // Avoid memory leak (warning 451)
	const char *name;
%mutable;
	help_t help;
%immutable;
	const char **header;
	const uint16 *badworks;
	const char *origin;
	const char *end;
	const char *cmnt;
%mutable;
	char ascsep;
	char accsep;
%immutable; // Avoid memory leak (warning 451)
	const char *esccodes;
	const char *a_ascii;
	const char *a_byte;
	const char *a_word;
	const char *a_dword;
	const char *a_qword;
	const char *a_oword;
	const char *a_float;
	const char *a_double;
	const char *a_tbyte;
	const char *a_packreal;
	const char *a_dups;
	const char *a_bss;
	const char *a_equ;
	const char *a_seg;
%mutable;
	asm_t_checkarg_dispatch_cb checkarg_dispatch;
%immutable; // Avoid memory leak (warning 451)
	void *_UNUSED1_was_atomprefix;
	void *_UNUSED2_was_checkarg_operations;
	const uchar *XlatAsciiOutput;
	const char *a_curip;
%mutable;
	asm_t_func_cb func_header;
	asm_t_func_cb func_footer;
%immutable; // Avoid memory leak (warning 451)
	const char *a_public;
	const char *a_weak;
	const char *a_extrn;
	const char *a_comdef;
%mutable;
	asm_t_get_type_name_cb get_type_name;
%immutable; // Avoid memory leak (warning 451)
	const char *a_align;
%mutable;
	char lbrace;
	char rbrace;
%immutable; // Avoid memory leak (warning 451)
	const char *a_mod;
	const char *a_band;
	const char *a_bor;
	const char *a_xor;
	const char *a_bnot;
	const char *a_shl;
	const char *a_shr;
	const char *a_sizeof_fmt;
%mutable;
	uint32 flag2;
%immutable; // Avoid memory leak (warning 451)
	const char *cmnt2;
	const char *low8;
	const char *high8;
	const char *low16;
	const char *high16;
	const char *a_include_fmt;
	const char *a_vstruc_fmt;
	const char *a_3byte;
	const char *a_rva;
};
%ignore asm_t;

%ignore processor_t::notify;
%ignore processor_t::header;
%ignore processor_t::footer;
%ignore processor_t::segstart;
%ignore processor_t::segend;
%ignore processor_t::assumes;
%ignore processor_t::u_ana;
%ignore processor_t::u_emu;
%ignore processor_t::u_out;
%ignore processor_t::u_outop;
%ignore processor_t::d_out;
%ignore processor_t::cmp_opnd;
%ignore processor_t::can_have_type;
%ignore processor_t::getreg;
%ignore processor_t::is_far_jump;
%ignore processor_t::translate;
%ignore processor_t::realcvt;
%ignore processor_t::is_switch;
%ignore processor_t::gen_map_file;
%ignore processor_t::extract_address;
%ignore processor_t::is_sp_based;
%ignore processor_t::create_func_frame;
%ignore processor_t::get_frame_retsize;
%ignore processor_t::gen_stkvar_def;
%ignore processor_t::u_outspec;
%ignore processor_t::is_align_insn;

%ignore processor_t::idp_notify;
%ignore processor_t::notify;
%ignore processor_t::set_idp_options;

%ignore free_processor_module;
%ignore read_config_file;

%ignore gen_idb_event;

%javaconstvalue(0x00000001) AS_OFFST; // Fix constant type
%javaconstvalue(0x00000002) AS_COLON; // Fix constant type
%javaconstvalue(0x00000004) AS_UDATA; // Fix constant type
%javaconstvalue(0x00000008) AS_2CHRE; // Fix constant type
%javaconstvalue(0x00000010) AS_NCHRE; // Fix constant type
%javaconstvalue(0x00000020) AS_N2CHR; // Fix constant type
%javaconstvalue(0x00000040) AS_1TEXT; // Fix constant type
%javaconstvalue(0x00000080) AS_NHIAS; // Fix constant type
%javaconstvalue(0x00000100) AS_NCMAS; // Fix constant type
%javaconstvalue(0x00000E00) AS_HEXFM; // Fix constant type
%javaconstvalue(0x00000000) ASH_HEXF0; // Fix constant type
%javaconstvalue(0x00000200) ASH_HEXF1; // Fix constant type
%javaconstvalue(0x00000400) ASH_HEXF2; // Fix constant type
%javaconstvalue(0x00000600) ASH_HEXF3; // Fix constant type
%javaconstvalue(0x00000800) ASH_HEXF4; // Fix constant type
%javaconstvalue(0x00000A00) ASH_HEXF5; // Fix constant type
%javaconstvalue(0x00003000) AS_DECFM; // Fix constant type
%javaconstvalue(0x00000000) ASD_DECF0; // Fix constant type
%javaconstvalue(0x00001000) ASD_DECF1; // Fix constant type
%javaconstvalue(0x00002000) ASD_DECF2; // Fix constant type
%javaconstvalue(0x00003000) ASD_DECF3; // Fix constant type
%javaconstvalue(0x0001C000) AS_OCTFM; // Fix constant type
%javaconstvalue(0x00000000) ASO_OCTF0; // Fix constant type
%javaconstvalue(0x00004000) ASO_OCTF1; // Fix constant type
%javaconstvalue(0x00008000) ASO_OCTF2; // Fix constant type
%javaconstvalue(0x0000C000) ASO_OCTF3; // Fix constant type
%javaconstvalue(0x00010000) ASO_OCTF4; // Fix constant type
%javaconstvalue(0x00014000) ASO_OCTF5; // Fix constant type
%javaconstvalue(0x00018000) ASO_OCTF6; // Fix constant type
%javaconstvalue(0x000E0000) AS_BINFM; // Fix constant type
%javaconstvalue(0x00000000) ASB_BINF0; // Fix constant type
%javaconstvalue(0x00020000) ASB_BINF1; // Fix constant type
%javaconstvalue(0x00040000) ASB_BINF2; // Fix constant type
%javaconstvalue(0x00060000) ASB_BINF3; // Fix constant type
%javaconstvalue(0x00080000) ASB_BINF4; // Fix constant type
%javaconstvalue(0x000A0000) ASB_BINF5; // Fix constant type
%javaconstvalue(0x00100000) AS_UNEQU; // Fix constant type
%javaconstvalue(0x00200000) AS_ONEDUP; // Fix constant type
%javaconstvalue(0x00400000) AS_NOXRF; // Fix constant type
%javaconstvalue(0x00800000) AS_XTRNTYPE; // Fix constant type
%javaconstvalue(0x01000000) AS_RELSUP; // Fix constant type
%javaconstvalue(0x02000000) AS_LALIGN; // Fix constant type
%javaconstvalue(0x04000000) AS_NOCODECLN; // Fix constant type
%javaconstvalue(0x08000000) AS_NOTAB; // Fix constant type
%javaconstvalue(0x10000000) AS_NOSPACE; // Fix constant type
%javaconstvalue(0x20000000) AS_ALIGN2; // Fix constant type
%javaconstvalue(0x40000000) AS_ASCIIC; // Fix constant type
%javaconstvalue(0x80000000) AS_ASCIIZ; // Fix constant type
%javaconstvalue(0x80000000) REG_SPOIL; // Fix constant type

%include <idp.hpp>
