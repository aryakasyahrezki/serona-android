#!/bin/bash

echo "==================================================="
echo "   ANDROID TO DOCKER - AUTO SETUP AND REVERSE"
echo "==================================================="

# 1. Deteksi OS dan Tentukan lokasi standar ADB
if [[ "$OSTYPE" == "darwin"* ]]; then
    SDK_PATH="$HOME/Library/Android/sdk/platform-tools"
    # Default Mac sekarang pakai .zshrc
    CONF_FILE="$HOME/.zshrc"
    [[ ! -f "$CONF_FILE" ]] && CONF_FILE="$HOME/.bash_profile"
else
    SDK_PATH="$HOME/Android/Sdk/platform-tools"
    # Linux biasanya pakai .bashrc
    CONF_FILE="$HOME/.bashrc"
fi

ADB_EXE="adb"

# 2. Cek apakah adb sudah ada di System PATH
if command -v adb >/dev/null 2>&1; then
    echo "[OK] ADB sudah terdaftar di System Path."
else
    echo "[WAIT] ADB belum terdaftar di Path. Mencari di lokasi standar..."
    
    if [ -f "$SDK_PATH/adb" ]; then
        echo "[ACTION] Menambahkan ADB ke $CONF_FILE secara permanen..."
        
        # Cek dulu biar nggak duplikat
        if ! grep -q "$SDK_PATH" "$CONF_FILE" 2>/dev/null; then
            echo "" >> "$CONF_FILE"
            echo "# Android SDK Platform Tools" >> "$CONF_FILE"
            echo "export PATH=\$PATH:$SDK_PATH" >> "$CONF_FILE"
            echo "[SUCCESS] Berhasil ditambahkan ke $CONF_FILE!"
            echo "[INFO] Silakan restart Terminal setelah ini."
        else
            echo "[SKIP] Path sudah ada di konfigurasi."
        fi
        
        # Gunakan path lengkap agar langsung bisa dipakai saat ini juga
        ADB_EXE="$SDK_PATH/adb"
    else
        echo "[ERROR] ADB tidak ditemukan di $SDK_PATH"
        read -p "Tekan [Enter] untuk keluar..."
        exit 1
    fi
fi

echo ""
echo "---------------------------------------------------"
echo "   MENGHUBUNGKAN PORT 8080 DAN 8000"
echo "---------------------------------------------------"

# 3. Jalankan ADB Reverse
"$ADB_EXE" -d reverse tcp:8080 tcp:8080
"$ADB_EXE" -d reverse tcp:8000 tcp:8000

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Jembatan AKTIF!"
else
    echo "[ERROR] Gagal! Pastikan HP sudah tercolok dan USB Debugging ON."
fi

echo "---------------------------------------------------"
echo "Selesai! Tekan [Enter] untuk menutup."
read