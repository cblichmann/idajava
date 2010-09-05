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

%ignore xrefblk_t_first_from; // Helper function, not to be called directly
%ignore xrefblk_t_next_from; // Helper function, not to be called directly
%ignore xrefblk_t_first_to; // Helper function, not to be called directly
%ignore xrefblk_t_next_to; // Helper function, not to be called directly

%ignore lastXR; // Not exported in ida.lib

%ignore create_xrefs_from; // Not exported in ida.lib
%ignore create_xrefs_from_data(ea_t ea); // Not exported in ida.lib
%ignore delete_all_xrefs_from; // Not exported in ida.lib
%ignore delete_data_xrefs_from; // Not exported in ida.lib
%ignore delete_code_xrefs_from; // Not exported in ida.lib
%ignore has_jump_or_flow_xref; // Not exported in ida.lib
%ignore has_call_xref; // Not exported in ida.lib
%ignore destroy_switch_info; // Not exported in ida.lib
%ignore del_switch_info_ex; // Not exported in ida.lib

%include <xref.hpp>
