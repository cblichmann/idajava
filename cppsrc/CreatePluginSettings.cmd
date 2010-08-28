@echo off
setlocal
pushd
cd /d %~dp0

set OUTFILE=PluginSettings.reg

echo Writing new "%OUTFILE%"...

set DIR=%~dp0\..\javasrc\bin

:: Replace \ with \\
set DIR=%DIR:\=\\%\\bin
(
  echo Windows Registry Editor Version 5.00
  echo.
  echo [HKEY_CURRENT_USER\Software\blichmann.de\IDAJava\0.2]
  echo "JvmWorkingDirectory"="%DIR%"
) > %OUTFILE%

popd
pause
endlocal
