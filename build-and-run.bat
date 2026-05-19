@echo off
setlocal enabledelayedexpansion

set KAFKA_HOME=C:\Users\2478111\Downloads\kafka_2.13-4.2.0\kafka_2.13-4.2.0
set BASE=C:\Users\2478111\OneDrive - Cognizant\Documents\TAX-EASE-MANAGEMENT-SYSTEM\TaxEase-Microservices
set JAVA_OPTS=-Xms256m -Xmx512m

echo.
echo ====================================================
echo   TaxEase - Build and Run All Services
echo ====================================================
echo.

where java >nul 2>&1 || (echo [ERROR] Java not found. && pause && exit /b 1)
where mvn  >nul 2>&1 || (echo [ERROR] Maven not found. && pause && exit /b 1)
echo [OK] Java and Maven found.

echo.
echo [BUILD] Running mvn package -DskipTests ...
echo         This may take 1-2 minutes on first run.
echo.
cd /d "%BASE%"
call mvn package -DskipTests -q
if %errorlevel% neq 0 (
    echo [ERROR] Build failed. Check output above.
    pause
    exit /b 1
)
echo [OK] Build successful.
echo.

echo [1/11] Starting Kafka...
start "Kafka" cmd /k ""%KAFKA_HOME%\bin\windows\kafka-server-start.bat" "%KAFKA_HOME%\config\server.properties""
timeout /t 10 /nobreak >nul

echo [2/11] Starting Service Registry (8760)...
start "ServiceRegistry" cmd /k "java %JAVA_OPTS% -jar "%BASE%\service-registry\target\service-registry-0.0.1-SNAPSHOT.jar""
timeout /t 20 /nobreak >nul

echo [3/11] Starting Config Server (8888)...
start "ConfigServer" cmd /k "java %JAVA_OPTS% -jar "%BASE%\config-server\target\config-server-0.0.1-SNAPSHOT.jar""
timeout /t 15 /nobreak >nul

echo [4/11] Starting API Gateway (8081)...
start "ApiGateway" cmd /k "java %JAVA_OPTS% -jar "%BASE%\api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

echo [5/11] Starting Identity Service (8082)...
start "IdentityService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\identity-service\target\identity-service-0.0.1-SNAPSHOT.jar""
timeout /t 8 /nobreak >nul

echo [6/11] Starting Taxpayer Service (8088)...
start "TaxpayerService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\taxpayer-service\target\taxpayer-service-0.0.1-SNAPSHOT.jar""
timeout /t 8 /nobreak >nul

echo [7/11] Starting Tax Filing Service (8083)...
start "TaxFilingService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\tax-filing-service\target\tax-filing-service-0.0.1-SNAPSHOT.jar""
timeout /t 8 /nobreak >nul

echo [8/11] Starting Payment Service (8084)...
start "PaymentService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\payment-service\target\payment-service-0.0.1-SNAPSHOT.jar""
timeout /t 8 /nobreak >nul

echo [9/11] Starting Audit Service (8085)...
start "AuditService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\audit-service\target\audit-service-0.0.1-SNAPSHOT.jar""
timeout /t 8 /nobreak >nul

echo [10/11] Starting Notification Service (8086)...
start "NotificationService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\notification-service\target\notification-service-0.0.1-SNAPSHOT.jar""
timeout /t 8 /nobreak >nul

echo [11/11] Starting Report Service (8087)...
start "ReportService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\report-service\target\report-service-0.0.1-SNAPSHOT.jar""

echo.
echo Waiting 20s for all services to register with Eureka...
timeout /t 20 /nobreak >nul

echo Starting Angular Frontend (4200)...
start "AngularFrontend" cmd /k "cd /d "%BASE%\taxease-frontend" && npx ng serve --open"

echo.
echo ====================================================
echo   All services started!
echo ====================================================
echo   Eureka       : http://localhost:8760
echo   API Gateway  : http://localhost:8081
echo   Frontend     : http://localhost:4200
echo   Config Server: http://localhost:8888
echo   Identity     : http://localhost:8082
echo   Taxpayer     : http://localhost:8088
echo   Tax Filing   : http://localhost:8083
echo   Payment      : http://localhost:8084
echo   Audit        : http://localhost:8085
echo   Notification : http://localhost:8086
echo   Report       : http://localhost:8087
echo ====================================================
echo   TIP: Run stop-all.bat to shut everything down.
echo ====================================================
echo.
pause
