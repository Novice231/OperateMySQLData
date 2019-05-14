package net.hunau.DBLink;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import net.hunau.entity.User;

public class DBLink {
    final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    public Connection openConnection(String url, String user, String password) {
        Connection conn = null;
        try {
            Class.forName(DRIVER_NAME);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            conn = null;
        } catch (SQLException e) {
            conn = null;
        }
        System.out.println(conn);
        return conn;
    }

    public List<User> query(Connection conn, String sql) {
        if (conn == null) {
            return null;
        }
        Statement statement = null;
        ResultSet result = null;
        List<User> list = new ArrayList<>();
        try {
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            System.out.println(result);
            if (result != null && result.first()) {
                int idColumnIndex = result.findColumn("id");
                int nameColumnIndex = result.findColumn("name");
                int sexyColumnIndex = result.findColumn("sexy");
                int pwdColumnIndex = result.findColumn("pwd");
                int isUsedColumnIndex = result.findColumn("isused");
                while (!result.isAfterLast()) {
                    User user = new User();
                    user.setSexy(result.getString(sexyColumnIndex));
                    user.setName(result.getString(nameColumnIndex));
                    user.setId(result.getInt(idColumnIndex));
                    user.setIsused(result.getInt(isUsedColumnIndex)==1?true:false);
                    user.setPwd(result.getString(pwdColumnIndex));
                    list.add(user);
                    result.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                    result = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        return list;
    }

    public int execSQL(Connection conn, String sql) {
        int execResult = -1;
        if (conn == null) {
            return execResult;
        }
        Statement statement = null;
        try {
            statement = conn.createStatement();
            if (statement != null) {
                execResult = statement.executeUpdate(sql);   //execute(sql);
            }
        } catch (SQLException e) {
            execResult = -1;
        }
        return execResult;
    }
}
