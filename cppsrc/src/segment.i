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

#undef NO_OBSOLETE_FUNCS
// Apply OUTPUT typemap
idaman ssize_t ida_export get_true_segm_name(const segment_t *s, char *OUTPUT, size_t bufsize);

%ignore enumerate_selectors; // Ignore functions with callbacks
%ignore enumerate_segments_with_selector; // Ignore functions with callbacks

// Kernel-only
%ignore init_groups; // Not exported in ida.lib
%ignore get_segm_expr; // Not exported in ida.lib
%ignore get_based_segm_expr; // Not exported in ida.lib
%ignore createSegmentation; // Not exported in ida.lib
%ignore initSegment; // Not exported in ida.lib
%ignore save_segments; // Not exported in ida.lib
%ignore termSegment; // Not exported in ida.lib
%ignore DeleteAllSegments; // Not exported in ida.lib
%ignore delete_debug_segments; // Not exported in ida.lib

%include <segment.hpp>
