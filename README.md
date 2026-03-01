# 📱 Serona Frontend (Android App)

> **Serona** is an AI-powered personal makeup assistant. This repository contains the **Frontend** mobile application built with **Kotlin** and **Jetpack Compose**, serving as the primary user interface for real-time face analysis and beauty recommendations.

---

## 🛠 Built With

- **Kotlin 1.9** & **Jetpack Compose** (Modern UI)
- **CameraX** (Real-time Frame Processing)
- **Dagger Hilt** (Dependency Injection)
- **Retrofit 2.9** (Network Orchestration)
- **Material 3** (User Experience)

---

## 📖 What is Serona?

**Serona** helps users discover makeup styles suited to their unique features. Using the device's camera, the app performs **live real-time scanning**. The ML model continuously analyzes face shape and skin tone, updating predictions automatically.

Once the user clicks **"See Result"**, the app provides:

- Detailed face shape classification  
- Skin tone analysis  
- Personalized step-by-step makeup tutorial recommendations  

### ✨ Core Features

1. **Live Face Scanning** — Real-time detection (Heart, Oblong, Oval, Round, Square)
2. **Skin Tone Detection** — Regional color analysis (Fair Light, Medium Tan, Deep)
3. **Personalized Tutorials** — Tailored articles based on combined analysis

🔗 [Download on Google Playstore](https://play.google.com/store/apps/details?id=com.serona.app)

---

# ⚙️ Local Installation & Setup

Follow these steps in order to ensure the environment is ready before running the Android app.

---

## 🧩 Step 1: Clone All Repositories & Run Services

You need the entire **Serona ecosystem** (ML, Backend, and Android) to run the project locally.

```bash
# 1️⃣ Clone and run Serona ML Service
git clone https://github.com/aryakasyahrezki/serona-ml.git
cd serona-ml
docker-compose up -d --build

# 2️⃣ Clone and run Serona Backend (Laravel)
cd ..
git clone https://github.com/aryakasyahrezki/serona-backend.git
cd serona-backend

# Copy .env.example → .env and configure if needed
docker-compose up -d --build

# 3️⃣ Clone Serona Android (This Repo)
cd ..
git clone https://github.com/aryakasyahrezki/serona-android.git
```

---

## 🔧 Step 2: Configure Android Build Variant

1. Open the `serona-android` project in **Android Studio (Otter | 2025.2.1 or newer)**  
2. Go to: `View` → `Tool Windows` → `Build Variants`


3. Set the **Active Build Variant** for the `:app` module to: `debug`

This ensures the app communicates with your local Docker containers using: `http://127.0.0.1:8080/api/`

---

## 🔁 Step 3: Execute the Network Bridge

The app uses `127.0.0.1` to access local services.  
You must bridge ports from your laptop to your device using `adb reverse`.

Run the script located in the root folder:

### Option A: Double Click
- Locate `connect_to_docker.bat`
- Double-click the file

### Option B: Via Terminal

```powershell
./connect_to_docker.bat
```

---

## 🚀 Step 4: Connect & Run the App

You can use either:

- **Android Emulator**
- **Physical Smartphone (USB Cable)**

---

### 🖥 Option 1: Android Emulator (Webcam Setup)

To enable real-time scanning:

1. Open **Device Manager** in Android Studio  
2. Click **Edit (pencil icon)** on your AVD  
3. Select **Show Advanced Settings**  
4. Scroll to **Camera**  
5. Set: `Front Camera` → `Webcam0`
6. Click **Finish**
7. Start the emulator
8. Click **Run** in Android Studio



### 📱 Option 2: Physical Device (USB)

1. Connect your phone via USB  
2. Enable **USB Debugging** in Developer Options  
3. Run `connect_to_docker.bat`  
4. Select your device in Android Studio  
5. Click **Run**



## 🔐 Build Variant Logic

| Variant  | Target Environment       | API Base URL                              |
|----------|-------------------------|-------------------------------------------|
| Debug    | Local Docker Containers | http://127.0.0.1:8080/api/                |
| Release  | Production (Azure)      | https://serona.azurewebsites.net/api/     |

> ⚠️ If you encounter a **"User Not Found"** error in Debug mode, register a new account locally because your local MySQL database starts empty.

---

## 🔗 Related Repositories

| Repository        | Role                              | Link |
|------------------|-----------------------------------|------|
| serona-backend   | Business Logic & Articles         | https://github.com/aryakasyahrezki/serona-backend |
| serona-ml        | Face Shape & Skin Tone Analysis   | https://github.com/aryakasyahrezki/serona-ml |
| serona-android   | Mobile UI (This Repository)       | https://github.com/aryakasyahrezki/serona-android |

---

## 👥 Team — Group 5 (DINAS Group)

| Name                               | Student ID  |
|------------------------------------|-------------|
| Aryaka Syahrezki                   | 2802540244  |
| Dea Audreyla Hadi                  | 2802540074  |
| I Gusti Ngurah Radithya Bagus Santosa | 2802538675 |
| Iyurichie Lay                      | 2802539980  |
| Shinta Aulia                       | 2802538731  |

---

# 📄 License

This project is developed for academic purposes — **Bina Nusantara University**
