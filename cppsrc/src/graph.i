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

%ignore node_ordering_t::clr; // Not exported in ida.lib
%ignore node_ordering_t::order; // Not exported in ida.lib
%ignore point_t::dstr; // Not exported in ida.lib
%ignore point_t::print; // Not exported in ida.lib
%ignore pointseq_t::dstr; // Not exported in ida.lib
%ignore pointseq_t::print; // Not exported in ida.lib
%ignore rect_t::operator<; // Not exported in ida.lib
%ignore edge_info_t::add_layout_point; // Not exported in ida.lib
%ignore selection_item_t::selection_item_t; // Not exported in ida.lib
%ignore graph_item_t::operator==; // Not exported in ida.lib
%ignore abstract_graph_t::clear; // Not exported in ida.lib
%ignore abstract_graph_t::dump_graph; // Not exported in ida.lib
%ignore abstract_graph_t::calc_bounds; // Not exported in ida.lib
%ignore abstract_graph_t::calc_fitting_params; // Not exported in ida.lib
%ignore abstract_graph_t::for_all_nodes_edges; // Not exported in ida.lib
%ignore abstract_graph_t::get_edge_ports; // Not exported in ida.lib
%ignore abstract_graph_t::add_node_edges; // Not exported in ida.lib
%ignore graph_dispatcher; // Not exported in ida.lib
%ignore mutable_graph_t::get_edge; // Not exported in ida.lib
%ignore mutable_graph_t::clone; // Not exported in ida.lib
%ignore mutable_graph_t::redo_layout; // Not exported in ida.lib
%ignore mutable_graph_t::resize; // Not exported in ida.lib
%ignore mutable_graph_t::add_node; // Not exported in ida.lib
%ignore mutable_graph_t::del_node; // Not exported in ida.lib
%ignore mutable_graph_t::add_edge; // Not exported in ida.lib
%ignore mutable_graph_t::del_edge; // Not exported in ida.lib
%ignore mutable_graph_t::replace_edge; // Not exported in ida.lib
%ignore mutable_graph_t::refresh; // Not exported in ida.lib
%ignore mutable_graph_t::set_nrect; // Not exported in ida.lib
%ignore mutable_graph_t::mutable_graph_t; // Not exported in ida.lib
%ignore mutable_graph_t::groups_are_present; // Not exported in ida.lib
%ignore mutable_graph_t::insert_visible_nodes; // Not exported in ida.lib
%ignore mutable_graph_t::insert_simple_nodes; // Not exported in ida.lib
%ignore mutable_graph_t::check_new_group; // Not exported in ida.lib
%ignore mutable_graph_t::change_visibility; // Not exported in ida.lib
%ignore mutable_graph_t::recalc_edges; // Not exported in ida.lib
%ignore mutable_graph_t::calc_center_of; // Not exported in ida.lib
%ignore mutable_graph_t::move_to_same_place; // Not exported in ida.lib
%ignore mutable_graph_t::move_grouped_nodes; // Not exported in ida.lib

%nodefaultctor user_graph_place_t;

%include <graph.hpp>
