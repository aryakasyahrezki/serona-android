#!/bin/bash

# Define ADB path based on OS (macOS or Linux)
if [[ "$OSTYPE" == "darwin"* ]]; then
    # Default path for macOS
    ADB_PATH="$HOME/Library/Android/sdk/platform-tools/adb"
else
    # Default path for Linux
    ADB_PATH="$HOME/Android/Sdk/platform-tools/adb"
fi

echo "----------------------------------------------------"
echo "  Android-to-Docker Connection Bridge (Unix/Mac)    "
echo "----------------------------------------------------"

# 1. Check if 'adb' is already in the System PATH
if command -v adb >/dev/null 2>&1; then
    adb reverse tcp:8080 tcp:8080
    adb reverse tcp:8000 tcp:8000
    echo "✅ Success! Using 'adb' from System PATH."
    echo "You can now use 127.0.0.1 in your Android code."

# 2. If not in PATH, check the standard SDK location
elif [ -f "$ADB_PATH" ]; then
    "$ADB_PATH" reverse tcp:8080 tcp:8080
    "$ADB_PATH" reverse tcp:8000 tcp:8000
    echo "✅ Success! Using 'adb' from SDK location."
    echo "You can now use 127.0.0.1 in your Android code."

else
    echo "❌ [ERROR] adb not found."
    echo "Please ensure Android SDK is installed or adb is in your PATH."
fi

echo "----------------------------------------------------"
read -p "Press [Enter] to exit..."