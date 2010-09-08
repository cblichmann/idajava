/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public class op_t {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public op_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(op_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IdaJavaJNI.delete_op_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setN(char value) {
    IdaJavaJNI.op_t_n_set(swigCPtr, this, value);
  }

  public char getN() {
    return IdaJavaJNI.op_t_n_get(swigCPtr, this);
  }

  public void setType(SWIGTYPE_p_optype_t value) {
    IdaJavaJNI.op_t_type_set(swigCPtr, this, SWIGTYPE_p_optype_t.getCPtr(value));
  }

  public SWIGTYPE_p_optype_t getType() {
    return new SWIGTYPE_p_optype_t(IdaJavaJNI.op_t_type_get(swigCPtr, this), true);
  }

  public void setOffb(char value) {
    IdaJavaJNI.op_t_offb_set(swigCPtr, this, value);
  }

  public char getOffb() {
    return IdaJavaJNI.op_t_offb_get(swigCPtr, this);
  }

  public void setOffo(char value) {
    IdaJavaJNI.op_t_offo_set(swigCPtr, this, value);
  }

  public char getOffo() {
    return IdaJavaJNI.op_t_offo_get(swigCPtr, this);
  }

  public void setFlags(short value) {
    IdaJavaJNI.op_t_flags_set(swigCPtr, this, value);
  }

  public short getFlags() {
    return IdaJavaJNI.op_t_flags_get(swigCPtr, this);
  }

  public void set_showed() {
    IdaJavaJNI.op_t_set_showed(swigCPtr, this);
  }

  public void clr_showed() {
    IdaJavaJNI.op_t_clr_showed(swigCPtr, this);
  }

  public boolean showed() {
    return IdaJavaJNI.op_t_showed(swigCPtr, this);
  }

  public void setDtyp(char value) {
    IdaJavaJNI.op_t_dtyp_set(swigCPtr, this, value);
  }

  public char getDtyp() {
    return IdaJavaJNI.op_t_dtyp_get(swigCPtr, this);
  }

  public void setReg(int value) {
    IdaJavaJNI.op_t_reg_set(swigCPtr, this, value);
  }

  public int getReg() {
    return IdaJavaJNI.op_t_reg_get(swigCPtr, this);
  }

  public boolean is_reg(int r) {
    return IdaJavaJNI.op_t_is_reg(swigCPtr, this, r);
  }

  public void setValue(long value) {
    IdaJavaJNI.op_t_value_set(swigCPtr, this, value);
  }

  public long getValue() {
    return IdaJavaJNI.op_t_value_get(swigCPtr, this);
  }

  public boolean is_imm(long v) {
    return IdaJavaJNI.op_t_is_imm(swigCPtr, this, v);
  }

  public void setAddr(long value) {
    IdaJavaJNI.op_t_addr_set(swigCPtr, this, value);
  }

  public long getAddr() {
    return IdaJavaJNI.op_t_addr_get(swigCPtr, this);
  }

  public void setSpecval(long value) {
    IdaJavaJNI.op_t_specval_set(swigCPtr, this, value);
  }

  public long getSpecval() {
    return IdaJavaJNI.op_t_specval_get(swigCPtr, this);
  }

  public void setSpecflag1(char value) {
    IdaJavaJNI.op_t_specflag1_set(swigCPtr, this, value);
  }

  public char getSpecflag1() {
    return IdaJavaJNI.op_t_specflag1_get(swigCPtr, this);
  }

  public void setSpecflag2(char value) {
    IdaJavaJNI.op_t_specflag2_set(swigCPtr, this, value);
  }

  public char getSpecflag2() {
    return IdaJavaJNI.op_t_specflag2_get(swigCPtr, this);
  }

  public void setSpecflag3(char value) {
    IdaJavaJNI.op_t_specflag3_set(swigCPtr, this, value);
  }

  public char getSpecflag3() {
    return IdaJavaJNI.op_t_specflag3_get(swigCPtr, this);
  }

  public void setSpecflag4(char value) {
    IdaJavaJNI.op_t_specflag4_set(swigCPtr, this, value);
  }

  public char getSpecflag4() {
    return IdaJavaJNI.op_t_specflag4_get(swigCPtr, this);
  }

}