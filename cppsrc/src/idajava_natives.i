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
 *
 * Build with SWIG 2.0.0, command line was:
 *   swig -Wall -c++ -java -package de.blichmann.idajava.natives I"C:\Dev\external\idasdk_5_7\include" idajava_natives.i
 */
 
%module(directors="1") IdaJava

// Include the header files in the wrapper code
%{
#define USE_DANGEROUS_FUNCTIONS
#include <pro.h>
#include <ida.hpp>
#pragma warning(push)
// Disable warning about conversion between nodeidx_t and size_t in netnode.hpp
#pragma warning(disable: 4267)
#include <idp.hpp>
#pragma warning( pop )
#include <expr.hpp>
#include <bytes.hpp>
#include <loader.hpp>
#include <ints.hpp>
#include <kernwin.hpp>
#include <diskio.hpp>
#include <llong.hpp>
#include <fpro.h>
#include <help.h>
#include <ua.hpp>
#include <area.hpp>
#include <segment.hpp>
#include <nalt.hpp>
#include <auto.hpp>
#include <funcs.hpp>
#include <name.hpp>
#include <struct.hpp>
#include <idp.hpp>
#include <frame.hpp>
#include <fixup.hpp>
#include <offset.hpp>

// Windows specific includes, omit rarely used APIs
#define WIN32_LEAN_AND_MEAN
#include <windows.h>

#include "idajava_natives.h"
%}

// Use Java 1.5 specific settings
%include <java.swg>
%include <enums.swg>
//%typemap(javain) enum SWIGTYPE "$javainput.ordinal()"
//%typemap(javaout) enum SWIGTYPE {
//    return $javaclassname.class.getEnumConstants()[$jnicall];
//}
//%typemap(javabody) enum SWIGTYPE ""

%include <arrays_java.i>
%include <typemaps.i>

%javaconst(1);
#pragma SWIG nowarn=322 // Ignore redundant declarations
%insert("runtime") %{
#define SWIG_JAVA_ATTACH_CURRENT_THREAD_AS_DAEMON
%}

// Enable Microsoft calling conventions and non-ISO types
%include <windows.i>

//// Rename C++ operators into Java-friendly function names
//%include "operators.i"

// pro.h
#define NO_OBSOLETE_FUNCS

%ignore hit_counter_t;
%ignore reg_hit_counter;
%ignore create_hit_counter;
%ignore hit_counter_timer;
%ignore print_all_counters; // Not exported in ida.lib
%ignore wchar2char; // Not exported in ida.lib

%include <pro.h>

// llong.hpp
#define __HAS_LONGLONG__
%ignore uint128; // TODO: Wrap in BigInteger
%ignore int128; // TODO: Wrap in BigInteger

%include <llong.hpp>

// ida.hpp
%ignore idainfo::init; // Not exported in ida.lib
%ignore idainfo::retrieve; // Not exported in ida.lib
%ignore idainfo::read; // Not exported in ida.lib
%ignore idainfo::write; // Not exported in ida.lib
%ignore dual_text_options_t::init; // Not exported in ida.lib
%ignore dual_text_options_t::copy_to_inf; // Not exported in ida.lib
%ignore dual_text_options_t::copy_from_inf; // Not exported in ida.lib
%ignore dual_text_options_t::restore; // Not exported in ida.lib
%ignore dual_text_options_t::save; // Not exported in ida.lib
%ignore text_options_t::copy_to_inf; // Not exported in ida.lib
%ignore text_options_t::copy_from_inf; // Not exported in ida.lib

%javaconstvalue(0xFF000000) MAXADDR; // Fix constant type, value will be negative int!

%include <ida.hpp>

// fpro.h
%ignore qgetchar;
%ignore qveprintf;
%ignore qeprintf;
%ignore qfscanf;

%include <fpro.h>

// help.h
%ignore HelpInit; // Not exported in ida.lib
%ignore GetMessage; // Not exported in ida.lib
%ignore itext; // Temporary messages should be handled in Java

%include <help.h>

// ints.hpp
%ignore init_predefs; // Not exported in ida.lib
%ignore term_predefs; // Not exported in ida.lib

%javaconstvalue(0xF0000000) A_CASECODE; // Fix constant type
%javaconstvalue(0x10000001) A_NETINIT; // Fix constant type

%include <ints.hpp>

// kernwin.hpp
#define _WINDOWS_ // For create_tform()

