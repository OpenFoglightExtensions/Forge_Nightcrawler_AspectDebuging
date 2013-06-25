@REM Copyright 2013 Dell Inc.
@REM ALL RIGHTS RESERVED.
@REM 
@REM This software is the confidential and proprietary information of
@REM Dell Inc. ("Confidential Information").  You shall not
@REM disclose such Confidential Information and shall use it only in
@REM accordance with the terms of the license agreement you entered
@REM into with Dell Inc.
@REM 
@REM DELL INC. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT
@REM THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED,
@REM INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
@REM MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
@REM NON-INFRINGEMENT. DELL SHALL NOT BE LIABLE FOR ANY
@REM DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
@REM OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
@echo off

SETLOCAL

rem This is for running the command line from the folder created by unzipping fglcmd.zip
set instroot="%~dp0."

IF EXIST %instroot%\foglight-cmdline-client.jar (
    set CMDLINE_HOME="%instroot%"
) ELSE (
    SET instroot="%~dp0.."
    set CMDLINE_HOME="%~dp0..\tools\lib"
)

rem First try our local JRE
set JRE=%instroot%\jre
if exist %JRE%\lib set JAVA_OPTS=-server %JAVA_OPTS% && goto runApp

rem Try to fall back on JAVA_HOME\jre
set JRE="%JAVA_HOME%"\jre
if exist "%JRE%\lib" goto runApp

rem Try to fall back on JAVA_HOME
set JRE="%JAVA_HOME%"
if exist %JRE%\lib goto runApp
echo.
echo The JDK wasn't found in directory %JRE%.
echo Please edit the %0 script so that the JRE variable points to the
echo root directory of your Java installation.
goto finish
:runApp

rem ##########################################################################
rem ################   NO CHANGES SHOULD BE NECESSARY BELOW   ################
rem ##########################################################################

set PATH="%JAVA_HOME%"\bin;%PATH%

%JRE%\bin\java %JAVA_OPTS% -jar %CMDLINE_HOME%\foglight-cmdline-client.jar %*
goto finish

:finish
ENDLOCAL

