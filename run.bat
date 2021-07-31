@echo off
set BASE=%~dp0

REM Uncomment the following line if you have OutOfMemoryError errors
set CC_OPTS=-Xms8m -Xmx64m

REM %~dp0 is name of current script under NT
set CCDIR=%~dp0

if EXIST %JAVA_PATH% set JAVA_PATH=%BASE%${JAVA_PATH}
if defined JAVA_PATH goto setClassPath

:checkJava
set JAVA_PATH=%JAVA_HOME%\bin\java
set TOOLS_PATH=%JAVA_HOME%\lib\tools.jar
goto setClassPath

:setClassPath
set CLASSPATH=%BASE%;^
%BASE%*;^
%BASE%\dist;^
%BASE%\dist\*;^
%BASE%libs\*;

"%JAVA_PATH%" %CC_OPTS% -Dpath.base="%BASE%." -classpath "%CLASSPATH%" org.tonylin.stock.StockDataCollector %*
