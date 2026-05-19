@echo off
echo ========================================
echo TaxEase Microservices - Quick Start
echo ========================================
echo.

echo [1/4] Checking prerequisites...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Install Java 21 first.
    exit /b 1
)
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven not found. Install Maven 3.9+ first.
    exit /b 1
)
echo ✓ Java and Maven found

echo.
echo [2/4] Compiling all services...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed. Check logs above.
    exit /b 1
)
echo ✓ All services compiled successfully

echo.
echo [3/4] Packaging all services...
call mvn package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: Packaging failed.
    exit /b 1
)
echo ✓ All services packaged

echo.
echo [4/4] Starting services with Docker Compose...
echo Make sure Docker Desktop is running!
pause
docker-compose up --build

echo.
echo ========================================
echo All services started!
echo ========================================
echo Eureka:     http://localhost:8760
echo Gateway:    http://localhost:8081
echo Prometheus: http://localhost:9090
echo Grafana:    http://localhost:3000
echo ========================================
