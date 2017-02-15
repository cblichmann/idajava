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
	typedef bool (idaapi *debugger_t_init_debugger_cb)(const char *hostname, int portnum, const char *password);
	typedef bool (idaapi *debugger_t_term_debugger_cb)(void);
	typedef int (idaapi *debugger_t_process_get_info_cb)(int n, process_info_t *info);
	typedef int (idaapi *debugger_t_start_process_cb)(const char *path, const char *args, const char *startdir, uint32 input_file_crc32);
	typedef int (idaapi *debugger_t_attach_process_cb)(pid_t pid, int event_id);
	typedef int (idaapi *debugger_t_detach_process_cb)(void);
	typedef void (idaapi *debugger_t_rebase_if_required_to_cb)(ea_t new_base);
	typedef int (idaapi *debugger_t_prepare_to_pause_process_cb)(void);
	typedef int (idaapi *debugger_t_exit_process_cb)(void);
	typedef gdecode_t (idaapi *debugger_t_get_debug_event_cb)(debug_event_t *event, bool ida_is_idle);
	typedef int (idaapi *debugger_t_continue_after_event_cb)(const debug_event_t *event);
	typedef void (idaapi *debugger_t_set_exception_info_cb)(const exception_info_t *info, int qty);
	typedef void (idaapi *debugger_t_stopped_at_debug_event_cb)(bool dlls_added);
	typedef int (idaapi *debugger_t_thread_suspend_cb) (thid_t tid);
	typedef int (idaapi *debugger_t_thread_continue_cb)(thid_t tid);
	typedef int (idaapi *debugger_t_thread_set_step_cb)(thid_t tid);
	typedef int (idaapi *debugger_t_read_registers_cb)(thid_t tid, int clsmask, regval_t *values);
	typedef int (idaapi *debugger_t_write_register_cb)(thid_t tid, int regidx, const regval_t *value);
	typedef int (idaapi *debugger_t_thread_get_sreg_base_cb)(thid_t tid, int sreg_value, ea_t *answer);
	typedef int (idaapi *debugger_t_get_memory_info_cb)(meminfo_vec_t &areas);
	typedef ssize_t (idaapi *debugger_t_read_memory_cb)(ea_t ea, void *buffer, size_t size);
	typedef ssize_t (idaapi *debugger_t_write_memory_cb)(ea_t ea, const void *buffer, size_t size);
	typedef int (idaapi *debugger_t_is_ok_bpt_cb)(bpttype_t type, ea_t ea, int len);
	typedef int (idaapi *debugger_t_add_bpt_cb)(bpttype_t type, ea_t ea, int len);
	typedef int (idaapi *debugger_t_del_bpt_cb)(ea_t ea, const uchar *orig_bytes, int len);
	typedef int  (idaapi *debugger_t_open_file_cb)(const char *file, uint32 *fsize, bool readonly);
	typedef void (idaapi *debugger_t_close_file_cb)(int fn);
	typedef ssize_t (idaapi *debugger_t_read_file_cb)(int fn, uint32 off, void *buf, size_t size);
	typedef ea_t (idaapi *debugger_t_map_address_cb)(ea_t off, const regval_t *regs, int regnum);
	typedef const char *(idaapi *debugger_t_set_dbg_options_cb)(const char *keyword, int value_type, const void *value);
	typedef const void *(idaapi *debugger_t_get_debmod_extensions_cb)(void);
	typedef bool (idaapi *debugger_t_update_call_stack_cb)(thid_t tid, call_stack_t *trace);
	typedef ea_t (idaapi *debugger_t_appcall_cb)(ea_t func_ea, thid_t tid, const struct func_type_info_t *fti, int nargs, const struct regobjs_t *regargs, struct relobj_t *stkargs, struct regobjs_t *retregs, qstring *errbuf, debug_event_t *event, int options);
	typedef int (idaapi *debugger_t_cleanup_appcall_cb)(thid_t tid);
%}
struct debugger_t
{
	int version;
	const char * name;
	int id;
	const char * processor;
	uint32 flags;
	bool is_remote(void) const { return (flags & DBG_FLAG_REMOTE) != 0; }
	bool must_have_hostname(void) const { return (flags & (DBG_FLAG_REMOTE|DBG_FLAG_NOHOST)) == DBG_FLAG_REMOTE; }
	bool can_continue_from_bpt(void) const { return (flags & DBG_FLAG_CAN_CONT_BPT) != 0; }
	bool may_disturb(void) const { return (flags & DBG_FLAG_DONT_DISTURB) == 0; }
	bool is_safe(void) const { return (flags & DBG_FLAG_SAFE) != 0; }
	bool use_sregs(void) const { return (flags & DBG_FLAG_USE_SREGS) != 0; }
	size_t cache_block_size(void) const { return (flags & DBG_FLAG_SMALLBLKS) != 0 ? 256 : 1024; }
	bool use_memregs(void) const { return (flags & DBG_FLAG_MANMEMINFO) != 0; }
	bool may_take_exit_snapshot(void) const { return (flags & DBG_FLAG_EXITSHOTOK) != 0; }
	bool virtual_threads(void) const { return (flags & DBG_FLAG_VIRTHREADS) != 0; }
	const char **register_classes;
	int register_classes_default;
	register_info_t *registers;
	int  registers_size;
	int memory_page_size;
	const uchar *bpt_bytes;
	int bpt_size;
	debugger_t_init_debugger_cb init_debugger;
	static const int default_port_number = 23946;
	debugger_t_term_debugger_cb term_debugger;
	debugger_t_process_get_info_cb process_get_info;
	debugger_t_start_process_cb start_process;
	debugger_t_attach_process_cb attach_process;
	debugger_t_detach_process_cb detach_process;
	debugger_t_rebase_if_required_to_cb rebase_if_required_to;
	debugger_t_prepare_to_pause_process_cb prepare_to_pause_process;
	debugger_t_exit_process_cb exit_process;
	debugger_t_get_debug_event_cb get_debug_event;
	debugger_t_continue_after_event_cb continue_after_event;
	debugger_t_set_exception_info_cb set_exception_info;
	debugger_t_stopped_at_debug_event_cb stopped_at_debug_event;
	debugger_t_thread_suspend_cb thread_suspend;
	debugger_t_thread_continue_cb thread_continue;
	debugger_t_thread_set_step_cb thread_set_step;
	debugger_t_read_registers_cb read_registers;
	debugger_t_write_register_cb write_register;
	debugger_t_thread_get_sreg_base_cb thread_get_sreg_base;
	debugger_t_get_memory_info_cb get_memory_info;
	debugger_t_read_memory_cb read_memory;
	debugger_t_write_memory_cb write_memory;
	debugger_t_is_ok_bpt_cb is_ok_bpt;
	debugger_t_add_bpt_cb add_bpt;
	debugger_t_del_bpt_cb del_bpt;
	debugger_t_open_file_cb open_file;
	debugger_t_close_file_cb close_file;
	debugger_t_read_file_cb read_file;
	debugger_t_map_address_cb map_address;
	debugger_t_set_dbg_options_cb set_dbg_options;
	debugger_t_get_debmod_extensions_cb get_debmod_extensions;
	debugger_t_update_call_stack_cb update_call_stack;
	debugger_t_appcall_cb appcall;
	debugger_t_cleanup_appcall_cb cleanup_appcall;
};
%ignore debugger_t;

%ignore handle_debug_event; // Not exported in ida.lib

%include <idd.hpp>
