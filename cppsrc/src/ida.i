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

%ignore idainfo::init; // Not exported in ida.lib
%ignore idainfo::retrieve; // Not exported in ida.lib
%ignore idainfo::read; // Not exported in ida.lib
%ignore idainfo::write; // Not exported in ida.lib
%ignore dual_text_options_t::init; // Not exported in ida.lib
%ignore dual_text_options_t::copy_to_inf; // Not exported in ida.lib
%ignore dual_text_options_t::copy_from_inf; // Not exported in ida.lib
%ignore dual_text_options_t::restore; // Not exported in ida.lib
%ignore dual_text_options_t::save; // Not exported in ida.lib
%ignore text_options_t::copy_to_inf; // Not exported in ida.lib
%ignore text_options_t::copy_from_inf; // Not exported in ida.lib

%javaconstvalue(0xFF000000) MAXADDR; // Fix constant type, value will be negative int!

%include <ida.hpp>
