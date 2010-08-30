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

typedef bool (idaapi *construct_macro_cb)(insn_t &s, bool may_go_forward);
%{typedef bool (idaapi *construct_macro_cb)(insn_t &s, bool may_go_forward);%}
idaman bool ida_export construct_macro(bool enable, construct_macro_cb build_macro);
%ignore construct_macro;
%ignore get_immval; // Not exported in ida.lib
%ignore ua_out; // Not exported in ida.lib
%ignore ua_use_fixup; // Not exported in ida.lib
%ignore init_ua; // Not exported in ida.lib
%ignore term_ua; // Not exported in ida.lib
%ignore get_equal_items; // Not exported in ida.lib

%include <ua.hpp>