#define FORM_MDI      0x01
#define FORM_TAB      0x02
#define FORM_RESTORE  0x04
#define FORM_ONTOP    0x08
#define FORM_MENU     0x10
#define FORM_CENTERED 0x20
#define FORM_SAVE           0x1
#define FORM_NO_CONTEXT     0x2
#define FORM_DONT_SAVE_SIZE 0x4

// Use typemap for output parameter
%apply int *OUTPUT {int *x, int *y}; // get_cursor()
%apply unsigned long *OUTPUT {ea_t *ea1, ea_t *ea2}; // read_selection()

typedef int HWND; // TODO: Make 64-bit safe (when IDA is 64-bit itself)
/*inline TForm *create_tform(const char *caption, HWND *OUTPUT);
%ignore create_tform;*/

/*inline HWND get_tform_handle(TForm *form);
%ignore get_tform_handle;*/

inline int askaddr(ea_t *OUTPUT, const char *format, ...);
%ignore askaddr;

%ignore ui_dbg_run_requests; // Debugger callgate
%ignore ui_dbg_get_running_request; // Debugger callgate
%ignore ui_dbg_get_running_notification; // Debugger callgate
%ignore ui_dbg_clear_requests_queue; // Debugger callgate
%ignore ui_dbg_get_process_state; // Debugger callgate
%ignore ui_dbg_start_process; // Debugger callgate
%ignore ui_dbg_request_start_process; // Debugger callgate
%ignore ui_dbg_suspend_process; // Debugger callgate
%ignore ui_dbg_request_suspend_process; // Debugger callgate
%ignore ui_dbg_continue_process; // Debugger callgate
%ignore ui_dbg_request_continue_process; // Debugger callgate
%ignore ui_dbg_exit_process; // Debugger callgate
%ignore ui_dbg_request_exit_process; // Debugger callgate
%ignore ui_dbg_get_thread_qty; // Debugger callgate
%ignore ui_dbg_getn_thread; // Debugger callgate
%ignore ui_dbg_select_thread; // Debugger callgate
%ignore ui_dbg_request_select_thread; // Debugger callgate
%ignore ui_dbg_step_into; // Debugger callgate
%ignore ui_dbg_request_step_into; // Debugger callgate
%ignore ui_dbg_step_over; // Debugger callgate
%ignore ui_dbg_request_step_over; // Debugger callgate
%ignore ui_dbg_run_to; // Debugger callgate
%ignore ui_dbg_request_run_to; // Debugger callgate
%ignore ui_dbg_step_until_ret; // Debugger callgate
%ignore ui_dbg_request_step_until_ret; // Debugger callgate
%ignore ui_dbg_get_reg_val; // Debugger callgate
%ignore ui_dbg_set_reg_val; // Debugger callgate
%ignore ui_dbg_request_set_reg_val; // Debugger callgate
%ignore ui_dbg_get_bpt_qty; // Debugger callgate
%ignore ui_dbg_getn_bpt; // Debugger callgate
%ignore ui_dbg_get_bpt; // Debugger callgate
%ignore ui_dbg_add_bpt; // Debugger callgate
%ignore ui_dbg_request_add_bpt; // Debugger callgate
%ignore ui_dbg_del_bpt; // Debugger callgate
%ignore ui_dbg_request_del_bpt; // Debugger callgate
%ignore ui_dbg_update_bpt; // Debugger callgate
%ignore ui_dbg_enable_bpt; // Debugger callgate
%ignore ui_dbg_request_enable_bpt; // Debugger callgate
%ignore ui_dbg_set_trace_size; // Debugger callgate
%ignore ui_dbg_clear_trace; // Debugger callgate
%ignore ui_dbg_request_clear_trace; // Debugger callgate
%ignore ui_dbg_is_step_trace_enabled; // Debugger callgate
%ignore ui_dbg_enable_step_trace; // Debugger callgate
%ignore ui_dbg_request_enable_step_trace; // Debugger callgate
%ignore ui_dbg_get_step_trace_options; // Debugger callgate
%ignore ui_dbg_set_step_trace_options; // Debugger callgate
%ignore ui_dbg_request_set_step_trace_options; // Debugger callgate
%ignore ui_dbg_is_insn_trace_enabled; // Debugger callgate
%ignore ui_dbg_enable_insn_trace; // Debugger callgate
%ignore ui_dbg_request_enable_insn_trace; // Debugger callgate
%ignore ui_dbg_get_insn_trace_options; // Debugger callgate
%ignore ui_dbg_set_insn_trace_options; // Debugger callgate
%ignore ui_dbg_request_set_insn_trace_options; // Debugger callgate
%ignore ui_dbg_is_func_trace_enabled; // Debugger callgate
%ignore ui_dbg_enable_func_trace; // Debugger callgate
%ignore ui_dbg_request_enable_func_trace; // Debugger callgate
%ignore ui_dbg_get_func_trace_options; // Debugger callgate
%ignore ui_dbg_set_func_trace_options; // Debugger callgate
%ignore ui_dbg_request_set_func_trace_options; // Debugger callgate
%ignore ui_dbg_get_tev_qty; // Debugger callgate
%ignore ui_dbg_get_tev_info; // Debugger callgate
%ignore ui_dbg_get_insn_tev_reg_val; // Debugger callgate
%ignore ui_dbg_get_insn_tev_reg_result; // Debugger callgate
%ignore ui_dbg_get_call_tev_callee; // Debugger callgate
%ignore ui_dbg_get_ret_tev_return; // Debugger callgate
%ignore ui_dbg_get_bpt_tev_ea; // Debugger callgate
%ignore ui_dbg_is_reg_integer; // Debugger callgate
%ignore ui_dbg_get_process_qty; // Debugger callgate
%ignore ui_dbg_get_process_info; // Debugger callgate
%ignore ui_dbg_attach_process; // Debugger callgate
%ignore ui_dbg_request_attach_process; // Debugger callgate
%ignore ui_dbg_detach_process; // Debugger callgate
%ignore ui_dbg_request_detach_process; // Debugger callgate
%ignore ui_dbg_get_first_module; // Debugger callgate
%ignore ui_dbg_get_next_module; // Debugger callgate
%ignore ui_dbg_bring_to_front; // Debugger callgate
%ignore ui_dbg_get_current_thread; // Debugger callgate
%ignore ui_dbg_end; // Debugger callgate

