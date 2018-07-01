/*
Интрерфейс констант
 */

public interface IConstants {
    final String DRIVER_NAME = "org.sqlite.JDBC"; //Имя класса драйвера базы данных SQLite
    final String SQLITE_DB = "jdbc:sqlite:chat.db"; //Путь до базы данных (в данном случае, файл прямо в папке проекта)
    final String SERVER_ADDR = "localhost"; // server net name or "127.0.0.1"
    final int SERVER_PORT = 2048; // servet port - адрес порта
    final String SERVER_START = "Server is started..."; //Сервер запущен
    final String SERVER_STOP = "Server stopped."; //Сервер остановлен
    final String CLIENT_JOINED = " client joined."; //-й клиент присоединился
    final String CLIENT_DISCONNECTED = " disconnected."; //отключился
    final String CLIENT_PROMPT = "> "; // знак-подсказка клиенту печатать в этой строке
    final String LOGIN_PROMPT = "Login: "; //подсказка ввести логин
    final String PASSWD_PROMPT = "Passwd: "; //подсказка ввести пароль
    final String AUTH_SIGN = "auth"; //для формирования строки аутентификации
    final String AUTH_FAIL = "Authentication failure."; //отказ в аутентификации
    final String SQL_SELECT = "SELECT * FROM users WHERE login=?"; //для метода проверки авторизации сервера
    final String PASSWD_COL = "passwd"; //имя колонки с паролем в объекте ResultSet
    final String CONNECT_TO_SERVER = "Connection to server established.";//Соединение с сервером установлено
    final String CONNECT_CLOSED = "Connection closed."; //Соединение закрыто
    final String EXIT_COMMAND = "exit"; // command for exit - команда для выхода клиента
}
