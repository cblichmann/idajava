/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public class exception_info_t {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public exception_info_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(exception_info_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IdaJavaJNI.delete_exception_info_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setCode(long value) {
    IdaJavaJNI.exception_info_t_code_set(swigCPtr, this, value);
  }

  public long getCode() {
    return IdaJavaJNI.exception_info_t_code_get(swigCPtr, this);
  }

  public void setFlags(long value) {
    IdaJavaJNI.exception_info_t_flags_set(swigCPtr, this, value);
  }

  public long getFlags() {
    return IdaJavaJNI.exception_info_t_flags_get(swigCPtr, this);
  }

  public boolean break_on() {
    return IdaJavaJNI.exception_info_t_break_on(swigCPtr, this);
  }

  public boolean handle() {
    return IdaJavaJNI.exception_info_t_handle(swigCPtr, this);
  }

  public void setName(SWIGTYPE_p__qstringT_char_t value) {
    IdaJavaJNI.exception_info_t_name_set(swigCPtr, this, SWIGTYPE_p__qstringT_char_t.getCPtr(value));
  }

  public SWIGTYPE_p__qstringT_char_t getName() {
    long cPtr = IdaJavaJNI.exception_info_t_name_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p__qstringT_char_t(cPtr, false);
  }

  public void setDesc(SWIGTYPE_p__qstringT_char_t value) {
    IdaJavaJNI.exception_info_t_desc_set(swigCPtr, this, SWIGTYPE_p__qstringT_char_t.getCPtr(value));
  }

  public SWIGTYPE_p__qstringT_char_t getDesc() {
    long cPtr = IdaJavaJNI.exception_info_t_desc_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p__qstringT_char_t(cPtr, false);
  }

  public exception_info_t() {
    this(IdaJavaJNI.new_exception_info_t__SWIG_0(), true);
  }

  public exception_info_t(long _code, long _flags, String _name, String _desc) {
    this(IdaJavaJNI.new_exception_info_t__SWIG_1(_code, _flags, _name, _desc), true);
  }

}