%ignore addblanks; // Java provides built-in string functions
%ignore trim; // Java provides built-in string functions
%ignore skipSpaces; // Java provides built-in string functions
%ignore stristr; // Java provides built-in string functions
%ignore strarray_t; // Java provides built-in string types
%ignore strarray; // Java provides built-in string functions

%ignore cli_t; // TODO: Wrap in custom class

%include <kernwin.hpp>

// loader.hpp
%ignore loader_t::accept_file; // Ignore callback members
%ignore loader_t::load_file; // Ignore callback members
%ignore loader_t::save_file; // Ignore callback members
%ignore loader_t::move_segm; // Ignore callback members
%ignore loader_t::init_loader_options; // Ignore callback members
%ignore plugin_t::init; // Ignore callback members
%ignore plugin_t::term; // Ignore callback members
%ignore plugin_t::run; // Ignore callback members

%ignore vloader_failure;
%ignore loader_failure;

%ignore impinfo_t;

%ignore load_dll; // Not exported in ida.lib
%ignore load_dll_or_die; // Not exported in ida.lib
%ignore init_fileregions; // Not exported in ida.lib
%ignore term_fileregions; // Not exported in ida.lib
%ignore add_fileregion; // Not exported in ida.lib
%ignore move_fileregions; // Not exported in ida.lib
%ignore local_gen_idc_file; // Not exported in ida.lib

%ignore print_all_places; // Not exported in ida.lib
%ignore save_text_line; // Not exported in ida.lib
%ignore print_all_structs; // Not exported in ida.lib
%ignore print_all_enums; // Not exported in ida.lib
%ignore database_id0; // Not exported in ida.lib
%ignore ida_database_memory; // Not exported in ida.lib
%ignore ida_workdir; // Not exported in ida.lib
%ignore pe_create_idata; // Not exported in ida.lib
%ignore pe_load_resources; // Not exported in ida.lib
%ignore pe_create_flat_group; // Not exported in ida.lib
%ignore initializing; // Not exported in ida.lib
%ignore highest_processor_level; // Not exported in ida.lib
%ignore check_database; // Not exported in ida.lib
%ignore open_database; // Not exported in ida.lib
%ignore get_workbase_fname; // Not exported in ida.lib
%ignore close_database; // Not exported in ida.lib
%ignore compress_btree; // Not exported in ida.lib
%ignore get_input_file_from_archive; // Not exported in ida.lib
%ignore loader_move_segm; // Not exported in ida.lib
%ignore generate_ida_copyright; // Not exported in ida.lib
%ignore clear_plugin_options; // Not exported in ida.lib
%ignore is_in_loader; // Not exported in ida.lib
%ignore is_embedded_dbfile_ext; // Not exported in ida.lib
%ignore get_ids_filename; // Not exported in ida.lib

