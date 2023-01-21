package connectingBase;

import java.sql.*;

public class Connecting {
    public static Connection getConnect() throws SQLException {
        Connection conn = null;
        try {
           //Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mp3","postgres", "root");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void freeConnection(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void freeStatement(Statement stmt) {
        if(stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void freeResultSet(ResultSet result) {
        if(result == null) {
            return;
        }
        try {
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void returnRollback(Connection con) {
        if (con == null) {
            return;
        }
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
