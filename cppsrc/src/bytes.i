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
 
%inline
%{
	typedef area_t *(idaapi *set_dbgmem_source_dbg_get_memory_config_cb)(int *n);
	typedef int (idaapi *set_dbgmem_source_memory_read_cb)(ea_t ea, void *buffer, int size);
	typedef int (idaapi *set_dbgmem_source_memory_write_cb)(ea_t ea, const void *buffer, int size);
%}
idaman void ida_export set_dbgmem_source(set_dbgmem_source_dbg_get_memory_config_cb dbg_get_memory_config_func, set_dbgmem_source_memory_read_cb memory_read_func, set_dbgmem_source_memory_write_cb memory_write_func);
%ignore set_dbgmem_source;

%ignore adjust_visea; // Not exported in ida.lib
%ignore is_first_visea; // Not exported in ida.lib
%ignore is_last_visea; // Not exported in ida.lib
%ignore is_visible_finally; // Not exported in ida.lib
%ignore flush_flags; // Not exported in ida.lib
%ignore get_ascii_char; // Not exported in ida.lib
%ignore del_opinfo; // Not exported in ida.lib
%ignore del_one_opinfo; // Not exported in ida.lib
%ignore doCode; // Not exported in ida.lib
%ignore get_repeatable_cmt; // Not exported in ida.lib
%ignore get_any_indented_cmt; // Not exported in ida.lib
%ignore del_code_comments; // Not exported in ida.lib
%ignore doRef; // Not exported in ida.lib
%ignore noRef; // Not exported in ida.lib
%ignore coagulate; // Not exported in ida.lib
%ignore coagulate_dref; // Not exported in ida.lib
%ignore init_hidden_areas; // Not exported in ida.lib
%ignore save_hidden_areas; // Not exported in ida.lib
%ignore term_hidden_areas; // Not exported in ida.lib
%ignore check_move_args; // Not exported in ida.lib
%ignore movechunk; // Not exported in ida.lib
%ignore lock_dbgmem_config; // Not exported in ida.lib
%ignore unlock_dbgmem_config; // Not exported in ida.lib
%ignore set_op_type_no_event; // Not exported in ida.lib
%ignore shuffle_tribytes; // Not exported in ida.lib
%ignore validate_tofs; // Not exported in ida.lib
%ignore ida_vpagesize; // Not exported in ida.lib
%ignore ida_vpages; // Not exported in ida.lib
%ignore ida_npagesize; // Not exported in ida.lib
%ignore ida_npages; // Not exported in ida.lib
%ignore fpnum_digits; // Not exported in ida.lib
%ignore fpnum_length; // Not exported in ida.lib
%ignore pnum_digits; // Not exported in ida.lib
%ignore pnum_length; // Not exported in ida.lib
%ignore init_flags; // Not exported in ida.lib
%ignore term_flags; // Not exported in ida.lib
%ignore reset_flags; // Not exported in ida.lib

%ignore MS_TAIL; // Kernel only
%ignore TL_TSFT; // Kernel only
%ignore TL_TOFF; // Kernel only
%ignore MAX_TOFF; // Kernel only

