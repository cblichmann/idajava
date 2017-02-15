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

%inline
%{
	typedef bool (idaapi *construct_macro_cb)(insn_t &s, bool may_go_forward);
%}
idaman bool ida_export construct_macro(bool enable, construct_macro_cb build_macro);
%ignore construct_macro;

%ignore get_immval; // Not exported in ida.lib
%ignore ua_out; // Not exported in ida.lib
%ignore ua_use_fixup; // Not exported in ida.lib
%ignore init_ua; // Not exported in ida.lib
%ignore term_ua; // Not exported in ida.lib
%ignore get_equal_items; // Not exported in ida.lib

enum optype_t {
	o_void     =  0,    
	o_reg      =  1,    
	o_mem      =  2,    
	o_phrase   =  3,    
	o_displ    =  4,    
	o_imm      =  5,    
	o_far      =  6,    
	o_near     =  7,    
	o_idpspec0 =  8,    
	o_idpspec1 =  9,    
	o_idpspec2 = 10,    
	o_idpspec3 = 11,    
	o_idpspec4 = 12,    
	o_idpspec5 = 13,    
	o_last     = 14
};

%ignore optype_t;


class op_t
{
public:
	char n;
	optype_t type;
	char offb;
	char offo;
	uchar flags;
	void set_showed() { flags |= OF_SHOW; }
	void clr_showed() { flags &= ~OF_SHOW; }
	bool showed() const { return (flags & OF_SHOW) != 0; }
	char dtyp;
	uint16 reg; // Omitted union
	bool is_reg(int r) const { return type == o_reg && reg == r; }
	uval_t value; // Omitted union
	bool is_imm(uval_t v) const { return type == o_imm && value == v; }
	ea_t addr; // Omitted union
	ea_t specval; // Omitted union
	char specflag1;
	char specflag2;
	char specflag3;
	char specflag4;

	%extend {
		bool hasSIB() {
			return $self->specflag1;
		}
	}

};
%ignore op_t;

class insn_t
{
public:
	ea_t cs;
	ea_t ip;
	ea_t ea;
	uint16 itype;
	inline bool is_canon_insn(void) const;
	inline uint32 get_canon_feature(void) const;
	inline const char * get_canon_mnem(void) const;
	uint16 size;
	uint16 auxpref;
	// auxpref_chars union omitted: SWIG cannot handle unnamed nested structs
	char segpref;
	char insnpref;
	op_t Operands[UA_MAXOP];
	char flags;
	bool is_macro(void) const { return (flags & INSN_MACRO) != 0; }
	
	%extend {
		op_t *getOperand(int index) {
			if (index < UA_MAXOP) {
				return &($self->Operands[index]);
			} else {
				return NULL;
			}
		}
	
		bool setOperand(int index, op_t op) {
			if (index < UA_MAXOP) {
				$self->Operands[index] = op;
				return 1;
			} else {
				return 0;
			}
		}
	}

};
%ignore insn_t;

%include <ua.hpp>
