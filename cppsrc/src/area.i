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

%ignore area_t_print; // Helper function

%ignore areaset_t::count;
%ignore areaset_t::lower_bound; // Ignore private member
%ignore areaset_t::upper_bound; // Ignore private member
%ignore areaset_t::move_chunk;
%ignore areaset_t::check_move_args;

%ignore areacb_t::areasCode; // Ignore private member
%ignore areacb_t::infosize; // Ignore private member
%ignore areacb_t::lastreq; // Ignore private member
%ignore areacb_t::reserved; // Ignore private member
%ignore areacb_t::sa; // Ignore private member
%ignore areacb_t::cache; // Ignore private member
%ignore areacb_t::allocate; // Ignore private member
%ignore areacb_t::search; // Ignore private member
%ignore areacb_t::readArea; // Ignore private member
%ignore areacb_t::findCache; // Ignore private member
%ignore areacb_t::addCache; // Ignore private member
%ignore areacb_t::delCache; // Ignore private member
%ignore areacb_t::free_cache; // Ignore private member
%ignore areacb_t::find_nth_start; // Ignore private member
%ignore areacb_t::build_optimizer; // Ignore private member
%ignore areacb_t::move_area_comment; // Ignore private member
%ignore areacb_t::pack_and_write_area; // Ignore private member
%ignore areacb_t::move_away; // Ignore private member

%ignore areacb_t::read_cb;
%ignore areacb_t::write_cb;
%ignore areacb_t::delcache_cb;
%ignore areacb_t::edit_cb;
%ignore areacb_t::kill_cb;
%ignore areacb_t::new_cb;
%ignore areacb_t::choose_area;
%ignore areacb_t::choose_area2;
%ignore areacb_t::find_prev_gap;
%ignore areacb_t::find_next_gap;
%ignore areacb_t::move_areas;
%ignore areacb_t::for_all_areas;

%javaconstvalue(0x01020304) AREA_LONG_COMMENT_TAG; // Fix constant type

%template(qvector_area_t) qvector<area_t>;

%include <area.hpp>
