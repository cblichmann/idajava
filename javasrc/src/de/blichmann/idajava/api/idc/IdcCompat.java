/*
 * IDAJava version 0.3
 * Copyright (c)2007-2010 Christian Blichmann
 *
 * IdaCompat Class
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

package de.blichmann.idajava.api.idc;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.blichmann.idajava.api.IdaConsole;
import de.blichmann.idajava.natives.IdaJava;
import de.blichmann.idajava.natives.IdaJavaConstants;
import de.blichmann.idajava.natives.func_t;
import de.blichmann.idajava.natives.idc_value_t;
import de.blichmann.idajava.natives.member_t;
import de.blichmann.idajava.natives.opinfo_t;
import de.blichmann.idajava.natives.segment_t;
import de.blichmann.idajava.natives.struc_t;

/**
 * This class contains IDA built-in function declarations and internal bit
 * definitions.
 * Each byte of the program has 32-bit flags (low 8 bits keep the byte value).
 * These 32 bits are used in GetFlags/SetFlags functions.
 * To ensure consistency and to prevent errors due to Java's signed int-type,
 * all bitmask checking routines use Java long.
 * You may freely examine these bits using GetFlags() but using SetFlags()
 * function is strongly discouraged.
 *
 * This file is subject to change without notice, it was converted from the
 * original idc.idc file that is shipped with IDA.
 * Future versions of IDA may use other definitions.
 *
 * @author Christian Blichmann
 * @since 0.2
 */
public class IdcCompat {
	/** Invalid address value */
	public static final long BADADDR = -1;

	/** Invalid selector value/number */
	public static final long BADSEL = -1;

	/** Max allowed address in IDA */
	public static final long MAXADDR = 0xFF000000;

	/** Max allowed address in 32-bit IDA */
	public static final long MAXADDR32 = MAXADDR;

	/** Max allowed address in 64-bit IDA */
	public static final long MAXADDR64 = 0xFF00000000000000L;

	/////////////////////////////////////////////////////////////////////////
	// Flag bit definitions (for GetFlags())
	/////////////////////////////////////////////////////////////////////////

	/** Mask for byte value */
	public static final long MS_VAL = 0x000000FFL;

	/** Byte has value? */
	public static final long FF_IVL = 0x00000100L;

	/**
	 * Do flags contain byte value, i.e. has the byte a value?
	 * If not, the byte is uninitialized.
	 * @param bitMask the byte to test for having a value
	 * @return true if the byte has a value, false otherwise.
	 */
	public static boolean hasValue(long bitMask) {
		return (bitMask & FF_IVL) != 0; // Any defined value?
	}

	/**
	 * Get byte value from flags
	 * Get value of byte provided that the byte is initialized.
	 * This function works ok only for 8-bit byte machines.
	 * @param bitMask the bit mask containing the byte's value
	 * @return the byte value
	 */
	public static long byteValue(long bitMask) {
		return bitMask & MS_VAL; // Quick replacement for Byte()
	}

	/**
	 * Is the byte initialized?
	 * @param ea address to test for being initialized
	 * @return true if the byte at the specified address is loaded, false
	 * otherwise.
	 */
	public static boolean isLoaded(long ea) {
		return hasValue(IdaJava.getFlags(ea)); // Any defined value?
	}

	/** Mask for typing */
	public static final long MS_CLS = 0x00000600L;

	/** Bitmask for code */
	public static final long FF_CODE = 0x00000600L;

	/** Bitmask for data */
	public static final long FF_DATA = 0x00000400L;

	/** Bitmask for tail byte */
	public static final long FF_TAIL = 0x00000200L;

	/** Bitmask for unknown byte */
	public static final long FF_UNK = 0x00000000L;

	/**
	 * Is code byte?
	 * @param bitMask the bit mask to test
	 * @return true if the specified bitmask describes a code byte, false
	 * otherwise.
	 */
	public static final boolean isCode(long bitMask) {
		return (bitMask & MS_CLS) == FF_CODE; // Is code byte?
	}

	/**
	 * Is data byte?
	 * @param bitMask the bit mask to test
	 * @return true if the specified bitmask describes a data byte, false
	 * otherwise.
	 */
	public static final boolean isData(long bitMask) {
		return (bitMask  & MS_CLS) == FF_DATA; // Is data byte?
	}

	/**
	 * Is tail byte?
	 * @param bitMask the bit mask to test
	 * @return true if the specified bitmask describes a tail byte, false
	 * otherwise.
	 */
	public static final boolean isTail(long bitMask) {
		return (bitMask  & MS_CLS) == FF_TAIL; // Is tail byte?
	}

	/**
	 * Is unexplored byte?
	 * @param bitMask the bit mask to test
	 * @return true if the specified bitmask describes a yet unexplored byte,
	 * false otherwise.
	 */
	public static final boolean isUnknown(long bitMask) {
		return (bitMask  & MS_CLS) == FF_UNK;  // Is unexplored byte?
	}

	/**
	 * Is start of code/data?
	 * @param bitMask the bit mask to test
	 * @return true if the specified bitmask marks the start of a code or data
	 * section otherwise.
	 */
	public static final boolean isHead(long bitMask) {
		return (bitMask  & FF_DATA) != 0; // Is start of code/data?
	}

	/////////////////////////////////////////////////////////////////////////
	// Common bits
	/////////////////////////////////////////////////////////////////////////

	/** Mask of common bits */
	public static final long MS_COMM = 0x000FF800L;

	/** Has comment? */
	public static final long FF_COMM = 0x00000800L;

	/** Has references? */
	public static final long FF_REF = 0x00001000L;

	/** Has next or prev cmt lines? */
	public static final long FF_LINE = 0x00002000L;

	/** Has user-defined name? */
	public static final long FF_NAME = 0x00004000L;

	/** Has dummy name? */
	public static final long FF_LABL = 0x00008000L;

	/** Exec flow from prev instruction? */
	public static final long FF_FLOW = 0x00010000L;

	/** Is byte variable? */
	public static final long FF_VAR = 0x00080000L;

	/** Has a defined name at all? */
	public static final long FF_ANYNAME = FF_LABL | FF_NAME;

	public static final boolean isFlow(long bitMask) {
		return (bitMask & FF_FLOW) != 0;
	}

	public static final boolean isVar(long bitMask) {
		return (bitMask & FF_VAR ) != 0;
	}

	public static final boolean isExtra(long bitMask) {
		return (bitMask & FF_LINE) != 0;
	}

	public static final boolean isRef(long bitMask) {
		return (bitMask & FF_REF) != 0;
	}

	public static final boolean hasName(long bitMask) {
		return (bitMask & FF_NAME) != 0 ;
	}

	public static final boolean hasUserName(long bitMask) {
		return (bitMask & FF_ANYNAME) == FF_NAME;
	}

	/** Mask for 1st arg typing */
	public static final long MS_0TYPE = 0x00F00000L;

	/** Void (unknown)? */
	public static final long FF_0VOID = 0x00000000L;

	/** Hexadecimal number? */
	public static final long FF_0NUMH = 0x00100000L;

	/** Decimal number? */
	public static final long FF_0NUMD = 0x00200000L;

	/** Char ('x')? */
	public static final long FF_0CHAR = 0x00300000L;

	/** Segment? */
	public static final long FF_0SEG = 0x00400000L;

	/** Offset? */
	public static final long FF_0OFF = 0x00500000L;

	/** Binary number? */
	public static final long FF_0NUMB = 0x00600000L;

	/** Octal number? */
	public static final long FF_0NUMO = 0x00700000L;

	/** Enumeration? */
	public static final long FF_0ENUM = 0x00800000L;

	/** Forced operand? */
	public static final long FF_0FOP = 0x00900000L;

	/** Struct offset? */
	public static final long FF_0STRO = 0x00A00000L;

	/** Stack variable? */
	public static final long FF_0STK = 0x00B00000L;

	/** Mask for 2nd arg typing */
	public static final long MS_1TYPE = 0x0F000000L;

	/** Void (unknown)? */
	public static final long FF_1VOID = 0x00000000L;

	/** Hexadecimal number? */
	public static final long FF_1NUMH = 0x01000000L;

	/** Decimal number? */
	public static final long FF_1NUMD = 0x02000000L;

	/** Char ('x')? */
	public static final long FF_1CHAR = 0x03000000L;

	/** Segment? */
	public static final long FF_1SEG = 0x04000000L;

	/** Offset? */
	public static final long FF_1OFF = 0x05000000L;

	/** Binary number? */
	public static final long FF_1NUMB = 0x06000000L;

	/** Octal number? */
	public static final long FF_1NUMO = 0x07000000L;

	/** Enumeration? */
	public static final long FF_1ENUM = 0x08000000L;

	/** Forced operand? */
	public static final long FF_1FOP = 0x09000000L;

	/** Struct offset? */
	public static final long FF_1STRO = 0x0A000000L;

	/** Stack variable? */
	public static final long FF_1STK = 0x0B000000L;

	// The following methods answer questions like 'is the 1st (or 2nd) operand
	// of the instruction or data of the given type'?
	// Please note that data items use only the 1st operand type (is...0)

	/**
	 * Tests a bitmask for 1st operang being typed.
	 */
	public static final boolean isDefArg0(long bitMask) {
		return (bitMask & MS_0TYPE) != FF_0VOID;
	}

	/**
	 * Tests a bitmask for 2nd operang being typed.
	 */
	public static final boolean isDefArg1(long bitMask) {
		return (bitMask & MS_1TYPE) != FF_1VOID;
	}

	/**
	 * Checks whether the first operand is a decimal number.
	 */
	public static final boolean isDec0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0NUMD;
	}

	/**
	 * Checks whether the second operand is a decimal number.
	 */
	public static final boolean isDec1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1NUMD;
	}

	/**
	 * Checks whether the first operand is a hexadecimal number.
	 */
	public static final boolean isHex0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0NUMH;
	}

	/**
	 * Checks whether the second operand is a hexadecimal number.
	 */
	public static final boolean isHex1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1NUMH;
	}

	/**
	 * Checks whether the first operand is a octal number.
	 */
	public static final boolean isOct0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0NUMO;
	}

	/**
	 * Checks whether the second operand is a octal number.
	 */
	public static final boolean isOct1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1NUMO;
	}

	/**
	 * Checks whether the first operand is a binary number.
	 */
	public static final boolean isBin0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0NUMB;
	}

	/**
	 * Checks whether the second operand is a binary number.
	 */
	public static final boolean isBin1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1NUMB;
	}

	/**
	 * Checks whether the first operand is an offset.
	 */
	public static final boolean isOff0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0OFF;
	}

	/**
	 * Checks whether the second operand is an offset.
	 */
	public static final boolean isOff1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1OFF;
	}

	/**
	 * Checks whether the first operand is a character.
	 */
	public static final boolean isChar0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0CHAR;
	}

	/**
	 * Checks whether the second operand is a character.
	 */
	public static final boolean isChar1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1CHAR;
	}

	/**
	 * Checks whether the first operand denotes a segment.
	 */
	public static final boolean isSeg0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0SEG;
	}

	/**
	 * Checks whether the second operand denotes a segment.
	 */
	public static final boolean isSeg1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1SEG;
	}

	/**
	 * Checks whether the first operand is an enumeration.
	 */
	public static final boolean isEnum0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0ENUM;
	}

	/**
	 * Checks whether the second operand is an enumeration.
	 */
	public static final boolean isEnum1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1ENUM;
	}

	/**
	 * Checks whether the first operand is a forced operand.
	 */
	public static final boolean isFop0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0FOP;
	}

	/**
	 * Checks whether the second operand is a forced operand.
	 */
	public static final boolean isFop1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1FOP;
	}

	/**
	 * Checks whether the first operand is an offset into a struct.
	 */
	public static final boolean isStroff0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0STRO;
	}

	/**
	 * Checks whether the second operand is an offset into a struct.
	 */
	public static final boolean isStroff1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1STRO;
	}

	/**
	 * Checks whether the first operand is a stack variable.
	 */
	public static final boolean isStkvar0(long bitMask) {
		return (bitMask & MS_0TYPE) == FF_0STK;
	}

	/**
	 * Checks whether the second operand is a stack variable.
	 */
	public static final boolean isStkvar1(long bitMask) {
		return (bitMask & MS_1TYPE) == FF_1STK;
	}

	/////////////////////////////////////////////////////////////////////////
	// Bits for DATA bytes
	/////////////////////////////////////////////////////////////////////////

	/** Mask for DATA typing */
	public static final long DT_TYPE = 0xF0000000L;

	/** byte */
	public static final long FF_BYTE = 0x00000000L; 

	/** word */
	public static final long FF_WORD = 0x10000000L; 

	/** dword */
	public static final long FF_DWRD = 0x20000000L; 

	/** qword */
	public static final long FF_QWRD = 0x30000000L; 

	/** tbyte */
	public static final long FF_TBYT = 0x40000000L; 

	/** ASCII? */
	public static final long FF_ASCI = 0x50000000L; 

	/** Struct? */
	public static final long FF_STRU = 0x60000000L; 

	/** octaword (16 bytes) */
	public static final long FF_OWRD = 0x70000000L; 

	/** float */
	public static final long FF_FLOAT = 0x80000000L;

	/** double */
	public static final long FF_DOUBLE = 0x90000000L;

	/** packed decimal real */
	public static final long FF_PACKREAL = 0xA0000000L;

	/** Alignment directive */
	public static final long FF_ALIGN = 0xB0000000L;

	public static boolean isByte(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_BYTE;
	}

	public static boolean isWord(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_WORD;
	}

	public static boolean isDwrd(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_DWRD;
	}

	public static boolean isQwrd(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_QWRD;
	}

	public static boolean isOwrd(long bitMask) { 
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_OWRD;
	}

	public static boolean isTbyt(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_TBYT;
	}

	public static boolean isFloat(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_FLOAT;
	}

	public static boolean isDouble(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_DOUBLE;
	}

	public static boolean isPackReal(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_PACKREAL;
	}

	public static boolean isASCII(long bitMask) { 
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_ASCI;
	}

	public static boolean isStruct(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_STRU;
	}

	public static boolean isAlign(long bitMask) {
		return isData(bitMask) && (bitMask & DT_TYPE) == FF_ALIGN;
	}

	/////////////////////////////////////////////////////////////////////////
	// Bits for CODE bytes
	/////////////////////////////////////////////////////////////////////////

	public static final long MS_CODE = 0xF0000000L;

	/** Function start? */
	public static final long FF_FUNC = 0x10000000L;

	/** Has Immediate value? */
	public static final long FF_IMMD = 0x40000000L;

	/** Has jump table */
	public static final long FF_JUMP = 0x80000000L;

	/////////////////////////////////////////////////////////////////////////
	// Loader flags
	/////////////////////////////////////////////////////////////////////////

	/** Create segments */
	public static final long NEF_SEGS = 0x0001;

	/** Load resources */
	public static final long NEF_RSCS = 0x0002;

	/** Rename entries */
	public static final long NEF_NAME = 0x0004;

	/** Manual load */
	public static final long NEF_MAN = 0x0008;

	/** Fill segment gaps */
	public static final long NEF_FILL = 0x0010;

	/** Create imports section */
	public static final long NEF_IMPS = 0x0020;

	/** Don't align segments (OMF) */
	public static final long NEF_TIGHT = 0x0040;

	/** This is the first file loaded */
	public static final long NEF_FIRST = 0x0080;

	/** for load_binary_file: */
	public static final long NEF_CODE = 0x0100;

	/** reload the file at the same place: */
	public static final long NEF_RELOAD = 0x0200;

	/** Autocreated FLAT group (PE) */
	public static final long NEF_FLAT = 0x0400;

	/////////////////////////////////////////////////////////////////////////
	// List of built-in functions
	/////////////////////////////////////////////////////////////////////////
	//
	// The following conventions are used in this list:
	//   'ea' is a linear address
	//   'success' is 0 if a function failed, 1 otherwise
	//   'void' means that function returns no meaningful value (always 0)
	//
	//  All function parameter conversions are made automatically.
	//
	/////////////////////////////////////////////////////////////////////////
	// MISCELLANEOUS
	/////////////////////////////////////////////////////////////////////////

	// Variable type-checks, not needed in Java:
	// success IsString(var);
	// success IsLong(var);
	// success IsFloat(var);

	/**
	 * Returns the value of the expression: ((seg<<4) + off)
	 * @param seg the Segment
	 * @param off the Offset
	 * @return the value of the expression: {@code ((seg<<4) + off)}.
	 */
	public static long MK_FP(long seg, long off) {
		return (seg << 4) + off;
	}

	// String formatting function. Use Java string formatting instead.
	//public static String form(final String format, Object ... args);

	// Function to return a substring. Use String.substring() instead.
	//public static String substr(final String str, long x1, long x2);

	// Function to find a string inside another string. Use String.indexof()
	// instead.
	//public static long strstr(final String str, final String substr);

	// Function to find the length of a string. Use String.length() instead.
	//public static long strlen(final String str);

	/**
	 * Decodes a String into a Long. Accepts decimal, hexadecimal, and octal
	 * numbers. See {@link Long.decode()}.
	 * @param str the {@code String} to decode.
	 * @return the {@code long} value represented by str. 
	 */
	public static long xtol(final String str) {
		return Long.decode(str).longValue();
	}

	/**
	 * Converts an address value to a string. Returns the address in the form
	 * 'seg000:1234' (the same as in line prefixes).
	 * @param ea the address to format
	 * @return the formatted string.
	 */
	public static String atoa(long ea) {
		StringBuilder segname = new StringBuilder(SegName(ea));
		if (segname.length() == 0)
			segname.append('0');
		segname.append(String.format(":%X", ea));
		return segname.toString();
	}

	// Function that converts a number to a string, use Long.toString()
	// instead.
	//public static String ltoa(long n, long radix);

	// Convert ascii string to a number, use Long.parseValue() instead
	//public static long atol(String str);

	// Function that returns the ASCI code of a character, use cast to byte
	// instead.
	//public static long ord(String str);

	/**
	 * Rotate a value to the left (or right)
	 * @param value value to rotate
	 * @param count number of times to rotate. negative counter means rotate
	 *     to the right
	 * @param nbits number of bits to rotate
	 * @param offset offset of the first bit to rotate
	 * @return the value with the specified field rotated all other bits are
	 *     not modified.
	 */
	public static long rotate_left(long value, long count, long nbits,
			long offset) {
		if (offset < 0)
			throw new IllegalArgumentException("Offset must be >= 0");
		if (nbits <= 0)
			throw new IllegalArgumentException("nbits must be > 0");

//		long mask = 2**(offset + nbits) - 2**offset;
		long mask = 1 >> (offset + nbits) - 1 >> offset;
		long tmp = value & mask;
		if (count > 0) {
			for (int i = 0; i < count; i++){
				if (((tmp >> (offset + nbits - 1)) & 1) != 0)
					tmp = (tmp << 1) | (1 << offset);
				else
					tmp = (tmp << 1);
			}
		} else {
			for (int i = 0; i < -count; i++) {
				if (((tmp >> (offset + nbits - 1)) & 1) != 0)
					tmp = (tmp >> 1) | (1 << offset + nbits - 1);
				else
					tmp = (tmp >> 1);
			}
		}
		value = (value - (value & mask)) | (tmp & mask);
	    return value;
	}

	public static final long rotate_dword(long x, long count) {
		return rotate_left(x, count, 32, 0);
	}

	public static final long rotate_word(long x, long count) {
		return rotate_left(x, count, 16, 0);
	}

	public static final long rotate_byte(long x, long count) {
		return rotate_left(x, count, 8, 0);
	}

	// AddHotkey return codes
	/** Ok */
	public static final long IDCHK_OK = 0;

	/** Bad argument(s) */
	public static final long IDCHK_ARG = -1;

	/** Bad hotkey name */
	public static final long IDCHK_KEY = -2;

	/** Too many IDC hotkeys */
	public static final long IDCHK_MAX = -3;

	/**
	 * Adds a hotkey for an IDC function.
	 * @param hotkey the hotkey name ('a', "Alt-A", etc.)
	 * @param idcfunc the name of the IDC function to assign to the hotkey
	 * @note GUI version doesn't support hotkeys
	 */
	public static long AddHotkey(final String hotkey, final String idcFunc) {
		return IdaJava.add_idc_hotkey(hotkey, idcFunc);
	}

	/**
	 * Deletes a hotkey for an IDC function.
	 * @param hotkey the hotkey name ('a', "Alt-A", etc.)
	 */
	public static boolean DelHotkey(final String hotkey) {
		return IdaJava.del_idc_hotkey(hotkey);
	}

	/**
	 * Move cursor to the specifed linear address. Refreshes the display after
	 * the jump.
	 * @param ea the linear address to jump to
	 * @return true on success, false otherwise
	 */
	public static boolean Jump(long ea) {
		return IdaJava.jumpto(ea);
	}

	/**
	 * Processes all entries in the autoanalysis queue.
	 * @note This function suspends execution of the caller until the
	 *     autoanalysis queue is empty.
	 */
	public static void Wait() {
		IdaJava.autoWait();
	}

	/**
     * Compiles an IDC script. The input should not contain functions that are
     * currently executing - otherwise the behaviour of the replaced functions
     * is undefined.
     * @param input if isfile != 0, then this is the name of file to compile
     *     otherwise it holds the text to compile
     * @param isfile specifies whether {@code input} holds a filename or the
     *     expression itself
     * @return an empty string on success, an error message otherwise
     */
	public static String CompileEx(final String input, boolean isFile) {
		String errBuf = new String(new char[255]);
		boolean error;
		if (isFile)
			error = IdaJava.Compile(input, errBuf, errBuf.length());
		else
			error = IdaJava.CompileLine(input, errBuf, errBuf.length());

		if (error)
			return errBuf;
		return "";
	}

	public static String Eval(final String expr) {
		idc_value_t retVal = new idc_value_t();
		String errBuf = new String(new char[255]);
		if (!IdaJava.calcexpr(BADADDR, expr, retVal, errBuf, errBuf.length()))
			return "IDC_FAILURE: " + errBuf;

		byte vType = (byte) retVal.getVtype();
		if (vType == 1) // String
			return retVal.getStr();
		if (vType == 2) // Long
			return Integer.toString(retVal.getNum());
		throw new RuntimeException("Eval() supports only string or " + 
				"long return values from script");
	}

	/**
	 * Checks for evaluation failures from {@link #Eval()}.
	 * @return true if the specified return value from Eval() denotes an error,
	 *     false otherwise.  
	 */
	public static boolean EVAL_FAILURE(final String code) {
		return code.startsWith("IDC_FAILURE: ");
	}

	/** Create backup file */
	public static long DBFL_BAK = 0x04;

	/**
	 * Save current database to the specified idb file.
	 * @param idbname name of the idb file. if empty, the current idb file will
	 *     be used.
	 * @param flags DBFL_BAK or 0
	 * @return true on success, false on error
	 */
    public static boolean SaveBase(final String idbName, long flags) {
		final String saveFileName = idbName.length() == 0 ?
				IdaJava.getDatabase_idb() : idbName;
		long saveFlags = IdaJava.getDatabase_flags();
		if ((flags & DBFL_BAK) != 0)
			IdaJava.setDatabase_flags(saveFlags & ~DBFL_BAK);
		boolean result = IdaJava.save_database(saveFileName, false);
		IdaJava.setDatabase_flags(saveFlags);
		return result;
	}

	/**
	 * Stop execution of IDC program, close the database and exit to OS.
	 * @param code the exit code to return to the OS
	 */
    public static void Exit(long code) {
    	IdaJava.qexit((int) code);
    }

    /**
     * Execute an OS command via the shell. IDA will wait for the started
     * program to finish.
     * @param command the command line to execute
     * @return the error code from returned by the OS
     */
    public static long Exec(final String command) {
    	Process proc;
		try {
			proc = Runtime.getRuntime().exec(command);
	    	return proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace(IdaConsole.out);
		} catch (InterruptedException e) {
			e.printStackTrace(IdaConsole.out);
		}
		return -1;
    }

    /**
     * Sleep the specified number of milliseconds. This function suspends IDA
     * for the specified amount of time
     * @param milliseconds the length of time to slepp in milliseconds
     */
    public static void Sleep(long milliseconds) {
    	try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace(IdaConsole.out);
		}
    }

    /**
     * Loads and runs an IDA plugin. The plugin name is a short plugin name
     * without an extension.
     * @param name the name of the plugin to load
     * @param arg an integer argument to supply to the plugin.
     * @return true on success, false on error
     */
    public static boolean RunPlugin(final String name, long arg) {
    	return IdaJava.load_and_run_plugin(name, (int) arg);
    }

    /**
	 * Load (plan to apply) a FLIRT signature file
     * @param name the signature name without path and extension
     * @return 0 if the signature file could not be loaded, the number of
     *     planned and applied signatures otherwise.
     */
    public static int ApplySig(final String name) {
    	return IdaJava.plan_to_apply_idasgn(name);
    }

	/////////////////////////////////////////////////////////////////////////
	// CHANGE PROGRAM REPRESENTATION
	/////////////////////////////////////////////////////////////////////////

	/**
	 * Delete all segments, instructions, comments, i.e. everything except
	 * values of bytes.
	 */
    public static void DeleteAll() {
    	long ea = 0;
    	//TODO: Make 64-bit safe (once IDA is 64-bit itself)
    	while (ea != BADADDR && ea <= MAXADDR) {
    		IdaJava.del_local_name(ea);
    		IdaJava.del_global_name(ea);
    		func_t func = IdaJava.get_func(ea);
    		if (func != null) {
    			IdaJava.del_func_cmt(func, false);
    			IdaJava.del_func_cmt(func, true);
    			IdaJava.del_func(ea);
    		}
    		IdaJava.del_hidden_area(ea);
    		segment_t seg = IdaJava.getseg(ea);
    		if (seg != null) {
    			IdaJava.del_segment_cmt(seg, false);
    			IdaJava.del_segment_cmt(seg, true);
    			IdaJava.del_segm(ea, IdaJavaConstants.SEGDEL_KEEP |
    					IdaJavaConstants.SEGDEL_SILENT);
    		}
    		ea = IdaJava.next_head(ea, MAXADDR);
    	}
    }

    /**
     * Creates an instruction at the specified address
     * @param ea the linear address
     * @return 0 if an instruction could not be created at location ea (no such
     *     opcode, the instruction would overlap with existing items, etc.),
     *     otherwise the length of the instruction in bytes. 
     */
    public static long MakeCode(long ea) {
    	return IdaJava.create_insn(ea);
    }

    /**
     * Perform full analysis of the specified area.
     * @param startEA the start of a linear address range
     * @param endEA the end of a linear address range
     * @return 1 on success, 0 if the analysis was cancelled by the user
     */
    public static long AnalyzeArea(long startEA, long endEA) {
    	return IdaJava.analyze_area(startEA, endEA);
    }

    /**
     * Rename an address.
     * @param ea the linear address
     * @param name the new name of the address. If name is an empty string, the
     *     old name is deleted.
     * @param flags a combination of SN_... constants
     * @return true on success, false otherwise
     */
    public static boolean MakeNameEx(long ea, final String name, long flags) {
    	return IdaJava.set_name(ea, name, (int) flags);
    }

    /**
     * Fail if the name contains invalid characters. If this bit is clear, all
     * invalid chars (those !is_ident_char()) will be replaced by SubstChar
     * (usually '_'). The List of valid characters is defined in ida.cfg
     */
	public static final long SN_CHECK = 0x01;

	/** Replace invalid chars with SubstChar */
	public static final long SN_NOCHECK = 0x00;

	/** If set, mark name public */
	public static final long SN_PUBLIC = 0x02;

	/** If set, mark name non-public */
	public static final long SN_NON_PUBLIC = 0x04;

	/** If set, mark name as weak */
	public static final long SN_WEAK = 0x08;

	/** If set, mark name as non-weak */
	public static final long SN_NON_WEAK = 0x10;

	/** If set, mark name as being autogenerated */
	public static final long SN_AUTO = 0x20;

	/** If set, mark name as being non-autogenerated */
	public static final long SN_NON_AUTO = 0x40;

	/**
	 * If set, exclude name from the list. If not set, then include the name in
	 * the list (however, if other bits are set, the name might be immediately
	 * already excluded)
	 */
	public static final long SN_NOLIST = 0x80;

	/** Do not display a warning on failure */
	public static final long SN_NOWARN = 0x100;

	/**
	 * Create local name, a function should exist. Local names cannot be public
	 * or weak. Also, they are not included in the list of names and cannot
	 * have dummy prefixes.
     */
	public static final long SN_LOCAL = 0x200;

	/**
	 * Set an indented regular comment of an item
	 * @param ea a linear address
	 * @param comment the comment string
	 */
	public static boolean MakeComm(long ea, final String comment) {
		return IdaJava.set_cmt(ea, comment, false);
	}

	/**
	 * Set an indented regular comment of an item
	 * @param ea a linear address
	 * @param comment the comment string
	 */
	public static boolean MakeRptCmt(long ea, final String comment) {
		return IdaJava.set_cmt(ea, comment, true);
	}

	/**
	 * Create an array.
	 * @param ea a linear address
	 * @param nitems size of array in items
	 * @note This function will create an array of the items with the same type
	 * as the type of the item at {@code ea}. If the byte at {@code ea} is
	 * undefined, then this function will create an array of bytes.
	 * @return true on succes, false otherwise
	 * @throws IdcException if a call to get_typeinfo() failed.
	 */
	public static boolean MakeArray(long ea, long nitems) throws IdcException {
		long flags = IdaJava.getFlags(ea);
		if (IdaJava.isUnknown(flags))
			flags = FF_BYTE;

		long tid, size;
		if (IdaJava.isStruct(flags)) {
			opinfo_t ti = new opinfo_t();
			if (IdaJava.get_opinfo(ea, 0, flags, ti) == null)
				throw new IdcException("get_typeinfo() failed");
			size = IdaJava.get_data_elsize(ea, flags, ti);
			tid = ti.getTid();
		} else {
			size = IdaJava.get_item_size(ea);
			tid = BADADDR;
		}
		return IdaJava.do_data_ex(ea, flags, size * nitems, tid);
	}

	/**
	 * Create a string. This function creates a string (the string type is
	 * determined by the value of GetLongPrm(INF_STRTYPE))
	 * @param ea a linear address
	 * @param endea the ending address of the string (excluded). If endEA ==
	 * BADADDR, then length of string will be calculated by the kernel.
	 * @return: true on success, false otherwise
	 * @note: The type of an existing string is returned by GetStringType().
	 */
