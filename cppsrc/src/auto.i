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

%ignore queue_weak_code; // Not exported in ida.lib
%ignore auto_process_all; // Not exported in ida.lib
%ignore autoPlanned;
%ignore nextPlanned;
%ignore is_planned_ea; // Not exported in ida.lib
%ignore get_next_planned_ea; // Not exported in ida.lib
%ignore autoDelCode; // Not exported in ida.lib
%ignore autoPeek;
%ignore autoProcess;
%ignore auto_init; // Not exported in ida.lib
%ignore auto_save; // Not exported in ida.lib
%ignore auto_term; // Not exported in ida.lib
%ignore ea_without_xrefs; // Not exported in ida.lib

%include <auto.hpp>
