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