//	public static boolean MakeStr(long ea, long endea) {
//		return IdaJava.make_ascii_string(ea, endea - ea,
//				GetLongPrm(INF_STRTYPE));
//	}
	
	/**
	 * Create a data item at the specified address
	 * @param ea a linear address
	 * @param flags FF_BYTE..FF_PACKREAL
	 * @param size the size of the item in bytes
	 * @param tid for FF_STRU the structure id
	 * @return true on success, false otherwise
	 */
	public static boolean  MakeData(long ea, long flags, long size, long tid) {
		return IdaJava.do_data_ex(ea, flags, size, tid);
	}
	
	/**
	 * Create a structure data item at the specified address
	 * @param ea a linear address
	 * @param size the size of the structure in bytes. -1 means that the size
	 *    will be calculated automatically
	 * @param strname the name of a structure type
	 * @return true on success, false otherwise
	 */
	public static boolean MakeStructEx(long ea,long size,
			final String strname) {
	    long strid = IdaJava.get_struc_id(strname);
	    if (size == -1)
	        size = IdaJava.get_struc_size(strid);
	    return IdaJava.doStruct(ea, size, strid);
	}

	/**
	 * Convert the current item to an alignment directive
	 * @param ea a linear address
	 * @param count the number of bytes to convert
	 * @param align 0 or 1..32, if align is 0, the correct alignment will be
	 *     calculated by the kernel
	 * @return true on success, false otherwise
	 */
	public static boolean MakeAlign(long ea, long count, long align) {
		return IdaJava.doAlign(ea, count, (int) align);
	}

	/**
     * Creates a local variable at the specified address.
     * @param start the start of a linear address range for the local variable
     * @param end the end of the address range. 
     * @param location the variable location in the "[bp+xx]" form where xx is
     *     a number. The location can also be specified as a register name.
     * @param name the name of the local variable
     * @note: For the stack variables the end address is ignored. If there is
     *     no function at 'start' then this function will fail.
     * @return true on success, false otherwise
     */
	public static boolean MakeLocal(long start, long end,
			final String location, final String name) {
		func_t func = IdaJava.get_func(start);
		if (func == null)
			return false;

		final Pattern p = Pattern.compile("\\[([a-z]+)([-+][0-9a-fx]+)");
		final Matcher m = p. matcher(location);
		if (m.matches()) {
			int reg = IdaJava.str2reg(m.group(1));
			long offset = Integer.decode(m.group(2));
			struc_t frame = IdaJava.get_frame(func);
			if (reg < 0 || frame == null)
				return false;
			offset += func.getFrsize();
			member_t member = IdaJava.get_member(frame, offset);
			if (member != null) {
				return IdaJava.set_member_name(frame, offset, name);
			}
			return IdaJava.add_struc_member(frame, name, offset,
					IdaJava.byteflag(), null, 1) == 0;
		}
		return IdaJava.add_regvar(func, start, end, location, name, null) == 0;
	}

	/**
	 * Marks the item at the specified address as explored.
	 * @param ea a linear address
	 * @param flags a combination of DOUNK_* constants
	 */
	public static void MakeUnkn(long ea, long flags) {
		IdaJava.do_unknown(ea, (int) flags);
	}

	/**
	 * Marks the item at the specified address as explored.
	 * @param ea a linear address
	 * @param size the size of the range to undefine
	 * @param flags a combination of DOUNK_* constants
	 */
	public static void MakeUnknown(long ea, long size, long flags) {
		IdaJava.do_unknown_range(ea, size, (int) flags);
	}

	/** Simply undefine the specified item */
	public static final long DOUNK_SIMPLE = 0x0000;

	/**
	 * Propogate undefined items, for example if removing an instruction
	 * removes all references to the next instruction, then plan to mark the
	 * next instruction as unexplored, too.
	 */
	public static final long DOUNK_EXPAND = 0x0001;

	/** Delete any names at the specified address(es) */
	public static final long DOUNK_DELNAMES = 0x0002;

	/**
	 * Set array representation format.
	 * @param ea a linear address
	 * @param flags a combination of AP_... constants or 0
	 * @param litems the number of items per line. 0 means auto
	 * @param align the element alignment:
	 *     -1: do not align
	 *	    0:  automatic alignment
	 *	   other values: element width
	 * @return true on success, false otherwise
	 * @throws IdcException 
	 */
	public static boolean SetArrayFormat(long ea, long flags, long litems,
			long align) throws IdcException {
		idc_value_t retVal = new idc_value_t();
		String errBuf = new String(new char[255]);
		if (!IdaJava.calcexpr(BADADDR, String.format("SetArrayFormat(0x%X, " +
				"0x%X, %d, %d)", ea, flags, litems, align), retVal, errBuf,
				errBuf.length()))
			return false;
	
		byte vType = (byte) retVal.getVtype();
		if (vType != 2) // Long
			throw new IdcException("Unexpected return type from SetArrayFormat");
		return retVal.getNum() != 0;
	}
	
	/** Use 'dup' construct */
	public static final long AP_ALLOWDUPS = 0x00000001L;

	/** Treats numbers as signed */
	public static final long AP_SIGNED = 0x00000002L;

	/** Display array element indexes as comments */
	public static final long AP_INDEX = 0x00000004L;

	/** Reserved (this flag is not stored in database) */
	public static final long AP_ARRAY = 0x00000008L;

	/** Mask for number base of the indexes */
	public static final long AP_IDXBASEMASK = 0x000000F0L;

	/** Display indexes in decimal */
	public static final long AP_IDXDEC = 0x00000000L;

	/** Display indexes in hex */
	public static final long AP_IDXHEX = 0x00000010L;

	/** Display indexes in octal */
	public static final long AP_IDXOCT = 0x00000020L;

	/** Display indexes in binary */
	public static final long AP_IDXBIN = 0x00000030L;

	/**
	 * Converts an operand of the item (instruction or data) to a binary
	 * number. The data items use only the type of the first operand.
	 * @param ea a linear address
	 * @param n the number of the operand:
	 *      0 - the first operand
	 *      1 - the second, third and all other operands
	 *     -1 - all operands
	 * @return true on success, false otherwise
	 */
	public static boolean OpBinary(long ea, int n) {
		return IdaJava.op_bin(ea, n);
	}

	/**
	 * Converts an operand of the item (instruction or data) to an octal
	 * number.
	 * @param ea a linear address
	 * @param n the number of the operand:
	 *      0 - the first operand
	 *      1 - the second, third and all other operands
	 *     -1 - all operands
	 * @return true on success, false otherwise
	 */
	public static boolean OpOctal(long ea, int n) {
		return IdaJava.op_oct(ea, n);
	}

	/**
	 * Converts an operand of the item (instruction or data) to a decimal
	 * number.
	 * @param ea a linear address
	 * @param n the number of the operand:
	 *      0 - the first operand
	 *      1 - the second, third and all other operands
	 *     -1 - all operands
	 * @return true on success, false otherwise
	 */
	public static boolean OpDecimal(long ea, int n) {
		return IdaJava.op_dec(ea, n);
	}

	/**
	 * Converts an operand of the item (instruction or data) to a hexadecimal
	 * number.
	 * @param ea a linear address
	 * @param n the number of the operand:
	 *      0 - the first operand
	 *      1 - the second, third and all other operands
	 *     -1 - all operands
	 * @return true on success, false otherwise
	 */
	public static boolean OpHex(long ea, int n) {
		return IdaJava.op_hex(ea, n);
	}

	/**
	 * Converts an operand of the item (instruction or data) to a character.
	 * @param ea a linear address
	 * @param n the number of the operand:
	 *      0 - the first operand
	 *      1 - the second, third and all other operands
	 *     -1 - all operands
	 * @return true on success, false otherwise
	 */
	public static boolean OpChr(long ea, int n) {
		return IdaJava.op_chr(ea, n);
	}

    /**
     * Converts an operand to an offset.
     * Example:
     * @code
     * seg000:2000 dw      1234h
     * @endcode
     * and there is a segment at paragraph 0x1000 and there is a data item
     * within the segment at 0x1234:
     * @code
     * seg000:1234 MyString        db 'Hello, world!',0
     * @endcode
     * Then you need to specify a linear address of the segment base to
     * create a proper offset:
     * @code
     * OpOff(["seg000",0x2000],0,0x10000);
     * @endcode
     * and you will have:
     * @code
     * seg000:2000 dw      offset MyString
     * @endcode
     * Motorola 680x0 processor have a concept of "outer offsets". If you want
     * to create an outer offset, you need to combine number of the operand
     * with the following bit: OPND_OUTER.
     * Please note that the outer offsets are meaningful only for Motorola
     * 680x0.
     *
     * @param ea a linear address
	 * @param n the number of the operand:
	 *      0 - the first operand
	 *      1 - the second, third and all other operands
	 *     -1 - all operands
     * @param base the base of the offset as a linear address. If
     *     base == BADADDR then the current operand becomes non-offset.
     */
   public static boolean OpOff(long ea, int n, long base) {
	   return IdaJava.set_offset(ea, n, base);
   }
		
	/** Outer offset base */
	public static final long OPND_OUTER = 0x80;

	/**
     * Converts an operand to a complex offset expression. This is a more
     * powerful version of the OpOff() method. It allows to explicitly specify
     * the reference type (off8, off16, etc.) and the expression target with a
     * possible target delta.
     * The complex expressions are represented by IDA in the following form:
     * @code
     * target + tdelta - base
     * @endcode
     * If the target is not present, then it will be calculated using
     * target = operand_value - tdelta + base
     * The target must be present for LOW.. and HIGH.. reference types
     * @param ea a linear address of the instruction/data
     * @param n the number of operand to convert (the same as in OpOff)
     * @param reftype one of the REF_... constants
     * @param target an explicitly specified expression target. if you don't
     *     want to specify it, use -1. Please note that LOW... and HIGH...
     *     reference type requre the target.
     * @param base the offset base (a linear address)
     * @param tdelta a displacement from the target which will be displayed in
     *     the expression.
     */
	public static boolean OpOffEx(long ea, int n, long reftype, long target,
			long base, long tdelta) {
		return IdaJava.op_offset(ea, n, (short) reftype, target, base, (int) tdelta) != 0;
	}

	/** 8bit full offset */
	public static final long REF_OFF8 = 0;

	/** 16bit full offset */
	public static final long REF_OFF16  = 1;

	/** 32bit full offset */
	public static final long REF_OFF32  = 2;

	/** Low 8bits of 16bit offset */
	public static final long REF_LOW8   = 3;

	/** Low 16bits of 32bit offset */
	public static final long REF_LOW16  = 4;

	/** High 8bits of 16bit offset */
	public static final long REF_HIGH8  = 5;

	/** High 16bits of 32bit offset */
	public static final long REF_HIGH16 = 6;

	/** High ph.high_fixup_bits of 32bit offset (processor dependent) */
	public static final long REF_VHIGH  = 7;

	/** Low (32-ph.high_fixup_bits) of 32bit offset (processor dependent) */
	public static final long REF_VLOW   = 8;

	/** 64bit full offset */
	public static final long REF_OFF64  = 9;

	/** Based reference (rva) */
	public static final long REFINFO_RVA = 0x10;

	/**
	 * Reference past an item - it may point to an nonexistitng address do not
	 * destroy alignment dirs
	 */
	public static final long REFINFO_PASTEND = 0x20;

	/**
	 * Offset base is a number - implies that base have be any value.
	 * @note Base xrefs are created only if base points to the middle of a
	 *     segment.
	 */
	public static final long REFINFO_NOBASE = 0x80; 

