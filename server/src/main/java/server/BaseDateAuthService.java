package server;


import java.sql.*;

public class BaseDateAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psInsert;


    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            connect();//CRUD Create Read Update Delete
            System.out.println("Подключились к базе");

        ResultSet rs = stmt.executeQuery("SELECT login,password,nickname FROM users");
        while (rs.next()) {
            if (rs.getString("login").equals(login) &&  rs.getString("password").equals(password)){
                String nickAnswer = rs.getString("nickname");
                rs.close();
                return nickAnswer;
            }
        }
        rs.close();
        return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        try {
            connect();//CRUD Create Read Update Delete
            System.out.println("Подключились к базе");

        ResultSet rs = stmt.executeQuery("SELECT login,password,nickname FROM users");
        while (rs.next()) {
            if (rs.getString("login").equals(login) ){
                rs.close();
                return false;
            }
        }
        if (password.trim().equals("")) {
            return false;
        }
        rs.close();
        prepareAllStatement();
            connection.setAutoCommit(false);
            psInsert.setString(1, login);
            psInsert.setString(2, password);
            psInsert.setString(3, nickname);
        connection.setAutoCommit(true)

        return true;
    }
     catch (Exception e) {
        e.printStackTrace();
    } finally {
        disconnect();
    }
    }


      //Попытка проверить базу
    
//    private static void selectEx() throws SQLException {
//        ResultSet rs = stmt.executeQuery("SELECT login,password,nickname FROM users");
//        while (rs.next()) {
//            System.out.println(rs.getString("login") );
//        }
//        rs.close();
//    }

    public static void prepareAllStatement() throws SQLException {
        psInsert = connection.prepareStatement("INSERT INTO users (login, password, nickname) VALUES (?,?,?);");
    }



    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:mainUser.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
