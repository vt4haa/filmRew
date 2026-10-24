# Каталог фильмов — курсовой проект Spring Boot

Учебное веб-приложение, разработанное по методическим указаниям «Курсовое проектирование SpringBoot».
Вариант предметной области: **«Список фильмов (Movies) + список актёров и кассовые сборы»** (вариант 9).

Приложение позволяет вести каталог фильмов и актёров, хранить характеристики фильмов,
рассчитывать прибыль и рентабельность, разграничивать доступ по ролям и журналировать действия
пользователей в базе данных и файле.

## Стек технологий

| Технология | Версия / назначение |
|---|---|
| Java | 17 (сборка), проверено на JDK 25 |
| Spring Boot | 4.1.2 |
| Spring MVC + Thymeleaf | серверный рендеринг HTML-форм |
| Spring Security | аутентификация, авторизация, BCrypt, CSRF |
| Spring Data JPA + Hibernate | доступ к данным |
| H2 Database | файловая БД `./db/courseproject.mv.db` |
| Lombok | сокращение шаблонного кода сущностей |
| Bean Validation | проверка форм |
| Spring Boot Actuator | мониторинг приложения |
| Spring Boot | приложение на Spring Boot |

## Быстрый запуск

Требуется установленная JDK 17 или новее. Maven устанавливать не нужно — используется Maven Wrapper.

```powershell
cd проект
.\mvnw.cmd spring-boot:run
```

Либо сборка и запуск готового jar:

```powershell
.\mvnw.cmd clean package
java -jar target\course-project-1.0.0.jar
```

Приложение доступно по адресу: **http://localhost:8080**

При первом запуске база автоматически создаётся и заполняется демонстрационными данными.

### Демонстрационные учётные записи

| Логин | Пароль | Роль | Возможности |
|---|---|---|---|
| `admin` | `admin123` | ADMIN | полный доступ, управление ролями, журнал, Actuator |
| `user` | `user123` | USER | только собственные фильмы и актёры |
| `guest` | `guest123` | READ_ONLY | только просмотр |

Новые пользователи, зарегистрированные через форму, получают роль **READ_ONLY**.

## Реализованные требования

- [x] форма входа и регистрации;
- [x] форма списка для двух таблиц: фильмы и актёры;
- [x] формы добавления и редактирования записей;
- [x] удаление записей с подтверждением;
- [x] главная страница;
- [x] страница «О приложении» со сведениями о разработчике;
- [x] калькулятор прибыли и рентабельности фильма;
- [x] форма управления ролями (только ADMIN);
- [x] просмотр журнала действий (только ADMIN);
- [x] роли READ_ONLY, USER, ADMIN и разграничение доступа;
- [x] USER видит только созданные им записи, ADMIN — все;
- [x] запись действий в таблицу `user_actions`;
- [x] Spring Boot Actuator;
- [x] H2 Console для просмотра структуры БД.

## Структура базы данных

| Таблица | Назначение | Основные колонки |
|---|---|---|
| `users` | пользователи | `id`, `username`, `password`, `full_name`, `role`, `created_at` |
| `movies` | основная таблица варианта | `id`, `title`, `genre`, `release_year`, `budget`, `box_office`, `owner_id` |
| `movie_details` | характеристики фильма | `id`, `duration_minutes`, `rating`, `studio`, `synopsis`, `movie_id` |
| `actors` | вторая таблица варианта | `id`, `full_name`, `country`, `fee`, `movie_id`, `owner_id` |


Связи:

- `movies.owner_id → users.id` — владелец записи (many-to-one);
- `movie_details.movie_id → movies.id` — один-к-одному;
- `actors.movie_id → movies.id` — актёры фильма (many-to-one);
- `actors.owner_id → users.id` — владелец записи.

## Разграничение прав

| Действие | READ_ONLY | USER | ADMIN |
|---|---|---|---|
| Просмотр доступных записей | да | да (только свои) | да (все) |
| Создание / изменение / удаление | нет | да (только свои) | да (все) |
| Управление ролями | нет | нет | да |
| Журнал действий | нет | нет | да |
| Actuator (кроме health/info) | нет | нет | да |

Ограничения реализованы на трёх уровнях: скрытие элементов интерфейса, правила `SecurityFilterChain`
и аннотации `@PreAuthorize` в сервисах и контроллерах.

## Основные URL

| URL | Доступ | Назначение |
|---|---|---|
| `/` | всем | главная страница |
| `/login`, `/register` | всем | вход и регистрация |
| `/movies` | авторизованным | список фильмов |
| `/actors` | авторизованным | список актёров |
| `/calculator` | авторизованным | расчёт прибыли |
| `/about` | всем | о приложении |

| `/users` | ADMIN | роли пользователей |
| `/h2-console` | ADMIN | консоль H2 |
| `/actuator` | ADMIN | Actuator (health/info — без авторизации) |

## Структура проекта

```text
src/main/java/ru/urfu/courseproject
├── config
│   ├── DataInitializer.java       # демонстрационные данные
│   └── WebSecurityConfig.java     # Spring Security, роли, CSRF
├── controller
│   ├── AboutController.java
│   ├── ActorController.java
│   ├── AuthController.java
│   ├── CalculationController.java
│   ├── GlobalModelAttributes.java
│   ├── HomeController.java
│   ├── LogController.java
│   ├── MovieController.java
│   └── UserController.java
├── dto                          # DTO форм и результатов расчёта
├── entity                       # User, Movie, MovieDetail, Actor, UserAction, Role
├── repository                   # Spring Data JPA репозитории
├── service                      # бизнес-логика и проверка прав
└── CourseProjectApplication.java

src/main/resources
├── static/css/style.css
├── templates                    # Thymeleaf-страницы и фрагменты
├── application.properties
└── (логирование отключено)
```

## Журналирование

Каждое значимое действие (вход, выход, регистрация, CRUD, расчёт, смена роли)
записывается:

1. в таблицу `user_actions` (дата, логин, описание) — просмотр на странице `/logs`;
2. в файл `logs/app.log` (с ежедневной ротацией, хранение 30 дней).

## Actuator

Доступны эндпоинты:

- `/actuator/health` — состояние приложения и БД;
- `/actuator/info` — сведения о приложении;
- `/actuator/metrics` — метрики;
- `/actuator/mappings` — карта маршрутов;
- `/actuator/loggers` — управление уровнями логирования.

`health` и `info` доступны без авторизации, остальные — только администратору.

## Тесты и сборка

```powershell
.\mvnw.cmd test      # контекстный тест приложения
.\mvnw.cmd package   # сборка исполняемого jar
```

## Сброс базы данных

Остановить приложение, удалить каталоги `db/` и `logs/`, запустить снова — схема и
демонстрационные данные будут созданы заново.