%ignore dev_code_t; // Experimental
%ignore gen_dev_event; // Experimental

/*%include <loader.hpp>*/

// diskio.hpp
typedef int (idaapi *enumerate_files_cb)(const char *file, void *ud);
%{typedef int (idaapi *enumerate_files_cb)(const char *file, void *ud);%}
idaman int ida_export enumerate_files(char *answer, size_t answer_size, const char *path, const char *fname, enumerate_files_cb func, void *ud);
%ignore enumerate_files;
inline int idaapi enumerate_system_files(char *answer, size_t answer_size, const char *subdir, const char *fname, enumerate_files_cb func, void *ud);
%ignore enumerate_system_files;

/*typedef const char *(idaapi *read_ioports_cb)(const ioport_t *ports, size_t numports, const char *line);
%{typedef const char *(idaapi *read_ioports_cb)(const ioport_t *ports, size_t numports, const char *line);%}
idaman ioport_t *ida_export read_ioports(size_t *_numports, const char *file, char *default_device, size_t dsize, read_ioports_cb func);//*/
%ignore read_ioports;

/*typedef const char *(idaapi *choose_ioport_device_cb)(const char *line, char *buf, size_t bufsize);
%{typedef const char *(idaapi *choose_ioport_device_cb)(const char *line, char *buf, size_t bufsize);%}
idaman bool ida_export choose_ioport_device(const char *file, char *device, size_t device_size, choose_ioport_device_cb func);//*/
%ignore choose_ioport_device;

%ignore get_thread_priority; // System specific, not exported in ida.lib

%ignore checkdspace; // Kernel only
%ignore lowdiskgo; // Kernel only
%ignore create_remote_linput; // Kernel only
%ignore ida_argv; // Kernel only
%ignore exename; // Kernel only

%include <diskio.hpp>

