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
%ignore rename_frame; // Not exported in ida.lib

%include <frame.hpp>
