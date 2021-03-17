import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*Главный класс программы. Создаются экземпляры классов Connector, Statement, Request.
 * Пользователь выбирает какое из 5 заданий выполнить.*/
public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String DB_URL = "jdbc:mysql://localhost/journal";
        String DB_USER ="root";
        String DB_PASS ="root";
        Connector connector = new Connector(DB_URL, DB_USER, DB_PASS);
        Statement statement = connector.connection();
        Request request = new Request(statement);
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите номер задания(1-5): ");
        int a = scan.nextInt();
        if (a == 1){
            String day = "Monday";
            String room = "107";
            System.err.println("Преподаватели, работающие в "+day+" в аудитории "+room);
            request.workingTeacher(day, room);
        }
        else if (a == 2){
            String day = "Sunday";
            System.err.println("Преподаватели, которе НЕ работают в "+day);
            request.notWorkingTeacher(day);
        }
        else if (a == 3) {
            int n = 2;
            System.err.println("Дни недели, когда есть "+n+" занятий");
            request.numOfLessons(n);
        }
        else if (a == 4) {
            int n = 2;
            System.err.println("Дни недели, когда занято "+n+" аудиторий");
            request.numOfClassrooms(n);
        }
        else if (a == 5) {
            String day= "Monday";
            System.err.println("Пренести первые занятия " +day+ " на последнее место");
            request.replaceTable(day);
        }
        else {
            System.err.println("Неверное число");
        }
    }
}