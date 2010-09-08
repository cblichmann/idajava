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

%ignore intseq_t::add_unique(const intseq_t & s); // Not exported in ida.lib
%ignore intseq_t::print; // Not exported in ida.lib
%ignore intseq_t::dstr; // Not exported in ida.lib
%ignore intseq_t::add_block; // Not exported in ida.lib
%ignore intseq_t::remove_block; // Not exported in ida.lib
%ignore intset_t::print; // Not exported in ida.lib
%ignore intset_t::dstr; // Not exported in ida.lib
%ignore intmap_t::print; // Not exported in ida.lib
%ignore intmap_t::dstr; // Not exported in ida.lib
%ignore node_set_t::node_set_t(const gdl_graph_t * g); // Not exported in ida.lib
%ignore node_set_t::sub; // Not exported in ida.lib
%ignore node_set_t::add; // Not exported in ida.lib
%ignore node_set_t::intersect; // Not exported in ida.lib
%ignore node_set_t::extract; // Not exported in ida.lib
%ignore gdl_graph_t::gen_gdl; // Not exported in ida.lib
%ignore gdl_graph_t::path(node_set_t & visited, int m, int n) const; // Not exported in ida.lib
%ignore gdl_graph_t::path_exists; // Not exported in ida.lib
%ignore gdl_graph_t::gen_dot; // Not exported in ida.lib

%ignore setup_graph_subsystem; // Called by GUI initially
%ignore default_graph_format; // Not exported in ida.lib

%ignore cancellable_graph_t::check_cancel; // Not exported in ida.lib

%include <gdl.hpp>
