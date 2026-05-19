@echo off
echo Stopping all TaxEase services...

REM Kill Java processes (all microservices)
taskkill /f /im java.exe >nul 2>&1
echo [OK] All Java microservices stopped.

REM Kill Node (Angular dev server)
taskkill /f /im node.exe >nul 2>&1
echo [OK] Angular frontend stopped.

REM Stop Kafka gracefully
set KAFKA_HOME=C:\Users\2478111\Downloads\kafka_2.13-4.2.0\kafka_2.13-4.2.0
call "%KAFKA_HOME%\bin\windows\kafka-server-stop.bat" >nul 2>&1
echo [OK] Kafka stopped.

echo.
echo All TaxEase services stopped.
pause
