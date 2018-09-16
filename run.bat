@ECHO OFF
SET tempTerm=%TERM%
SET TERM=dumb
./gradlew.bat run

SET TERM=%tempTerm%
