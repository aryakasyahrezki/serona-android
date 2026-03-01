@echo off
set "ADB_PATH=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"

echo Menghubungkan Android ke Docker Laptop...

if exist "%ADB_PATH%" (
    "%ADB_PATH%" reverse tcp:8080 tcp:8080
    "%ADB_PATH%" reverse tcp:8000 tcp:8000
    echo Selesai! Sekarang kamu bisa pakai 127.0.0.1 di Android.
) else (
    echo [ERROR] adb tidak ditemukan di lokasi standar.
    echo Pastikan Android SDK terinstal di %LOCALAPPDATA%\Android\Sdk
)

pause