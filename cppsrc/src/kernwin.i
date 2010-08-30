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

#define _WINDOWS_ // For create_tform()

#define FORM_MDI      0x01
#define FORM_TAB      0x02
#define FORM_RESTORE  0x04
#define FORM_ONTOP    0x08
#define FORM_MENU     0x10
#define FORM_CENTERED 0x20
#define FORM_SAVE           0x1
#define FORM_NO_CONTEXT     0x2
#define FORM_DONT_SAVE_SIZE 0x4

// Use typemap for output parameter
%apply int *OUTPUT {int *x, int *y}; // get_cursor()
%apply unsigned long *OUTPUT {ea_t *ea1, ea_t *ea2}; // read_selection()

typedef int HWND; // TODO: Make 64-bit safe (when IDA is 64-bit itself)
inline TForm *create_tform(const char *caption, HWND *OUTPUT);
%ignore create_tform;

/*inline HWND get_tform_handle(TForm *form);
%ignore get_tform_handle;*/

inline int askaddr(ea_t *OUTPUT, const char *format, ...);
%ignore askaddr;

%ignore addblanks; // Java provides built-in string functions
%ignore trim; // Java provides built-in string functions
%ignore skipSpaces; // Java provides built-in string functions
%ignore stristr; // Java provides built-in string functions
%ignore strarray_t; // Java provides built-in string types
%ignore strarray; // Java provides built-in string functions

%ignore cli_t; // TODO: Wrap in custom class

%include <kernwin.hpp>
