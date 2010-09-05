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
//%ignore instruc_t;
%ignore s_preline;
%ignore ca_operation_t;
%ignore _chkarg_cmd;
%ignore ENUM_SIZE;

%ignore asm_t::checkarg_dispatch;
%ignore asm_t::func_header;
%ignore asm_t::func_footer;
%ignore asm_t::get_type_name;
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
