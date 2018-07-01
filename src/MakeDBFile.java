/**
 * Java. Level 2. Lesson 7
 * Making SQLite db file with users (Простая инструкция, как развернуть у себя возможность работы с SQLite)
 * Note:
 * a) Download latest ver of sqlite-jdbc-(VER).jar from (Скачать драйвер SQLite)
 *    https://bitbucket.org/xerial/sqlite-jdbc/downloads
 * b) Put this jar into [JDK]\jre\lib\ext
 * c) See also http://www.tutorialspoint.com/sqlite/sqlite_java.htm
 *
 * @author Sergey Iryupin
 * @version 0.3 dated Jan 16, 2018
 */
import java.sql.*;

public class MakeDBFile implements IConstants{
    final String NAME_TABLE = "users"; //имя таблицы

    /* Константа SQL_CREATE_TABLE содержит в себе команды для SQL-запросов:
     - DROP TABLE IF EXISTS - уничтожить таблицу, если она существует (с именем NAME_TABLE)
     - CREATE TABLE - создать таблицу с двумя полями по 6 символов: login CHAR(6) и passwd CHAR(6), поле login ключевое
    */
    final String SQL_CREATE_TABLE =
            "DROP TABLE IF EXISTS " + NAME_TABLE + ";" +
                    "CREATE TABLE " + NAME_TABLE +
                    "(login  CHAR(6) PRIMARY KEY NOT NULL," +
                    " passwd CHAR(6) NOT NULL);";

    /* Константа SQL_INSERT_MIKE содержит в себе команды для SQL-запросов:
     - INSERT INTO - вставить в (таблицу NAME_TABLE) поля (login, passwd)
     - VALUES - со значениями ('mike', 'qwe')
    */
    final String SQL_INSERT_MIKE =
            "INSERT INTO " + NAME_TABLE +
                    " (login, passwd) " +
                    "VALUES ('mike', 'qwe');";
    final String SQL_INSERT_JONH =
            "INSERT INTO " + NAME_TABLE +
                    " (login, passwd) " +
                    "VALUES ('john', 'rty');";

    final String SQL_SELECT = "SELECT * FROM " + NAME_TABLE + ";";// содержит команды "Выбрать записи из (БД)"

    public static void main(String[] args) {
        new MakeDBFile();
    }

    MakeDBFile() {
        try {
            // loads a class, including running its static initializers (Загружается класс драйвера из DRIVER_NAME = "org.sqlite.JDBC")
            Class.forName(DRIVER_NAME);
            // attempts to establish a connection to the given database URL (Соединяет с БД, если нет файла БД, то создаёт его)
            Connection connect = DriverManager.getConnection(SQLITE_DB);
            // сreates an object for sending SQL statements to the database (Обеспечивает соединение)
            Statement stmt = connect.createStatement();
            //Выполняем 4 SQL-запроса
            //1 - create table (создание SQL-таблицы)
            stmt.executeUpdate(SQL_CREATE_TABLE);

            //2,3 - insert record(s) (2 запроса для вставки записи в таблицу)
            stmt.executeUpdate(SQL_INSERT_MIKE);
            stmt.executeUpdate(SQL_INSERT_JONH);

            //4 - print records (печать записей)
            //Записи, выбранные из таблицы помещаются в объект ResultSet
            ResultSet rs = stmt.executeQuery(SQL_SELECT);
            System.out.println("LOGIN\tPASSWD");//Шапка таблицы
            //Вывод содержимого "rs" по названию колонок
            while (rs.next())
                System.out.println(
                        rs.getString("login") + "\t" +
                                rs.getString(PASSWD_COL));
            //объекты необходимо закрыть:
            rs.close();
            stmt.close();
            connect.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
