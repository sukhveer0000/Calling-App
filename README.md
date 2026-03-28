# 📞 RealCallingApp

A modern, high-performance Android Dialer application built with **Jetpack Compose** and **Clean Architecture**. This app provides a seamless experience for managing call logs and contacts with real-time updates.

---

## ✨ Features

* **Real-time Call Logs:** Instantly fetches and displays call history (Incoming, Outgoing, Missed).
* **Live Updates:** Automatically refreshes the log list when returning from a call using Lifecycle observers.
* **Contact Integration:** Smoothly syncs and displays device contacts with quick-dial functionality.
* **Modern Material 3 UI:** Clean, intuitive design with Google Dialer-inspired aesthetics, including circular avatars and dynamic colors.
* **Smart Time Formatting:** Human-readable timestamps like "2 min ago", "Just now", or "12 Oct".
* **Optimized Performance:** Uses Kotlin Coroutines and Flow to ensure the UI remains smooth and "stuck-free" during data fetching.
* **Permission Handling:** Robust implementation of Android's runtime permissions for Contacts and Call Logs.

---

## 🛠 Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Asynchronous:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & `withContext(Dispatchers.IO)`
* **Lifecycle:** Lifecycle-aware components (ViewModel, DisposableEffect, LifecycleObserver)
* **Components:** Scaffold, TopAppBar, LazyColumn, HorizontalDivider

---

## 🏗 Project Structure

```text
com.kabir.realcallingapp
├── model          # Data classes (CallLogModel, ContactModel)
├── repository     # Data fetching logic (Cursors, ContentProviders)
├── viewmodel      # Business logic and UI State management
├── screens        # Jetpack Compose UI screens (Logs, Contacts)
└── ui.theme       # Custom Material3 theme and styling
