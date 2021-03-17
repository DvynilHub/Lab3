import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*Класс, организующий подключение к БД*/
public class Connector {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private final String dbUrl;
    private final String user;
    private final String pass;

    public Connector(String dburl, String user, String pass) {
        this.dbUrl = dburl;
        this.user = user;
        this.pass = pass;
    }

    public Statement connection() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        Statement statement = null;
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(this.dbUrl, this.user, this.pass);
        statement = connection.createStatement();
        return statement;
    }

}
