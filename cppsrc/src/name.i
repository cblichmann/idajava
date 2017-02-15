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
