/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public class selection_item_t {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  public selection_item_t(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  public static long getCPtr(selection_item_t obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IdaJavaJNI.delete_selection_item_t(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setIs_node(boolean value) {
    IdaJavaJNI.selection_item_t_is_node_set(swigCPtr, this, value);
  }

  public boolean getIs_node() {
    return IdaJavaJNI.selection_item_t_is_node_get(swigCPtr, this);
  }

  public void setNode(int value) {
    IdaJavaJNI.selection_item_t_node_set(swigCPtr, this, value);
  }

  public int getNode() {
    return IdaJavaJNI.selection_item_t_node_get(swigCPtr, this);
  }

  public void setElp(edge_layout_point_t value) {
    IdaJavaJNI.selection_item_t_elp_set(swigCPtr, this, edge_layout_point_t.getCPtr(value), value);
  }

  public edge_layout_point_t getElp() {
    long cPtr = IdaJavaJNI.selection_item_t_elp_get(swigCPtr, this);
    return (cPtr == 0) ? null : new edge_layout_point_t(cPtr, false);
  }

  public int compare(selection_item_t r) {
    return IdaJavaJNI.selection_item_t_compare(swigCPtr, this, selection_item_t.getCPtr(r), r);
  }

  public boolean __eq__(selection_item_t r) {
    return IdaJavaJNI.selection_item_t___eq__(swigCPtr, this, selection_item_t.getCPtr(r), r);
  }

  public boolean __lt__(selection_item_t r) {
    return IdaJavaJNI.selection_item_t___lt__(swigCPtr, this, selection_item_t.getCPtr(r), r);
  }

}