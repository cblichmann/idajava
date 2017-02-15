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
 *
 * Build with SWIG 2.0.0, command line was:
 *   swig -Wall -c++ -java -package de.blichmann.idajava.natives I"C:\Dev\external\idasdk_5_7\include" idajava_natives.i
 */
 
%module(directors="1") IdaJava

// Include the header files in the wrapper code
%{
#define USE_DANGEROUS_FUNCTIONS
#define USE_STANDARD_FILE_FUNCTIONS
#pragma warning(push)
#pragma warning(disable: 4267) // netnode.hpp: Conversion nodeidx_t <-> size_t
#pragma warning(disable: 4800) // graph.hpp: Performance int forced to bool
#include <pro.h>
#include <ida.hpp>
#include <fpro.h>
#include <netnode.hpp>
#include <nalt.hpp>
#include <idp.hpp>
#include <kernwin.hpp>

#include <allins.hpp>
#include <area.hpp>
#include <auto.hpp>
#include <bytes.hpp>
//#include <compress.hpp>
//#include <dbg.hpp>
#include <demangle.hpp>
#include <diskio.hpp>
#include <entry.hpp>
#include <enum.hpp>
#include <exehdr.h>
#include <expr.hpp>
#include <fixup.hpp>
#include <funcs.hpp>
#include <frame.hpp>
#include <gdl.hpp>
#include <graph.hpp>

#include <help.h>
#include <idd.hpp>
#include <idp.hpp>
#include <ieee.h>
#include <intel.hpp>
#include <ints.hpp>
#include <lex.hpp>
#include <lines.hpp>
#include <llong.hpp>
#include <loader.hpp>
#include <md5.h>
//#include <moves.hpp>
#include <name.hpp>
#include <offset.hpp>
//#include <prodir.h>
//#include <queue.hpp>
//#include <regex.hpp>
//#include <search.hpp>
#include <segment.hpp>
//#include <sistack.hpp>
#include <srarea.hpp>
//#include <strlist.hpp>
#include <struct.hpp>
//#include <typeinf.hpp>
#include <ua.hpp>
//#include <va.hpp>
//#include <vm.hpp>
#include <xref.hpp>
#pragma warning(pop)

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
%include "ida.i"
%include "fpro.i"
//%include "netnode.i" // Not yet wrapped
%include "nalt.i"
%include "idp.i"
%include "kernwin.i"

%include "llong.i"
%include "help.i"
%include "ints.i"
%include "loader.i"
%include "diskio.i"
%include "expr.i"
%include "ua.i"
%include "area.i"
%include "segment.i"
%include "srarea.i"
%include "bytes.i"
%include "auto.i"
%include "funcs.i"
%include "name.i"
%include "struct.i"
%include "frame.i"
%include "fixup.i"
%include "offset.i"
%include "xref.i"
%include "allins.i"
//%include "compress.i" // Not yet wrapped, use Java Zip routines
//%include "dbg.i" // Not yet wrapped, need to work around #ifndef SWIG
%include "demangle.i"
%include "entry.i"
%include "enum.i"
//%include "err.i" // Not used in IDA
%include "exehdr.i"
//%include "gdl.i" // TODO: Wrapping incomplete
%include "graph.i"
%include "idd.i"
%include "ieee.i"
%include "intel.i" // TODO: Wrapping incomplete
%include "lex.i"
%include "md5.i"
//%include "moves.i" // Not yet wrapped
//%include "name.i" // Not yet wrapped
//%include "offset.i" // Not yet wrapped
//%include "prodir.i" // Not yet wrapped
//%include "queue.i" // Not yet wrapped
//%include "regex.i" // Not yet wrapped
//%include "search.i" // Not yet wrapped
//%include "sistack.i" // Not yet wrapped
//%include "strlist.i" // Not yet wrapped
//%include "typeinf.i" // Not yet wrapped
//%include "va.i" // Not yet wrapped
//%include "vm.i" // Not yet wrapped

// idajava_natives.i, should come last
%include "idajava_natives.i"