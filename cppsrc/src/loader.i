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
%ignore del_fileregions; // Not exported in ida.lib
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
%ignore add_plugin_option; // Not exported in ida.lib

%ignore dev_code_t; // Experimental
%ignore gen_dev_event; // Experimental

typedef int (idaapi *enum_processor_modules_cb)(const char *file, void *ud);
%{typedef int (idaapi *enum_processor_modules_cb)(const char *file, void *ud);%}
idaman int ida_export enum_processor_modules(enum_processor_modules_cb func, void *ud, char *answer, size_t answer_size, const extlang_t **el = NULL);
%ignore enum_processor_modules;

typedef int (idaapi *enum_plugins_cb)(const char *file, void *ud);
%{typedef int (idaapi *enum_plugins_cb)(const char *file, void *ud);%}
idaman int ida_export enum_plugins(enum_plugins_cb func, void *ud, char *answer, size_t answer_size, const extlang_t **el = NULL);
%ignore enum_plugins;

%include <loader.hpp>
