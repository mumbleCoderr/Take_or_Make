# 📱 Take or Make

**Take or Make** is a native Android mobile application that acts as an innovative hybrid between a classified ads service and a service booking platform (combining the best features of apps like OLX, Booksy, and Vinted).

🎓 *This project was developed as my **Engineering Thesis (B.Eng.)** at the Faculty of Computer Science (February 2026).*

---

## 🎥 Video Presentation

Click the image below to watch a short showcase of the app's capabilities on YouTube:

[Take or Make App Presentation](https://youtu.be/yacjtEgf6FU?si=320oDoP6HVnMz68P)

---

## 🚀 About the Project

Nowadays, there is a lack of a single, cohesive tool for offering local neighborhood services and selling items in one place. "Take or Make" fills this gap, allowing users to both advertise their services (e.g., lawn mowing, tutoring, finishing works) and sell physical products.

The application is divided into two main modules:
* **🛒 Take:** Browse the latest listings, use advanced filtering (location, price, categories), add items to favorites, and place orders/book services.
* **🛠 Make:** Manage your own offers and use an intuitive, step-by-step Wizard for creating new listings with real-time data validation.

### ✨ Key Features
- **Guest Mode with Account Conversion:** Users can browse the app anonymously. If they decide to create an account, their temporary session (including liked offers) is seamlessly converted into a permanent account.
- **Authentication:** Supports both Google Sign-In and standard Email/Password login.
- **Pagination & Optimization:** Data is fetched from Firestore in chunks (using the `startAfter` cursor), which minimizes bandwidth consumption and memory usage.
- **Advanced Local Filtering:** A deterministic search engine operating directly in the device's memory (Client-side filtering) for instant results.
- **Optimistic UI Updates:** Immediate interface feedback (e.g., when liking an offer) without waiting for the server's response, backed by a rollback mechanism in case of network errors.

---

## 🛠 Tech Stack & Architecture

The app was built following Google's latest modern development guidelines, emphasizing code quality, testability, and scalability.

* **Language:** Kotlin (2.1.0)
* **UI:** Jetpack Compose (Material Design 3)
* **Architecture:** Clean Architecture + MVVM + Feature-First Modularization
* **Concurrency:** Kotlin Coroutines & StateFlow
* **Dependency Injection (DI):** Koin
* **Backend as a Service:** Firebase (Firestore NoSQL, Firebase Auth)
* **Navigation:** Navigation Compose (Nested Graphs)
* **Build System:** Gradle (Kotlin DSL + Version Catalogs)
* **Testing:** JUnit 4, Mockk, Jetpack Compose UI Test

---

## 📂 Project Structure (Feature-First)

The project steps away from the classic *Package by Layer* approach in favor of **Package by Feature**. Each functional module (e.g., `auth`, `offer`, `profile`) is self-contained and holds its own `domain`, `data`, and `presentation` layers.

---

