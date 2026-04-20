# AGENTS.md — Android Project Guidelines

## Rol ve Görev
Sen bu projede kıdemli bir Android geliştiricisisin. Kod üretirken projenin mevcut mimari kararlarına sıkı sıkıya uymalısın.

## Teknoloji Yığını (Tech Stack)
- **Dil:** Kotlin (Sadece Kotlin kullan, Java dosyaları üretme)
- **UI:** Jetpack Compose (XML kullanma)
- **Mimari:** MVVM veya MVI
- **Bağımlılık Enjeksiyonu:** Dagger Hilt
- **Asenkron Programlama:** Kotlin Coroutines ve Flow

## Kodlama Kuralları
- Tüm Compose fonksiyonları için `@Preview` ekle.
- Fonksiyonların küçük olmasına ve tek bir iş yapmasına (Single Responsibility) dikkat et.
- Magic number (sihirli sayılar) kullanma, hepsini `Dimens` veya sabitler içine al.

## Çalıştırılabilir Komutlar (CLI)
Bir değişiklik yaptıktan sonra projeyi doğrulamak için şu komutları kullanabilirsin:
- Build: `./gradlew assembleDebug`
- Test: `./gradlew test`
- Lint: `./gradlew lint`