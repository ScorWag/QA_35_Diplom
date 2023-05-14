# Процедура запуска автотестов
## Окружение, на котором осуществлялись разработка и прогон автотестов:
* Java: 
  * openjdk 11.0.18 2023-01-17
  * OpenJDK Runtime Environment Temurin-11.0.18+10 (build 11.0.18+10)
  * OpenJDK 64-Bit Server VM Temurin-11.0.18+10 (build 11.0.18+10, mixed mode)
* Android Studio Dolphin 2021.3.1 Patch 1, эмулятор Pixel3a API 29 Android 10.0
* Allure версии 2.20.1 - для формирование отчета о прогоне тестов
## Запуск автотестов
* Склонировать репозиторий ``` git clone ```
* Открыть проект fmh-android в Android Studio с предустановленным эмулятором и произвести запуск с помощью интерфейса Android Studio либо командой в терминале ``` ./gradlew connectedAndroidTest ```
## Отчет о прогоне тестов в Allure
* Из корня склонированного репозитория командой ``` allure serve ``` открыть отчет Allure 
