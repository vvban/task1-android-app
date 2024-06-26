# task1-android-app

Мобільний застосунок для відстеження погоди. Android 5.0 і вище (Lollipop API 21+).

https://github.com/vvban/task1-android-app/assets/48902183/ddae749a-25a4-4317-9d8c-baf622cadd6d

(Координати міст не точні, тому є похибка в кліматичних умовах) 

### Реалізація
1. MVVM архітектура.
    - data
    - data.api.weather
    - data.source
    - ui.cityDetail
    - ui.cityList
    - utils
2. Патерни
   - Adapter
   - Factory
   - Singleton
3. Використано ViewBinding, для Retrofit застосовано GsonConverterFactory.
4. Ввімкнене кешування запитів на 15 хвилин та кеш для картинок.
5. Бібліотеки 
   - [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
   - [Picasso](https://square.github.io/picasso/) - A powerful image downloading and caching library for Android.

### Недоліки рішення
1. В безплатній версії плану сервісу погоди не можливо здійснити bulk request, тому для кожного міста відправляється окремий запит.
2. Картинки погоди стягуються через CDN з автоматичним кешуванням від Picasso, ефективніше буде завантажувати їх із zip архіву.

### Корисні посилання
1. [API сервіс погоди weatherapi.com](https://www.weatherapi.com/api-explorer.aspx#current)
2. [Сайт для пошуку координат latitude.to](https://latitude.to/map/ua/ukraine/regions)

```
Задачка:
Зробити додаток на 2 екрани.

1. Список міст та попередній прогноз погоди (однією цифрою).
2. Деталізація по місту.

Екрани можна робити без дизайну.
Compose не використовувати.
Архітектурний та технологіничй стек не обмежений (але бажано MVVM).
```

```
Правки:

- Використати Kotlin Coroutines для запитів.
- Використовувати ViewBinding.
- Зробити завантаження даних через пагінацію.
- ListAdapter міняємо на RecyclerViewAdapter.
```
