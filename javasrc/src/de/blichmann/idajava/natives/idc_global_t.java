/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public class idc_global_t {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public idc_global_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(idc_global_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IdaJavaJNI.delete_idc_global_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setName(SWIGTYPE_p__qstringT_char_t value) {
    IdaJavaJNI.idc_global_t_name_set(swigCPtr, this, SWIGTYPE_p__qstringT_char_t.getCPtr(value));
  }

  public SWIGTYPE_p__qstringT_char_t getName() {
    long cPtr = IdaJavaJNI.idc_global_t_name_get(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p__qstringT_char_t(cPtr, false);
  }

  public void setValue(idc_value_t value) {
    IdaJavaJNI.idc_global_t_value_set(swigCPtr, this, idc_value_t.getCPtr(value), value);
  }

  public idc_value_t getValue() {
    long cPtr = IdaJavaJNI.idc_global_t_value_get(swigCPtr, this);
    return (cPtr == 0) ? null : new idc_value_t(cPtr, false);
  }

  public idc_global_t() {
    this(IdaJavaJNI.new_idc_global_t__SWIG_0(), true);
  }

  public idc_global_t(String n) {
    this(IdaJavaJNI.new_idc_global_t__SWIG_1(n), true);
  }

}