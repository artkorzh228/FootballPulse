# FootballPulse ⚽

Android app for tracking football matches and standings across top European leagues.

## Features
- Live and finished match results
- League selector: Premier League, La Liga, Bundesliga, Ligue 1, Serie A
- Match scores and status
- Clean MVVM architecture

## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM + Repository pattern
- **Networking:** Retrofit 2 + OkHttp
- **Async:** Kotlin Coroutines + StateFlow
- **DI:** Manual (Hilt planned)
- **Database:** Room (planned)
- **Min SDK:** 26 (Android 8.0)

## Setup
1. Clone the repository
2. Get a free API key from [football-data.org](https://www.football-data.org)
3. Add to `local.properties`:
```
FOOTBALL_API_KEY=your_key_here
```
4. Build and run

## Architecture
```
UI (Activity + RecyclerView)
    ↓
ViewModel + StateFlow
    ↓
Repository
    ↓
Retrofit API
```

## Screenshots
_Coming soon_
