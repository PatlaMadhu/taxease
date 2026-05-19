@echo off
setlocal enabledelayedexpansion

set KAFKA_HOME=C:\Users\2478111\Downloads\kafka_2.13-4.2.0\kafka_2.13-4.2.0
set BASE=C:\Users\2478111\OneDrive - Cognizant\Documents\TAX-EASE-MANAGEMENT-SYSTEM\TaxEase-Microservices
set JAVA_OPTS=-Xms256m -Xmx512m

echo.
echo ============================================================
echo   TaxEase - Full Stack Startup
echo ============================================================
echo.

REM ── Step 1: Start Kafka (KRaft mode) ─────────────────────────
echo [1/13] Starting Kafka (KRaft)...
start "Kafka" cmd /k ""%KAFKA_HOME%\bin\windows\kafka-server-start.bat" "%KAFKA_HOME%\config\server.properties""
echo       Waiting 10s for Kafka to be ready...
timeout /t 10 /nobreak >nul

REM ── Step 2: Service Registry (Eureka) ────────────────────────
echo [2/13] Starting Service Registry (port 8760)...
start "ServiceRegistry" cmd /k "java %JAVA_OPTS% -jar "%BASE%\service-registry\target\service-registry-0.0.1-SNAPSHOT.jar""
echo       Waiting 20s for Eureka to be ready...
timeout /t 20 /nobreak >nul

REM ── Step 3: Config Server ─────────────────────────────────────
echo [3/13] Starting Config Server (port 8888)...
start "ConfigServer" cmd /k "java %JAVA_OPTS% -jar "%BASE%\config-server\target\config-server-0.0.1-SNAPSHOT.jar""
echo       Waiting 15s...
timeout /t 15 /nobreak >nul

REM ── Step 4: API Gateway ───────────────────────────────────────
echo [4/13] Starting API Gateway (port 8081)...
start "ApiGateway" cmd /k "java %JAVA_OPTS% -jar "%BASE%\api-gateway\target\api-gateway-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 5: Identity Service ──────────────────────────────────
echo [5/13] Starting Identity Service (port 8082)...
start "IdentityService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\identity-service\target\identity-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 6: Taxpayer Service ──────────────────────────────────
echo [6/13] Starting Taxpayer Service (port 8088)...
start "TaxpayerService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\taxpayer-service\target\taxpayer-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 7: Tax Filing Service ────────────────────────────────
echo [7/13] Starting Tax Filing Service (port 8083)...
start "TaxFilingService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\tax-filing-service\target\tax-filing-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 8: Payment Service ───────────────────────────────────
echo [8/13] Starting Payment Service (port 8084)...
start "PaymentService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\payment-service\target\payment-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 9: Audit Service ─────────────────────────────────────
echo [9/13] Starting Audit Service (port 8085)...
start "AuditService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\audit-service\target\audit-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 10: Notification Service ────────────────────────────
echo [10/13] Starting Notification Service (port 8086)...
start "NotificationService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\notification-service\target\notification-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 11: Report Service ───────────────────────────────────
echo [11/13] Starting Report Service (port 8087)...
start "ReportService" cmd /k "java %JAVA_OPTS% -jar "%BASE%\report-service\target\report-service-0.0.1-SNAPSHOT.jar""
timeout /t 10 /nobreak >nul

REM ── Step 12: Wait for all services to register ───────────────
echo [12/13] Waiting 20s for all services to register with Eureka...
timeout /t 20 /nobreak >nul

REM ── Step 13: Angular Frontend ────────────────────────────────
echo [13/13] Starting Angular Frontend (port 4200)...
start "AngularFrontend" cmd /k "cd /d "%BASE%\taxease-frontend" && npx ng serve --open"

echo.
echo ============================================================
echo   All services started!
echo ============================================================
echo.
echo   Eureka Dashboard  : http://localhost:8760
echo   API Gateway       : http://localhost:8081
echo   Angular Frontend  : http://localhost:4200
echo   Config Server     : http://localhost:8888
echo.
echo   Microservices:
echo     Identity        : http://localhost:8082
echo     Taxpayer        : http://localhost:8088
echo     Tax Filing      : http://localhost:8083
echo     Payment         : http://localhost:8084
echo     Audit           : http://localhost:8085
echo     Notification    : http://localhost:8086
echo     Report          : http://localhost:8087
echo.
echo   NOTE: Services take ~60s to fully start.
echo         Check Eureka at http://localhost:8760 to verify.
echo ============================================================
echo.
pause
