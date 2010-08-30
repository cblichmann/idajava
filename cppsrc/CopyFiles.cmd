@echo off
setlocal enabledelayedexpansion

if not "%SKIP_POST_BUILD_EVENTS%" == "" (
	goto :eof
)

rem openfiles > nul 2>&1
rem if "%ERRORLEVEL%" == "1" (
rem 	echo Logged-on user does not have administrative privilege, skipping
rem 	echo post build script.
rem 	goto :eof
rem )


:: IDA home directory (leave empty for auto-configuration)
if not defined IDA_HOME set IDA_HOME=
set BIN_DIR=.\Debug

if "%PROCESSOR_ARCHITECTURE%" == "AMD64" (
	set REG=%SystemRoot%\SysWOW64\reg.exe
) else (
	set REG=%SystemRoot%\System32\reg.exe
)

:: ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

if "%IDA_HOME%" == "" ( 
	:: Get IDA installation directory from registry
	set IDA_REG="HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\IDA Pro_is1" /v InstallLocation
	"%REG%" query !IDA_REG! > nul 2>&1
	if %ERRORLEVEL% equ 0 (
		for /f "skip=2 tokens=2,*" %%I in ('"%REG%" query !IDA_REG! 2^>^&1') do (
echo MARK
			set IDA_HOME=%%J
			if "!IDA_HOME:~-1!" == "\" (
				:: Clean up trailing slash
				set IDA_HOME=!IDA_HOME:~0,-1!
			)
		)
	)
	
	:: Still empty? Try to set a sensible default
	if "!IDA_HOME!" == "" set IDA_HOME=%ProgramFiles%\IDA
)

if not exist "!IDA_HOME!" (
	echo [%0] Error: Cannot determine IDA installation directory.
	echo [%0]        You can either edit this script to set the directory or define
	echo [%0]        the environment variable IDA_HOME.
	goto :eof
)

set IDA_PLUGINDIR=%IDA_HOME%\plugins
if not exist "%IDA_PLUGINDIR%" mkdir "%IDA_PLUGINDIR%"

cd /d %~dp0
echo [%0] Info: Copying plugins to "%IDA_PLUGINDIR%"...
for %%I in (idajava.plw idajava.p64) do (
	if exist "%BIN_DIR%\%%I" (
		copy /Y "%BIN_DIR%\%%I" "%IDA_PLUGINDIR%"
	)
)

echo.> %BIN_DIR%\copied.stamp

endlocal
