![banner GitHub Viewer](app/src/main/res/drawable/banner.png)
# GitHub Viewer
## Android-приложение с подключением через GitHub для просмотра списка репозиториев, файлов и коммитов.

### Функциональные требования к приложению
- Авторизация через OAuth GitHub
- Получение своего списка репозиториев
- Создание нового репозитория
- Поиск по репозиториям и по пользователям
- Список Задач (Issues) и их просмотр

### Планируемые требования:
- Отправка push-уведомлений при добавленном коммите
- Просмотр файлов репозитория, коммитов и его состояния на разных ветках
- Командный чат репозитория через Firebase?


#### Оформление файла Constants.js со своими ключами
```kotlin
object Constants {
const val API_URL = "https://api.github.com/"
const val GITHUB_URL = "https://github.com/"

// ключи необходимо вставить из вашего GitHubApp
// Иконка профиля в GH -> Settings -> Developer Settings -> OAuth Apps
const val CLIENT_ID = ""
const val CLIENT_SECRET = ""
}
```
