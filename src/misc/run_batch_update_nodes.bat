@echo off
SETLOCAL
Set PY_PATH=%1%
Set SETTING_PATH=%2%
Set TO_DEPLOYS=%3%
call "%SETTING_PATH%"
call "%WAS_PATH%\setupCmdLine.bat"

call "%WAS_HOME%\bin\wsadmin" ^
-f %PY_PATH% %TO_DEPLOYS%

ENDLOCAL
EXIT