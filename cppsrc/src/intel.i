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

%ignore getreg_t;
%ignore pc_get_operand_info;
%ignore getr;

//This doesn't create an equivalent structure, so I'm just remaking it entirely.
//%javaconstvalue(1000) pc_module_t::set_difbase; // Fix SWIG wrapping failure
//%javaconstvalue(1005) pc_module_t::find_reg_value; // This value just goes missing for some reason...
//%javaconstvalue(1006) pc_module_t::dbgtools_path;

namespace pc_module_t
{
  enum event_codes_t
  {
    set_difbase = 1000,
    restore_pushinfo,
    save_pushinfo,
    prolog_analyzed,
    verify_epilog,
    find_reg_value,
    dbgtools_path,
  };
}

%ignore pc_module_t;
%ignore event_codes_t;
%ignore set_difbase;
%ignore restore_pushinfo;
%ignore save_pushinfo;
%ignore prolog_analyzed;
%ignore verify_epilog;
%ignore find_reg_value;
%ignore dbgtools_path;


//These are the symbols which cause linker failure:
%ignore insn_default_opsize_64;
%ignore op64;
%ignore operand_dtyp;
%ignore has_stack_displ;
%ignore intel_node;
%ignore get_ret_target;
%ignore set_ret_target;
%ignore del_ret_target;
%ignore get_fbase_reg;
%ignore set_fbase_reg;
%ignore loading_complete;
%ignore prolog_color;
%ignore epilog_color;
%ignore switch_color;
%ignore idpflags;
%ignore should_af_push;
%ignore should_af_nop;
%ignore should_af_movoff;
%ignore should_af_movoff2;
%ignore should_af_zeroins;
%ignore should_af_brtti;
%ignore should_af_urtti;
%ignore should_af_fexp;
%ignore should_af_difbase;
%ignore should_af_nopref;
%ignore should_af_vxd;
%ignore should_af_fpemu;
%ignore should_af_showrip;
%ignore should_af_seh;
%ignore cvt_to_wholereg;
%ignore intel_header;
%ignore intel_footer;
%ignore intel_assumes;
%ignore intel_segstart;
%ignore intel_segend;
%ignore intel_out;
%ignore gen_stkvar_def;
%ignore get_type_name;
%ignore is_align_insn;
%ignore get_segval;
%ignore get_mem_ea;
%ignore get_imm_outf;
%ignore get_displ_outf;
%ignore pc_data;
%ignore pc_ana;
%ignore pc_emu;
%ignore pc_outop;
%ignore pushinfo_t::verify;
%ignore pushinfo_t::save_to_idb;
%ignore pushinfo_t::restore_from_idb;
%ignore get_spec_func_type;
%ignore spoils;
%ignore find_reg_value;
%ignore is_switch;
%ignore equal_ops;
%ignore sp_based;
%ignore create_func_frame;
%ignore is_jump_func;
%ignore is_alloca_probe;
%ignore check_new_name;
%ignore is_stack_changing_func;
%ignore verify_sp;
%ignore find_callee;
%ignore pc_calc_purged_from_type;
%ignore pc_calc_arglocs;
%ignore pc_calc_varglocs;
%ignore pc_calc_retloc;
%ignore pc_use_stkvar_type;
%ignore pc_use_regvar_type;
%ignore use_pc_arg_types;
%ignore get_fastcall_regs;
%ignore get_thiscall_regs;
%ignore calc_cdecl_purged_bytes;
%ignore calc_sp_delta;
%ignore analyze_frame;
%ignore del_additional_frame_info;
%ignore calc_func_call_delta;
%ignore calc_ebp_phrase;
%ignore is_pc_return;
%ignore is_move_insn;
%ignore setup_til;
%ignore is_userland_app;
%ignore is_userland_pe;
%ignore is_vxd_interrupt;
%ignore vxd_information;
%ignore pc_move_segm;
%ignore borland_template;
%ignore borland_coagulate;
%ignore borland_signature;
%ignore get_codeseq_target;
%ignore cover_func_finalize;
%ignore compiler_finalize;
%ignore is_sane_insn;
%ignore may_be_func;
%ignore validate_flirt_func;
%ignore might_change_sp;
%ignore parse_func_seh;
%ignore reattach_handler_block;
%ignore label_seh_table;
%ignore pc_get_reg_name;
%ignore pc_get_one_reg_name;
%ignore is_segment_normal;
%ignore is_seh_entry;
%ignore is_local_label;
%ignore del_func_seh;
%ignore procnum;
%ignore pflag;

%include <intel.hpp>
