/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.blichmann.idajava.natives;

public enum filetype_t {
  f_EXE_old,
  f_COM_old,
  f_BIN,
  f_DRV,
  f_WIN,
  f_HEX,
  f_MEX,
  f_LX,
  f_LE,
  f_NLM,
  f_COFF,
  f_PE,
  f_OMF,
  f_SREC,
  f_ZIP,
  f_OMFLIB,
  f_AR,
  f_LOADER,
  f_ELF,
  f_W32RUN,
  f_AOUT,
  f_PRC,
  f_EXE,
  f_COM,
  f_AIXAR,
  f_MACHO;

  public final int swigValue() {
    return swigValue;
  }

  public static filetype_t swigToEnum(int swigValue) {
    filetype_t[] swigValues = filetype_t.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (filetype_t swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + filetype_t.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private filetype_t() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private filetype_t(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private filetype_t(filetype_t swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}
