import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Request {
    private final Statement statement;

    //Конструктор класса Request
    Request(Statement statement) {
        this.statement = statement;
    }

    //вывести информацию о преподавателях, работающих в заданный день в заданной аудитории
    public void workingTeacher(String day, String classroom) throws SQLException {
        //запрос, считывающий всю таблицу расписание
        String sql = "SELECT * FROM schedule";
        ResultSet res = this.statement.executeQuery(sql);
        //Массив идентификаторов преподавателей
        ArrayList<String> teachers = new ArrayList<>();
        /*Получаем из таблицы данные о днях и аудиториях и сравниваем их с заданными значениями
        Если true, записываем в массив преподавателей идентификатор преподавателя*/
        while (res.next()) {
            if (res.getString("Day").equals(day) && res.getString("classroom").equals(classroom)) {
                teachers.add(res.getString("Teacher"));
            }
        }
        //запрос, считывающий таблицу преподавателей
        String sql2 = "SELECT * FROM teachers";
        res = this.statement.executeQuery(sql2);
        /*Сравниваем идентификаторы в таблице с идентификаторами в массиве.
         *При совпадении выводим информацию о преподавателе (ФИО).*/
        while (res.next()) {
            for (String i : teachers)
                if (res.getString("ID").equals(i)) {
                    System.out.println("\n--------------------\n");
                    System.out.println("Фамилия: " + res.getString("Surname"));
                    System.out.println("Имя: " + res.getString("Name"));
                    System.out.println("Отчество: " + res.getString("Patronymic"));
                }
        }
    }

    //вывести информацию о преподавателях, которые НЕ ведут занятия в заданный день недели
    public void notWorkingTeacher(String day) throws SQLException {
        //SQL запрос, считывающий всю таблицу расписание
        String sql = "SELECT * FROM schedule";
        ResultSet res = this.statement.executeQuery(sql);
        //Массив идентификаторов преподавателей
        ArrayList<String> teachers = new ArrayList<>();
        /*Получаем из таблицы данные о днях и сравниваем с заданным значением
         * Если true, записываем в массив преподавателей идентификатор преподавателя*/
        while (res.next()) {
            if (!res.getString("Day").equals(day)) {
                teachers.add(res.getString("Teacher"));
            }
        }
        //SQL запрос, считывающий таблицу преподавателей
        String sql2 = "SELECT * FROM teachers";
        res = this.statement.executeQuery(sql2);
        /*Сравниваем идентификаторы в таблице с идентификаторами в массиве
         * При совпадении выводим информацию о преподавателе (ФИО).*/
        while (res.next()) {
            for (String i : teachers)
                if (res.getString("ID").equals(i)) {
                    System.out.println("\n--------------------\n");
                    System.out.println("Фамилия: " + res.getString("Surname"));
                    System.out.println("Имя: " + res.getString("Name"));
                    System.out.println("Отчество: " + res.getString("Patronymic"));
                }
        }
    }

    //вывести дни недели, в которые проводится данное кол-во занятий
    public void numOfLessons(int a) throws SQLException {
        //SQL запрос, считывающий всю таблицу расписание
        String sql = "SELECT * FROM schedule";
        ResultSet res = this.statement.executeQuery(sql);
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayList<String> arr = new ArrayList<>();
        int count = 0;
        //Добавляем в массив дни из таблицы расписание
        while (res.next()) {
            arr.add(res.getString("Day"));
        }
        /*Подсчитываем сколько раз в массиве встречается каждый день и сравниваем с заданным числом
         * Если true, выводим нужный день*/
        for (String day : days) {
            count = Collections.frequency(arr, day);
            if (count == a) {
                System.out.println(day);
            }
        }
    }

    //вывести дни недели, в которые занято заданное кол-во аудиторий
    public void numOfClassrooms(int a) throws SQLException {
        //SQL запрос, выбирающий день и считающий кол-во занятых аудиторий в этот день
        String sql = "SELECT day, COUNT(classroom) as count FROM schedule GROUP BY day";
        ResultSet res = this.statement.executeQuery(sql);
        //Если кол-во занятых аудиторий совпадает с заданным значением, выводим этот день
        while (res.next()) {
            if (res.getInt("count") == a) {
                System.out.println(res.getString("Day"));
            }
        }
    }

    //перенести первые занятия заданных дней на последнее место
    public void replaceTable(String day) throws SQLException {
        //SQL запрос, удаляющий из таблицы расписание строку с указанным днем и минимальным ID
        String sql = "DELETE FROM schedule WHERE ID IN (SELECT MIN(ID) FROM schedule WHERE Day = '" + day + "')";
        //SQL запрос, выбирающий из таблицы расписание строку с указанным днем и минимальным ID
        String sql1 = "SELECT * FROM schedule WHERE ID IN (SELECT MIN(ID) FROM schedule WHERE Day = '" + day + "')";
        ResultSet res = this.statement.executeQuery(sql1);
        ArrayList<String> arr = new ArrayList<>();
        //Записываем в массив всю строку
        if (res.first()) {
            arr.add(String.valueOf(res.getInt("Teacher")));
            arr.add(String.valueOf(res.getInt("Subject")));
            arr.add(res.getString("Day"));
            arr.add(String.valueOf(res.getInt("classroom")));
            arr.add(String.valueOf(res.getInt("Quantity")));
        }
        //Удаление строки
        this.statement.executeUpdate(sql);
        //Добавление новой строки с данными из массива
        String sql2 = "INSERT INTO schedule (Teacher, Subject, Day, classroom, Quantity) value ("
                + Integer.parseInt(arr.get(0)) + ","
                + Integer.parseInt(arr.get(1)) + ",'"
                + arr.get(2) + "',"
                + Integer.parseInt(arr.get(3)) + ","
                + Integer.parseInt(arr.get(4)) + ")";
        this.statement.executeUpdate(sql2);
    }
}