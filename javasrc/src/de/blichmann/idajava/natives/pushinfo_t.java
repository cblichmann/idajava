/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public class pushinfo_t {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public pushinfo_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(pushinfo_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IdaJavaJNI.delete_pushinfo_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setFlags(int value) {
    IdaJavaJNI.pushinfo_t_flags_set(swigCPtr, this, value);
  }

  public int getFlags() {
    return IdaJavaJNI.pushinfo_t_flags_get(swigCPtr, this);
  }

  public void setPsi(SWIGTYPE_p_qvectorT_pushreg_t_t value) {
    IdaJavaJNI.pushinfo_t_psi_set(swigCPtr, this, SWIGTYPE_p_qvectorT_pushreg_t_t.getCPtr(value));
  }

  public SWIGTYPE_p_qvectorT_pushreg_t_t getPsi() {
    long cPtr = IdaJavaJNI.pushinfo_t_psi_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_qvectorT_pushreg_t_t(cPtr, false);
  }

  public void setBpidx(int value) {
    IdaJavaJNI.pushinfo_t_bpidx_set(swigCPtr, this, value);
  }

  public int getBpidx() {
    return IdaJavaJNI.pushinfo_t_bpidx_get(swigCPtr, this);
  }

  public void setSpoiled(long value) {
    IdaJavaJNI.pushinfo_t_spoiled_set(swigCPtr, this, value);
  }

  public long getSpoiled() {
    return IdaJavaJNI.pushinfo_t_spoiled_get(swigCPtr, this);
  }

  public void setProlog_insns(SWIGTYPE_p_qvectorT_unsigned_int_t value) {
    IdaJavaJNI.pushinfo_t_prolog_insns_set(swigCPtr, this, SWIGTYPE_p_qvectorT_unsigned_int_t.getCPtr(value));
  }

  public SWIGTYPE_p_qvectorT_unsigned_int_t getProlog_insns() {
    long cPtr = IdaJavaJNI.pushinfo_t_prolog_insns_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_qvectorT_unsigned_int_t(cPtr, false);
  }

  public void setPops(SWIGTYPE_p_qvectorT_qvectorT_unsigned_int_t_t value) {
    IdaJavaJNI.pushinfo_t_pops_set(swigCPtr, this, SWIGTYPE_p_qvectorT_qvectorT_unsigned_int_t_t.getCPtr(value));
  }

  public SWIGTYPE_p_qvectorT_qvectorT_unsigned_int_t_t getPops() {
    long cPtr = IdaJavaJNI.pushinfo_t_pops_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_qvectorT_qvectorT_unsigned_int_t_t(cPtr, false);
  }

  public void setEh_type(int value) {
    IdaJavaJNI.pushinfo_t_eh_type_set(swigCPtr, this, value);
  }

  public int getEh_type() {
    return IdaJavaJNI.pushinfo_t_eh_type_get(swigCPtr, this);
  }

  public void setEh_info(long value) {
    IdaJavaJNI.pushinfo_t_eh_info_set(swigCPtr, this, value);
  }

  public long getEh_info() {
    return IdaJavaJNI.pushinfo_t_eh_info_get(swigCPtr, this);
  }

  public pushinfo_t() {
    this(IdaJavaJNI.new_pushinfo_t(), true);
  }

  public final static int PUSHINFO_VERSION = 3;

}
