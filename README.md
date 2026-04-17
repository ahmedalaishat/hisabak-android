# Hisabak

Personal finance tracker for Android — Kotlin + Jetpack Compose, clean
architecture per feature, offline-first.

> ⚠️ Early development. Details will change; this README is intentionally
> short and will be expanded as the app matures.

## Tech stack

- Kotlin · Jetpack Compose (Material 3)
- Kotlin Coroutines · Flow
- Koin (DI)
- AndroidX Lifecycle (ViewModel, collectAsStateWithLifecycle)

Storage and remote sync are intentionally deferred; the data layer
currently uses in-memory mock repositories so the UI can be built
against the same contracts the database implementation will satisfy.

## Architecture

Feature-by-layer with clean-architecture inside each feature:

```
com.hisabak
├── core/common/                         shared value objects and primitives
└── feature/<name>/
    ├── domain/        entities, use cases, repository interfaces
    ├── data/          repository implementations
    └── presentation/  stateful Route + stateless Screen + ViewModel
```

## Inspiration

Hisabak is inspired by [**Hisabi**](https://github.com/hisabi-app/hisabi) —
a self-hosted Laravel personal-finance web app by Saleem Hadad. The
domain model (transactions, brands, categories, budgets, SMS ingestion,
dashboard metrics) mirrors Hisabi's so concepts map cleanly between the
two projects.

## License

[MIT](LICENSE).
