# 📝 Grammar Quiz App

A production-quality offline MCQ quiz app built with **Kotlin, Jetpack Compose, MVVM, and Room**.

---

## ✨ Features

- 5 Grammar MCQs loaded from Room DB (pre-seeded on first launch)
- Select one option per question with animated feedback
- Previous / Next navigation with slide transitions
- Real-time progress indicator (Q1/5, percentage)
- Full Results screen with score, grade, and expandable reviews
- Offline-first — no network needed

---

## 🏗 Architecture

```
com.quizapp/
├── data/
│   ├── local/
│   │   ├── dao/            QuestionDao.kt
│   │   ├── entity/         QuestionEntity.kt
│   │   └── database/       QuizDatabase.kt, DatabaseSeeder.kt
│   └── repository/         QuestionRepository.kt, QuestionRepositoryImpl.kt
├── domain/
│   ├── model/              Question.kt, QuizResult.kt
│   └── usecase/            GetQuestionsUseCase.kt
├── presentation/
│   ├── quiz/               QuizScreen.kt, QuizViewModel.kt, QuizContract.kt
│   ├── results/            ResultsScreen.kt
│   ├── components/         QuizComponents.kt (reusable UI)
│   └── theme/              Theme.kt, Typography.kt
├── di/                     AppModule.kt (Hilt)
├── QuizApplication.kt
└── MainActivity.kt
```

### Layer Responsibilities

| Layer         | Responsibility                                                  |
|---------------|-----------------------------------------------------------------|
| **Data**      | Room DB, DAO, Entity, Repository implementation, DB seeding     |
| **Domain**    | Pure Kotlin models, Use cases, business logic                   |
| **Presentation** | ViewModel (StateFlow), Compose UI, sealed state/events       |

---

## 🎨 UI & Animations

- **Material 3** with a custom Indigo/Emerald/Rose color palette
- **Card-based layout** for questions and review items
- **Animated question transitions** — horizontal slide in/out via `AnimatedContent`
- **Option selection animation** — color, border, scale spring animations
- **Progress ring** animation on results screen (animated sweep arc)
- **Lottie animation** on results — trophy for perfect score, stars otherwise
- **Expandable review items** — chevron rotation + expand/shrink with fade

---

## 🗄 Room Database

```kotlin
// Entity
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: Int,
    val questionText: String,
    val optionA/B/C/D: String,
    val correctOption: String,  // "A" | "B" | "C" | "D"
    val explanation: String,
    val category: String
)
```

The DB is seeded on first launch via `DatabaseSeeder` — no migration headaches, no hardcoded SQL.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 35

### Steps

1. Clone / unzip the project
2. Open in Android Studio
3. Let Gradle sync finish
4. Run on emulator (API 26+) or physical device
---

## 📦 Key Dependencies

| Library              | Version   | Purpose                        |
|----------------------|-----------|--------------------------------|
| Jetpack Compose BOM  | 2024.08   | Declarative UI                 |
| Material 3           | BOM-linked| Design system                  |
| Room                 | 2.6.1     | Local SQLite DB (offline)      |
| Hilt                 | 2.51.1    | Dependency injection           |
| Lottie Compose       | 6.4.1     | Results screen animation       |
| Coroutines           | 1.8.1     | Async DB access                |
| Navigation Compose   | 2.7.7     | Screen navigation              |
| Lifecycle (ViewModel)| 2.8.4     | StateFlow + lifecycle-aware UI |

---

## 🧩 State Management

```
QuizScreenState (sealed interface)
├── Loading
├── Active(quizState: QuizState)
├── Finished(result: QuizResult)
└── Error(message: String)

QuizEvent (sealed interface)
├── SelectOption(questionId, optionKey)
├── NavigateNext
├── NavigatePrevious
├── SubmitQuiz
└── RestartQuiz
```

ViewModel exposes a single `StateFlow<QuizScreenState>`. UI composables are **stateless** — they only call `onEvent(QuizEvent)`.

---

## 📸 Screen Flow

```
Loading → Active Quiz → Results
              ↑              |
              └── Restart ←──┘
```
### 🧠 Quiz Screen
<img src="https://github.com/user-attachments/assets/96a502f6-1cf8-409c-a97d-048ccff36fb9" width="300" height="450"/>

### 🏆 Results Screen
<img src="https://github.com/user-attachments/assets/c3b37ab2-c8f0-41ef-97f3-3c492f29afe1" width="300" height="450"/>
<img src="https://github.com/user-attachments/assets/947d4470-4b83-4dfd-aa02-bfb5dc592574" width="300" height="450"/>

---

## 🔄 Adding More Questions

Open `DatabaseSeeder.kt` and add entries to the `grammarQuestions` list. The seeder only runs when the DB is empty, so you'll need to clear app data or increment the Room schema version to re-seed.
