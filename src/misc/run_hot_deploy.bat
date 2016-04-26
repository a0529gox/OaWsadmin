@echo off
SETLOCAL
chcp 1252
SET TO_DEPLOYS=%1%
SET PY_PATH=%2%
SET SETTING_PATH=%3%
call "%SETTING_PATH%"

echo WAS_PATH=%WAS_PATH%
echo SERVER_NAME=%SERVER_NAME%
echo WORKSPACE_PATH=%WORKSPACE_PATH%
echo TO_DEPLOYS=%TO_DEPLOYS%
echo PY_PATH=%PY_PATH%
call "%WAS_PATH%\setupCmdLine.bat"

echo msg.starting
call "%WAS_HOME%\bin\wsadmin" -f %PY_PATH% %WORKSPACE_PATH% %TO_DEPLOYS%

echo msg.server.closing
rem call "%WAS_HOME%\bin\stopServer.bat" %SERVER_NAME%

echo msg.server.starting
rem call "%WAS_HOME%\bin\startServer.bat" %SERVER_NAME%

echo msg.deploy.done


ENDLOCAL
EXIT