%javaconstvalue(0x000000FF) MS_VAL; // Fix constant type
%javaconstvalue(0x00000100) FF_IVL; // Fix constant type
%javaconstvalue(0x00000600) MS_CLS; // Fix constant type
%javaconstvalue(0x00000600) FF_CODE; // Fix constant type
%javaconstvalue(0x00000400) FF_DATA; // Fix constant type
%javaconstvalue(0x00000200) FF_TAIL; // Fix constant type
%javaconstvalue(0x00000000) FF_UNK; // Fix constant type
%javaconstvalue(0x000FF800) MS_COMM; // Fix constant type
%javaconstvalue(0x00000800) FF_COMM; // Fix constant type
%javaconstvalue(0x00001000) FF_REF; // Fix constant type
%javaconstvalue(0x00002000) FF_LINE; // Fix constant type
%javaconstvalue(0x00004000) FF_NAME; // Fix constant type
%javaconstvalue(0x00008000) FF_LABL; // Fix constant type
%javaconstvalue(0x00010000) FF_FLOW; // Fix constant type
%javaconstvalue(0x00020000) FF_SIGN; // Fix constant type
%javaconstvalue(0x00040000) FF_BNOT; // Fix constant type
%javaconstvalue(0x00080000) FF_VAR; // Fix constant type
%javaconstvalue(0x0000C000) FF_ANYNAME ; // Fix constant type
%javaconstvalue(0x00F00000) MS_0TYPE; // Fix constant type
%javaconstvalue(0x00000000) FF_0VOID; // Fix constant type
%javaconstvalue(0x00100000) FF_0NUMH; // Fix constant type
%javaconstvalue(0x00200000) FF_0NUMD; // Fix constant type
%javaconstvalue(0x00300000) FF_0CHAR; // Fix constant type
%javaconstvalue(0x00400000) FF_0SEG; // Fix constant type
%javaconstvalue(0x00500000) FF_0OFF; // Fix constant type
%javaconstvalue(0x00600000) FF_0NUMB; // Fix constant type
%javaconstvalue(0x00700000) FF_0NUMO; // Fix constant type
%javaconstvalue(0x00800000) FF_0ENUM; // Fix constant type
%javaconstvalue(0x00900000) FF_0FOP; // Fix constant type
%javaconstvalue(0x00A00000) FF_0STRO; // Fix constant type
%javaconstvalue(0x00B00000) FF_0STK; // Fix constant type
%javaconstvalue(0x00C00000) FF_0FLT; // Fix constant type
%javaconstvalue(0x00D00000) FF_0CUST; // Fix constant type
%javaconstvalue(0x0F000000) MS_1TYPE; // Fix constant type
%javaconstvalue(0x00000000) FF_1VOID; // Fix constant type
%javaconstvalue(0x01000000) FF_1NUMH; // Fix constant type
%javaconstvalue(0x02000000) FF_1NUMD; // Fix constant type
%javaconstvalue(0x03000000) FF_1CHAR; // Fix constant type
%javaconstvalue(0x04000000) FF_1SEG; // Fix constant type
%javaconstvalue(0x05000000) FF_1OFF; // Fix constant type
%javaconstvalue(0x06000000) FF_1NUMB; // Fix constant type
%javaconstvalue(0x07000000) FF_1NUMO; // Fix constant type
%javaconstvalue(0x08000000) FF_1ENUM; // Fix constant type
%javaconstvalue(0x09000000) FF_1FOP; // Fix constant type
%javaconstvalue(0x0A000000) FF_1STRO; // Fix constant type
%javaconstvalue(0x0B000000) FF_1STK; // Fix constant type
%javaconstvalue(0x0C000000) FF_1FLT; // Fix constant type
%javaconstvalue(0x0D000000) FF_1CUST; // Fix constant type
%javaconstvalue(0xF0000000) DT_TYPE; // Fix constant type
%javaconstvalue(0x00000000) FF_BYTE; // Fix constant type
%javaconstvalue(0x10000000) FF_WORD; // Fix constant type
%javaconstvalue(0x20000000) FF_DWRD; // Fix constant type
%javaconstvalue(0x30000000) FF_QWRD; // Fix constant type
%javaconstvalue(0x40000000) FF_TBYT; // Fix constant type
%javaconstvalue(0x50000000) FF_ASCI; // Fix constant type
%javaconstvalue(0x60000000) FF_STRU; // Fix constant type
%javaconstvalue(0x70000000) FF_OWRD; // Fix constant type
%javaconstvalue(0x80000000) FF_FLOAT; // Fix constant type
%javaconstvalue(0x90000000) FF_DOUBLE; // Fix constant type
%javaconstvalue(0xA0000000) FF_PACKREAL; // Fix constant type
%javaconstvalue(0xB0000000) FF_ALIGN; // Fix constant type
%javaconstvalue(0xC0000000) FF_3BYTE; // Fix constant type
%javaconstvalue(0xD0000000) FF_CUSTOM; // Fix constant type
%javaconstvalue(0xF0000000) MS_CODE; // Fix constant type
%javaconstvalue(0x10000000) FF_FUNC; // Fix constant type
%javaconstvalue(0x40000000) FF_IMMD; // Fix constant type
%javaconstvalue(0x80000000) FF_JUMP; // Fix constant type

%ignore data_type_t;
%ignore data_format_t;

%include <bytes.hpp>
