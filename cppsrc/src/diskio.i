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

%inline
%{
	typedef int (idaapi *enumerate_files_cb)(const char *file, void *ud);
%}
idaman int ida_export enumerate_files(char *answer, size_t answer_size, const char *path, const char *fname, enumerate_files_cb func, void *ud);
%ignore enumerate_files;

inline int idaapi enumerate_system_files(char *answer, size_t answer_size, const char *subdir, const char *fname, enumerate_files_cb func, void *ud);
%ignore enumerate_system_files;

/*typedef const char *(idaapi *read_ioports_cb)(const ioport_t *ports, size_t numports, const char *line);
%{typedef const char *(idaapi *read_ioports_cb)(const ioport_t *ports, size_t numports, const char *line);%}
idaman ioport_t *ida_export read_ioports(size_t *_numports, const char *file, char *default_device, size_t dsize, read_ioports_cb func);//*/
%ignore read_ioports;

/*typedef const char *(idaapi *choose_ioport_device_cb)(const char *line, char *buf, size_t bufsize);
%{typedef const char *(idaapi *choose_ioport_device_cb)(const char *line, char *buf, size_t bufsize);%}
idaman bool ida_export choose_ioport_device(const char *file, char *device, size_t device_size, choose_ioport_device_cb func);//*/
%ignore choose_ioport_device;

%ignore get_thread_priority; // System specific, not exported in ida.lib

%ignore checkdspace; // Kernel only
%ignore lowdiskgo; // Kernel only
%ignore create_remote_linput; // Kernel only
%ignore ida_argv; // Kernel only
%ignore exename; // Kernel only

%include <diskio.hpp>
