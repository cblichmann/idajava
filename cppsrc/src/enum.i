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

%ignore enums; // Private
%ignore ENUM_REVERSE; // Private
%ignore ENUM_SELMEMS; // Private
%ignore ENUM_QTY_IDX; // Private
%ignore ENUM_FLG_IDX; // Private
%ignore ENUM_FLAGS; // Private
%ignore ENUM_FLAGS_IS_BF; // Private
%ignore ENUM_FLAGS_HIDDEN; // Private
%ignore ENUM_MASKS; // Private
%ignore ENUM_MEMBERS; // Private
%ignore ENUM_ORDINAL; // Private
%ignore CONST_ENUM; // Private
%ignore CONST_VALUE; // Private
%ignore CONST_BMASK; // Private
%ignore CONST_SERIAL; // Private
%ignore CONST_SERIALS; // Private

%ignore set_enum_flag; // Not exported in ida.lib
%ignore sync_from_enum; // Not exported in ida.lib
%ignore del_all_enum_members; // Not exported in ida.lib
%ignore getn_enum; // Not exported in ida.lib
%ignore get_enum_idx; // Not exported in ida.lib
%ignore is_good_bmask; // Not exported in ida.lib
%ignore get_bmask_enum; // Not exported in ida.lib

%include <enum.hpp>
