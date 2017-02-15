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
