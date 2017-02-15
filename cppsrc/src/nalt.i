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

%ignore nmSerEA; // Not exported in ida.lib
%ignore nmSerN; // Not exported in ida.lib
%ignore maxSerialName; // Not exported in ida.lib

%javaconstvalue(0x00000001) AFL_LINNUM; // Fix constant type
%javaconstvalue(0x00000002) AFL_USERSP; // Fix constant type
%javaconstvalue(0x00000004) AFL_PUBNAM; // Fix constant type
%javaconstvalue(0x00000008) AFL_WEAKNAM; // Fix constant type
%javaconstvalue(0x00000010) AFL_HIDDEN; // Fix constant type
%javaconstvalue(0x00000020) AFL_MANUAL; // Fix constant type
%javaconstvalue(0x00000040) AFL_NOBRD; // Fix constant type
%javaconstvalue(0x00000080) AFL_ZSTROFF; // Fix constant type
%javaconstvalue(0x00000100) AFL_BNOT0; // Fix constant type
%javaconstvalue(0x00000200) AFL_BNOT1; // Fix constant type
%javaconstvalue(0x00000400) AFL_LIB; // Fix constant type
%javaconstvalue(0x00000800) AFL_TI; // Fix constant type
%javaconstvalue(0x00001000) AFL_TI0; // Fix constant type
%javaconstvalue(0x00002000) AFL_TI1; // Fix constant type
%javaconstvalue(0x00004000) AFL_LNAME; // Fix constant type
%javaconstvalue(0x00008000) AFL_TILCMT; // Fix constant type
%javaconstvalue(0x00010000) AFL_LZERO0; // Fix constant type
%javaconstvalue(0x00020000) AFL_LZERO1; // Fix constant type
%javaconstvalue(0x00040000) AFL_COLORED; // Fix constant type
%javaconstvalue(0x00080000) AFL_TERSESTR; // Fix constant type
%javaconstvalue(0x00100000) AFL_SIGN0; // Fix constant type
%javaconstvalue(0x00200000) AFL_SIGN1; // Fix constant type
%javaconstvalue(0x00400000) AFL_NORET; // Fix constant type
%javaconstvalue(0x00800000) AFL_FIXEDSPD; // Fix constant type
%javaconstvalue(0x01000000) AFL_ALIGNFLOW; // Fix constant type
%javaconstvalue(0x02000000) AFL_USERTI; // Fix constant type
%javaconstvalue(0x04000000) AFL_RETFP; // Fix constant type

%include <nalt.hpp>
