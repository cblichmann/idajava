/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public class IdaMenuItemListener {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public IdaMenuItemListener(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(IdaMenuItemListener obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IdaJavaJNI.delete_IdaMenuItemListener(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void actionPerformed() {
    IdaJavaJNI.IdaMenuItemListener_actionPerformed(swigCPtr, this);
  }

  public IdaMenuItemListener() {
    this(IdaJavaJNI.new_IdaMenuItemListener(), true);
  }

}