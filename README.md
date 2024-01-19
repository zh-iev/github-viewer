![banner GitHub Viewer](app/src/main/res/drawable/banner.png)
# GitHub Viewer
## Android-приложение с подключением через GitHub для просмотра списка репозиториев, файлов и коммитов.

### Функциональные требования к приложению
- Авторизация через OAuth GitHub
- Получение своего списка репозиториев
- Просмотр списка файлов репозитория
- Просмотр файлов
- Просмотр коммитов репозитория на разных ветках

### Планируемые требования:
- Отправка уведомлений при добавленном коммите
- Просмотр чужих репозиториев
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