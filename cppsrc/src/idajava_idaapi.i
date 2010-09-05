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
 *
 * Build with SWIG 2.0.0, command line was:
 *   swig -Wall -c++ -java -package de.blichmann.idajava.natives I"C:\Dev\external\idasdk_5_7\include" idajava_natives.i
 */
 
%module(directors="1") IdaJava

// Include the header files in the wrapper code
%{
#define USE_DANGEROUS_FUNCTIONS
#define USE_STANDARD_FILE_FUNCTIONS
#include <pro.h>
#include <ida.hpp>
#pragma warning(push)
#pragma warning(disable: 4267) // netnode.hpp: Conversion nodeidx_t <-> size_t
#include <idp.hpp>
#pragma warning( pop )
#include <allins.hpp>
#include <expr.hpp>
#include <bytes.hpp>
#include <loader.hpp>
#include <ints.hpp>
#include <kernwin.hpp>
#include <diskio.hpp>
#include <demangle.hpp>
#include <llong.hpp>
#include <fpro.h>
#include <help.h>
#include <ua.hpp>
#include <area.hpp>
#include <segment.hpp>
#include <srarea.hpp>
#include <nalt.hpp>
#include <auto.hpp>
#include <funcs.hpp>
#include <name.hpp>
#include <struct.hpp>
#include <idp.hpp>
#include <frame.hpp>
#include <fixup.hpp>
#include <offset.hpp>
#include <xref.hpp>

// Windows specific includes, omit rarely used APIs
#define WIN32_LEAN_AND_MEAN
#include <windows.h>

#include "idajava_natives.h"
%}

// Use Java 1.5 specific settings
%include <java.swg>
%include <enums.swg>
//%typemap(javain) enum SWIGTYPE "$javainput.ordinal()"
//%typemap(javaout) enum SWIGTYPE {
//    return $javaclassname.class.getEnumConstants()[$jnicall];
//}
//%typemap(javabody) enum SWIGTYPE ""

%include <arrays_java.i>
%include <typemaps.i>

%javaconst(1);
#pragma SWIG nowarn=322 // Ignore redundant declarations
%insert("runtime") %{
#define SWIG_JAVA_ATTACH_CURRENT_THREAD_AS_DAEMON
%}

// Enable Microsoft calling conventions and non-ISO types
%include <windows.i>

// Rename C++ operators into Java-friendly function names
%include "operators.i"

%include "pro.i"
%include "llong.i"
%include "ida.i"
%include "fpro.i"
%include "help.i"
%include "ints.i"
%include "kernwin.i"
%include "loader.i"
%include "diskio.i"
%include "expr.i"
%include "ua.i"
%include "area.i"
%include "segment.i"
%include "srarea.i"
%include "nalt.i"
%include "bytes.i"
%include "auto.i"
%include "funcs.i"
%include "name.i"
%include "struct.i"
%include "idp.i"
%include "frame.i"
%include "fixup.i"
%include "offset.i"
%include "xref.i"
%include "allins.i"
//%include "compress.i" // Not yet wrapped, use Java Zip routines
//%include "dbg.i" // Not yet wrapped, need to work around #ifndef SWIG
//%include "demangle.i"
//%include "entry.i" // Not yet wrapped
//%include "enum.i" // Not yet wrapped
//%include "err.i" // Not yet wrapped

// idajava_natives.i, should come last
%include "idajava_natives.i"