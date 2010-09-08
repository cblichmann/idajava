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

%include <intel.hpp>
