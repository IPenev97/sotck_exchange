📈 Stock Exchange App (Android · Jetpack Compose)

A lightweight stock-tracking demo application built entirely with Jetpack Compose, Hilt, MVVM, and Navigation Component.
The app simulates live stock updates, allows navigating to stock details, supports deep links, and demonstrates real-time UI reactions (e.g., price flashing green/red when changing).

🚀 Features
✔ Real-Time Stock Feed

Simulated live stock prices streamed through a central IStockRepository.

Connect / start / stop feed toggle.

UI reacts instantly to each update.

✔ Stock List

Displays all stocks with:

Current price

Arrow indicator

Color-flash on price change (green ↑, red ↓)

✔ Stock Details Screen

Shows full details for a selected stock.

Shares the same live data stream as the list screen.

Price flashing reusable composable used in both screens.

✔ Deep Linking

Supports opening the details screen directly using a custom URI:

stocks://symbol/{symbol}


Example:

adb shell am start -a android.intent.action.VIEW -d "stocks://symbol/AAPL"

✔ MVVM + StateFlow + SavedStateHandle

ViewModels observe repository flows.

Navigation arguments automatically injected into ViewModels.

Repository feed state synced on every screen entry.

🧱 Architecture Overview

This project intentionally uses a simplified architecture to keep the focus on UI, Compose, and state management.

UI (Jetpack Compose)
        ↓
ViewModel (MVVM, exposes UI state)
        ↓
Repository (in-memory fake stock data, simulates updates)

❗ No Dedicated Data Layer (By Design)

This app does not use a traditional data layer (Room, Retrofit, DTO mappers, etc.).

Instead:

Sample stocks are instantiated directly inside the repository.

The repository acts as the single source of truth.

Stock updates are simulated through a coroutine loop.

No API, no database, no separate DTO → Model layer.

This keeps the project small and focused on UI reactivity and architecture patterns.

🧩 Tech Stack
Kotlin
Jetpack Compose

Material3

Navigation Compose

Compose UI testing

Hilt

Coroutines & Flow

🧪 Testing

This project includes two example UI tests that demonstrate the basic Jetpack Compose testing flow:

Interacting with UI elements (clicking buttons, rows, etc.)

Verifying UI state changes through Compose test assertions

These tests serve as simple, clear examples of how to write Compose UI tests in a real application context.

