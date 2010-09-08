@echo off
setlocal

set SWIGDIR=C:\Program Files (x86)\SWIG
set IDASDK=C:\Dev\external\idasdk_5_7\include
set SRCDIR=%~dp0\src
set OUTDIR=%~dp0..\javasrc\src\de\blichmann\idajava\natives

cd %SRCDIR%

rem "%SWIGDIR%\swig.exe" --help

rem del /q "%OUTDIR%\*.*"
"%SWIGDIR%\swig.exe" ^
	-c++ -Fstandard ^
	-java -package de.blichmann.idajava.natives ^
	-I"%IDASDK%" ^
	-outdir "%OUTDIR%" ^
	"idajava_idaapi.i"
pause

endlocal
