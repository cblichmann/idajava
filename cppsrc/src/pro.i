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
 
#define NO_OBSOLETE_FUNCS

%ignore hit_counter_t;
%ignore reg_hit_counter;
%ignore create_hit_counter;
%ignore hit_counter_timer;
%ignore print_all_counters; // Not exported in ida.lib
%ignore wchar2char; // Not exported in ida.lib

%include <pro.h>
%template(qvector_uchar) qvector<uchar>;

// Do not move this. We need to override the define from pro.h
#define CASSERT(type)