//// Convert operand to a segment expression
//// (for the explanations of 'ea' and 'n' please see OpBinary())
//	public static boolean OpSeg           (long ea,int n);
//// Convert operand to a number (with default number base, radix)
//// (for the explanations of 'ea' and 'n' please see OpBinary())
//	public static boolean OpNumber        (long ea,int n);
//// Convert operand to a floating-point number
//// (for the explanations of 'ea' and 'n' please see OpBinary())
//	public static boolean OpFloat         (long ea,int n);
//// Specify operand represenation manually.
//// (for the explanations of 'ea' and 'n' please see OpBinary())
////	      str - a string represenation of the operand
//// IDA will not check the specified operand, it will simply display
//// it instead of the orginal representation of the operand.
//	public static boolean OpAlt           (long ea,long n, final String str);// manually enter n-th operand
//// Change sign of the operand.
//// (for the explanations of 'ea' and 'n' please see OpBinary())
//success OpSign          (long ea,int n);        // change operand sign
//// Toggle the bitwise not operator for the operand
//// (for the explanations of 'ea' and 'n' please see OpBinary())
//public static boolean OpNot (long ea,int n);
//// Convert operand to a symbolic constant
//// (for the explanations of 'ea' and 'n' please see OpBinary())
////	      enumid - id of enumeration type
////	      serial - serial number of the constant in the enumeration
////	               The serial numbers are used if there are more than
////	               one symbolic constant with the same value in the
////	               enumeration. In this case the first defined constant
////	               get the serial number 0, then second 1, etc.
////	               There could be 256 symbolic constants with the same
////	               value in the enumeration.
//public static boolean OpEnumEx(long ea,int n,long enumid,long serial);
//// Convert operand to an offset in a structure
//// (for the explanations of 'ea' and 'n' please see OpBinary())
////	      strid - id of a structure type
////	      delta - struct offset delta. usually 0. denotes the difference
////	              between the structure base and the pointer into the structure.
//public static boolean OpStroffEx (long ea,int n,long strid, long delta); // make operand a struct offset
//// Convert operand to a stack variable
//// (for the explanations of 'ea' and 'n' please see OpBinary())
//public static boolean OpStkvar (long ea,int n); // make operand a stack variable
//// Convert operand to a high offset
//// High offset is the upper 16bits of an offset.
//// This type is used by TMS320C6 processors (and probably by other
//// RISC processors too)
//// (for the explanations of 'ea' and 'n' please see OpBinary())
////	      target - the full value (all 32bits) of the offset
//public static boolean OpHigh (long ea,int n,long target);
//// Mark the location as "variable"
//// Note: All that IDA does is to mark the location as "variable". Nothing else,
//// no additional analysis is performed.
//// This function may disappear in the future.
//void    MakeVar         (long ea);              // the location is 'variable'
//// Specify an additional line to display before the generated ones.
////	      ea   - linear address
////	      n    - number of anterior additioal line (0..MAX_ITEM_LINES)
////	      line - the line to display
//// IDA displays additional lines from number 0 up to the first unexisting
//// additional line. So, if you specify additional line #150 and there is no
//// additional line #149, your line will not be displayed.
//// MAX_ITEM_LINES is defined in IDA.CFG
//void    ExtLinA         (long ea,long n, final String line); // insert an additional line before the generated ones
//// Specify an additional line to display after the generated ones.
////	      ea   - linear address
////	      n    - number of posterior additioal line (0..MAX_ITEM_LINES)
////	      line - the line to display
//// IDA displays additional lines from number 0 up to the first unexisting
//// additional line. So, if you specify additional line #150 and there is no
//// additional line #149, your line will not be displayed.
//// MAX_ITEM_LINES is defined in IDA.CFG
//void    ExtLinB         (long ea,long n, final String line); // insert an additional line after the generated ones
//// Delete an additional anterior line
////	      ea   - linear address
////	      n    - number of anterior additioal line (0..500)
//void    DelExtLnA       (long ea,long n);       // delete an additional line before the generated ones
//// Delete an additional posterior line
////	      ea   - linear address
////	      n    - number of posterior additioal line (0..500)
//void    DelExtLnB       (long ea,long n);       // delete an additional line aftr  the generated ones
//// Specify instruction represenation manually.
////	      ea   - linear address
////	      insn - a string represenation of the operand
//// IDA will not check the specified instruction, it will simply display
//// it instead of the orginal representation.
//void    SetManualInsn   (long ea, string insn);
//// Get manual representation of instruction
////	      ea   - linear address
//// This function returns value set by SetManualInsn earlier.
//string    GetManualInsn   (long ea);
//// Change a byte in the debugged process memory only
////	      ea    - linear address
////	      value - new value of the byte
//// Returns: 1 if successful, 0 if not
////success PatchDbgByte    (long ea,long value);   // change a byte
//// Change value of a program byte
//// If debugger was active then the debugged process memory will be patched too
////	      ea    - linear address
////	      value - new value of the byte
//// Returns: 1 if successful, 0 if not
//success PatchByte       (long ea,long value);   // change a byte
//// Change value of a program word (2 bytes)
////	      ea    - linear address
////	      value - new value of the word
//// Returns: 1 if successful, 0 if not
//success PatchWord       (long ea,long value);   // change a word (2 bytes)
//// Change value of a double word
////	      ea    - linear address
////	      value - new value of the double word
//// Returns: 1 if successful, 0 if not
//success PatchDword      (long ea,long value);   // change a dword (4 bytes)
//// Set new value of flags
//// This function should not used be used directly if possible.
//// It changes properties of a program byte and if misused, may lead to
//// very-very strange results.
//void    SetFlags        (long ea,long flags);   // change internal flags for ea
//// Set value of a segment register.
////	      ea - linear address
////	      reg - name of a register, like "cs", "ds", "es", etc.
////	      value - new value of the segment register.
////	      tag   - one of SR_... constants
//// IDA keeps tracks of all the points where segment registers change their
//// values. This function allows you to specify the correct value of a segment
//// register if IDA is not able to find the corrent value.
//// See also SetReg() compatibility macro.
//success SetRegEx(long ea, final String reg,long value,long tag);
//
//public static final long SR_inherit = 1; // the value is inherited from the previous area
//public static final long SR_user = 2; // the value is specified by the user
//public static final long SR_auto = 3; // the value is determined by IDA
//public static final long SR_autostart = 4; // used as SR_auto for segment starting address
//
//// Plan to perform an action in the future.
//// This function will put your request to a special autoanalysis queue.
//// Later IDA will retrieve the request from the queue and process
//// it. There are several autoanalysis queue types. IDA will process all
//// queries from the first queue and then switch to the second queue, etc.
//// plan/unplan range of addresses
//public static void AutoMark2(long start, long end, long queuetype);
//public static void AutoUnmark(long start, long end,long queuetype);
//
//// plan to analyze an address
//public static void AutoMark(long ea, long qtype) {
//	AutoMark2(ea, ea + 1, qtype);
//}
//public static final long AU_UNK = 10; // make unknown
//public static final long AU_CODE = 20; // convert to instruction
//public static final long AU_PROC = 30; // make function
//public static final long AU_USED = 40; // reanalyze
//public static final long AU_LIBF = 60; // apply a flirt signature (the current signature!)
//public static final long AU_FINAL = 200; // coagulate unexplored items
//
///////////////////////////////////////////////////////////////////////////
//// Produce output files
///////////////////////////////////////////////////////////////////////////
//
//// Generate an output file
////	      type  - type of output file. One of OFILE_... symbols. See below.
////	      fp    - the output file handle
////	      ea1   - start address. For some file types this argument is ignored
////	      ea2   - end address. For some file types this argument is ignored
////	      flags - bit combination of GENFLG_...
//// returns: number of the generated lines.
////	          -1 if an error occured
////	          OFILE_EXE: 0-can't generate exe file, 1-ok
//public static int GenerateFile(long type, long file_handle, long ea1, long ea2, long flags);
//
//// output file types:
//public static final OFILE_MAP 0
//public static final OFILE_EXE 1
//public static final OFILE_IDC 2
//public static final OFILE_LST 3
//public static final OFILE_ASM 4
//public static final OFILE_DIF 5
//// output control flags:
//public static final GENFLG_MAPSEGS = 0x0001 // map: generate map of segments
//public static final GENFLG_MAPNAME = 0x0002 // map: include dummy names
//public static final GENFLG_MAPDMNG = 0x0004 // map: demangle names
//public static final GENFLG_MAPLOC = 0x0008 // map: include local names
//public static final GENFLG_IDCTYPE = 0x0008 // idc: gen only information about types
//public static final GENFLG_ASMTYPE = 0x0010 // asm&lst: gen information about types too
//public static final GENFLG_GENHTML = 0x0020 // asm&lst: generate html (gui version only)
//public static final GENFLG_ASMINC = 0x0040 // asm&lst: gen information only about types
//
//// Generate a flow chart GDL file
////	      outfile - output file name. GDL extension will be used
////	      title   - graph title
////	      ea1     - beginning of the area to flow chart
////	      ea2     - end of the area to flow chart. if ea2 == BADADDR
////	                then ea1 is treated as an address within a function.
////	                That function will be flow charted.
////	      flags   - combination of CHART_... constants
//success GenFuncGdl(final String outfile, string title, long ea1, long ea2, long flags);
//
//public static final CHART_PRINT_NAMES = 0x1000 // print labels for each block?
//public static final CHART_GEN_GDL = 0x4000 // generate .gdl file (file extension is forced to .gdl)
//public static final CHART_WINGRAPH = 0x8000 // call wingraph32 to display the graph
//public static final CHART_NOLIBFUNCS = 0x0400 // don't include library functions in the graph
//
//// Generate a function call graph GDL file
////	      outfile - output file name. GDL extension will be used
////	      title   - graph title
////	      ea1     - beginning of the area to flow chart
////	      ea2     - end of the area to flow chart. if ea2 == BADADDR
////	                then ea1 is treated as an address within a function.
////	                That function will be flow charted.
////	      flags   - combination of CHART_GEN_GDL, CHART_WINGRAPH, CHART_NOLIBFUNCS
//success GenCallGdl(final String outfile, string title, long flags);
///*------------------------------------------------------------------------*/
//static const char a_GenCallGdl[] =
//                        { VT_STR, VT_STR, VT_LONG,VT_LONG, VT_LONG, 0 };
//// ----------------------------------------------------------------------------
// C O M M O N   I N F O R M A T I O N
//// ----------------------------------------------------------------------------
//// Get IDA directory
//// This function returns the directory where IDA.EXE resides
//string    GetIdaDirectory ();
//// Get input file name
//// This function returns name of the file being disassembled
//string    GetInputFile    ();             // only the file name
//string    GetInputFilePath();             // full path
//// Set input file name
//// This function updates the file name that is stored in the database
//// It is used by the debugger and other parts of IDA
//// Use it when the database is moved to another location or when you
//// use remote debugging.
//void      SetInputFilePath(final String path);
//// Get IDB full path
//// This function returns full path of the current IDB database
//string    GetIdbPath();
//// Get MD5 hash of the input file.
//// This function returns the MD5 hash string of the input file (32 chars)
//string    GetInputMD5();
//// Get internal flags
////	      ea - linear address
//// returns: 32-bit value of internal flags. See start of IDC.IDC file
//// for explanations.
//	public static long    GetFlags        (long ea);              // get internal flags for ea
//// Get one byte (8-bit) of the program at 'ea' from the database
//// even if the debugger is active.
////	      ea - linear address
//// returns: byte value. If the byte has no value then = 0xFF is returned.
//// If the current byte size is different from 8 bits, then the returned value
//// may have more 1's.
//// To check if a byte has a value, use this expr: hasValue(GetFlags(ea))
//	public static long    IdbByte         (long ea);              // get a byte at ea
//// Get value of program byte
////	      ea - linear address
//// returns: value of byte. If byte has no value then returns = 0xFF
//// If the current byte size is different from 8 bits, then the returned value
//// might have more 1's.
//// To check if a byte has a value, use functions hasValue(GetFlags(ea))
//	public static long    Byte            (long ea);              // get a byte at ea
//// Get original value of program byte
////	      ea - linear address
//// returns: the original value of byte before any patch applied to it
//	public static long    GetOriginalByte(long ea);
//// Get value of program word (2 bytes)
////	      ea - linear address
//// returns: the value of the word. If word has no value then returns = 0xFFFF
//// If the current byte size is different from 8 bits, then the returned value
//// might have more 1's.
//	public static long    Word            (long ea);              // get a word (2 bytes) at ea
//// Get value of program double word (4 bytes)
////	      ea - linear address
//// returns: the value of the double word. If failed, returns -1
//	public static long    Dword           (long ea);              // get a double-word (4 bytes) at ea
//// Get value of program quadro word (8 bytes)
////	      ea - linear address
//// returns: the value of the quadro word. If failed, returns -1
//// Note: this function is available only in the 64-bit version of IDA Pro
//	public static long    Qword           (long ea);
//// Get value of a floating point number (4/8 bytes)
////	      ea - linear address
//// Returns: a floating point number at the specified address.
//// If the bytes at the specified address can not be represented as a floating
//// point number, then return -1.
//
//public static final GetFloat(ea) GetFpNum(ea, 4)
//public static final GetDouble(ea) GetFpNum(ea, 8)
//
//// Get linear address of a name
////	      from - the referring address.
////	             Allows to retrieve local label addresses in functions.
////	             If a local name is not found, then address of a global name is returned.
////	      name - name of program byte
//// returns: address of the name
////	          BADADDR - no such name
//// Dummy names (like byte_xxxx where xxxx are hex digits) are parsed by this
//// function to obtain the address. The database is not consulted for them.
//	public static long    LocByName       (final String name);              // BADADDR - no such name
//	public static long    LocByNameEx     (long from, string name);   // BADADDR - no such name
//// Get segment by segment base
////	      base - segment base paragraph or selector
//// returns: linear address of the start of the segment
////	          BADADDR - no such segment
//	public static long    SegByBase       (long base);            // BADADDR - no such segment
//// Get linear address of cursor
//	public static long    ScreenEA        ();                     // the current screen ea
//// Get the disassembly line at the cursor
//string  GetCurrentLine  ();
//// Get start address of the selected area
//// returns BADADDR - the user has not selected an area
//	public static long    SelStart        ();                     // the selected area start ea
//// BADADDR - no selected area
//// Get end address of the selected area
//// returns BADADDR - the user has not selected an area
//	public static long    SelEnd          ();                     // the selected area end ea
//// BADADDR - no selected area
//// Get value of segment register at the specified address
////	      ea - linear address
////	      reg - name of segment register
//// returns: the value of the segment register. The segment registers in
//// 32bit program usually contain selectors, so to get paragraph pointed by
//// the segment register you need to call AskSelector() function.
//	public static long    GetReg          (long ea, final String reg);     // get segment register value
//// Get next addresss in the program
////	      ea - linear address
//// returns: BADADDR - the specified address in the last used address
//	public static long    NextAddr        (long ea);              // returns next defined address
//// BADADDR if no such address exists
//// Get previous addresss in the program
////	      ea - linear address
//// returns: BADADDR - the specified address in the first address
//	public static long    PrevAddr        (long ea);              // returns prev defined address
//// BADADDR if no such address exists
//// Get next defined item (instruction or data) in the program
////	      ea    - linear address to start search from
////	      maxea - the search will stop at the address
////	              maxea is not included in the search range
//// returns: BADADDR - no (more) defined items
////	public static long    NextHead        (long ea, long maxea);  // returns next defined item address
////// BADADDR if no such address exists
//// Get previous defined item (instruction or data) in the program
////	      ea    - linear address to start search from
////	      minea - the search will stop at the address
////	              minea is included in the search range
//// returns: BADADDR - no (more) defined items
//	public static long    PrevHead        (long ea, long minea);  // returns prev defined item address
//// BADADDR if no such address exists
//// Get next not-tail address in the program
//// This function searches for the next displayable address in the program.
//// The tail bytes of instructions and data are not displayable.
////	      ea - linear address
//// returns: BADADDR - no (more) not-tail addresses
//	public static long    NextNotTail     (long ea);              // returns next not tail address
//// BADADDR if no such address exists
//// Get previous not-tail address in the program
//// This function searches for the previous displayable address in the program.
//// The tail bytes of instructions and data are not displayable.
////	      ea - linear address
//// returns: BADADDR - no (more) not-tail addresses
//	public static long    PrevNotTail     (long ea);              // returns prev not tail address
//// BADADDR if no such address exists
//// Get address of the end of the item (instruction or data)
////	      ea - linear address
//// returns: address past end of the item at 'ea'
//	public static long    ItemEnd         (long ea);              // returns address past end of
//// the item
//// Get size of instruction or data item in bytes
////	      ea - linear address
//// returns: 1..n
//	public static long    ItemSize        (long ea);              // returns item size, min answer=1
//// Get visible name of program byte
//// This function returns name of byte as it is displayed on the screen.
//// If a name contains illegal characters, IDA replaces them by the substitution
//// character during displaying. See IDA.CFG for the definition of the
//// substitution character.
////	      from - the referring address. may be BADADDR.
////	             Allows to retrieve local label addresses in functions.
////	             If a local name is not found, then a global name is returned.
////	      ea   - linear address
//// returns: 0 - byte has no name
//string  NameEx          (long from, long ea);   // get visible name of the byte
//// Get true name of program byte
//// This function returns name of byte as is without any replacements.
////	      from - the referring address. may be BADADDR.
////	             Allows to retrieve local label addresses in functions.
////	             If a local name is not found, then a global name is returned.
////	      ea   - linear address
//// returns: 0 - byte has no name
//string  GetTrueNameEx   (long from, long ea);   // get true name of the byte
//// Demangle a name
////	      name - name to demangle
////	      disable_mask - a mask that tells how to demangle the name
////	                     it is a good idea to get this mask using
////	                     GetLongPrm(INF_SHORT_DN) or GetLongPrm(INF_LONG_DN)
//// Returns: a demangled name
//// If the input name cannot be demangled, returns 0
//string  Demangle        (final String name, long disable_mask);
//// Get disassembly line
////	      ea - linear address of instruction
//// returns: 0 - no instruction at the specified location
//// note: this function may not return exactly the same mnemonics
//// as you see on the screen.
//string  GetDisasm       (long ea);              // get disassembly line
//// Get instruction mnemonics
////	      ea - linear address of instruction
//// returns: 0 - no instruction at the specified location
//// note: this function may not return exactly the same mnemonics
//// as you see on the screen.
//string  GetMnem         (long ea);              // get instruction name
//// Get operand of an instruction
////	      ea - linear address of instruction
////	      n  - number of operand:
////	              0 - the first operand
////	              1 - the second operand
//// returns: the current text representation of operand
//string  GetOpnd         (long ea,long n);       // get instruction operand
//// n=0 - first operand
//// Get type of instruction operand
////	      ea - linear address of instruction
////	      n  - number of operand:
////	              0 - the first operand
////	              1 - the second operand
//// returns:
////	      -1      bad operand number passed
//
//public static final o_void 0 // No Operand ----------
//public static final o_reg 1 // General Register (al,ax,es,ds...) reg
//public static final o_mem 2 // Direct Memory Reference (DATA) addr
//public static final o_phrase 3 // Memory Ref [Base Reg + Index Reg] phrase
//public static final o_displ 4 // Memory Reg [Base Reg + Index Reg + Displacement] phrase+addr
//public static final o_imm 5 // Immediate Value value
//public static final o_far 6 // Immediate Far Address (CODE) addr
//public static final o_near 7 // Immediate Near Address (CODE) addr
//public static final o_idpspec0 8 // IDP specific type
//public static final o_idpspec1 9 // IDP specific type
//public static final o_idpspec2 10 // IDP specific type
//public static final o_idpspec3 11 // IDP specific type
//public static final o_idpspec4 12 // IDP specific type
//public static final o_idpspec5 13 // IDP specific type
//// x86
//public static final o_trreg o_idpspec0 // trace register
//public static final o_dbreg o_idpspec1 // debug register
//public static final o_crreg o_idpspec2 // control register
//public static final o_fpreg o_idpspec3 // floating point register
//public static final o_mmxreg o_idpspec4 // mmx register
//public static final o_xmmreg o_idpspec5 // xmm register
//// arm
//public static final o_reglist o_idpspec1 // Register list (for LDM/STM)
//public static final o_creglist o_idpspec2 // Coprocessor register list (for CDP)
//public static final o_creg o_idpspec3 // Coprocessor register (for LDC/STC)
//public static final o_fpreg o_idpspec4 // Floating point register
//public static final o_fpreglist o_idpspec5 // Floating point register list
//public static final o_text (o_idpspec5+1) // Arbitrary text stored in the operand
//// ppc
//public static final o_spr o_idpspec0 // Special purpose register
//public static final o_twofpr o_idpspec1 // Two FPRs
//public static final o_shmbme o_idpspec2 // SH & MB & ME
//public static final o_crf o_idpspec3 // crfield x.reg
//public static final o_crb o_idpspec4 // crbit x.reg
//public static final o_dcr o_idpspec5 // Device control register
//
//	public static long    GetOpType       (long ea,long n);       // get operand type
//// Get number used in the operand
//// This function returns an immediate number used in the operand
////	      ea - linear address of instruction
////	      n  - the operand number
//// The return values are:
////	      operand is an immediate value => immediate value
////	      operand has a displacement => displacement
////	      operand is a direct memory ref => memory address
////	      operand is a register => register number
////	      operand is a register phrase => phrase number
////	      otherwise => -1
//	public static long    GetOperandValue (long ea,long n);       // get instruction operand value
//// Get anterior line
////	      ea - linear address
////	      num - number of anterior line (0..MAX_ITEM_LINES)
////	            MAX_ITEM_LINES is defined in IDA.CFG
//string  LineA           (long ea,long num);     // get additional line before generated ones
//// Get posterior line
////	      ea - linear address
////	      num - number of posterior line (0..MAX_ITEM_LINES)
//string  LineB           (long ea,long num);     // get additional line after generated ones
//// Get indented comment
////	      ea - linear address
////	      repeatable: 0-regular, !=0-repeatable comment
//string  CommentEx       (long ea, long repeatable);
//// Get manually entered operand string
////	      ea - linear address
////	      n  - number of operand:
////	              0 - the first operand
////	              1 - the second operand
//string  AltOp           (long ea,long n);       // get manually entered operand
//// Get string contents
////	      ea   - linear address
////	      len  - string length. -1 means to calculate the max string length
////	      type - the string type (one of ASCSTR_... constants)
//// Returns: string contents or empty string
//string GetString(long ea, long len, long type);
//// Get string type
////	      ea - linear address
//// Returns one of ASCSTR_... constants
//	public static long GetStringType(long ea);
////
////	      The following functions search for the specified byte
////	          ea - address to start from
////	          flag is combination of the following bits:
//
//public static final SEARCH_UP = 0x00 // search backward
//public static final SEARCH_DOWN = 0x01 // search forward
//public static final SEARCH_NEXT = 0x02 // start the search at the next/prev item
//public static final SEARCH_CASE = 0x04 // search case-sensitive
//// (only for bin&txt search)
//public static final SEARCH_REGEX = 0x08 // enable regular expressions (only for txt)
//public static final SEARCH_NOBRK = 0x10 // don't test ctrl-break
//public static final SEARCH_NOSHOW = 0x20 // don't display the search progress
//
////	      returns BADADDR - not found
////
//public static long FindVoid (long ea,long flag);
//public static long FindCode (long ea,long flag);
//public static long FindData (long ea,long flag);
//public static long FindUnexplored (long ea,long flag);
//public static long FindExplored (long ea,long flag);
//public static long FindImmediate (long ea,long flag,long value);
//public static long FindText (long ea,long flag,long y,long x, final String str);
//            // y - number of text line at ea to start from (0..MAX_ITEM_LINES)
//// x - x coordinate in this line
//public static long FindBinary(long ea,long flag, final String str);
//                // str - a string as a user enters it for Search Text in Core
////      example:  "41 42" - find 2 bytes 41h,42h
//// The default radix depends on the current IDP module
//// (radix for ibm pc is 16)
//// ----------------------------------------------------------------------------
//   G L O B A L   S E T T I N G S
//// ----------------------------------------------------------------------------
//// Parse one or more ida.cfg config directives
////	      line - directives to process, for example: PACK_DATABASE=2
//// If the directives are erroneous, a fatal error will be generated.
//// The changes will be effective only for the current session.
//   void  ChangeConfig(final String directive) {
//   }
//// The following functions allow you to set/get common parameters.
//// Please note that not all parameters can be set directly.
//	public static long GetLongPrm (long offset) {
//		return 0;
//	}
//	public static long GetShortPrm(long offset) {
//		return 0;
//	}
//	public static long GetCharPrm (long offset){
//		return 0;
//	}
//
//	public static boolean SetLongPrm(long offset, long value) {
//		return false;
//	}
//	public static boolean SetShortPrm(long offset, long value) {
//		return false;
//	}
//	public static boolean SetCharPrm (long offset, long value) {
//		return false;
//	}
//
//// 'offset' may be one of the following
///** short; Version of database */
//public static final long INF_VERSION 3;
///** char[8]; Name of current processor */
//public static final long INF_PROCNAME 5;
///** char; IDP-dependent flags */
//public static final long INF_LFLAGS 13;
///** decode floating point processor */
//public static final long LFLG_PC_FPP = 0x01;
//// instructions?
///** Flat model? */
//public static final long LFLG_PC_FLAT = 0x02;
///** 64-bit program? */
//public static final long LFLG_64BIT = 0x04;
///** do not store input full path */
//public static final long LFLG_DBG_NOPATH = 0x08;
///** is memory snapshot? */
//public static final long LFLG_SNAPSHOT = 0x10;
//// in debugger process options
///** char; display demangled names as: */
//public static final long INF_DEMNAMES 14;
///** comments */
//public static final long DEMNAM_CMNT 0;
///** regular names */
//public static final long DEMNAM_NAME 1;
///** don't display */
//public static final long DEMNAM_NONE 2;
///** assume gcc3 names (valid for gnu compiler) */
//public static final long DEMNAM_GCC3 4;
///** short; type of input file (see ida.hpp) */
//public static final long INF_FILETYPE 15;
///** MS DOS EXE File (obsolete) */
//public static final long FT_EXE_OLD 0;
///** MS DOS COM File (obsolete) */
//public static final long FT_COM_OLD 1;
///** Binary File */
//public static final long FT_BIN 2;
///** MS DOS Driver */
//public static final long FT_DRV 3;
///** New Executable (NE) */
//public static final long FT_WIN 4;
///** Intel Hex Object File */
//public static final long FT_HEX 5;
///** MOS Technology Hex Object File */
//public static final long FT_MEX 6;
///** Linear Executable (LX) */
//public static final long FT_LX 7;
///** Linear Executable (LE) */
//public static final long FT_LE 8;
///** Netware Loadable Module (NLM) */
//public static final long FT_NLM 9;
///** Common Object File Format (COFF) */
//public static final long FT_COFF 10;
///** Portable Executable (PE) */
//public static final long FT_PE 11;
///** Object Module Format */
//public static final long FT_OMF 12;
///** R-records */
//public static final long FT_SREC 13;
///** ZIP file (this file is never loaded to IDA database) */
//public static final long FT_ZIP 14;
///** Library of OMF Modules */
//public static final long FT_OMFLIB 15;
///** ar library */
//public static final long FT_AR 16;
///** file is loaded using LOADER DLL */
//public static final long FT_LOADER 17;
///** Executable and Linkable Format (ELF) */
//public static final long FT_ELF 18;
///** Watcom DOS32 Extender (W32RUN) */
//public static final long FT_W32RUN 19;
///** Linux a.out (AOUT) */
//public static final long FT_AOUT 20;
///** PalmPilot program file */
//public static final long FT_PRC 21;
///** MS DOS EXE File */
//public static final long FT_EXE 22;
///** MS DOS COM File */
//public static final long FT_COM 23;
///** AIX ar library */
//public static final long FT_AIXAR 24;
//public static final long INF_FCORESIZ 17
//public static final long INF_CORESTART 21
///** short; FLIRT: OS type the program is for */
//public static final long INF_OSTYPE 25;
//public static final long OSTYPE_MSDOS = 0x0001;
//public static final long OSTYPE_WIN = 0x0002;
//public static final long OSTYPE_OS2 = 0x0004;
//public static final long OSTYPE_NETW = 0x0008;
///** short; FLIRT: Application type */
//public static final long INF_APPTYPE 27;
///** console */
//public static final long APPT_CONSOLE = 0x0001;
///** graphics */
//public static final long APPT_GRAPHIC = 0x0002;
///** EXE */
//public static final long APPT_PROGRAM = 0x0004;
///** DLL */
//public static final long APPT_LIBRARY = 0x0008;
///** DRIVER */
//public static final long APPT_DRIVER = 0x0010;
///** Singlethread */
//public static final long APPT_1THREAD = 0x0020;
///** Multithread */
//public static final long APPT_MTHREAD = 0x0040;
///** 16 bit application */
//public static final long APPT_16BIT = 0x0080;
///** 32 bit application */
//public static final long APPT_32BIT = 0x0100;
///** int32; SP register value at the start of */
//public static final long INF_START_SP 29;
//// program execution
///** short; Analysis flags: */
//public static final long INF_AF 33;
///** Create offsets and segments using fixup info */
//public static final long AF_FIXUP = 0x0001;
///** Mark typical code sequences as code */
//public static final long AF_MARKCODE = 0x0002;
///** Delete instructions with no xrefs */
//public static final long AF_UNK = 0x0004;
///** Trace execution flow */
//public static final long AF_CODE = 0x0008;
///** Create functions if call is present */
//public static final long AF_PROC = 0x0010;
///** Analyze and create all xrefs */
//public static final long AF_USED = 0x0020;
///** Use flirt signatures */
//public static final long AF_FLIRT = 0x0040;
///** Create function if data xref data->code32 exists */
//public static final long AF_PROCPTR = 0x0080;
///** Rename jump functions as j_... */
//public static final long AF_JFUNC = 0x0100;
///** Rename empty functions as nullsub_... */
//public static final long AF_NULLSUB = 0x0200;
///** Create stack variables */
//public static final long AF_LVAR = 0x0400;
///** Trace stack pointer */
//public static final long AF_TRACE = 0x0800;
///** Create ascii string if data xref exists */
//public static final long AF_ASCII = 0x1000;
///** Convert 32bit instruction operand to offset */
//public static final long AF_IMMOFF = 0x2000;
///** Create offset if data xref to seg32 exists */
//public static final long AF_DREFOFF = 0x4000;
///** Final pass of analysis */
//public static final long AF_FINAL = 0x8000;
///** int32; IP register value at the start of */
//public static final long INF_START_IP = 35;
//// program execution
///** int32; Linear address of program entry point */
//public static final long INF_BEGIN_EA = 39;
///** int32; The lowest address used */
//public static final long INF_MIN_EA = 43;
//// in the program
///** int32; The highest address used */
//public static final long INF_MAX_EA = 47;
//// in the program - 1
//public static final long INF_OMIN_EA = 51;
//public static final long INF_OMAX_EA = 55;
///** int32; low limit of voids */
//public static final long INF_LOW_OFF = 59;
///** int32; high limit of voids */
//public static final long INF_HIGH_OFF = 63;
///** int32; max xref depth */
//public static final long INF_MAXREF = 67;
///** char; ASCII line break symbol */
//public static final long INF_ASCII_BREAK = 71;
//public static final long INF_WIDE_HIGH_BYTE_FIRST = 72;
///** char; Indention for instructions */
//public static final long INF_INDENT = 73;
///** char; Indention for comments */
//public static final long INF_COMMENT = 74;
///** char; Number of references to generate */
//public static final long INF_XREFNUM = 75;
//// 0 - xrefs won't be generated at all
///** char; Use '\t' chars in the output file? */
//public static final long INF_ENTAB = 76;
//public static final long INF_SPECSEGS = 77;
///** char; Display void marks? */
//public static final long INF_VOIDS = 78;
///** char; Display autoanalysis indicator? */
//public static final long INF_SHOWAUTO = 80;
///** char; Autoanalysis is enabled? */
//public static final long INF_AUTO = 81;
///** char; Generate borders? */
//public static final long INF_BORDER = 82;
///** char; Generate empty lines? */
//public static final long INF_NULL = 83;
///** char; General flags: */
//public static final long INF_GENFLAGS = 84;
///** Generate leading zeroes in numbers */
//public static final long INFFL_LZERO = 0x01;
///** May use constructs not supported by */
//public static final long INFFL_ALLASM = 0x02;
//// the target assembler
///** Loading an idc file that contains database info */
//public static final long INFFL_LOADIDC = 0x04;
///** char; Show line prefixes? */
//public static final long INF_SHOWPREF = 85;
///** char; line prefixes with segment name? */
//public static final long INF_PREFSEG = 86;
///** char; target assembler number (0..n) */
//public static final long INF_ASMTYPE = 87;
///** int32; base paragraph of the program */
//public static final long INF_BASEADDR = 88;
///** char; xrefs representation: */
//public static final long INF_XREFS = 92;
///** show segments in xrefs? */
//public static final long SW_SEGXRF = 0x01;
///** show xref type marks? */
//public static final long SW_XRFMRK = 0x02;
///** show function offsets? */
//public static final long SW_XRFFNC = 0x04;
///** show xref values? (otherwise-"...") */
//public static final long SW_XRFVAL = 0x08;
///** short; # of instruction bytes to show */
//public static final long INF_BINPREF = 93;
//// in line prefix
///** char; comments: */
//public static final long INF_CMTFLAG = 95;
///** show repeatable comments? */
//public static final long SW_RPTCMT = 0x01;
///** comment all lines? */
//public static final long SW_ALLCMT = 0x02;
///** no comments at all */
//public static final long SW_NOCMT = 0x04;
///** show source line numbers */
//public static final long SW_LINNUM = 0x08;
///** show microcode (if implemented) */
//public static final long SW_MICRO = 0x10;
///** char; dummy names represenation type */
//public static final long INF_NAMETYPE = 96;
//public static final long NM_REL_OFF = 0;
//public static final long NM_PTR_OFF = 1;
//public static final long NM_NAM_OFF = 2;
//public static final long NM_REL_EA = 3;
//public static final long NM_PTR_EA = 4;
//public static final long NM_NAM_EA = 5;
//public static final long NM_EA = 6;
//public static final long NM_EA4 = 7;
//public static final long NM_EA8 = 8;
//public static final long NM_SHORT = 9;
//public static final long NM_SERIAL = 10;
///** char; show bad instructions? */
//public static final long INF_SHOWBADS = 97;
//// an instruction is bad if it appears
//// in the ash.badworks array
///** char; line prefix type: */
//public static final long INF_PREFFLAG = 98;
///** show segment addresses? */
//public static final long PREF_SEGADR = 0x01;
///** show function offsets? */
//public static final long PREF_FNCOFF = 0x02;
///** show stack pointer? */
//public static final long PREF_STACK = 0x04;
///** char; pack database? */
//public static final long INF_PACKBASE = 99;
///** uchar; ascii flags */
//public static final long INF_ASCIIFLAGS = 100;
///** generate ASCII names? */
//public static final long ASCF_GEN = 0x01;
///** ASCII names have 'autogenerated' bit? */
//public static final long ASCF_AUTO = 0x02;
///** generate serial names? */
//public static final long ASCF_SERIAL = 0x04;
///** generate auto comment for ascii references? */
//public static final long ASCF_COMMENT = 0x10;
///** preserve case of ascii strings for identifiers */
//public static final long ASCF_SAVECASE = 0x20;
///** uchar; What names should be included in the list? */
//public static final long INF_LISTNAMES 101;
///** normal names */
//public static final long LN_NORMAL = 0x01;
///** public names */
//public static final long LN_PUBLIC = 0x02;
///** autogenerated names */
//public static final long LN_AUTO = 0x04;
///** weak names */
//public static final long LN_WEAK = 0x08;
///** char[16];ASCII names prefix */
//public static final long INF_ASCIIPREF = 102;
///** uint32; serial number */
//public static final long INF_ASCIISERNUM = 118;
///** char; leading zeroes */
//public static final long INF_ASCIIZEROES = 122;
///** char; order of bytes in 3-byte items */
//public static final long INF_TRIBYTE_ORDER = 125;
///** regular most significant byte first (big endian) - default */
//public static final long TRIBYTE_123 = 0;
//public static final long TRIBYTE_132 = 1;
//public static final long TRIBYTE_213 = 2;
//public static final long TRIBYTE_231 = 3;
//public static final long TRIBYTE_312 = 4;
///** regular least significant byte first (little endian) */
//public static final long TRIBYTE_321 = 5;
///** uchar; Byte order: 1==MSB first */
//public static final long INF_MF = 126;
///** char; Generate 'org' directives? */
//public static final long INF_ORG = 127;
///** char; Generate 'assume' directives? */
//public static final long INF_ASSUME = 128;
///** char; Check manual operands? */
//public static final long INF_CHECKARG = 129;
///** int32; value of SS at the start */
//public static final long INF_START_SS = 130;
///** int32; value of CS at the start */
//public static final long INF_START_CS = 134;
///** int32; address of main() */
//public static final long INF_MAIN = 138;
///** int32; short form of demangled names */
//public static final long INF_SHORT_DN = 142;
///** int32; long form of demangled names */
//public static final long INF_LONG_DN = 146;
//// see demangle.h for definitions
///** int32; data types allowed in data carousel */
//public static final long INF_DATATYPES = 150;
///** int32; current ascii string type */
//public static final long INF_STRTYPE = 154;
//// is considered as several bytes:
//// low byte:
///** Character-terminated ASCII string */
//public static final long ASCSTR_TERMCHR = 0;
///** C-string, zero terminated */
//public static final long ASCSTR_C = 0;
///** Pascal-style ASCII string (length byte) */
//public static final long ASCSTR_PASCAL = 1;
///** Pascal-style, length is 2 bytes */
//public static final long ASCSTR_LEN2 = 2;
///** Unicode string */
//public static final long ASCSTR_UNICODE = 3;
///** Delphi string, length is 4 bytes */
//public static final long ASCSTR_LEN4 = 4;
///** Pascal-style Unicode, length is 2 bytes */
//public static final long ASCSTR_ULEN2 = 5;
///** Pascal-style Unicode, length is 4 bytes */
//public static final long ASCSTR_ULEN4 = 6;
///** Last string type */
//public static final long ASCSTR_LAST = 6;
//// 2nd byte - termination chracters for ASCSTR_TERMCHR:
//public static long STRTERM1(long strtype) { return (strtype >> 8) & 0xFF; }
//// 3d byte:
//public static long STRTERM2(strtype) { return (strtype >> 16) & 0xFF; }
//// The termination characters are kept in
//// the 2nd and 3d bytes of string type
//// if the second termination character is
//// '\0', then it is ignored.
///** ushort; Analysis flags 2 */
//public static final long INF_AF2 = 158;
///** Locate and create jump tables */
//public static final long AF2_JUMPTBL = 0x0001;
///** Coagulate data segs at the final pass */
//public static final long AF2_DODATA = 0x0002;
///** Automatically hide library functions */
//public static final long AF2_HFLIRT = 0x0004;
///** Propagate stack argument information */
//public static final long AF2_STKARG = 0x0008;
///** Propagate register argument information */
//public static final long AF2_REGARG = 0x0010;
///** Check for unicode strings */
//public static final long AF2_CHKUNI = 0x0020;
///** Append a signature name comment for recognized anonymous library functions */
//public static final long AF2_SIGCMT = 0x0040;
///** Allow recognition of several copies of the same function */
//public static final long AF2_SIGMLT = 0x0080;
///** Create function tails */
//public static final long AF2_FTAIL = 0x0100;
///** Automatically convert data to offsets */
//public static final long AF2_DATOFF = 0x0200;
///** Perform 'no-return' analysis */
//public static final long AF2_ANORET = 0x0400;
///** Perform full stack pointer analysis */
//public static final long AF2_VERSP = 0x0800;
///** Coagulate code segs at the final pass */
//public static final long AF2_DOCODE = 0x1000;
///** Truncate functions upon code deletion */
//public static final long AF2_TRFUNC = 0x2000;
///** Control flow to data segment is ignored */
//public static final long AF2_PURDAT = 0x4000;
///** ushort; max name length (without zero byte) */
//public static final long INF_NAMELEN = 160;
///** ushort; max length of data lines */
//public static final long INF_MARGIN = 162;
///** ushort; max length of line with xrefs */
//public static final long INF_LENXREF = 164;
///** char[16];prefix of local names */
//public static final long INF_LPREFIX = 166;
//// if a new name has this prefix,
//// it will be automatically converted to a local name
///** uchar; length of the lprefix */
//public static final long INF_LPREFIXLEN = 182;
///** uchar; compiler */
//public static final long INF_COMPILER = 183;
///** mask to apply to get the pure compiler id */
//public static final long COMP_MASK = 0x0F;
///** Unknown */
//public static final long COMP_UNK = 0x00;
///** Visual C++ */
//public static final long COMP_MS = 0x01;
///** Borland C++ */
//public static final long COMP_BC = 0x02;
///** Watcom C++ */
//public static final long COMP_WATCOM = 0x03;
///** GNU C++ */
//public static final long COMP_GNU = 0x06;
///** Visual Age C++ */
//public static final long COMP_VISAGE = 0x07;
///** Delphi */
//public static final long COMP_BP = 0x08;
///** uchar; memory model & calling convention */
//public static final long INF_MODEL 184;
///** uchar; sizeof(int) */
//public static final long INF_SIZEOF_INT 185;
///** uchar; sizeof(bool) */
//public static final long INF_SIZEOF_BOOL 186;
///** uchar; sizeof(enum) */
//public static final long INF_SIZEOF_ENUM 187;
///** uchar; default alignment */
//public static final long INF_SIZEOF_ALGN 188;
//public static final long INF_SIZEOF_SHORT 189
//public static final long INF_SIZEOF_LONG 190
//public static final long INF_SIZEOF_LLONG 191
///** uint32; database change counter; keeps track of byte and segment modifications */
//public static final long INF_CHANGE_COUNTER 192;
///** uchar; sizeof(long double) */
//public static final long INF_SIZEOF_LDBL 196;
//
//// Offsets for 64-bit version
//public static final long INF_CORESTART64 = 25;
//public static final long INF_OSTYPE64 = 33;
//public static final long INF_APPTYPE64 = 35;
//public static final long INF_START_SP64 = 37;
//public static final long INF_AF64 = 45;
//public static final long INF_START_IP64 = 47;
//public static final long INF_BEGIN_EA64 = 55;
//public static final long INF_MIN_EA64 = 63;
//public static final long INF_MAX_EA64 = 71;
//public static final long INF_OMIN_EA64 = 79;
//public static final long INF_OMAX_EA64 = 87;
//public static final long INF_LOW_OFF64 = 95;
//public static final long INF_HIGH_OFF64 = 103;
//public static final long INF_MAXREF64 = 111;
//public static final long INF_ASCII_BREAK64 = 119;
//public static final long INF_WIDE_HIGH_BYTE_FIRST64 = 120;
//public static final long INF_INDENT64 = 121;
//public static final long INF_COMMENT64 = 122;
//public static final long INF_XREFNUM64 = 123;
//public static final long INF_ENTAB64 = 124;
//public static final long INF_SPECSEGS64 = 125;
//public static final long INF_VOIDS64 = 126;
//public static final long INF_SHOWAUTO64 = 128;
//public static final long INF_AUTO64 = 129;
//public static final long INF_BORDER64 = 130;
//public static final long INF_NULL64 = 131;
//public static final long INF_GENFLAGS64 = 132;
//public static final long INF_SHOWPREF64 = 133;
//public static final long INF_PREFSEG64 = 134;
//public static final long INF_ASMTYPE64 = 135;
//public static final long INF_BASEADDR64 = 136;
//public static final long INF_XREFS64 = 144;
//public static final long INF_BINPREF64 = 145;
//public static final long INF_CMTFLAG64 = 147;
//public static final long INF_NAMETYPE64 = 148
//public static final long INF_SHOWBADS64 = 149;
//public static final long INF_PREFFLAG64 = 150;
//public static final long INF_PACKBASE64 = 151;
//public static final long INF_ASCIIFLAGS64 = 152;
//public static final long INF_LISTNAMES64 = 153;
//public static final long INF_ASCIIPREF64 = 154;
//public static final long INF_ASCIISERNUM64 = 170;
//public static final long INF_ASCIIZEROES64 = 178;
//public static final long INF_MF64 = 182;
//public static final long INF_ORG64 = 183;
//public static final long INF_ASSUME64 = 184;
//public static final long INF_CHECKARG64 = 185;
//public static final long INF_START_SS64 = 186;
//public static final long INF_START_CS64 = 194;
//public static final long INF_MAIN64 = 202;
//public static final long INF_SHORT_DN64 = 210;
//public static final long INF_LONG_DN64 = 218;
//public static final long INF_DATATYPES64 = 226;
//public static final long INF_STRTYPE64 = 234;
//public static final long INF_AF264 = 242;
//public static final long INF_NAMELEN64 = 244;
//public static final long INF_MARGIN64 = 246;
//public static final long INF_LENXREF64 = 248;
//public static final long INF_LPREFIX64 = 250;
//public static final long INF_LPREFIXLEN64 = 266;
//public static final long INF_COMPILER64 = 267;
//public static final long INF_MODEL64 = 268;
//public static final long INF_SIZEOF_INT64 = 269;
//public static final long INF_SIZEOF_BOOL64 = 270;
//public static final long INF_SIZEOF_ENUM64 = 271;
//public static final long INF_SIZEOF_ALGN64 = 272;
//public static final long INF_SIZEOF_SHORT64 = 273;
//public static final long INF_SIZEOF_LONG64 = 274;
//public static final long INF_SIZEOF_LLONG64 = 275;
//public static final long INF_CHANGE_COUNTER = 276;
//public static final long INF_SIZEOF_LBDL64 = 280;
//////
////--------------------------------------------------------------------------
//// Change current processor
////	      processor - name of processor in short form.
////	                  run 'ida ?' to get list of allowed processor types
////	      level     - the power of request:
////	        SETPROC_COMPAT - search for the processor type in the current module
////	        SETPROC_ALL    - search for the processor type in all modules
////	                         only if there were not calls with SETPROC_USER
////	        SETPROC_USER   - search for the processor type in all modules
////	                         and prohibit level SETPROC_USER
////	        SETPROC_FATAL  - can be combined with previous bits.
////	                         means that if the processor type can't be
////	                         set, IDA should display an error message and exit.
//public static final long SETPROC_COMPAT = 0;
//public static final long SETPROC_ALL = 1;
//public static final long SETPROC_USER = 2;
//public static final long SETPROC_FATAL = 0x80;
//public static boolean SetPrcsr(final String processor) {
//	return SetProcessorType(processor, SETPROC_COMPAT);
//}
//
//public static boolean SetProcessorType (final String processor, long level); // set processor type
//// Enable/disable batch mode of operation
////	      batch:  0 - ida will display dialog boxes and wait for the user input
////	              1 - ida will not display dialog boxes, warnings, etc.
//// returns: old balue of batch flag
//public static boolean Batch(boolean batch);           // enable/disable batch mode
//// returns old value

