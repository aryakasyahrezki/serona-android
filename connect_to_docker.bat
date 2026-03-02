@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   ANDROID TO DOCKER - AUTO SETUP AND REVERSE
echo ===================================================

:: 1. Tentukan lokasi standar ADB
set "SDK_PATH=%LOCALAPPDATA%\Android\Sdk\platform-tools"
set "ADB_EXE=adb"

:: 2. Cek apakah adb sudah ada di System PATH
where adb >nul 2>nul
if %errorlevel% equ 0 (
    echo [OK] ADB sudah terdaftar di System Path.
) else (
    echo [WAIT] ADB belum terdaftar di Path. Mencari di lokasi standar...
    
    if exist "%SDK_PATH%\adb.exe" (
        echo [ACTION] Menambahkan ADB ke System Path secara permanen...
        
        :: Tambahkan ke Path User lewat PowerShell
        powershell -Command "$oldPath = [Environment]::GetEnvironmentVariable('Path', 'User'); if ($oldPath -notlike '*%SDK_PATH%*') { [Environment]::SetEnvironmentVariable('Path', \"$oldPath;%SDK_PATH%\", 'User') }"
        
        echo [SUCCESS] ADB berhasil ditambahkan ke Path!
        set "ADB_EXE=%SDK_PATH%\adb.exe"
    ) else (
        echo [ERROR] ADB tidak ditemukan di lokasi standar.
        pause
        exit
    )
)

echo.
echo ---------------------------------------------------
echo   MENGHUBUNGKAN PORT 8080 DAN 8000
echo ---------------------------------------------------

:: 3. Jalankan ADB Reverse (tanpa simbol aneh di echo)
"%ADB_EXE%" -d reverse tcp:8080 tcp:8080
"%ADB_EXE%" -d reverse tcp:8000 tcp:8000

if %errorlevel% equ 0 (
    echo [SUCCESS] Jembatan AKTIF!
    echo - Laptop port 8080 ke Android 8080
    echo - Laptop port 8000 ke Android 8000
) else (
    echo [ERROR] Gagal! Pastikan HP sudah tercolok dan USB Debugging ON.
)

echo ---------------------------------------------------
echo Selesai! Tekan apa saja untuk menutup.
pause