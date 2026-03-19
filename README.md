Исполнитель: Егорова Эльвира

Дата сдачи: 19.03.2026
# Облачное хранилище (Cloud File Storage)

Дипломный проект – REST-сервис для загрузки, скачивания и управления файлами с аутентификацией пользователей.  
Фронтенд предоставляется отдельно и взаимодействует с бэкендом по спецификации OpenAPI.

---

## Содержание
- [Технологии](#технологии)
- [Требования к окружению](#требования-к-окружению)
- [Запуск проекта](#запуск-проекта)
  - [Локальный запуск (без Docker)](#локальный-запуск-без-docker)
  - [Запуск через Docker Compose](#запуск-через-docker-compose)
- [Конфигурация](#конфигурация)
- [API Endpoints](#api-endpoints)
- [Тестирование](#тестирование)
- [Структура проекта](#структура-проекта)


---

## Технологии
- **Java 17**
- **Spring Boot 3.2.1** (Web, Data JPA, Security, Actuator)
- **PostgreSQL 15** – основная база данных
- **H2** – база данных для тестов
- **Maven** – сборка
- **Docker, Docker Compose** – контейнеризация
- **JWT (jjwt)** – аутентификация
- **Testcontainers** – интеграционные тесты
- **Mockito** – unit‑тесты
- **Swagger/OpenAPI** – документация API

---

## Требования к окружению
- **Java 17** (установлена и настроена переменная `JAVA_HOME`)
- **Maven** (или использование встроенного Maven Wrapper `./mvnw`)
- **Docker** и **Docker Compose** (для запуска через контейнеры)
- **Node.js** и **npm** (для запуска фронтенда, версия не ниже 19.7.0)

---

## 🚀 Запуск проекта
**
### 1. Клонирование репозитория**
```bash
git clone https://github.com/your-username/file-storage-backend.git
cd file-storage-backend

**### 2. Запуск через Docker** 
bash
docker-compose up --build
Будут запущены:
PostgreSQL (порт 5432)
Бэкенд (порт 8080)
После запуска бэкенд доступен по адресу http://localhost:8080, а база данных – на стандартном порту.
**

##Конфигурация**
**
Основные настройки находятся в файле src/main/resources/application.yml.
Переменные окружения, используемые в docker-compose:
Переменная                	        Описание	                                              Значение по умолчанию
SPRING_DATASOURCE_URL	JDBC    URL базы данных	                                   jdbc:postgresql://localhost:5432/file_storage_db
SPRING_DATASOURCE_USERNAME  	Пользователь                                        БД postgres
SPRING_DATASOURCE_PASSWORD    	Пароль                                              БД postgres
JWT_SECRET	                    Секретный ключ для подписи JWT                      super-secret-key (в production замените!)
STORAGE_LOCATION	            Папка для хранения загруженных файлов	           ./uploads


** ##API Endpoints**
 **
 
Метод	          URL	                  Описание	                    Тело запроса / параметры
POST	      /login	        Аутентификация пользователя	              {"login": "user", "password": "password"}
GET	          /file	            Получение списка файлов (с пагинацией)	  ?limit=3
POST	      /file/{filename}	Загрузка файла	                          multipart/form-data с полем file
DELETE	      /file/{filename}  Удаление файла	                            –
GET	          /file/{filename}	Скачивание файла	                          –

**## Тестирование**
**
**### Unit‑тесты (Mockito)**
bash
./mvnw test
**### Интеграционные тесты (Testcontainers)**
Интеграционные тесты используют Testcontainers и требуют работающего Docker.
При отсутствии Docker тесты автоматически пропускаются (skipped), сборка не ломается.
Для запуска в CI-среде (например, GitHub Actions) Docker доступен, и тесты выполнятся.

**Запуск всех тестов:**
bash
./mvnw verify

**## Структура проекта**
src
├── main
│   ├── java/ru/netology/filestorage
│   │   ├── config          – конфигурационные классы (CORS, инициализация данных)
│   │   ├── controller       – REST-контроллеры (AuthController, FileController)
│   │   ├── dto              – объекты для передачи данных
│   │   ├── entity           – сущности JPA (User, FileEntity)
│   │   ├── repository       – JPA-репозитории
│   │   ├── security         – JWT-фильтр, провайдер токена, конфигурация Security
│   │   └── service          – бизнес-логика (FileStorageService, AuthService)
│   │              
│   └── resources
│       └──  application.yml        – основная конфигурация
│      
└── test
    └── java/ru/netology/filestorage
        ├── integration                 – интеграционные тесты с Testcontainers
        └── service                     – unit‑тесты сервисов с Mockito