/////////////////////////////////////////////////////////////////////////
// Interaction with the user
/////////////////////////////////////////////////////////////////////////

// Ask the user to enter a string
//	      defval - the default string value. This value
//	               will appear in the dialog box.
//	      prompt - the prompt to display in the dialog box
// Returns: the entered string or 0.
public static String AskStr(final String defval, final String prompt) {
	  return IdaJava.askstr(IdaJavaConstants.HIST_IDENT, defval, prompt);
}
// Ask the user to choose a file
//	      forsave- 0: "Open" dialog box, 1: "Save" dialog box
//	      mask   - the input file mask as "*.*" or the default file name.
//	      prompt - the prompt to display in the dialog box
// Returns: the selected file or 0.
public static String AskFile(boolean forsave, final String mask,
		final String prompt) {
	return IdaJava.askfile_c(forsave ? 1 : 0, mask, prompt);
}
// Ask the user to enter an address
//	      defval - the default address value. This value
//	               will appear in the dialog box.
//	      prompt - the prompt to display in the dialog box
// Returns: the entered address or BADADDR.
public static long AskAddr(long defval, final String prompt) {
	long[] val = new long[] { defval };
	if (IdaJava.askaddr(val, prompt) == 1)
		return val[0];
	return BADADDR;
}
//// Ask the user to enter a number
////	      defval - the default value. This value
////	               will appear in the dialog box.
////	      prompt - the prompt to display in the dialog box
//// Returns: the entered number or -1.
//	public static long    AskLong         (long defval, final String prompt); // -1 - no or bad input
//// Ask the user to enter a segment value
////	      defval - the default value. This value
////	               will appear in the dialog box.
////	      prompt - the prompt to display in the dialog box
//// Returns: the entered segment selector or BADSEL.
//	public static long    AskSeg          (long defval, final String prompt); // BADSEL - no or bad input
// // returns the segment selector
//// Ask the user to enter an identifier
////	      defval - the default identifier. This value
////	               will appear in the dialog box.
////	      prompt - the prompt to display in the dialog box
//// Returns: the entered identifier or 0.
//string  AskIdent        (final String defval, final String prompt);
//// Ask the user a question and let him answer Yes/No/Cancel
////	      defval - the default answer. This answer will be selected if the user
////	               presses Enter. -1:cancel,0-no,1-ok
////	      prompt - the prompt to display in the dialog box
//// Returns: -1:cancel,0-no,1-ok
//	public static long    AskYN           (long defval, final String prompt); // -1:cancel,0-no,1-ok
//// Display a message in the message window
////	      format - printf() style format string
////	      ...    - additional parameters if any
//// This function can be used to debug IDC scripts
//void    Message         (final String format,...);
//// Display a message in a message box
////	      format - printf() style format string
////	      ...    - additional parameters if any
//// This function can be used to debug IDC scripts
//// The user will be able to hide messages if they appear twice in a row on the screen
//void    Warning         (final String format,...);      // show a warning a dialog box
//// Display a fatal message in a message box and quit IDA
////	      format - printf() style format string
////	      ...    - additional parameters if any
//void    Fatal           (final String format,...);      // exit IDA immediately
//// Change IDA indicator.
//// Returns the previous status.
//	public static long    SetStatus       (long status);
//
//public static final IDA_STATUS_READY 0 // READY IDA is idle
//public static final IDA_STATUS_THINKING 1 // THINKING Analyzing but the user may press keys
//public static final IDA_STATUS_WAITING 2 // WAITING Waiting for the user input
//public static final IDA_STATUS_WORK 3 // BUSY IDA is busy
//
//// Refresh all disassembly views
//void    Refresh         (void);
//// Refresh all list views (names, functions, etc)
//void    RefreshLists    (void);
//// ----------------------------------------------------------------------------
////	                        S E G M E N T A T I O N
//// ----------------------------------------------------------------------------
////
//// ***********************************************
//// ** get a selector value
////	         arguments:      sel - the selector number
////	         returns:        selector value if found
////	                         otherwise the input value (sel)
////	         note:           selector values are always in paragraphs
//	public static long    AskSelector     (long sel);     // returns paragraph
//// ***********************************************
//// ** find a selector which has the specifed value
////	         arguments:      val - value to search for
////	         returns:        the selector number if found
////	                         otherwise the input value (val & = 0xFFFF)
////	         note:           selector values are always in paragraphs
//	public static long    FindSelector    (long val);
//// ***********************************************
//// ** set a selector value
////	         arguments:      sel - the selector number
////	                         val - value of selector
////	         returns:        nothing
////	         note:           ida supports up to 4096 selectors.
////	                         if 'sel' == 'val' then the
////	                         selector is destroyed because
////	                         it has no significance
//void    SetSelector     (long sel,long value);
//// ***********************************************
//// ** delete a selector
////	         arguments:      sel - the selector number to delete
////	         returns:        nothing
////	         note:           if the selector is found, it will
////	                         be deleted
//void    DelSelector     (long sel);
//// ***********************************************
//// ** SEGMENT FUNCTIONS
//// Get first segment
//// returns: linear address of the start of the first segment
//// BADADDR - no segments are defined
//	public static long    FirstSeg        ();                     // returns start of the first
//// segment, BADADDR - no segments
//// Get next segment
////	      ea - linear address
//// returns: start of the next segment
////	          BADADDR - no next segment
//	public static long    NextSeg         (long ea);              // returns start of the next
//// segment, BADADDR - no more segs
//// Get start address of a segment
////	      ea - any address in the segment
//// returns: start of segment
////	          BADADDR - the specified address doesn't belong to any segment
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long    SegStart        (long ea);              // returns start of the segment
//// BADADDR if bad address passed
//// Get end address of a segment
////	      ea - any address in the segment
//// returns: end of segment (an address past end of the segment)
////	          BADADDR - the specified address doesn't belong to any segment
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long    SegEnd          (long ea);              // return end of the segment
//// this address doesn't belong
//// to the segment
//// BADADDR if bad address passed
//// Get name of a segment
////	      ea - any address in the segment
//// returns: 0 - no segment at the specified address
public static String SegName(long ea) {
	final segment_t seg = IdaJava.getseg(ea);
	if (seg == null)
		return "";

	String result = new String(new char[255]); 
	int len = IdaJava.get_true_segm_name(seg, result, 255);
	return len != 0 ? result : "";
}
//
//// Create a new segment
////	      startea  - linear address of the start of the segment
////	      endea    - linear address of the end of the segment
////	                 this address will not belong to the segment
////	                 'endea' should be higher than 'startea'
////	      base     - base paragraph or selector of the segment.
////	                 a paragraph is 16byte memory chunk.
////	                 If a selector value is specified, the selector should be
////	                 already defined.
////	      use32    - 0: 16bit segment, 1: 32bit segment, 2: 64bit segment
////	      align    - segment alignment. see below for alignment values
////	      comb     - segment combination. see below for combination values.
//// returns: 0-failed, 1-ok
//success AddSeg(long startea,long endea,long base, long use32,long align,long comb);
//// Delete a segment
////   ea      - any address in the segment
////   flags   - combination of SEGMOD_... flags
//success DelSeg(long ea, long flags);
//
//public static final SEGMOD_KILL = 0x0001 // disable addresses if segment gets
// // shrinked or deleted
//public static final SEGMOD_KEEP = 0x0002 // keep information (code & data, etc)
//public static final SEGMOD_SILENT = 0x0004 // be silent
//
//// Change segment boundaries
////   ea      - any address in the segment
////   startea - new start address of the segment
////   endea   - new end address of the segment
////   flags   - combination of SEGMOD_... flags
//success SetSegBounds(long ea, long startea, long endea, long flags);
//// Change name of the segment
////   ea      - any address in the segment
////   name    - new name of the segment
//success RenameSeg(long ea, final String name);
//// Change class of the segment
////   ea      - any address in the segment
////   class   - new class of the segment
//success SetSegClass(long ea, final String class);
//// Change alignment of the segment
////   ea      - any address in the segment
////   align   - new alignment of the segment, one of sa... constants
//
//public static final SegAlign(ea, alignment) SetSegmentAttr(ea, SEGATTR_ALIGN, alignment)
//        public static final saAbs      0    // Absolute segment.
//public static final saRelByte 1 // Relocatable, byte aligned.
//public static final saRelWord 2 // Relocatable, word (2-byte, 16-bit) aligned.
//public static final saRelPara 3 // Relocatable, paragraph (16-byte) aligned.
//public static final saRelPage 4 // Relocatable, aligned on 256-byte boundary (a "page"
//// in the original Intel specification).
//public static final saRelDble 5 // Relocatable, aligned on a double word (4-byte)
//// boundary. This value is used by the PharLap OMF for
//// the same alignment.
//public static final saRel4K 6 // This value is used by the PharLap OMF for page (4K)
//// alignment. It is not supported by LINK.
//public static final saGroup 7 // Segment group
//public static final saRel32Bytes 8 // 32 bytes
//public static final saRel64Bytes 9 // 64 bytes
//public static final saRelQword 10 // 8 bytes
//
//// Change combination of the segment
////   ea      - any address in the segment
////   comb    - new combination of the segment, one of sc... constants
//
//public static final SegComb(ea, comb) SetSegmentAttr(ea, SEGATTR_COMB, comb)
//        public static final scPriv     0    // Private. Do not combine with any other program
//// segment.
//public static final scPub 2 // Public. Combine by appending at an offset that meets
//// the alignment requirement.
//public static final scPub2 4 // As defined by Microsoft, same as C=2 (public).
//public static final scStack 5 // Stack. Combine as for C=2. This combine type forces
//// byte alignment.
//public static final scCommon 6 // Common. Combine by overlay using maximum size.
//public static final scPub3 7 // As defined by Microsoft, same as C=2 (public).
//
//// Change segment addressing
////   ea      - any address in the segment
////   bitness - 0: 16bit, 1: 32bit, 2: 64bit
//success SetSegAddressing(long ea, long bitness);
//// Get segment by name
////	      segname - name of segment
//// returns: segment selector or BADADDR
//	public static long    SegByName       (final String segname);
//// Set default segment register value for a segment
////   ea      - any address in the segment
////	             if no segment is present at the specified address
////	             then all segments will be affected
////   reg     - name of segment register
////   value   - default value of the segment register. -1-undefined.
//success SetSegDefReg(long ea, final String reg,long value);
//// ***********************************************
//// ** set segment type
////	         arguments:      segea - any address within segment
////	                         type  - new segment type:
////	         returns:        !=0 - ok
//// note: this function is a macro, see its definition at the end of idc.idc
//
//public static final SEG_NORM 0
//public static final SEG_XTRN 1 // * segment with 'extern' definitions
////   no instructions are allowed
//public static final SEG_CODE 2 // pure code segment
//public static final SEG_DATA 3 // pure data segment
//public static final SEG_IMP 4 // implementation segment
//public static final SEG_GRP 6 // * group of segments
////   no instructions are allowed
//public static final SEG_NULL 7 // zero-length segment
//public static final SEG_UNDF 8 // undefined segment type
//public static final SEG_BSS 9 // uninitialized segment
//public static final SEG_ABSSYM 10 // * segment with definitions of absolute symbols
////   no instructions are allowed
//public static final SEG_COMM 11 // * segment with communal definitions
////   no instructions are allowed
//public static final SEG_IMEM 12 // internal processor memory & sfr (8051)
//
//success SetSegmentType  (long segea,long type);
//// ***********************************************
//// ** get segment attribute
////	         arguments:      segea - any address within segment
////	                         attr  - one of SEGATTR_... constants
//	public static long    GetSegmentAttr  (long segea,long attr);
//// ***********************************************
//// ** set segment attribute
////	         arguments:      segea - any address within segment
////	                         attr  - one of SEGATTR_... constants
//// Please note that not all segment attributes are modifiable.
//// Also some of them should be modified using special functions
//// like SetSegAddressing, etc.
//	public static long    SetSegmentAttr  (long segea, long attr, long value);
//
//#ifndef __EA64__
//public static final SEGATTR_START 0 // starting address
//public static final SEGATTR_END 4 // ending address
//public static final SEGATTR_ORGBASE 16
//public static final SEGATTR_ALIGN 20 // alignment
//public static final SEGATTR_COMB 21 // combination
//public static final SEGATTR_PERM 22 // permissions
//public static final SEGATTR_BITNESS 23 // bitness (0: 16, 1: 32, 2: 64 bit segment)
//// Note: modifying the attrbite directly does
//// not lead to the reanalysis of the segment.
//// Using SetSegAddressing() is more correct.
//public static final SEGATTR_FLAGS 24 // segment flags
//public static final SEGATTR_SEL 26 // segment selector
//public static final SEGATTR_ES 30 // default ES value
//public static final SEGATTR_CS 34 // default CS value
//public static final SEGATTR_SS 38 // default SS value
//public static final SEGATTR_DS 42 // default DS value
//public static final SEGATTR_FS 46 // default FS value
//public static final SEGATTR_GS 50 // default GS value
//public static final SEGATTR_TYPE 94 // segment type
//public static final SEGATTR_COLOR 95 // segment color
//#else
//public static final SEGATTR_START 0
//public static final SEGATTR_END 8
//public static final SEGATTR_ORGBASE 32
//public static final SEGATTR_ALIGN 40
//public static final SEGATTR_COMB 41
//public static final SEGATTR_PERM 42
//public static final SEGATTR_BITNESS 43
//public static final SEGATTR_FLAGS 44
//public static final SEGATTR_SEL 46
//public static final SEGATTR_ES 54
//public static final SEGATTR_CS 62
//public static final SEGATTR_SS 70
//public static final SEGATTR_DS 78
//public static final SEGATTR_FS 86
//public static final SEGATTR_GS 94
//public static final SEGATTR_TYPE 182
//public static final SEGATTR_COLOR 183
//
//// Valid segment flags
//public static final SFL_COMORG = 0x01 // IDP dependent field (IBM PC: if set, ORG directive is not commented out)
//public static final SFL_OBOK = 0x02 // orgbase is present? (IDP dependent field)
//public static final SFL_HIDDEN = 0x04 	// is the segment hidden?
//public static final SFL_DEBUG = 0x08 // is the segment created for the debugger?
//public static final SFL_LOADER = 0x10 // is the segment created by the loader?
//public static final SFL_HIDETYPE = 0x20 // hide segment type (do not print it in the listing)
//
//// Move a segment to a new address
//// This function moves all information to the new address
//// It fixes up address sensitive information in the kernel
//// The total effect is equal to reloading the segment to the target address
////	      ea    - any address within the segment to move
////	      to    - new segment start address
////	      flags - combination MFS_... constants
//// returns: MOVE_SEGM_... error code
//
//public static final MSF_SILENT = 0x0001 // don't display a "please wait" box on the screen
//public static final MSF_NOFIX = 0x0002 // don't call the loader to fix relocations
//public static final MSF_LDKEEP = 0x0004 // keep the loader in the memory (optimization)
//public static final MSF_FIXONCE = 0x0008 // valid for rebase_program(): call loader only once
//
//	public static long MoveSegm(long ea, long to, long flags);
//
//public static final MOVE_SEGM_OK 0 // all ok
//public static final MOVE_SEGM_PARAM -1 // The specified segment does not exist
//public static final MOVE_SEGM_ROOM -2 // Not enough free room at the target address
//public static final MOVE_SEGM_IDP -3 // IDP module forbids moving the segment
//public static final MOVE_SEGM_CHUNK -4 // Too many chunks are defined, can't move
//public static final MOVE_SEGM_LOADER -5 // The segment has been moved but the loader complained
//public static final MOVE_SEGM_ODD -6 // Can't move segments by an odd number of bytes
//
//// Rebase the whole program by 'delta' bytes
////	      delta - number of bytes to move the program
////	      flags - combination of MFS_... constants
////	              it is recommended to use MSF_FIXONCE so that the loader takes
////	              care of global variables it stored in the database
//// returns: error code MOVE_SEGM_...
//	public static long rebase_program(long delta, long flags);
//// Set storage type
////	      startEA - starting address
////	      endEA   - ending address
////	      stt     - new storage type, one of STT_VA and STT_MM
//// returns: 0 - ok, otherwise internal error code
//	public static long SetStorageType(long startEA, long endEA, long stt);
//
//public static final STT_VA 0 // regular storage: virtual arrays, an explicit flag for each byte
//public static final STT_MM 1 // memory map: sparse storage. useful for huge objects
//
//// ----------------------------------------------------------------------------
////	                    C R O S S   R E F E R E N C E S
//// ----------------------------------------------------------------------------
////	      See sample file xrefs.idc to learn to use these functions.
////	      Flow types (combine with XREF_USER!):
//
//public static final fl_CF 16 // Call Far
//public static final fl_CN 17 // Call Near
//public static final fl_JF 18 // Jump Far
//public static final fl_JN 19 // Jump Near
//public static final fl_F 21 // Ordinary flow
//public static final XREF_USER 32 // All user-specified xref types
//// must be combined with this bit
//
//                                        // Mark exec flow 'from' 'to'
//void    AddCodeXref(long From,long To,long flowtype);
//	public static long    DelCodeXref(long From,long To,int undef);// Unmark exec flow 'from' 'to'
//// undef - make 'To' undefined if no
////        more references to it
//// returns 1 - planned to be
//// made undefined
//// The following functions include the ordinary flows:
//// (the ordinary flow references are returned first)
//	public static long    Rfirst  (long From);            // Get first code xref from 'From'
//	public static long    Rnext   (long From,long current);// Get next code xref from
//	public static long    RfirstB (long To);              // Get first code xref to 'To'
//	public static long    RnextB  (long To,long current); // Get next code xref to 'To'
//// The following functions don't take into account the ordinary flows:
//	public static long    Rfirst0 (long From);
//	public static long    Rnext0  (long From,long current);
//	public static long    RfirstB0(long To);
//	public static long    RnextB0 (long To,long current);
////	      Data reference types (combine with XREF_USER!):
//
//public static final dr_O 1 // Offset
//public static final dr_W 2 // Write
//public static final dr_R 3 // Read
//public static final dr_T 4 // Text (names in manual operands)
//public static final dr_I 5 // Informational
//
//void    add_dref(long From,long To,long drefType);      // Create Data Ref
//void    del_dref(long From,long To);    // Unmark Data Ref
//	public static long    Dfirst  (long From);            // Get first data xref from 'From'
//	public static long    Dnext   (long From,long current);
//	public static long    DfirstB (long To);              // Get first data xref to 'To'
//	public static long    DnextB  (long To,long current);
//	public static long    XrefType(void);                 // returns type of the last xref
//// obtained by [RD]first/next[B0]
//// functions. Return values
//// are fl_... or dr_...
//
//	/////////////////////////////////////////////////////////////////////////
//	// File I/O
//	/////////////////////////////////////////////////////////////////////////
//
//	// Open a file. Use Java functionality instead
//	public static long fopen(final String file, final String mode);
//// ***********************************************
//// ** close a file
////	         arguments:      file handle
////	         returns:        nothing
//void    fclose          (long handle);
//// ***********************************************
//// ** get file length
////	         arguments:      file handle
////	         returns:        -1 - error
////	                         otherwise file length in bytes
//	public static long    filelength      (long handle);
//// ***********************************************
//// ** set cursor position in the file
////	         arguments:      handle  - file handle
////	                         offset  - offset from origin
////	                         origin  - 0 = from the start of file
////	                                   1 = from the current cursor position
////	                                   2 = from the end of file
////	         returns:        0 - ok
////	                         otherwise error
//	public static long    fseek           (long handle,long offset,long origin);
//// ***********************************************
//// ** get cursor position in the file
////	         arguments:      file handle
////	         returns:        -1 - error
////	                         otherwise current cursor position
//	public static long    ftell           (long handle);
//// ***********************************************
//// ** load file into IDA database
////	         arguments:      handle  - file handle
////	                         pos     - position in the file
////	                         ea      - linear address to load
////	                         size    - number of bytes to load
////	         returns:        0 - error
////	                         1 - ok
//success loadfile        (long handle,long pos,long ea,long size);
//// ***********************************************
//// ** save from IDA database to file
////	         arguments:      handle  - file handle
////	                         pos     - position in the file
////	                         ea      - linear address to save from
////	                         size    - number of bytes to save
////	         returns:        0 - error
////	                         1 - ok
//success savefile        (long handle,long pos,long ea,long size);
//// ***********************************************
//// ** read one byte from file
////	         arguments:      handle  - file handle
////	         returns:        -1 - error
////	                         otherwise a byte read.
//	public static long    fgetc           (long handle);
//// ***********************************************
//// ** write one byte to file
////	         arguments:      handle  - file handle
////	                         byte    - byte to write
////	         returns:        0 - ok
////	                         -1 - error
//	public static long    fputc           (long byte,long handle);
//// ***********************************************
//// ** fprintf
////	         arguments:      handle  - file handle
////	                         format  - format string
////	         returns:        0 - ok
////	                         -1 - error
//	public static long    fprintf         (long handle, final String format,...);
//// ***********************************************
//// ** read 2 bytes from file
////	         arguments:      handle  - file hanlde
////	                         mostfirst 0 - least significant byte is first (intel)
////	                                   1 - most  significant byte is first
////	         returns:        -1 - error
////	                         otherwise: a 16-bit value
//	public static long    readshort       (long handle,long mostfirst);
//// ***********************************************
//// ** read 4 bytes from file
////	         arguments:      handle  - file hanlde
////	                         mostfirst 0 - least significant byte is first (intel)
////	                                   1 - most  significant byte is first
////	         returns:        a 32-bit value
//	public static long    readlong        (long handle,long mostfirst);
//// ***********************************************
//// ** write 2 bytes to file
////	         arguments:      handle  - file hanlde
////	                         word    - a 16-bit value to write
////	                         mostfirst 0 - least significant byte is first (intel)
////	                                   1 - most  significant byte is first
////	         returns:        0 - ok
//	public static long    writeshort      (long handle,long word,long mostfirst);
//// ***********************************************
//// ** write 4 bytes to file
////	         arguments:      handle  - file hanlde
////	                         dword   - a 32-bit value to write
////	                         mostfirst 0 - least significant byte is first (intel)
////	                                   1 - most  significant byte is first
////	         returns:        0 - ok
//	public static long    writelong       (long handle,long dword,long mostfirst);
//// ***********************************************
//// ** read a string from file
////	         arguments:      handle  - file hanlde
////	         returns:        a string
////	                         on EOF, returns -1
//string    readstr         (long handle);
//// ***********************************************
//// ** write a string to file
////	         arguments:      handle  - file hanlde
////	                         str     - string to write
////	         returns:        0 - ok
//	public static long    writestr        (long handle, final String str);
//// ----------------------------------------------------------------------------
////	                           F U N C T I O N S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** create a function
////	         arguments:      start,end - function bounds
////	                         If the function end address is BADADDR, then
////	                         IDA will try to determine the function bounds
////	                         automatically. IDA will define all necessary
////	                         instructions to determine the function bounds.
////	         returns:        !=0 - ok
////	         note:           an instruction should be present at the start address
//success MakeFunction(long start,long end);
//// ***********************************************
//// ** delete a function
////	         arguments:      ea - any address belonging to the function
////	         returns:        !=0 - ok
//success DelFunction(long ea);
//// ***********************************************
//// ** change function end address
////	         arguments:      ea - any address belonging to the function
////	                         end - new function end address
////	         returns:        !=0 - ok
//success SetFunctionEnd(long ea,long end);
//// ***********************************************
//// ** find next function
////	         arguments:      ea - any address belonging to the function
////	         returns:        -1 - no more functions
////	                         otherwise returns the next function start address
//	public static long NextFunction(long ea);
//// ***********************************************
//// ** find previous function
////	         arguments:      ea - any address belonging to the function
////	         returns:        -1 - no more functions
////	                         otherwise returns the previous function start address
//	public static long PrevFunction(long ea);
//// ***********************************************
//// ** get a function attribute
////	         arguments:      ea - any address belonging to the function
////	                         attr - one of FUNCATTR_... constants
////	         returns:        -1 - error
////	                         otherwise returns the attribute value
//	public static long GetFunctionAttr(long ea, long attr);
//
//#ifndef __EA64__
//public static final FUNCATTR_START 0 // function start address
//public static final FUNCATTR_END 4 // function end address
//public static final FUNCATTR_FLAGS 8 // function flags
//public static final FUNCATTR_FRAME 10 // function frame id
//public static final FUNCATTR_FRSIZE 14 // size of local variables
//public static final FUNCATTR_FRREGS 18 // size of saved registers area
//public static final FUNCATTR_ARGSIZE 20 // number of bytes purged from the stack
//public static final FUNCATTR_FPD 24 // frame pointer delta
//public static final FUNCATTR_COLOR 28 // function color code
//public static final FUNCATTR_OWNER 10 // chunk owner (valid only for tail chunks)
//public static final FUNCATTR_REFQTY 14 // number of chunk parents (valid only for tail chunks)
//#else
//public static final FUNCATTR_START 0
//public static final FUNCATTR_END 8
//public static final FUNCATTR_FLAGS 16
//public static final FUNCATTR_FRAME 18
//public static final FUNCATTR_FRSIZE 26
//public static final FUNCATTR_FRREGS 34
//public static final FUNCATTR_ARGSIZE 36
//public static final FUNCATTR_FPD 44
//public static final FUNCATTR_COLOR 52
//public static final FUNCATTR_OWNER 18
//public static final FUNCATTR_REFQTY 26
//
//
//// ***********************************************
//// ** set a function attribute
////	         arguments:      ea - any address belonging to the function
////	                         attr - one of FUNCATTR_... constants
////	                         value - new value of the attribute
////	         returns:        1-ok, 0-failed
//success SetFunctionAttr(long ea, long attr, long value);
//// ***********************************************
//// ** retrieve function flags
////	         arguments:      ea - any address belonging to the function
////	         returns:        -1 - function doesn't exist
////	                         otherwise returns the flags:
//
//public static final FUNC_NORET = 0x00000001L // function doesn't return
//public static final FUNC_FAR = 0x00000002L // far function
//public static final FUNC_LIB = 0x00000004L // library function
//public static final FUNC_STATIC = 0x00000008L // static function
//public static final FUNC_FRAME = 0x00000010L // function uses frame pointer (BP)
//public static final FUNC_USERFAR = 0x00000020L // user has specified far-ness
//// of the function
//public static final FUNC_HIDDEN = 0x00000040L // a hidden function
//public static final FUNC_THUNK = 0x00000080L // thunk (jump) function
//public static final FUNC_BOTTOMBP = 0x00000100L // BP points to the bottom of the stack frame
//
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long GetFunctionFlags(long ea);
//// ***********************************************
//// ** change function flags
////	         arguments:      ea - any address belonging to the function
////	                         flags - see GetFunctionFlags() for explanations
////	         returns:        !=0 - ok
//// note: this function is a macro, see its definition at the end of idc.idc
//success SetFunctionFlags(long ea,long flags);
//// ***********************************************
//// ** retrieve function name
////	         arguments:      ea - any address belonging to the function
////	         returns:        0 - function doesn't exist
////	                         otherwise returns function name
//string GetFunctionName(long ea);
//// ***********************************************
//// ** retrieve function comment
////	         arguments:      ea - any address belonging to the function
////	                         repeatable - 1: get repeatable comment
////	                                      0: get regular comment
////	         returns:        function comment string
//string GetFunctionCmt(long ea, long repeatable);
//// ***********************************************
//// ** set function comment
////	         arguments:      ea - any address belonging to the function
////	                         cmt - a function comment line
////	                         repeatable - 1: get repeatable comment
////	                                      0: get regular comment
//void SetFunctionCmt(long ea, string cmt, long repeatable);
//// ***********************************************
//// ** ask the user to select a function
////	         arguments:      title - title of the dialog box
////	         returns:        -1 - user refused to select a function
////	                         otherwise returns the selected function start address
//	public static long ChooseFunction(final String title);
//// ***********************************************
//// ** convert address to 'funcname+offset' string
////	         arguments:      ea - address to convert
////	         returns:        if the address belongs to a function then
////	                           return a string formed as 'name+offset'
////	                           where 'name' is a function name
////	                           'offset' is offset within the function
////	                         else
////	                           return 0
//string GetFuncOffset(long ea);
//// ***********************************************
//// ** Determine a new function boundaries
//// **
////	         arguments:      ea  - starting address of a new function
////	         returns:        if a function already exists, then return
////	                         its end address.
////	                         if a function end cannot be determined,
////	                         the return BADADDR
////	                         otherwise return the end address of the new function
//	public static long FindFuncEnd(long ea);
//// ***********************************************
//// ** Get ID of function frame structure
//// **
////	         arguments:      ea - any address belonging to the function
////	         returns:        ID of function frame or -1
////	                         In order to access stack variables you need to use
////	                         structure member manipulaion functions with the
////	                         obtained ID.
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long GetFrame(long ea);
//// ***********************************************
//// ** Get size of local variables in function frame
//// **
////	         arguments:      ea - any address belonging to the function
////	         returns:        Size of local variables in bytes.
////	                         If the function doesn't have a frame, return 0
////	                         If the function does't exist, return -1
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long GetFrameLvarSize(long ea);
//// ***********************************************
//// ** Get size of saved registers in function frame
//// **
////	         arguments:      ea - any address belonging to the function
////	         returns:        Size of saved registers in bytes.
////	                         If the function doesn't have a frame, return 0
////	                         This value is used as offset for BP
////	                         (if FUNC_FRAME is set)
////	                         If the function does't exist, return -1
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long GetFrameRegsSize(long ea);
//// ***********************************************
//// ** Get size of arguments in function frame which are purged upon return
//// **
////	         arguments:      ea - any address belonging to the function
////	         returns:        Size of function arguments in bytes.
////	                         If the function doesn't have a frame, return 0
////	                         If the function does't exist, return -1
//// note: this function is a macro, see its definition at the end of idc.idc
//	public static long GetFrameArgsSize(long ea);
//// ***********************************************
//// ** Get full size of function frame
//// **
////	         arguments:      ea - any address belonging to the function
////	         returns:        Size of function frame in bytes.
////	                         This function takes into account size of local
////	                         variables + size of saved registers + size of
////	                         return address + size of function arguments
////	                         If the function doesn't have a frame, return size of
////	                         function return address in the stack.
////	                         If the function does't exist, return 0
//	public static long GetFrameSize(long ea);
//// ***********************************************
//// ** Make function frame
//// **
////	         arguments:      ea      - any address belonging to the function
////	                         lvsize  - size of function local variables
////	                         frregs  - size of saved registers
////	                         argsize - size of function arguments
////	         returns:        ID of function frame or -1
////	                         If the function did not have a frame, the frame
////	                         will be created. Otherwise the frame will be
////	                         modified
//	public static long MakeFrame(long ea,long lvsize,long frregs,long argsize);
//// ***********************************************
//// ** Get current delta for the stack pointer
//// **
////	         arguments:      ea      - end address of the instruction
////	                                   i.e.the last address of the instruction+1
////	         returns:        The difference between the original SP upon
////	                         entering the function and SP for
////	                         the specified address
//	public static long GetSpd(long ea);
//// ***********************************************
//// ** Get modification of SP made by the instruction
//// **
////	         arguments:      ea      - end address of the instruction
////	                                   i.e.the last address of the instruction+1
////	         returns:        Get modification of SP made at the specified location
////	                         If the specified location doesn't contain a SP
////	                         change point, return 0
////	                         Otherwise return delta of SP modification
//	public static long GetSpDiff(long ea);
//// ***********************************************
//// ** Setup modification of SP made by the instruction
//// **
////	         arguments:      ea      - end address of the instruction
////	                                   i.e.the last address of the instruction+1
////	                         delta   - the difference made by the current
////	                                   instruction.
////	         returns:        1-ok, 0-failed
//success SetSpDiff(long ea,long delta);
//// Below are the function chunk (or function tail) related functions
//// ***********************************************
//// ** Get a function chunk attribute
//// **
////	         arguments:      ea     - any address in the chunk
////	                         attr   - one of: FUNCATTR_START, FUNCATTR_END
////	                                  FUNCATTR_OWNER, FUNCATTR_REFQTY
////	         returns:        desired attribute or -1
//	public static long GetFchunkAttr(long ea, long attr);
//// ***********************************************
//// ** Set a function chunk attribute
//// **
////	         arguments:      ea     - any address in the chunk
////	                         attr   - nothing defined yet
////	                         value  - desired bg color (RGB)
////	         returns:        0 if failed, 1 if success
//success SetFchunkAttr(long ea, long attr, long value);
//// ***********************************************
//// ** Get a function chunk referer
//// **
////	         arguments:      ea     - any address in the chunk
////	                         idx    - referer index (0..GetFchunkAttr(FUNCATTR_REFQTY))
////	         returns:        referer address or BADADDR
//	public static long GetFchunkReferer(long ea, long idx);
//// ***********************************************
//// ** Get next function chunk
//// **
////	         arguments:      ea     - any address
////	         returns:        the starting address of the next
////	                         function chunk or BADADDR
//// This function enumerates all chunks of all functions in the database
//	public static long NextFchunk(long ea);
//// ***********************************************
//// ** Get previous function chunk
//// **
////	         arguments:      ea     - any address
////	         returns:        the starting address of the previous
////	                         function chunk or BADADDR
//// This function enumerates all chunks of all functions in the database
//	public static long PrevFchunk(long ea);
//// ***********************************************
//// ** Append a function chunk to the function
//// **
////	         arguments:      funcea   - any address in the function
////	                         ea1, ea2 - boundaries of a function tail
////	                                    to add. If a chunk exists at the
////	                                    specified addresses, it must have exactly
////	                                    the specified boundaries
////	         returns:        0 if failed, 1 if success
//success AppendFchunk(long funcea, long ea1, long ea2);
//// ***********************************************
//// ** Remove a function chunk from the function
//// **
////	         arguments:      funcea - any address in the function
////	                         ea1    - any address in the function chunk
////	                                  to remove
////	         returns:        0 if failed, 1 if success
//success RemoveFchunk(long funcea, long tailea);
//// ***********************************************
//// ** Change the function chunk owner
//// **
////	         arguments:      tailea - any address in the function chunk
////	                         funcea - the starting address of the new owner
////	         returns:        0 if failed, 1 if success
//// The new owner must already have the chunk appended before the call
//success SetFchunkOwner(long tailea, long funcea);
//// ***********************************************
//// ** Get the first function chunk of the specified function
//// **
////	         arguments:      funcea - any address in the function
////	         returns:        the function entry point or BADADDR
//// This function returns the first (main) chunk of the specified function
//	public static long FirstFuncFchunk(long funcea);
//// ***********************************************
//// ** Get the next function chunk of the specified function
//// **
////	         arguments:      funcea - any address in the function
////	                         tailea - any address in the current chunk
////	         returns:        the starting address of the next
////	                         function chunk or BADADDR
//// This function returns the next chunk of the specified function
//	public static long NextFuncFchunk(long funcea, long tailea);
//// ----------------------------------------------------------------------------
////	                        E N T R Y   P O I N T S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** retrieve number of entry points
////	         arguments:      none
////	         returns:        number of entry points
//	public static long GetEntryPointQty(void);
//// ***********************************************
//// ** add entry point
////	         arguments:      ordinal  - entry point number
////	                                    if entry point doesn't have an ordinal
////	                                    number, 'ordinal' should be equal to 'ea'
////	                         ea       - address of the entry point
////	                         name     - name of the entry point. If null string,
////	                                    the entry point won't be renamed.
////	                         makecode - if 1 then this entry point is a start
////	                                    of a function. Otherwise it denotes data
////	                                    bytes.
////	         returns:        0 - entry point with the specifed ordinal already
////	                                 exists
////	                         1 - ok
//success AddEntryPoint(long ordinal,long ea, final String name,long makecode);
//// ***********************************************
//// ** retrieve entry point ordinal number
////	         arguments:      index - 0..GetEntryPointQty()-1
////	         returns:        0 if entry point doesn't exist
////	                         otherwise entry point ordinal
//	public static long GetEntryOrdinal(long index);
//// ***********************************************
//// ** retrieve entry point address
////	         arguments:      ordinal - entry point number
////	                                   it is returned by GetEntryPointOrdinal()
////	         returns:        -1 if entry point doesn't exist
////	                         otherwise entry point address.
////	                         If entry point address is equal to its ordinal
////	                         number, then the entry point has no ordinal.
//	public static long GetEntryPoint(long ordinal);
//// ***********************************************
//// ** retrieve entry point name
////	         arguments:      ordinal - entry point number
////	                                   it is returned by GetEntryPointOrdinal()
////	         returns:        -entry point name or ""
//string GetEntryName(long ordinal);
//// ***********************************************
//// ** rename entry point
////	         arguments:      ordinal - entry point number
////	                         name    - new name
////	         returns:        !=0 - ok
//success RenameEntryPoint(long ordinal, final String name);
//// ----------------------------------------------------------------------------
////	                              F I X U P S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** find next address with fixup information
////	         arguments:      ea - current address
////	         returns:        -1 - no more fixups
////	                         otherwise returns the next address with
////	                                                 fixup information
//	public static long GetNextFixupEA(long ea);
//// ***********************************************
//// ** find previous address with fixup information
////	         arguments:      ea - current address
////	         returns:        -1 - no more fixups
////	                         otherwise returns the previous address with
////	                                                 fixup information
//	public static long GetPrevFixupEA(long ea);
//// ***********************************************
//// ** get fixup target type
////	         arguments:      ea - address to get information about
////	         returns:        -1 - no fixup at the specified address
////	                         otherwise returns fixup target type:
//
//public static final FIXUP_MASK = 0xF
//public static final FIXUP_BYTE FIXUP_OFF8 // 8-bit offset.
//public static final FIXUP_OFF8 0 // 8-bit offset.
//public static final FIXUP_OFF16 1 // 16-bit offset.
//public static final FIXUP_SEG16 2 // 16-bit base--logical segment base (selector).
//public static final FIXUP_PTR32 3 // 32-bit long pointer (16-bit base:16-bit
//// offset).
//public static final FIXUP_OFF32 4 // 32-bit offset.
//public static final FIXUP_PTR48 5 // 48-bit pointer (16-bit base:32-bit offset).
//public static final FIXUP_HI8 6 // high 8 bits of 16bit offset
//public static final FIXUP_HI16 7 // high 16 bits of 32bit offset
//public static final FIXUP_LOW8 8 // low 8 bits of 16bit offset
//public static final FIXUP_LOW16 9 // low 16 bits of 32bit offset
//public static final FIXUP_REL = 0x10 // fixup is relative to the linear address
//// specified in the 3d parameter to set_fixup()
//public static final FIXUP_SELFREL = 0x0 // self-relative?
////   - disallows the kernel to convert operands
////      in the first pass
////   - this fixup is used during output
//// This type of fixups is not used anymore.
//// Anyway you can use it for commenting purposes
//// in the loader modules
//public static final FIXUP_EXTDEF = 0x20 // target is a location (otherwise - segment)
//public static final FIXUP_UNUSED = 0x40 // fixup is ignored by IDA
////   - disallows the kernel to convert operands
////   - this fixup is not used during output
//public static final FIXUP_CREATED = 0x80 // fixup was not present in the input file
//
//	public static long GetFixupTgtType(long ea);
//// ***********************************************
//// ** get fixup target selector
////	         arguments:      ea - address to get information about
////	         returns:        -1 - no fixup at the specified address
////	                         otherwise returns fixup target selector
//	public static long GetFixupTgtSel(long ea);
//// ***********************************************
//// ** get fixup target offset
////	         arguments:      ea - address to get information about
////	         returns:        -1 - no fixup at the specified address
////	                         otherwise returns fixup target offset
//	public static long GetFixupTgtOff(long ea);
//// ***********************************************
//// ** get fixup target displacement
////	         arguments:      ea - address to get information about
////	         returns:        -1 - no fixup at the specified address
////	                         otherwise returns fixup target displacement
//	public static long GetFixupTgtDispl(long ea);
//// ***********************************************
//// ** set fixup information
////	         arguments:      ea        - address to set fixup information about
////	                         type      - fixup type. see GetFixupTgtType()
////	                                     for possible fixup types.
////	                         targetsel - target selector
////	                         targetoff - target offset
////	                         displ     - displacement
////	         returns:        none
//void SetFixup(long ea,long type,long targetsel,long targetoff,long displ);
//// ***********************************************
//// ** delete fixup information
////	         arguments:      ea - address to delete fixup information about
////	         returns:        none
//void DelFixup(long ea);
//// ----------------------------------------------------------------------------
////	                    M A R K E D   P O S I T I O N S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** mark position
////	         arguments:      ea      - address to mark
////	                         lnnum   - number of generated line for the 'ea'
////	                         x       - x coordinate of cursor
////	                         y       - y coordinate of cursor
////	                         slot    - slot number: 1..1023
////	                                   if the specifed value is not within the
////	                                   range, IDA will ask the user to select slot.
////	                         comment - description of the mark.
////	                                   Should be not empty.
////	         returns:        none
//void MarkPosition(long ea,long lnnum,long x,long y,long slot, final String comment);
//// ***********************************************
//// ** get marked position
////	         arguments:      slot    - slot number: 1..1023
////	                                   if the specifed value is <= 0
////	                                   range, IDA will ask the user to select slot.
////	         returns:        -1 - the slot doesn't contain a marked address
////	                         otherwise returns the marked address
//	public static long GetMarkedPos(long slot);
//// ***********************************************
//// ** get marked position comment
////	         arguments:      slot    - slot number: 1..1023
////	         returns:        0 if the slot doesn't contain
////	                                         a marked address
////	                         otherwise returns the marked address comment
//string GetMarkComment(long slot);
//// ----------------------------------------------------------------------------
////	                          S T R U C T U R E S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** get number of defined structure types
////	         arguments:      none
////	         returns:        number of structure types
//	public static long GetStrucQty(void);
//// ***********************************************
//// ** get index of first structure type
////	         arguments:      none
////	         returns:        -1 if no structure type is defined
////	                         index of first structure type.
////	                         Each structure type has an index and ID.
////	                         INDEX determines position of structure definition
////	                          in the list of structure definitions. Index 1
////	                          is listed first, after index 2 and so on.
////	                          The index of a structure type can be changed any
////	                          time, leading to movement of the structure definition
////	                          in the list of structure definitions.
////	                         ID uniquely denotes a structure type. A structure
////	                          gets a unique ID at the creation time and this ID
////	                          can't be changed. Even when the structure type gets
////	                          deleted, its ID won't be resued in the future.
//	public static long GetFirstStrucIdx(void);
//// ***********************************************
//// ** get index of last structure type
////	         arguments:      none
////	         returns:        -1 if no structure type is defined
////	                         index of last structure type.
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
//	public static long GetLastStrucIdx(void);
//// ***********************************************
//// ** get index of next structure type
////	         arguments:      current structure index
////	         returns:        -1 if no (more) structure type is defined
////	                         index of the next structure type.
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
//	public static long GetNextStrucIdx(long index);
//// ***********************************************
//// ** get index of previous structure type
////	         arguments:      current structure index
////	         returns:        -1 if no (more) structure type is defined
////	                         index of the presiouvs structure type.
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
//	public static long GetPrevStrucIdx(long index);
//// ***********************************************
//// ** get structure index by structure ID
////	         arguments:      structure ID
////	         returns:        -1 if bad structure ID is passed
////	                         otherwise returns structure index.
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
//	public static long GetStrucIdx(long id);
//// ***********************************************
//// ** get structure ID by structure index
////	         arguments:      structure index
////	         returns:        -1 if bad structure index is passed
////	                         otherwise returns structure ID.
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
//	public static long GetStrucId(long index);
//// ***********************************************
//// ** get structure ID by structure name
////	         arguments:      structure type name
////	         returns:        -1 if bad structure type name is passed
////	                         otherwise returns structure ID.
//	public static long GetStrucIdByName(final String name);
//// ***********************************************
//// ** get structure type name
////	         arguments:      structure type ID
////	         returns:        -1 if bad structure type ID is passed
////	                         otherwise returns structure type name.
//string GetStrucName(long id);
//// ***********************************************
//// ** get structure type comment
////	         arguments:      id         - structure type ID
////	                         repeatable - 1: get repeatable comment
////	                                      0: get regular comment
////	         returns:        0 if bad structure type ID is passed
////	                         otherwise returns comment.
//string GetStrucComment(long id,long repeatable);
//// ***********************************************
//// ** get size of a structure
////	         arguments:      id         - structure type ID
////	         returns:        -1 if bad structure type ID is passed
////	                         otherwise returns size of structure in bytes.
//	public static long GetStrucSize(long id);
//// ***********************************************
//// ** get number of members of a structure
////	         arguments:      id         - structure type ID
////	         returns:        -1 if bad structure type ID is passed
////	                         otherwise returns number of members.
//	public static long GetMemberQty(long id);
//// ***********************************************
//// ** get previous offset in a structure
////	         arguments:      id     - structure type ID
////	                         offset - current offset
////	         returns:        -1 if bad structure type ID is passed
////	                            or no (more) offsets in the structure
////	                         otherwise returns previous offset in a structure.
////	                         NOTE: IDA allows 'holes' between members of a
////	                               structure. It treats these 'holes'
////	                               as unnamed arrays of bytes.
////	                         This function returns a member offset or a hole offset.
////	                         It will return size of the structure if input
////	                         'offset' is bigger than the structure size.
//	public static long GetStrucPrevOff(long id,long offset);
//// ***********************************************
//// ** get next offset in a structure
////	         arguments:      id     - structure type ID
////	                         offset - current offset
////	         returns:        -1 if bad structure type ID is passed
////	                            or no (more) offsets in the structure
////	                         otherwise returns next offset in a structure.
////	                         NOTE: IDA allows 'holes' between members of a
////	                               structure. It treats these 'holes'
////	                               as unnamed arrays of bytes.
////	                         This function returns a member offset or a hole offset.
////	                         It will return size of the structure if input
////	                         'offset' belongs to the last member of the structure.
//	public static long GetStrucNextOff(long id,long offset);
//// ***********************************************
//// ** get offset of the first member of a structure
////	         arguments:      id            - structure type ID
////	         returns:        -1 if bad structure type ID is passed
////	                            or structure has no members
////	                         otherwise returns offset of the first member.
////	                         NOTE: IDA allows 'holes' between members of a
////	                               structure. It treats these 'holes'
////	                               as unnamed arrays of bytes.
//	public static long GetFirstMember(long id);
//// ***********************************************
//// ** get offset of the last member of a structure
////	         arguments:      id            - structure type ID
////	         returns:        -1 if bad structure type ID is passed
////	                            or structure has no members
////	                         otherwise returns offset of the last member.
////	                         NOTE: IDA allows 'holes' between members of a
////	                               structure. It treats these 'holes'
////	                               as unnamed arrays of bytes.
//	public static long GetLastMember(long id);
//// ***********************************************
//// ** get offset of a member of a structure by the member name
////	         arguments:      id            - structure type ID
////	                         member_name   - name of structure member
////	         returns:        -1 if bad structure type ID is passed
////	                            or no such member in the structure
////	                         otherwise returns offset of the specified member.
//	public static long GetMemberOffset(long id, final String member_name);
//// ***********************************************
//// ** get name of a member of a structure
////	         arguments:      id            - structure type ID
////	                         member_offset - member offset. The offset can be
////	                                         any offset in the member. For example,
////	                                         is a member is 4 bytes long and starts
////	                                         at offset 2, then 2,3,4,5 denote
////	                                         the same structure member.
////	         returns:        0 if bad structure type ID is passed
////	                            or no such member in the structure
////	                         otherwise returns name of the specified member.
//string GetMemberName(long id,long member_offset);
//// ***********************************************
//// ** get comment of a member
////	         arguments:      id            - structure type ID
////	                         member_offset - member offset. The offset can be
////	                                         any offset in the member. For example,
////	                                         is a member is 4 bytes long and starts
////	                                         at offset 2, then 2,3,4,5 denote
////	                                         the same structure member.
////	                         repeatable - 1: get repeatable comment
////	                                      0: get regular comment
////	         returns:        0 if bad structure type ID is passed
////	                            or no such member in the structure
////	                         otherwise returns comment of the specified member.
//string GetMemberComment(long id,long member_offset,long repeatable);
//// ***********************************************
//// ** get size of a member
////	         arguments:      id            - structure type ID
////	                         member_offset - member offset. The offset can be
////	                                         any offset in the member. For example,
////	                                         is a member is 4 bytes long and starts
////	                                         at offset 2, then 2,3,4,5 denote
////	                                         the same structure member.
////	         returns:        -1 if bad structure type ID is passed
////	                            or no such member in the structure
////	                         otherwise returns size of the specified
////	                                           member in bytes.
//	public static long GetMemberSize(long id,long member_offset);
//// ***********************************************
//// ** get type of a member
////	         arguments:      id            - structure type ID
////	                         member_offset - member offset. The offset can be
////	                                         any offset in the member. For example,
////	                                         is a member is 4 bytes long and starts
////	                                         at offset 2, then 2,3,4,5 denote
////	                                         the same structure member.
////	         returns:        -1 if bad structure type ID is passed
////	                            or no such member in the structure
////	                         otherwise returns type of the member, see bit
////	                         definitions above. If the member type is a structure
////	                         then function GetMemberStrid() should be used to
////	                         get the structure type id.
//	public static long GetMemberFlag(long id,long member_offset);
//// ***********************************************
//// ** get structure id of a member
////	         arguments:      id            - structure type ID
////	                         member_offset - member offset. The offset can be
////	                                         any offset in the member. For example,
////	                                         is a member is 4 bytes long and starts
////	                                         at offset 2, then 2,3,4,5 denote
////	                                         the same structure member.
////	         returns:        -1 if bad structure type ID is passed
////	                            or no such member in the structure
////	                         otherwise returns structure id of the member.
////	                         If the current member is not a structure, returns -1.
//	public static long GetMemberStrId(long id,long member_offset);
//// ***********************************************
//// ** is a structure a union?
////	      arguments:      id            - structure type ID
////	         returns:        1: yes, this is a union id
////	                         0: no
////
////	                         Unions are a special kind of structures
//	public static long IsUnion(long id);
//// ***********************************************
//// ** define a new structure type
////	         arguments:      index         - index of new structure type
////	                         If another structure has the specified index,
////	                         then index of that structure and all other
////	                         structures will be increentedfreeing the specifed
////	                         index. If index is == -1, then the biggest index
////	                         number will be used.
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
////
////	                         name - name of the new structure type.
////
////	                         is_union - 0: structure
////	                                    1: union
////
////	         returns:        -1 if can't define structure type because of
////	                         bad structure name: the name is ill-formed or is
////	                         already used in the program.
////	                         otherwise returns ID of the new structure type
//	public static long AddStrucEx(long index, final String name,long is_union);
//// ***********************************************
//// ** delete a structure type
////	         arguments:      id            - structure type ID
////	         returns:        0 if bad structure type ID is passed
////	                         1 otherwise the structure type is deleted. All data
////	                         and other structure types referencing to the
////	                         deleted structure type will be displayed as array
////	                         of bytes.
//success DelStruc(long id);
//// ***********************************************
//// ** change structure index
////	         arguments:      id      - structure type ID
////	                         index   - new index of the structure
////	                         See GetFirstStrucIdx() for the explanation of
////	                         structure indices and IDs.
////	         returns:        !=0 - ok
//	public static long SetStrucIdx(long id,long index);
//// ***********************************************
//// ** change structure name
////	         arguments:      id      - structure type ID
////	                         name    - new name of the structure
////	         returns:        !=0 - ok
//	public static long SetStrucName(long id, final String name);
//// ***********************************************
//// ** change structure comment
////	         arguments:      id      - structure type ID
////	                         comment - new comment of the structure
////	                         repeatable - 1: change repeatable comment
////	                                      0: change regular comment
////	         returns:        !=0 - ok
//	public static long SetStrucComment(long id, final String comment,long repeatable);
//// ***********************************************
//// ** add structure member
//// arguments:
////   id      - structure type ID
////   name    - name of the new member
////   offset  - offset of the new member
////	             -1 means to add at the end of the structure
////   flag    - type of the new member. Should be one of
////	             FF_BYTE..FF_PACKREAL (see above)
////	             combined with FF_DATA
////   typeid  - if isStruc(flag) then typeid specifies
////	             the structure id for the member
////	             if isOff0(flag) then typeid specifies
////	             the offset base.
////	             if isASCII(flag) then typeid specifies
////	             the string type (ASCSTR_...).
////	             if isStroff(flag) then typeid specifies
////	             the structure id
////	             if isEnum(flag) then typeid specifies
////	             the enum id
////	             Otherwise typeid should be -1
////   nbytes  - number of bytes in the new member
//// the remaining arguments are allowed only if isOff0(flag) and you want
//// to specify a complex offset expression
////   target  - target address of the offset expr. You may specify it as
////	             -1, ida will calculate it itself
////   tdelta  - offset target delta. usually 0
////   reftype - see REF_... definitions
//// returns: 0 - ok, otherwise error code:
//
//public static final STRUC_ERROR_MEMBER_NAME (-1) // already has member with this name (bad name)
//public static final STRUC_ERROR_MEMBER_OFFSET (-2) // already has member at this offset
//public static final STRUC_ERROR_MEMBER_SIZE (-3) // bad number of bytes or bad sizeof(type)
//public static final STRUC_ERROR_MEMBER_TINFO (-4) // bad typeid parameter
//public static final STRUC_ERROR_MEMBER_STRUCT (-5) // bad struct id (the 1st argument)
//public static final STRUC_ERROR_MEMBER_UNIVAR (-6) // unions can't have variable sized members
//public static final STRUC_ERROR_MEMBER_VARLAST (-7) // variable sized member should be the last member in the structure
//
//// first form:
//	public static long AddStrucMember(long id, final String name,long offset,long flag,
//                    long typeid,long nbytes);
//// second form:
//	public static long AddStrucMember(long id, final String name,long offset,long flag,
//                    long typeid,long nbytes,
//                    long target,long tdelta, long reftype);
//// ***********************************************
//// ** delete structure member
////	         arguments:      id            - structure type ID
////	                         member_offset - offset of the member
////	         returns:        !=0 - ok.
////	                         NOTE: IDA allows 'holes' between members of a
////	                               structure. It treats these 'holes'
////	                               as unnamed arrays of bytes.
//	public static long DelStrucMember(long id,long member_offset);
//// ***********************************************
//// ** change structure member name
////	         arguments:      id            - structure type ID
////	                         member_offset - offset of the member
////	                         name          - new name of the member
////	         returns:        !=0 - ok.
//	public static long SetMemberName(long id,long member_offset, final String name);
//// ***********************************************
//// ** change structure member type
//// arguments:
////   id            - structure type ID
////   member_offset - offset of the member
////   flag    - new type of the member. Should be one of
////	             FF_BYTE..FF_PACKREAL (see above)
////	             combined with FF_DATA
////   typeid  - if isStruc(flag) then typeid specifies
////	             the structure id for the member
////	             if isOff0(flag) then typeid specifies
////	             the offset base.
////	             if isASCII(flag) then typeid specifies
////	             the string type (ASCSTR_...).
////	             if isStroff(flag) then typeid specifies
////	             the structure id
////	             if isEnum(flag) then typeid specifies
////	             the enum id
////	             Otherwise typeid should be -1
////   nitems  - number of items in the member
//// the remaining arguments are allowed only if isOff0(flag) and you want
//// to specify a complex offset expression
////   target  - target address of the offset expr. You may specify it as
////	             -1, ida will calculate it itself
////   tdelta  - offset target delta. usually 0
////   reftype - see REF_... definitions
//// returns:        !=0 - ok.
//// first form:
//	public static long SetMemberType(long id,long member_offset,long flag,long typeid,long nitems);
//// second form:
//	public static long SetMemberType(long id,long member_offset,long flag,long typeid,long nitems,
//                    long target,long tdelta,long reftype);
//// ***********************************************
//// ** change structure member comment
////	         arguments:      id      - structure type ID
////	                         member_offset - offset of the member
////	                         comment - new comment of the structure member
////	                         repeatable - 1: change repeatable comment
////	                                      0: change regular comment
////	         returns:        !=0 - ok
//	public static long SetMemberComment(long id,long member_offset, final String comment,long repeatable);
//// ----------------------------------------------------------------------------
////	                          E N U M S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** get number of enum types
////	         arguments:      none
////	         returns:        number of enumerations
//	public static long GetEnumQty(void);
//// ***********************************************
//// ** get ID of the specified enum by its serial number
////	         arguments:      idx - number of enum (0..GetEnumQty()-1)
////	         returns:        ID of enum or -1 if error
//	public static long GetnEnum(long idx);
//// ***********************************************
//// ** get serial number of enum by its ID
////	         arguments:      enum_id - ID of enum
////	         returns:        (0..GetEnumQty()-1) or -1 if error
//	public static long GetEnumIdx(long enum_id);
//// ***********************************************
//// ** get enum ID by the name of enum
////	         arguments:      name - name of enum
////	         returns:        ID of enum or -1 if no such enum exists
//	public static long GetEnum(final String name);
//// ***********************************************
//// ** get name of enum
////	         arguments:      enum_id - ID of enum
////	         returns:        name of enum or empty string
//string GetEnumName(long enum_id);
//// ***********************************************
//// ** get comment of enum
////	         arguments:      enum_id - ID of enum
////	                         repeatable - 0:get regular comment
////	                                      1:get repeatable comment
////	         returns:        comment of enum
//string GetEnumCmt(long enum_id,long repeatable);
//// ***********************************************
//// ** get size of enum
////	         arguments:      enum_id - ID of enum
////	         returns:        number of constants in the enum
////	                         Returns 0 if enum_id is bad.
//	public static long GetEnumSize(long enum_id);
//// ***********************************************
//// ** get width of enum elements
////	         arguments:      enum_id - ID of enum
////	         returns:        log2(size of enum elements in bytes)+1
////	                         possible returned values are 1..7
////	                         1-1byte,2-2bytes,3-4bytes,4-8bytes,etc
////	                         Returns 0 if enum_id is bad or the width is unknown.
//	public static long GetEnumWidth(long enum_id);
//// ***********************************************
//// ** get flag of enum
////	         arguments:      enum_id - ID of enum
////	         returns:        flags of enum. These flags determine representation
////	                         of numeric constants (binary,octal,decimal,hex)
////	                         in the enum definition. See start of this file for
////	                         more information about flags.
////	                         Returns 0 if enum_id is bad.
//	public static long GetEnumFlag(long enum_id);
//// ***********************************************
//// ** get member of enum - a symbolic constant ID
////	         arguments:      name - name of symbolic constant
////	         returns:        ID of constant or -1
//	public static long GetConstByName(final String name);
//// ***********************************************
//// ** get value of symbolic constant
////	         arguments:      const_id - id of symbolic constant
////	         returns:        value of constant or 0
//	public static long GetConstValue(long const_id);
//// ***********************************************
//// ** get bit mask of symbolic constant
////	         arguments:      const_id - id of symbolic constant
////	         returns:        bitmask of constant or 0
////	                         ordinary enums have bitmask = -1
//	public static long GetConstBmask(long const_id);
//// ***********************************************
//// ** get id of enum by id of constant
////	         arguments:      const_id - id of symbolic constant
////	         returns:        id of enum the constant belongs to.
////	                         -1 if const_id is bad.
//	public static long GetConstEnum(long const_id);
//// ***********************************************
//// ** get id of constant
////	         arguments:      enum_id - id of enum
////	                         value   - value of constant
////	                         serial  - serial number of the constant in the
////	                                   enumeration. See OpEnumEx() for
////	                                   for details.
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	         returns:        id of constant or -1 if error
//	public static long GetConstEx(long enum_id,long value,long serial,long bmask);
//// ***********************************************
//// ** get first bitmask in the enum (bitfield)
////	         arguments:      enum_id - id of enum (bitfield)
////	         returns:        the smallest bitmask of constant or -1
////	                         no bitmasks are defined yet
////	                         All bitmasks are sorted by their values
////	                         as unsigned longs.
//	public static long GetFirstBmask(long enum_id);
//// ***********************************************
//// ** get last bitmask in the enum (bitfield)
////	         arguments:      enum_id - id of enum
////	         returns:        the biggest bitmask or -1 no bitmasks are defined yet
////	                         All bitmasks are sorted by their values
////	                         as unsigned longs.
//	public static long GetLastBmask(long enum_id);
//// ***********************************************
//// ** get next bitmask in the enum (bitfield)
////	         arguments:      enum_id - id of enum
////	                         bmask   - value of the current bitmask
////	         returns:        value of a bitmask with value higher than the specified
////	                         value. -1 if no such bitmasks exist.
////	                         All bitmasks are sorted by their values
////	                         as unsigned longs.
//	public static long GetNextBmask(long enum_id,long value);
//// ***********************************************
//// ** get prev bitmask in the enum (bitfield)
////	         arguments:      enum_id - id of enum
////	                         value   - value of the current bitmask
////	         returns:        value of a bitmask with value lower than the specified
////	                         value. -1 no such bitmasks exist.
////	                         All bitmasks are sorted by their values
////	                         as unsigned longs.
//	public static long GetPrevBmask(long enum_id,long value);
//// ***********************************************
//// ** get bitmask name (only for bitfields)
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	         returns:        name of bitmask if it exists. otherwise returns 0.
//	public static long GetBmaskName(long enum_id,long bmask);
//// ***********************************************
//// ** get bitmask comment (only for bitfields)
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                         repeatable - type of comment, 0-regular, 1-repeatable
////	         returns:        comment attached to bitmask if it exists.
////	                         otherwise returns 0.
//	public static long GetBmaskCmt(long enum_id,long bmask,long repeatable);
//// ***********************************************
//// ** set bitmask name (only for bitfields)
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                         name    - name of bitmask
////	         returns:        1-ok, 0-failed
//success SetBmaskName(long enum_id,long bmask, final String name);
//// ***********************************************
//// ** set bitmask comment (only for bitfields)
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                         cmt     - comment
////	                         repeatable - type of comment, 0-regular, 1-repeatable
////	         returns:        1-ok, 0-failed
//	public static long SetBmaskCmt(long enum_id,long bmask, final String cmt,long repeatable);
//// ***********************************************
//// ** get first constant in the enum
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	         returns:        value of constant or -1 no constants are defined
////	                         All constants are sorted by their values
////	                         as unsigned longs.
//	public static long GetFirstConst(long enum_id,long bmask);
//// ***********************************************
//// ** get last constant in the enum
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	         returns:        value of constant or -1 no constants are defined
////	                         All constants are sorted by their values
////	                         as unsigned longs.
//	public static long GetLastConst(long enum_id,long bmask);
//// ***********************************************
//// ** get next constant in the enum
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	                         value   - value of the current constant
////	         returns:        value of a constant with value higher than the specified
////	                         value. -1 no such constants exist.
////	                         All constants are sorted by their values
////	                         as unsigned longs.
//	public static long GetNextConst(long enum_id,long value,long bmask);
//// ***********************************************
//// ** get prev constant in the enum
////	         arguments:      enum_id - id of enum
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	                         value   - value of the current constant
////	         returns:        value of a constant with value lower than the specified
////	                         value. -1 no such constants exist.
////	                         All constants are sorted by their values
////	                         as unsigned longs.
//	public static long GetPrevConst(long enum_id,long value,long bmask);
//// ***********************************************
//// ** get name of a constant
////	         arguments:      const_id - id of const
////	         returns:        name of constant
//string GetConstName(long const_id);
//// ***********************************************
//// ** get comment of a constant
////	         arguments:      const_id - id of const
////	                         repeatable - 0:get regular comment
////	                                      1:get repeatable comment
////	         returns:        comment string
//string GetConstCmt(long const_id,long repeatable);
//// ***********************************************
//// ** add a new enum type
////	         arguments:      idx - serial number of the new enum.
////	                               If another enum with the same serial number
////	                               exists, then all enums with serial
////	                               numbers >= the specified idx get their
////	                               serial numbers incremented (in other words,
////	                               the new enum is put in the middle of the list
////	                               of enums).
////	                               If idx >= GetEnumQty() or idx == -1
////	                               then the new enum is created at the end of
////	                               the list of enums.
////	                         name - name of the enum.
////	                         flag - flags for representation of numeric constants
////	                                in the definition of enum.
////	         returns:        id of new enum or -1.
//	public static long AddEnum(long idx, final String name,long flag);
//// ***********************************************
//// ** delete enum type
////	         arguments:      enum_id - id of enum
//void DelEnum(long enum_id);
//// ***********************************************
//// ** give another serial number to a enum
////	         arguments:      enum_id - id of enum
////	                         idx     - new serial number.
////	                               If another enum with the same serial number
////	                               exists, then all enums with serial
////	                               numbers >= the specified idx get their
////	                               serial numbers incremented (in other words,
////	                               the new enum is put in the middle of the list
////	                               of enums).
////	                               If idx >= GetEnumQty() then the enum is
////	                               moved to the end of the list of enums.
////	         returns:        comment string
//success SetEnumIdx(long enum_id,long idx);
//// ***********************************************
//// ** rename enum
////	         arguments:      enum_id - id of enum
////	                         name    - new name of enum
////	         returns:        1-ok,0-failed
//success SetEnumName(long enum_id, final String name);
//// ***********************************************
//// ** set comment of enum
////	         arguments:      enum_id - id of enum
////	                         cmt     - new comment for the enum
////	                         repeatable - 0:set regular comment
////	                                      1:set repeatable comment
////	         returns:        1-ok,0-failed
//success SetEnumCmt(long enum_id, final String cmt,long repeatable);
//// ***********************************************
//// ** set flag of enum
////	         arguments:      enum_id - id of enum
////	                         flag - flags for representation of numeric constants
////	                                in the definition of enum.
////	         returns:        1-ok,0-failed
//success SetEnumFlag(long enum_id,long flag);
//// ***********************************************
//// ** set bitfield property of enum
////	         arguments:      enum_id - id of enum
////	                         flag - 1: convert to bitfield
////	                                0: convert to ordinary enum
////	         returns:        1-ok,0-failed
//success SetEnumBf(long enum_id,long flag);
//// ***********************************************
//// ** set width of enum elements
////	         arguments:      enum_id - id of enum
////	                         width - element width in bytes
////	                                 allowed values: 0-unknown
////	                                 or 1..7: (log2 of the element size)+1
////	         returns:        1-ok,0-failed
//success SetEnumWidth(long enum_id, long width);
//// ***********************************************
//// ** is enum a bitfield?
////	         arguments:      enum_id - id of enum
////	         returns:        1-yes, 0-no, ordinary enum
//success IsBitfield(long enum_id);
//// ***********************************************
//// ** add a member of enum - a symbolic constant
////	         arguments:      enum_id - id of enum
////	                         name    - name of symbolic constant. Must be unique
////	                                   in the program.
////	                         value   - value of symbolic constant.
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	                                   all bits set in value should be set in bmask too
////	         returns:        0-ok, otherwise error code:
//
//public static final CONST_ERROR_NAME 1 // already have member with this name (bad name)
//public static final CONST_ERROR_VALUE 2 // already have member with this value
//public static final CONST_ERROR_ENUM 3 // bad enum id
//public static final CONST_ERROR_MASK 4 // bad bmask
//public static final CONST_ERROR_ILLV 5 // bad bmask and value combination (~bmask & value != 0)
//
//	public static long AddConstEx(long enum_id, final String name,long value,long bmask);
//// ***********************************************
//// ** delete a member of enum - a symbolic constant
////	         arguments:      enum_id - id of enum
////	                         value   - value of symbolic constant.
////	                         serial  - serial number of the constant in the
////	                                   enumeration. See OpEnumEx() for
////	                                   for details.
////	                         bmask   - bitmask of the constant
////	                                   ordinary enums accept only -1 as a bitmask
////	         returns:        1-ok,0-failed
//success DelConstEx(long enum_id,long value,long serial,long bmask);
//// ***********************************************
//// ** rename a member of enum - a symbolic constant
////	         arguments:      const_id - id of const
////	                         name     - new name of constant
////	         returns:        1-ok,0-failed
//success SetConstName(long const_id, final String name);
//// ***********************************************
//// ** set a comment of a symbolic constant
////	         arguments:      const_id - id of const
////	                         cmt     - new comment for the constant
////	                         repeatable - 0:set regular comment
////	                                      1:set repeatable comment
////	         returns:        1-ok,0-failed
//success SetConstCmt(long const_id, final String cmt,long repeatable);
//// ----------------------------------------------------------------------------
////	                          A R R A Y S  I N  I D C
//// ----------------------------------------------------------------------------
//// The following functions allow you to manipulate arrays in IDC.
//// They have nothing to do with arrays in the disassembled program.
//// The IDC arrays are persistent and are kept in the database.
//// They remain until you explicitly delete them using DeleteArray().
////
//// The arrays are virtual. IDA allocates space for and keeps only the specified
//// elements of an array. The array index is 32-bit long. Actually, each array
//// may keep a set of strings and a set of long(32bit) values.
//// ***********************************************
//// ** create array
////	         arguments:      name - name of array. There are no restrictions
////	                                on the name (its length should be less than
////	                                120 characters, though)
////	         returns:        -1 - can't create array (it already exists)
////	                         otherwise returns id of the array
//	public static long CreateArray(final String name);
//// ***********************************************
//// ** get array id by its name
////	         arguments:      name - name of existing array.
////	         returns:        -1 - no such array
////	                         otherwise returns id of the array
//	public static long GetArrayId(final String name);
//// ***********************************************
//// ** rename array
////	         arguments:      id      - array id returned by CreateArray() or
////	                                   GetArrayId()
////	                         newname - new name of array. There are no
////	                                   restrictions on the name (its length should
////	                                   be less than 120 characters, though)
////	         returns:        1-ok, 0-failed
//success RenameArray(long id, final String newname);
//// ***********************************************
//// ** delete array
////	    This function deletes all elements of the array.
////	         arguments:      id      - array id
//void DeleteArray(long id);
//// ***********************************************
//// ** set 32bit value of array element
////	         arguments:      id      - array id
////	                         idx     - index of an element
////	                         value   - 32bit value to store in the array
////	         returns:        1-ok, 0-failed
//success SetArrayLong(long id,long idx,long value);
//// ***********************************************
//// ** set string value of array element
////	         arguments:      id      - array id
////	                         idx     - index of an element
////	                         str     - string to store in array element
////	         returns:        1-ok, 0-failed
//success SetArrayString(long id,long idx, final String str);
//// ***********************************************
//// ** get value of array element
////	         arguments:      tag     - tag of array, specifies one of two
////	                                   array types:
//
//public static final AR_LONG 'A' // array of longs
//public static final AR_STR 'S' // array of strings
//
////	                         id      - array id
////	                         idx     - index of an element
////	         returns:        value of the specified array element.
////	                         note that this function may return char or long
////	                         result. Unexistent array elements give zero as
////	                         a result.
//string or long GetArrayElement(long tag,long id,long idx);
//// ***********************************************
//// ** delete an array element
////	         arguments:      tag     - tag of array (AR_LONG or AR_STR)
////	                         id      - array id
////	                         idx     - index of an element
////	         returns:        1-ok, 0-failed
//success DelArrayElement(long tag,long id,long idx);
//// ***********************************************
//// ** get index of the first existing array element
////	         arguments:      tag     - tag of array (AR_LONG or AR_STR)
////	                         id      - array id
////	         returns:        -1 - array is empty
////	                         otherwise returns index of the first array element
//	public static long GetFirstIndex(long tag,long id);
//// ***********************************************
//// ** get index of the last existing array element
////	         arguments:      tag     - tag of array (AR_LONG or AR_STR)
////	                         id      - array id
////	         returns:        -1 - array is empty
////	                         otherwise returns index of the last array element
//	public static long GetLastIndex(long tag,long id);
//// ***********************************************
//// ** get index of the next existing array element
////	         arguments:      tag     - tag of array (AR_LONG or AR_STR)
////	                         id      - array id
////	                         idx     - index of the current element
////	         returns:        -1 - no more array elements
////	                         otherwise returns index of the next array element
//	public static long GetNextIndex(long tag,long id,long idx);
//// ***********************************************
//// ** get index of the previous existing array element
////	         arguments:      tag     - tag of array (AR_LONG or AR_STR)
////	                         id      - array id
////	                         idx     - index of the current element
////	         returns:        -1 - no more array elements
////	                         otherwise returns index of the previous array element
//	public static long GetPrevIndex(long tag,long id,long idx);
//// ***********************************************
//// ** associative arrays (the same as hashes in Perl)
//// ** to create a hash, use CreateArray() function
//// ** you can use the following function with hashes:
//// **      GetArrayId(), RenameArray(), DeleteArray()
//// ** The following additional functions are defined:
//success SetHashLong(long id, final String idx,long value);
//success SetHashString(long id, final String idx, final String value);
//	public static long    GetHashLong(long id, final String idx);
//string  GetHashString(long id, final String idx);
//success DelHashElement(long id, final String idx);
//string  GetFirstHashKey(long id);
//string  GetNextHashKey(long id, final String idx);
//string  GetLastHashKey(long id);
//string  GetPrevHashKey(long id, final String idx);
//// ----------------------------------------------------------------------------
////	                 S O U R C E   F I L E / L I N E   N U M B E R S
//// ----------------------------------------------------------------------------
////
////   IDA can keep information about source files used to create the program.
////   Each source file is represented by a range of addresses.
////   A source file may contains several address ranges.
//// ***********************************************
//// ** Mark a range of address as belonging to a source file
////	    An address range may belong only to one source file.
////	    A source file may be represented by several address ranges.
////	         ea1     - linear address of start of the address range
////	         ea2     - linear address of end of the address range
////	         filename- name of source file.
////	    returns: 1-ok, 0-failed.
//success AddSourceFile(long ea1,ulong ea2, final String filename);
//// ***********************************************
//// ** Get name of source file occupying the given address
////	         ea - linear address
////	    returns: NULL - source file information is not found
////	             otherwise returns pointer to file name
//string GetSourceFile(long ea);
//// ***********************************************
//// ** Delete information about the source file
////	         ea - linear address belonging to the source file
////	    returns: NULL - source file information is not found
////	             otherwise returns pointer to file name
//success DelSourceFile(long ea);
//// ***********************************************
//// ** set source line number
////	         arguments:      ea      - linear address
////	                         lnnum   - number of line in the source file
////	         returns:        nothing
//void SetLineNumber(long ea,long lnnum);
//// ***********************************************
//// ** get source line number
////	         arguments:      ea      - linear address
////	         returns:        number of line in the source file or -1
//	public static long GetLineNumber(long ea);
//// ***********************************************
//// ** delete information about source line number
////	         arguments:      ea      - linear address
////	         returns:        nothing
//void DelLineNumber(long ea);
//// ----------------------------------------------------------------------------
////	                 T Y P E  L I B R A R I E S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** Load a type library
////	         name - name of type library.
////	    returns: 1-ok, 0-failed.
//success LoadTil(final String name);
//// ***********************************************
//// ** Copy information from type library to database
////	    Copy structure, union, or enum definition from the type library
////	    to the IDA database.
////	         idx       - the position of the new type in the list of
////	                     types (structures or enums)
////	                     -1 means at the end of the list
////	         type_name - name of type to copy
////	    returns: BADNODE-failed, otherwise the type id
////	                 (structure id or enum id)
//	public static long Til2Idb(long idx, string type_name);
//// ***********************************************
//// ** Get type of function/variable
////	         ea - the address of the object
////	    returns: type string, 0 - failed
//string GetType(long ea);
//// ***********************************************
//// ** Guess type of function/variable
////	         ea - the address of the object.
////	              can be the structure member id too
////	    returns: type string, 0 - failed
//string GuessType(long ea);
//// ***********************************************
//// ** Set type of function/variable
////	         ea   - the address of the object
////	         type - the type string in C declaration form.
////	                must contain the closing ';'
////	                if specified as an empty string, then the type
////	                assciated with 'ea' will be deleted
////	    returns: 1-ok, 0-failed.
//success SetType(long ea, string type);
//// ***********************************************
//// ** Parse type declarations
////	         input -  file name or C declarations (depending on the flags)
////	         flags -  combination of PT_... constants or 0
////	    returns: number of errors
//	public static long ParseTypes(final String input, long flags);
//
//public static final PT_FILE = 0x0001 // input if a file name (otherwise contains type declarations)
//public static final PT_SILENT = 0x0002 // silent mode
//public static final PT_PAKDEF = 0x0000 // default pack value
//public static final PT_PAK1 = 0x0010 // #pragma pack(1)
//public static final PT_PAK2 = 0x0020 // #pragma pack(2)
//public static final PT_PAK4 = 0x0030 // #pragma pack(4)
//public static final PT_PAK8 = 0x0040 // #pragma pack(8)
//public static final PT_PAK16 = 0x0050 // #pragma pack(16)
//
//// ***********************************************
//// ** Get number of local types + 1
////	    returns: value >= 1. 1 means that there are no local types.
//	public static long GetMaxLocalType(void);
//// ***********************************************
//// ** Parse one type declaration and store it in the specified slot
////	         ordinal -  slot number (1...NumberOfLocalTypes)
////	                    -1 means allocate new slot or reuse the slot
////	                    of the existing named type
////	         input -  C declaration. Empty input empties the slot
////	         flags -  combination of PT_... constants or 0
////	    returns: slot number or 0 if error
//success SetLocalType(long ordinal, string input, long flags);
//// ***********************************************
//// ** Retrieve a local type declaration
////	         ordinal -  slot number (1...NumberOfLocalTypes)
////	    returns: local type as a C declaration or ""
//string GetLocalType(long ordinal, long flags);
//
//public static final PRTYPE_1LINE = 0x0000 // print to one line
//public static final PRTYPE_MULTI = 0x0001 // print to many lines
//public static final PRTYPE_TYPE = 0x0002 // print type declaration (not variable declaration)
//public static final PRTYPE_PRAGMA = 0x0004 // print pragmas for alignment
//
//// ***********************************************
//// ** Retrieve a local type name
////	         ordinal -  slot number (1...NumberOfLocalTypes)
////	    returns: local type name or ""
//string GetLocalTypeName(long ordinal);
//// ----------------------------------------------------------------------------
////	                           H I D D E N  A R E A S
//// ----------------------------------------------------------------------------
//// Hidden areas - address ranges which can be replaced by their descriptions
//// ***********************************************
//// ** hide an area
////	    arguments:
////	         start,end   - area boundaries
////	         description - description to display if the area is collapsed
////	         header      - header lines to display if the area is expanded
////	         footer      - footer lines to display if the area is expanded
////	         visible     - the area state
////	         color       - RGB color code (-1 means default color)
////	    returns:        !=0 - ok
//success HideArea(long start,long end, string description, string header, string footer, long color);
//// ***********************************************
//// ** set hidden area state
////	    arguments:
////	         ea      - any address belonging to the hidden area
////	         visible - new state of the area
////	    returns: !=0 - ok
//success SetHiddenArea(long ea, long visible);
//// ***********************************************
//// ** delete a hidden area
////	    arguments:      ea - any address belonging to the hidden area
////	    returns:        !=0 - ok
//success DelHiddenArea(long ea);
//// ----------------------------------------------------------------------------
////	                           D E B U G G E R  I N T E R F A C E
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** Load the debugger
////	    arguments:
////	         dbgname - debugger module name
////	                   Examples: win32, linux, mac.
////	         use_remote - 0/1: use remote debugger or not
//// This function is needed only when running idc scripts from the command line.
//// In other cases IDA loads the debugger module automatically.
//success LoadDebugger(final String dbgname, long use_remote);
//// ***********************************************
//// ** Launch the debugger
////	    arguments:
////	         path - path to the executable file.
////	         args - command line arguments
////	         sdir - initial directory for the process
//// for all args: if empty, the default value from the database will be used
////	    returns: -1-failed, 0-cancelled by the user, 1-ok
//// See the important note to the StepInto() function
//	public static long StartDebugger(final String path, string args, string sdir);
//// ***********************************************
//// ** Stop the debugger
//// Kills the currently debugger process and returns to the disassembly mode
////	    arguments: none
////	    returns: success
//success StopDebugger(void);
//// ***********************************************
//// ** Suspend the running process
//// Tries to suspend the process. If successful, the PROCESS_SUSPEND
//// debug event will arrive (see GetDebuggerEvent)
////	    arguments: none
////	    returns: success
//// To resume a suspended process use the GetDebuggerEvent function.
//// See the important note to the StepInto() function
//success PauseProcess(void);
//// ***********************************************
//// Take a snapshot of running processes and return their number.
//	public static long GetProcessQty(void);
//// ***********************************************
//// Get information about a running process
////	      idx - number of process, is in range 0..GetProcessQty()-1
//// returns: 0 if failure
//	public static long GetProcessPid(long idx);
//string GetProccessName(long idx);
//// ***********************************************
//// Attach the debugger to a running process
////	     pid - PID of the process to attach to. If NO_PROCESS, a dialog box
////	           will interactively ask the user for the process to attach to.
////	     event_id - reserved, must be -1
//// returns:
////	         -2 - impossible to find a compatible process
////	         -1 - impossible to attach to the given process (process died, privilege
////	              needed, not supported by the debugger plugin, ...)
////	          0 - the user cancelled the attaching to the process
////	          1 - the debugger properly attached to the process
//// See the important note to the StepInto() function
//	public static long AttachProcess(long pid, long event_id);
//// ***********************************************
//// Detach the debugger from the debugged process.
//success DetachProcess(void);
//// ***********************************************
//// Get number of threads.
//	public static long GetThreadQty(void);
//// ***********************************************
//// Get the ID of a thread
////	     idx - number of thread, is in range 0..GetThreadQty()-1
//// returns: -1 if failure
//	public static long GetThreadId(long idx);
//// ***********************************************
//// Get current thread ID
//// returns: -1 if failure
//	public static long GetCurrentThreadId(void);
//// ***********************************************
//// Select the given thread as the current debugged thread.
////	     tid - ID of the thread to select
//// The process must be suspended to select a new thread.
//// returns: success
//success SelectThread(long tid);
//// ***********************************************
//// Suspend thread
//// Suspending a thread may deadlock the whole application if the suspended
//// was owning some synchronization objects.
////	     tid - thread id
//// Return: -1:network error, 0-failed, 1-ok
//	public static long SuspendThread(long tid);
//// ***********************************************
//// Resume thread
////	     tid - thread id
//// Return: -1:network error, 0-failed, 1-ok
//	public static long ResumeThread(long tid);
//// ***********************************************
//// Enumerate process modules
//// These function return the module base address
//	public static long GetFirstModule(void);
//	public static long GetNextModule(long base);
//// ***********************************************
//// Get process module name
////	      base - the base address of the module
//// returns: required info or 0
//string GetModuleName(long base);
//// ***********************************************
//// Get process module size
////	      base - the base address of the module
//// returns: required info or -1
//	public static long GetModuleSize(long base);
//// ***********************************************
//// Execute one instruction in the current thread.
//// Other threads are kept suspended.
////
//// NOTE
////   You must call GetDebuggerEvent() after this call
////   in order to find out what happened. Normally you will
////   get the STEP event but other events are possible (for example,
////   an exception might occur or the process might exit).
////   This remark applies to all execution control functions.
////   The event codes depend on the issued command.
//// returns: success
//success StepInto(void);
//// ***********************************************
//// Execute one instruction in the current thread,
//// but without entering into functions
//// Others threads keep suspended.
//// See the important note to the StepInto() function
//success StepOver(void);
//// ***********************************************
//// Execute the process until the given address is reached.
//// If no process is active, a new process is started.
//// See the important note to the StepInto() function
//success RunTo(long ea);
//// ***********************************************
//// Execute instructions in the current thread until
//// a function return instruction is reached.
//// Other threads are kept suspended.
//// See the important note to the StepInto() function
//success StepUntilRet(void);
//// ***********************************************
//// Wait for the next event
//// This function (optionally) resumes the process
//// execution and wait for a debugger event until timeout
////	      wfne - combination of WFNE_... constants
////	      timeout - number of seconds to wait, -1-infinity
//// returns: debugger event codes, see below
//	public static long GetDebuggerEvent(long wfne, long timeout);
//
//// convenience function
//public static final ResumeProcess() GetDebuggerEvent(WFNE_CONT|WFNE_NOWAIT, 0)
//// wfne flag is combination of the following:
//public static final WFNE_ANY = 0x0001 // return the first event (even if it doesn't suspend the process)
//   // if the process is still running, the database
//   // does not reflect the memory state. you might want
//   // to call RefreshDebuggerMemory() in this case
//public static final WFNE_SUSP = 0x0002 // wait until the process gets suspended
//public static final WFNE_SILENT = 0x0004 // 1: be slient, 0:display modal boxes if necessary
//public static final WFNE_CONT = 0x0008 // continue from the suspended state
//public static final WFNE_NOWAIT = 0x0010 // do not wait for any event, immediately return DEC_TIMEOUT
//   // (to be used with WFNE_CONT)
//public static final WFNE_USEC = 0x0020 // timeout is specified in microseconds
//   // (minimum non-zero timeout is 40000us)
//// debugger event codes
//public static final NOTASK -2 // process does not exist
//public static final DBG_ERROR -1 // error (e.g. network problems)
//public static final DBG_TIMEOUT 0 // timeout
//public static final PROCESS_START = 0x00000001 // New process started
//public static final PROCESS_EXIT = 0x00000002 // Process stopped
//public static final THREAD_START = 0x00000004 // New thread started
//public static final THREAD_EXIT = 0x00000008 // Thread stopped
//public static final BREAKPOINT = 0x00000010 // Breakpoint reached
//public static final STEP = 0x00000020 // One instruction executed
//public static final EXCEPTION = 0x00000040 // Exception
//public static final LIBRARY_LOAD = 0x00000080 // New library loaded
//public static final LIBRARY_UNLOAD = 0x00000100 // Library unloaded
//public static final INFORMATION = 0x00000200 // User-defined information
//public static final SYSCALL = 0x00000400 // Syscall (not used yet)
//public static final WINMESSAGE = 0x00000800 // Window message (not used yet)
//public static final PROCESS_ATTACH = 0x00001000 // Attached to running process
//public static final PROCESS_DETACH = 0x00002000 // Detached from process
//public static final PROCESS_SUSPEND = 0x00004000 // Process has been suspended
//
//// ***********************************************
//// Refresh debugger memory
//// Upon this call IDA will forget all cached information
//// about the debugged process. This includes the segmentation
//// information and memory contents (register cache is managed
//// automatically). Also, this function refreshes exported name
//// from loaded DLLs.
//// You must call this function before using the segmentation
//// information, memory contents, or names of a non-suspended process.
//// This is an expensive call.
//void RefreshDebuggerMemory(void);
//// ***********************************************
//// Take memory snapshot of the debugged process
////	      only_loader_segs: 0-copy all segments to idb
////	                        1-copy only SFL_LOADER segments
//success TakeMemorySnapshot(long only_loader_segs);
//// ***********************************************
//// Get debugged process state
//// returns: one of the DBG_... constants (see below)
//	public static long GetProcessState(void);
//
//public static final DSTATE_SUSP_FOR_EVENT -2 // process is currently suspended to react to a debug event
//public static final DSTATE_SUSP -1 // process is suspended
//public static final DSTATE_NOTASK 0 // no process is currently debugged
//public static final DSTATE_RUN 1 // process is running
//public static final DSTATE_RUN_WAIT_ATTACH 2 // process is running, waiting for process properly attached
//public static final DSTATE_RUN_WAIT_END 3 // process is running, but the user asked to kill/detach the process
//   //   remark: in this case, most events are ignored
//
//// ***********************************************
//// Get various information about the current debug event
//// These function are valid only when the current event exists
//// (the process is in the suspended state)
//// For all events:
//	public static long GetEventId(void);
//	public static long GetEventPid(void);
//	public static long GetEventTid(void);
//	public static long GetEventEa(void);
//	public static long IsEventHandled(void);
//// For PROCESS_START, PROCESS_ATTACH, LIBRARY_LOAD events:
//string GetEventModuleName(void);
//	public static long GetEventModuleBase(void);
//	public static long GetEventModuleSize(void);
//// For PROCESS_EXIT, THREAD_EXIT events
//	public static long GetEventExitCode(void);
//// For LIBRARY_UNLOAD (unloaded library name)
//// For INFORMATION (message to display)
//string GetEventInfo(void);
//// For BREAKPOINT event
//	public static long GetEventBptHardwareEa(void);
//// For EXCEPTION event
//	public static long GetEventExceptionCode(void);
//	public static long GetEventExceptionEa(void);
//	public static long CanExceptionContinue(void);
//string GetEventExceptionInfo(void);
//// ***********************************************
//// Get/set debugger options
////	      opt - combination of DOPT_... constants
//// returns: old options
//	public static long SetDebuggerOptions(long opt);
//
//public static final DOPT_SEGM_MSGS = 0x00000001 // print messages on debugger segments modifications
//public static final DOPT_START_BPT = 0x00000002 // break on process start
//public static final DOPT_THREAD_MSGS = 0x00000004 // print messages on thread start/exit
//public static final DOPT_THREAD_BPT = 0x00000008 // break on thread start/exit
//public static final DOPT_BPT_MSGS = 0x00000010 // print message on breakpoint
//public static final DOPT_LIB_MSGS = 0x00000040 // print message on library load/unlad
//public static final DOPT_LIB_BPT = 0x00000080 // break on library load/unlad
//public static final DOPT_INFO_MSGS = 0x00000100 // print message on debugging information
//public static final DOPT_INFO_BPT = 0x00000200 // break on debugging information
//public static final DOPT_REAL_MEMORY = 0x00000400 // don't hide breakpoint instructions
//public static final DOPT_REDO_STACK = 0x00000800 // reconstruct the stack
//public static final DOPT_ENTRY_BPT = 0x00001000 // break on program entry point
//public static final DOPT_EXCDLG = 0x00006000 // exception dialogs:
//#  define EXCDLG_NEVER = 0x00000000 // never display exception dialogs
//#  define EXCDLG_UNKNOWN = 0x00002000 // display for unknown exceptions
//#  define EXCDLG_ALWAYS = 0x00006000 // always display
//public static final DOPT_LOAD_DINFO = 0x00008000 // automatically load debug files (pdb)
//
//// ***********************************************
//// Set remote debugging options
////	      hostname - remote host name or address
////	                 if empty, revert to local debugger
////	      password - password for the debugger server
////	      portnum  - port number to connect (-1: don't change)
//// returns: nothing
//void SetRemoteDebugger(final String hostname, string password, long portnum);
//// ***********************************************
//// Get number of defined exception codes
//	public static long GetExceptionQty(void);
//// ***********************************************
//// Get exception code
////	      idx - number of exception in the vector (0..GetExceptionQty()-1)
//// returns: exception code (0 - error)
//	public static long GetExceptionCode(long idx);
//// ***********************************************
//// Get exception information
////	      code - exception code
//string GetExceptionName(long code); // returns "" on error
//	public static long GetExceptionFlags(long code);  // returns -1 on error
//// ***********************************************
//// Add exception handling information
////	      code - exception code
////	      name - exception name
////	      desc - exception description
////	      flags - exception flags (combination of EXC_...)
//// returns: failure description or ""
//string DefineException(long code, string name, string desc, long flags);
//
//public static final EXC_BREAK = 0x0001 // break on the exception
//public static final EXC_HANDLE = 0x0002 // should be handled by the debugger?
////
//// ***********************************************
//// Set exception flags
////	      code - exception code
////	      flags - exception flags (combination of EXC_...)
//success SetExceptionFlags(long code, long flags);
//// ***********************************************
//// Delete exception handling information
////	      code - exception code
//success ForgetException(long code);
//// ***********************************************
//// ** get register value
////	    arguments:
////	         name - the register name
////	    the debugger should be running. otherwise the function fails
////	    the register name should be valid.
////	    It is not necessary to use this function to get register values
////	    because a register name in the script will do too.
////	    returns: register value (integer or floating point)
//number GetRegValue(final String name);
//// ***********************************************
//// ** set register value
////	    arguments:
////	         name - the register name
////	         value - new register value
////	    the debugger should be running
////	    It is not necessary to use this function to set register values.
////	    A register name in the left side of an assignment will do too.
//success SetRegValue(number value, string name);
//// Get number of breakpoints.
//// Returns: number of breakpoints
//	public static long GetBptQty();
//// Get breakpoint address
////	      n - number of breakpoint, is in range 0..GetBptQty()-1
//// returns: addresss of the breakpoint or BADADDR
//	public static long GetBptEA(long n);
//// Get the characteristics of a breakpoint
////	      address - any address in the breakpoint range
////	      bptattr - the desired attribute code, one of BPTATTR_... constants
//// Returns: the desired attribute value or -1
//	public static long GetBptAttr(long ea, number bptattr);
//
//public static final BPTATTR_EA 0 // starting address of the breakpoint
//public static final BPTATTR_SIZE 4 // size of the breakpoint (undefined for software breakpoint)
//public static final BPTATTR_TYPE 8 // type of the breakpoint
//  // Breakpoint types:
//public static final BPT_EXEC 0 // Hardware: Execute instruction
//public static final BPT_WRITE 1 // Hardware: Write access
//public static final BPT_RDWR 3 // Hardware: Read/write access
//public static final BPT_SOFT 4 // Software breakpoint
//public static final BPTATTR_COUNT 12 // number of times the breakpoint is hit before stopping
//public static final BPTATTR_FLAGS 16 // Breakpoint attributes:
//public static final BPT_BRK = 0x01 // the debugger stops on this breakpoint
//public static final BPT_TRACE = 0x02 // the debugger adds trace information when
//  // this breakpoint is reached
//public static final BPTATTR_COND 20 // Breakpoint condition
//  // NOTE: the return value is a string in this case
//#ifdef __EA64__           // redefine the offsets for 64-bit version
//public static final BPTATTR_SIZE 8
//public static final BPTATTR_TYPE 16
//public static final BPTATTR_COUNT 20
//public static final BPTATTR_FLAGS 24
//public static final BPTATTR_COND 28
//
//
//// Set modifiable characteristics of a breakpoint
////	      address - any address in the breakpoint range
////	      bptattr - the attribute code, one of BPTATTR_... constants
////	                BPTATTR_CND is not allowed, see SetBptCnd()
////	      value   - the attibute value
//// Returns: success
//success SetBptAttr(long ea, number bptattr, long value);
//// Set breakpoint condition
////	      address - any address in the breakpoint range
////	      cnd     - breakpoint condition
//// Returns: success
//success SetBptCnd(long ea, string cnd);
//// Add a new breakpoint
////	     ea   - any address in the process memory space:
////	     size - size of the breakpoint (irrelevant for software breakpoints):
////	     type - type of the breakpoint (one of BPT_... constants)
//// Only one breakpoint can exist at a given address.
//// Returns: success
//success AddBptEx(long ea, long size, long bpttype);
//// Shorthand for software breakpoints:
//
//public static final AddBpt(ea) AddBptEx(ea, 0, BPT_SOFT)
//
//// Delete breakpoint
////	     ea   - any address in the process memory space:
//// Returns: success
//success DelBpt(long ea);
//// Enable/disable breakpoint
////	     ea   - any address in the process memory space
//// Disabled breakpoints are not written to the process memory
//// To check the state of a breakpoint, use CheckBpt()
//// Returns: success
//success EnableBpt(long ea, long enable);
//// Check a breakpoint
////	     ea   - any address in the process memory space
//// Returns: one of BPTCK_... constants
//	public static long CheckBpt(long ea);
//
//public static final BPTCK_NONE -1 // breakpoint does not exist
//public static final BPTCK_NO 0 // breakpoint is disabled
//public static final BPTCK_YES 1 // breakpoint is enabled
//public static final BPTCK_ACT 2 // breakpoint is active (written to the process)
//
//// Enable step tracing
////	      trace_level - what kind of trace to modify
////	      enable      - 0: turn off, 1: turn on
//// Returns: success
//success EnableTracing(long trace_level, long enable);
//
//public static final TRACE_STEP = 0x0 // lowest level trace. trace buffers are not maintained
//public static final TRACE_INSN = 0x1 // instruction level trace
//public static final TRACE_FUNC = 0x2 // function level trace (calls & rets)
//
//// ----------------------------------------------------------------------------
////	                           C O L O R S
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** get item color
////	    arguments:
////	         ea - address of the item
////	         what - type of the item (one of COLWHAT... constants)
////	    returns: color code in RGB (hex = 0xBBGGRR)
//	public static long GetColor(long ea, long what);
//
//// color item codes:
//public static final CIC_ITEM 1 // one instruction or data
//public static final CIC_FUNC 2 // function
//public static final CIC_SEGM 3 // segment
//public static final DEFCOLOR = 0xFFFFFFFF // Default color
//
//// ***********************************************
//// ** set item color
////	    arguments:
////	         ea - address of the item
////	         what - type of the item (one of COLWHAT... constants)
////	         color - new color code in RGB (hex = 0xBBGGRR)
////	    @return true on success, false otherwise
//success SetColor(long ea, long what, long color);
//// ----------------------------------------------------------------------------
////	                               X M L
//// ----------------------------------------------------------------------------
//// ***********************************************
//// ** set or update one or more XML values.
////	      arguments: path  - XPath expression of elements
////	                         where to create value(s)
////	                 name  - name of the element/attribute
////	                         (use @XXX for an attribute) to create.
////	                         If 'name' is empty, the elements or
////	                         attributes returned by XPath are directly
////	                         updated to contain the new 'value'.
////	                 value - value of the element/attribute
////	      returns:   1-ok, 0-failed
//success SetXML(final String path, string name, string value);
//// ***********************************************
//// ** get one XML value.
////	      arguments: path - XPath expression to an element
////	                        or attribute whose value is
////	                        requested
////	      returns:   the value, 0 if failed
//string or long GetXML(final String path);
//// ----------------------------------------------------------------------------
////	                       A R M   S P E C I F I C
//// ----------------------------------------------------------------------------
////	    Some ARM compilers in Thumb mode use BL (branch-and-link)
////	    instead of B (branch) for long jumps, since BL has more range.
////	    By default, IDA tries to determine if BL is a jump or a call.
////	    You can override IDA's decision using commands in Edit/Other menu
////	    (Force BL call/Force BL jump) or the following two functions.
////	    Force BL instruction to be a jump
////	      arguments: ea - address of the BL instruction
////	      returns:   1-ok, 0-failed
//success ArmForceBLJump(long ea);
////	    Force BL instruction to be a call
////	      arguments: ea - address of the BL instruction
////	      returns:   1-ok, 0-failed
//success ArmForceBLCall(long ea);
//// ----------------------------------------------------------------------------
// // _notdefinedsymbol
//
//	/////////////////////////////////////////////////////////////////////////
//	// Compatibility methods
//	/////////////////////////////////////////////////////////////////////////
//	public static String Compile(final String file) {
//		return CompileEx(file, 1);
//	}
//	public static boolean OpOffset(long ea, long base) {
//		return OpOff(ea, -1, base);
//	}
//
//	public static boolean OpNum(long ea) {
//		return OpNumber(ea, -1);
//	}
//
//	public static boolean OpChar(long ea) {
//		return OpChr(ea, -1);
//	}
//
//	public static boolean OpSegment(long ea) {
//		return OpSeg(ea, -1);
//	}
//
//	public static boolean OpDec(long ea) {
//		return OpDecimal(ea, -1);
//	}
//
//	public static boolean OpAlt1(long ea, final String str) {
//		return OpAlt(ea,0,str);
//	}
//
//	public static boolean OpAlt2(long ea, final String str) {
//		return OpAlt(ea, 1, str);
//	}
//
//	public static boolean StringStp(long x) {
//		return SetCharPrm(INF_ASCII_BREAK, x);
//	}
//
//	public static boolean LowVoids(long x) {
//		return SetLongPrm(INF_LOW_OFF, x);
//	}
//
//	public static boolean HighVoids(long x) {
//		return SetLongPrm(INF_HIGH_OFF, x);
//	}
//
//	public static boolean TailDepth(long x) {
//		return SetLongPrm(INF_MAXREF, x);
//	}
//
//	public static boolean Analysis(long x) {
//		return SetCharPrm(INF_AUTO, x);
//	}
//
//	public static boolean Tabs(long x) {
//		return SetCharPrm(INF_ENTAB, x);
//	}
//
//	public static boolean Comments(long x) {
//		return SetCharPrm(INF_CMTFLAG, x != 0 ?
//				(SW_ALLCMT | GetCharPrm(INF_CMTFLAG)) :
//				(~SW_ALLCMT & GetCharPrm(INF_CMTFLAG)));
//	}
//
//	public static boolean Voids(long x) {
//		return SetCharPrm(INF_VOIDS, x);
//	}
//
//	public static boolean XrefShow(long x) {
//		return SetCharPrm(INF_XREFNUM, x);
//	}
//
//	public static boolean Indent(long x) {
//		return SetCharPrm(INF_INDENT, x);
//	}
//
//	public static boolean CmtIndent(long x) {
//		return SetCharPrm(INF_COMMENT, x);
//	}
//
//	public static boolean AutoShow(long x) {
//		return SetCharPrm(INF_SHOWAUTO, x);
//	}
//
//	public static boolean MinEA() {
//		return GetLongPrm(INF_MIN_EA);
//	}
//
//	public static boolean MaxEA() {
//		return GetLongPrm(INF_MAX_EA);
//	}
//
//	public static boolean BeginEA() {
//		return GetLongPrm(INF_BEGIN_EA);
//	}
//
//	public static boolean set_start_cs(long x) {
//		return SetLongPrm(INF_START_CS, x);
//	}
//
//	public static boolean set_start_ip(long x) {
//		return SetLongPrm(INF_START_IP, x);
//	}
//
//public static final WriteMap(file) \
//        GenerateFile(OFILE_MAP, fopen(file,"w"), 0, BADADDR, \
//        GENFLG_MAPSEGS|GENFLG_MAPNAME)
//public static final WriteTxt(file,ea1,ea2) \
//        GenerateFile(OFILE_ASM,fopen(file,"w"), ea1, ea2, 0)
//public static final WriteExe(file) \
//        GenerateFile(OFILE_EXE,fopen(file,"wb"), 0, BADADDR, 0)
//public static AddConst(enum_id,name,value) AddConstEx(enum_id,name,value,-1)
//public static AddStruc(index,name) AddStrucEx(index,name,0)
//public static AddUnion(index,name) AddStrucEx(index,name,1)
//public static OpStroff(ea,n,strid) OpStroffEx(ea,n,strid,0)
//public static OpEnum(ea,n,enumid) OpEnumEx(ea,n,enumid,0)
//public static DelConst(id,v,mask) DelConstEx(id,v,0,mask)
//public static GetConst(id,v,mask) GetConstEx(id,v,0,mask)
//public static AnalyseArea(sEA, eEA) AnalyzeArea(sEA,eEA)
//public static MakeStruct(ea,name) MakeStructEx(ea, -1, name)
//public static Name(ea) NameEx(BADADDR, ea)
//public static GetTrueName(ea) GetTrueNameEx(BADADDR, ea)
//public static MakeName(ea,name) MakeNameEx(ea,name,SN_CHECK)
//public static GetFrame(ea) GetFunctionAttr(ea, FUNCATTR_FRAME)
//public static GetFrameLvarSize(ea) GetFunctionAttr(ea, FUNCATTR_FRSIZE)
//public static GetFrameRegsSize(ea) GetFunctionAttr(ea, FUNCATTR_FRREGS)
//public static GetFrameArgsSize(ea) GetFunctionAttr(ea, FUNCATTR_ARGSIZE)
//public static GetFunctionFlags(ea) GetFunctionAttr(ea, FUNCATTR_FLAGS)
//public static SetFunctionFlags(ea, flags) SetFunctionAttr(ea, FUNCATTR_FLAGS, flags)
//public static SegStart(ea) GetSegmentAttr(ea, SEGATTR_START)
//public static SegEnd(ea) GetSegmentAttr(ea, SEGATTR_END)
//public static SetSegmentType(ea, type) SetSegmentAttr(ea, SEGATTR_TYPE, type)
//public static SegCreate(a1, a2, base, use32, align, comb) AddSeg(a1, a2, base, use32, align, comb)
//public static SegDelete(ea, flags) DelSeg(ea, flags)
//public static SegBounds(ea, startea, endea, flags) SetSegBounds(ea, startea, endea, flags)
//public static SegRename(ea, name) RenameSeg(ea, name)
//public static SegClass(ea, class) SetSegClass(ea, name)
//public static SegAddrng(ea, bitness) SetSegAddressing(ea, bitness)
//public static SegDefReg(ea, reg, value) SetSegDefReg(ea, reg, value)
//public static Comment(ea) CommentEx(ea, 0)
//public static RptCmt(ea) CommentEx(ea, 1)
//public static MakeByte(ea) MakeData(ea, FF_BYTE, 1, BADADDR)
//public static MakeWord(ea) MakeData(ea, FF_WORD, 2, BADADDR)
//public static MakeDword(ea) MakeData(ea, FF_DWRD, 4, BADADDR)
//public static MakeQword(ea) MakeData(ea, FF_QWRD, 8, BADADDR)
//public static MakeOword(ea) MakeData(ea, FF_OWRD, 16, BADADDR)
//public static MakeFloat(ea) MakeData(ea, FF_FLOAT, 4, BADADDR)
//public static MakeDouble(ea) MakeData(ea, FF_DOUBLE, 8, BADADDR)
//public static MakePackReal(ea) MakeData(ea, FF_PACKREAL, 10, BADADDR)
//public static MakeTbyte(ea) MakeData(ea, FF_TBYT, 10, BADADDR)
//public static SetReg(ea,reg,value) SetRegEx(ea,reg,value,SR_user)
//// erroneous name of INF_AF:
//public static final INF_START_AF 33
//// Convenience macros:
//public static final long here() {
//	return IdaJava.get_screen_ea();
//}
//public static final boolean isEnabled(long ea) {
//	return PrevAddr(ea + 1) == ea;
//}
//	/** Obsolete flag: Permanently, i.e. disable addresses */
//	@Deprecated
//	public static final long SEGDEL_PERM = 0x0001;
//
//	/** Obsolete flag: Keep information (code & data, etc) */
//	@Deprecated
//	public static final long SEGDEL_KEEP = 0x0002;
//
//	/** Obsolete flag: Be silent */
//	@Deprecated
//	public static final long SEGDEL_SILENT = 0x0004;
//
//	/////////////////////////////////////////////////////////////////////////
//	//	Templates
//	/////////////////////////////////////////////////////////////////////////
//
//	// Icon index definitions for templates
//	public static final int I_IDA         =  0;
//	public static final int I_AMIGA       =  1;
//	public static final int I_ARM         =  2;
//	public static final int I_BEOS        =  3;
//	public static final int I_BIN         =  4;
//	public static final int I_DLL         =  5;
//	public static final int I_DOS         =  6;
//	public static final int I_DREAMCAST   =  7;
//	public static final int I_DRV         =  8;
//	public static final int I_DMP         =  9;
//	public static final int I_EXE         = 10;
//	public static final int I_GAMEBOY     = 11;
//	public static final int I_GEOS        = 12;
//	public static final int I_HEX         = 13;
//	public static final int I_JAVA        = 14;
//	public static final int I_LIB         = 15;
//	public static final int I_MAC         = 16;
//	public static final int I_N64         = 17;
//	public static final int I_NET         = 18;
//	public static final int I_NETWARE     = 19;
//	public static final int I_OBJ         = 20;
//	public static final int I_OCX         = 21;
//	public static final int I_OS2         = 22;
//	public static final int I_OSX         = 23;
//	public static final int I_PALM        = 24;
//	public static final int I_PLAYSTATION = 25;
//	public static final int I_RAW         = 26;
//	public static final int I_ROM         = 27;
//	public static final int I_SIS         = 28;
//	public static final int I_SYMBIAN     = 29;
//	public static final int I_UNIX        = 30;
//	public static final int I_UNK         = 31;
//	public static final int I_WIN         = 32;
//	public static final int I_XBOX        = 33;
//	public static final int I_ZIP         = 34;
}
