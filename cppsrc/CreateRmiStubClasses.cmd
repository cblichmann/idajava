@echo off
setlocal
pushd

set RMIC=C:\Programme\Java\jdk1.6.0_10\bin\rmic.exe
set CLASS_FILES=C:\Dev\idajava\javasrc\bin

echo Generating stub classes for RMI...

cd /d "%CLASS_FILES%"
"%RMIC%" de.blichmann.idajava.rmi.IDARemoteAutomationPlugin
"%RMIC%" de.blichmann.idajava.pool.PoolServer

pause

popd
endlocal