/*// expr.hpp
typedef error_t (idaapi *funcset_t_cb)(void);
%{typedef error_t (idaapi *funcset_t_cb)(void);%}
struct funcset_t
{
  int qnty;
  extfun_t *f;
  funcset_t_cb startup;
  funcset_t_cb shutdown;
};
%ignore funcset_t;

typedef bool (idaapi *extlang_t_compile_cb)(const char *name, ea_t current_ea, const char *expr, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_compile_cb)(const char *name, ea_t current_ea, const char *expr, char *errbuf, size_t errbufsize);%}
typedef bool (idaapi *extlang_t_run_cb)(const char *name, int nargs, const idc_value_t args[], idc_value_t *result, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_run_cb)(const char *name, int nargs, const idc_value_t args[], idc_value_t *result, char *errbuf, size_t errbufsize);%}
typedef bool (idaapi *extlang_t_calcexpr_cb)(ea_t current_ea, const char *expr, idc_value_t *rv, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_calcexpr_cb)(ea_t current_ea, const char *expr, idc_value_t *rv, char *errbuf, size_t errbufsize);%}
typedef bool (idaapi *extlang_t_compile_file_cb)(const char *file, char *OUTPUT, size_t errbufsize);
%{typedef bool (idaapi *extlang_t_compile_file_cb)(const char *file, char *errbuf, size_t errbufsize);%}
struct extlang_t
{
  size_t size;
  uint32 flags;
  const char *name;
  extlang_t_compile_cb compile;
  extlang_t_run_cb run;
  extlang_t_calcexpr_cb calcexpr;
  extlang_t_compile_file_cb compile_file;
  const char *fileext;         
};
%ignore extlang_t;

typedef uval_t (idaapi *getname_cb)(const char *name);
%{typedef uval_t (idaapi *getname_cb)(const char *name);%}
idaman bool ida_export CompileLine(const char *line, char *errbuf, size_t errbufsize, getname_cb getname_func=NULL);
%ignore CompileLine;
idaman bool ida_export ExecuteLine(const char *line, const char *func, getname_cb getname_func, int argsnum, const idc_value_t args[], idc_value_t *result, char *errbuf, size_t errbufsize);
%ignore ExecuteLine;

%ignore idc_stacksize; // Not exported in ida.lib
%ignore idc_calldepth; // Not exported in ida.lib
%ignore expr_printf; // Not exported in ida.lib
%ignore expr_sprintf; // Not exported in ida.lib
%ignore expr_printfer; // Not exported in ida.lib
%ignore init_idc; // Not exported in ida.lib
%ignore term_idc; // Not exported in ida.lib
%ignore del_idc_userfuncs; // Not exported in ida.lib
%ignore del_idc_userdefs; // Not exported in ida.lib
%ignore find_builtin_idc_func; // Not exported in ida.lib
%ignore idc_lx; // Not exported in ida.lib
%ignore idc_vars; // Not exported in ida.lib

%include <expr.hpp>

// ua.hpp
%include <ua.hpp>

// area.hpp
%ignore areacb_t::areasCode; // Ignore private member
%ignore areacb_t::infosize; // Ignore private member
%ignore areacb_t::lastreq; // Ignore private member
%ignore areacb_t::reserved; // Ignore private member
%ignore areacb_t::sa; // Ignore private member
%ignore areacb_t::cache; // Ignore private member
%ignore areacb_t::allocate; // Ignore private member
%ignore areacb_t::search; // Ignore private member
%ignore areacb_t::readArea; // Ignore private member
%ignore areacb_t::findCache; // Ignore private member
%ignore areacb_t::addCache; // Ignore private member
%ignore areacb_t::delCache; // Ignore private member
%ignore areacb_t::free_cache; // Ignore private member
%ignore areacb_t::find_nth_start; // Ignore private member
%ignore areacb_t::build_optimizer; // Ignore private member
%ignore areacb_t::move_area_comment; // Ignore private member
%ignore areacb_t::pack_and_write_area; // Ignore private member
%ignore areacb_t::move_away; // Ignore private member

%ignore areacb_t::read_cb;
%ignore areacb_t::write_cb;
%ignore areacb_t::delcache_cb;
%ignore areacb_t::edit_cb;
%ignore areacb_t::kill_cb;
%ignore areacb_t::new_cb;
%ignore areacb_t::choose_area;
%ignore areacb_t::choose_area2;
%ignore areacb_t::find_prev_gap;
%ignore areacb_t::find_next_gap;
%ignore areacb_t::move_areas;
%ignore areacb_t::for_all_areas;

%javaconstvalue(0x01020304) AREA_LONG_COMMENT_TAG; // Fix constant type

%include <area.hpp>

// segment.hpp
#undef NO_OBSOLETE_FUNCS
// Apply OUTPUT typemap
idaman ssize_t ida_export get_true_segm_name(const segment_t *s, char *OUTPUT, size_t bufsize);

%ignore enumerate_selectors; // Ignore functions with callbacks
%ignore enumerate_segments_with_selector; // Ignore functions with callbacks

// Kernel-only
%ignore init_groups; // Not exported in ida.lib
%ignore get_segm_expr; // Not exported in ida.lib
%ignore get_based_segm_expr; // Not exported in ida.lib
%ignore createSegmentation; // Not exported in ida.lib
%ignore initSegment; // Not exported in ida.lib
%ignore save_segments; // Not exported in ida.lib
%ignore termSegment; // Not exported in ida.lib
%ignore DeleteAllSegments; // Not exported in ida.lib
%ignore delete_debug_segments; // Not exported in ida.lib

%include <segment.hpp>
#define NO_OBSOLETE_FUNCS

// nalt.hpp
%ignore nmSerEA; // Not exported in ida.lib
%ignore nmSerN; // Not exported in ida.lib
%ignore maxSerialName; // Not exported in ida.lib

%javaconstvalue(0x00000001) AFL_LINNUM; // Fix constant type
%javaconstvalue(0x00000002) AFL_USERSP; // Fix constant type
%javaconstvalue(0x00000004) AFL_PUBNAM; // Fix constant type
%javaconstvalue(0x00000008) AFL_WEAKNAM; // Fix constant type
%javaconstvalue(0x00000010) AFL_HIDDEN; // Fix constant type
%javaconstvalue(0x00000020) AFL_MANUAL; // Fix constant type
%javaconstvalue(0x00000040) AFL_NOBRD; // Fix constant type
%javaconstvalue(0x00000080) AFL_ZSTROFF; // Fix constant type
%javaconstvalue(0x00000100) AFL_BNOT0; // Fix constant type
%javaconstvalue(0x00000200) AFL_BNOT1; // Fix constant type
%javaconstvalue(0x00000400) AFL_LIB; // Fix constant type
%javaconstvalue(0x00000800) AFL_TI; // Fix constant type
%javaconstvalue(0x00001000) AFL_TI0; // Fix constant type
%javaconstvalue(0x00002000) AFL_TI1; // Fix constant type
%javaconstvalue(0x00004000) AFL_LNAME; // Fix constant type
%javaconstvalue(0x00008000) AFL_TILCMT; // Fix constant type
%javaconstvalue(0x00010000) AFL_LZERO0; // Fix constant type
%javaconstvalue(0x00020000) AFL_LZERO1; // Fix constant type
%javaconstvalue(0x00040000) AFL_COLORED; // Fix constant type
%javaconstvalue(0x00080000) AFL_TERSESTR; // Fix constant type
%javaconstvalue(0x00100000) AFL_SIGN0; // Fix constant type
%javaconstvalue(0x00200000) AFL_SIGN1; // Fix constant type
%javaconstvalue(0x00400000) AFL_NORET; // Fix constant type
%javaconstvalue(0x00800000) AFL_FIXEDSPD; // Fix constant type
%javaconstvalue(0x01000000) AFL_ALIGNFLOW; // Fix constant type
%javaconstvalue(0x02000000) AFL_USERTI; // Fix constant type
%javaconstvalue(0x04000000) AFL_RETFP; // Fix constant type

%include <nalt.hpp>

// bytes.hpp
typedef area_t *(idaapi *set_dbgmem_source_dbg_get_memory_config_cb)(int *n);
%{typedef area_t *(idaapi *set_dbgmem_source_dbg_get_memory_config_cb)(int *n);%}
typedef int (idaapi *set_dbgmem_source_memory_read_cb) (ea_t ea, void *buffer, int size);
%{typedef int (idaapi *set_dbgmem_source_memory_read_cb) (ea_t ea, void *buffer, int size);%}
typedef int (idaapi *set_dbgmem_source_memory_write_cb)(ea_t ea, const void *buffer, int size);
%{typedef int (idaapi *set_dbgmem_source_memory_write_cb)(ea_t ea, const void *buffer, int size);%}
idaman void ida_export set_dbgmem_source(set_dbgmem_source_dbg_get_memory_config_cb dbg_get_memory_config_func, set_dbgmem_source_memory_read_cb memory_read_func, set_dbgmem_source_memory_write_cb memory_write_func);
%ignore set_dbgmem_source;

%ignore adjust_visea; // Not exported in ida.lib
%ignore is_first_visea; // Not exported in ida.lib
%ignore is_last_visea; // Not exported in ida.lib
%ignore is_visible_finally; // Not exported in ida.lib
%ignore flush_flags; // Not exported in ida.lib
%ignore get_ascii_char; // Not exported in ida.lib
%ignore del_typeinfo; // Not exported in ida.lib
%ignore del_operand_typeinfo; // Not exported in ida.lib
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
%javaconstvalue(0xF0000000) MS_CODE; // Fix constant type
%javaconstvalue(0x10000000) FF_FUNC; // Fix constant type
%javaconstvalue(0x40000000) FF_IMMD; // Fix constant type
%javaconstvalue(0x80000000) FF_JUMP; // Fix constant type

%include <bytes.hpp>

// auto.hpp
%ignore queue_weak_code; // Not exported in ida.lib
%ignore auto_process_all; // Not exported in ida.lib
%ignore autoPlanned;
%ignore nextPlanned;
%ignore is_planned_ea; // Not exported in ida.lib
%ignore get_next_planned_ea; // Not exported in ida.lib
%ignore autoDelCode; // Not exported in ida.lib
%ignore autoPeek;
%ignore autoProcess;
%ignore auto_init; // Not exported in ida.lib
%ignore auto_save; // Not exported in ida.lib
%ignore auto_term; // Not exported in ida.lib
%ignore ea_without_xrefs; // Not exported in ida.lib

%include <auto.hpp>

// funcs.hpp
%ignore iterate_func_chunks;
%ignore del_regargs; // Not exported in ida.lib
%ignore write_regargs; // Not exported in ida.lib
%ignore find_regarg;
%ignore free_regarg; // Not exported in ida.lib
%ignore init_signatures; // Not exported in ida.lib
%ignore term_signatures; // Not exported in ida.lib
%ignore init_funcs; // Not exported in ida.lib
%ignore save_funcs; // Not exported in ida.lib
%ignore term_funcs; // Not exported in ida.lib
%ignore move_funcs; // Not exported in ida.lib
%ignore copy_noret_info; // Not exported in ida.lib
%ignore recalc_func_noret_flag; // Not exported in ida.lib
%ignore plan_for_noret_analysis; // Not exported in ida.lib
%ignore invalidate_sp_analysis; // Not exported in ida.lib
%ignore create_func_eas_array; // Not exported in ida.lib
%ignore auto_add_func_tails; // Not exported in ida.lib
%ignore read_tails; // Not exported in ida.lib
%ignore determine_rtl;
%ignore save_signatures;

%{
ea_t get_fchunk_referer(ea_t ea, size_t idx)
{
    func_t *pfn = get_fchunk(ea);
    func_parent_iterator_t dummy(pfn); // read referer info
    if (idx >= static_cast<size_t>(pfn->refqty) || pfn->referers == NULL)
        return BADADDR;
    return pfn->referers[idx];
}
%}

%include <funcs.hpp>

// name.hpp
%apply unsigned long *OUTPUT { uval_t *value };

%ignore addDummyName; // Not exported in ida.lib
%ignore convert_debug_names_to_normal; // Not exported in ida.lib
%ignore convert_name_formats; // Not exported in ida.lib
%ignore showhide_name; // Not exported in ida.lib
%ignore clear_lname_bit; // Not exported in ida.lib
%ignore fix_new_name; // Not exported in ida.lib
%ignore rename; // Not exported in ida.lib
%ignore move_names; // Not exported in ida.lib
%ignore is_noret_name; // Not exported in ida.lib
%ignore nameVa; // Not exported in ida.lib
%ignore get_short_name;
%ignore get_long_name;
%ignore get_colored_short_name;
%ignore get_colored_long_name;
%ignore is_exit_name;
%ignore dummy_name_ea;

%include <name.hpp>

// struct.hpp
%ignore init_structs; // Not exported in ida.lib
%ignore save_structs; // Not exported in ida.lib
%ignore term_structs; // Not exported in ida.lib
%ignore sync_from_struc; // Not exported in ida.lib

%include <struct.hpp>
// Add a get_member() member function to struc_t.
// This helps to access the members array in the class.
%extend struc_t {
    member_t * get_member(int index) { return &(self->members[index]); }
}

// idp.hpp
%ignore AbstractRegister; // Deprecated
%ignore WorkReg; // Deprecated
%ignore rginfo;
%ignore bytes_t;
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
%ignore instruc_t;
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

// frame.hpp
%ignore add_frame_spec_member; // Not exported in ida.lib
%ignore del_stkvars; // Not exported in ida.lib
%ignore calc_frame_offset; // Not exported in ida.lib
%ignore read_regvars; // Not exported in ida.lib
%ignore write_regvars; // Not exported in ida.lib
%ignore del_regvars; // Not exported in ida.lib
%ignore free_regvar; // Not exported in ida.lib
%ignore gen_regvar_defs; // Not exported in ida.lib
%ignore set_llabel; // Not exported in ida.lib
%ignore get_llabel_ea; // Not exported in ida.lib
%ignore get_llabel; // Not exported in ida.lib
%ignore read_llabels; // Not exported in ida.lib
%ignore write_llabels; // Not exported in ida.lib
%ignore del_llabels; // Not exported in ida.lib
%ignore free_llabel; // Not exported in ida.lib
%ignore read_stkpnts; // Not exported in ida.lib
%ignore write_stkpnts; // Not exported in ida.lib
%ignore del_stkpnts; // Not exported in ida.lib

%include <frame.hpp>

// fixup.hpp
%ignore apply_fixup; // Not exported in ida.lib
%ignore convert_fixups; // Not exported in ida.lib
%ignore move_fixups; // Not exported in ida.lib

%include <fixup.hpp>

// offset.hpp
%ignore calc_probable_base; // Not exported in ida.lib

%include <offset.hpp>

// idajava_natives.h, should come last
%feature("director") IdaMenuItemListener;
*/
%include "idajava_natives.h"
