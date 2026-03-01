# 📱 Serona Android - Mobile App

> Mobile application for **Serona**, an AI-powered personal makeup assistant that provides real-time face analysis and personalized beauty recommendations.

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-7F52FF)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-1.5-4285F4)
![Min SDK](https://img.shields.io/badge/Min_SDK-24-3DDC84)
![Target SDK](https://img.shields.io/badge/Target_SDK-34-3DDC84)

**Built with:** Kotlin • Jetpack Compose • CameraX • Retrofit • Hilt

---

## 📖 What is Serona?

**Serona** is an Android app that helps users discover makeup styles suited to their unique features through **live real-time face scanning**.

**Key Features:**
1. **Live face shape detection** — Heart, Oblong, Oval, Round, Square
2. **Skin tone analysis** — Fair Light, Medium Tan, Deep
3. **Personalized recommendations** — Makeup tutorials tailored to face shape and skin tone

🔗 [Download on Google Play Store](https://play.google.com/store/apps/details?id=com.serona.app&pcampaignid=web_share)

---

## ⚡ Quick Start (This App Only)

### Prerequisites
- Android Studio (Otter 2025.2.1 or newer)
- Android Emulator or Physical Device
- Backend services running (see Complete System Setup below)

### Run in Android Studio
```bash
# Clone repository
git clone https://github.com/aryakasyahrezki/serona-android.git
cd serona-android

# Open in Android Studio
# File → Open → Select serona-android folder

# Select Build Variant: debug
# View → Tool Windows → Build Variants → Set :app to debug

# Run app (Shift + F10)
```

**Note:** This assumes backend services (ML + Backend) are already running. If not, see Complete System Setup below.

---

## 🔗 Running Complete Serona System

> **Note:** To use the app, you need all services running (ML + Backend + Android). Follow the steps below to set up the complete system.

### System Architecture
```
Android App (serona-android)
    ├─→ ML API (serona-ml) :8000
    │   └─→ Returns face shape + skin tone
    │
    └─→ Backend API (serona-backend) :8080
        └─→ Stores user data + provides articles
```

**Note:** ML API and Backend API are independent services. The Android app communicates with both separately.

---

### Prerequisites
- Docker & Docker Compose
- Android Studio (Otter 2025.2.1+)
- Git

---

### Step 1: Start ML Service
```bash
# Clone ML repository
git clone https://github.com/aryakasyahrezki/serona-ml.git
cd serona-ml

# Build and run
docker build -t serona-ml .
docker run -d -p 8000:8000 --name serona-ml-api serona-ml

# Verify
curl http://localhost:8000/
# Expected: {"status":"online",...}
```

---

### Step 2: Start Backend Service
```bash
# Navigate to parent directory
cd ..

# Clone backend repository
git clone https://github.com/aryakasyahrezki/serona-backend.git
cd serona-backend

# Copy environment file
copy .env.example .env

# Build and run (Laravel + MySQL)
docker compose up -d --build

# Wait ~30 seconds for migrations to complete

# Verify
curl http://localhost:8080/
# Expected: Laravel response
```

---

### Step 3: Run Android App
```bash
# Navigate to parent directory
cd ..

# Clone Android repository
git clone https://github.com/aryakasyahrezki/serona-android.git
cd serona-android

# Open in Android Studio
# File → Open → Select serona-android folder
```

---

### Step 4: Setup Port Forwarding

**Android devices cannot access `localhost` directly. Run the connection script:**

**Windows:**
```bash
connect_to_docker.bat
```

**Mac/Linux:**
```bash
./connect_to_docker.sh
```

This forwards device ports to your computer:
- Port 8080 (Backend) → `http://127.0.0.1:8080`
- Port 8000 (ML API) → `http://127.0.0.1:8000`

---

### Step 5: Configure Camera (Emulator Only)

If using Android Emulator for face scanning:

1. **Device Manager** → Edit AVD
2. **Show Advanced Settings**
3. **Camera** → Front Camera → Webcam0
4. **Finish**

---

### Step 6: Run App

1. **Select Build Variant:** View → Build Variants → Set `:app` to `debug`
2. **Run:** Click Run (▶️) or press Shift+F10
3. **Test:**
   - Register new account
   - Login
   - Open camera for face scan
   - View results and recommendations

---

### Verification Checklist

After starting all services:

- [ ] ML API running: http://localhost:8000
- [ ] Backend running: http://localhost:8080
- [ ] Port forwarding active: Run `adb reverse --list`
- [ ] Android app opens without crashes
- [ ] Face scan works and returns predictions

### Service Ports

| Service | Port | Desktop URL | Android Emulator URL |
|---------|------|-------------|---------------------|
| ML API | 8000 | http://localhost:8000 | http://127.0.0.1:8000 |
| Backend | 8080 | http://localhost:8080 | http://127.0.0.1:8080 |
| Database | 3306 | localhost:3306 | - |

---

## 🔐 Build Variants

| Variant | Environment | API Base URL |
|---------|-------------|--------------|
| **debug** | Local Docker | http://127.0.0.1:8080/api/ |
| **release** | Production | https://serona.azurewebsites.net/api/ |

**Note:** In debug mode, you must register a new account since the local database starts empty.

---

## 🔗 Related Repositories

| Repository | Description | Link |
|------------|-------------|------|
| **serona-ml** | ML API for face analysis | [serona-ml](https://github.com/aryakasyahrezki/serona-ml) |
| **serona-backend** | Backend API & database | [serona-backend](https://github.com/aryakasyahrezki/serona-backend) |
| **serona-android** | Android mobile app ← *You are here* | [serona-android](https://github.com/aryakasyahrezki/serona-android) |

---

## 👥 Team

**Group 5 — DINAS**

| Name | Student ID |
|------|-----------|
| Aryaka Syahrezki | 2802540244 |
| Dea Audreyla Hadi | 2802540074 |
| I Gusti Ngurah Radithya Bagus Santosa | 2802538675 |
| Iyurichie Lay | 2802539980 |
| Shinta Aulia | 2802538731 |

---

## 📄 License

This project is for academic purposes — Bina Nusantara University.
