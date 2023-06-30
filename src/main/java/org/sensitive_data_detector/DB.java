package org.sensitive_data_detector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private String ip;
    private String username;
    private String password;
    private String db_name;
    private Connection db;
    private Statement statement;
    public DB(String ip, String username, String password, String db_name) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.db_name = db_name;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + "/" + db_name;
            this.db = DriverManager.getConnection(url, username, password);
            this.statement = db.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 通过schemata获取所有数据库名称
    public List<String> getDatabase() {
        List<String> result = new ArrayList<>();
        try {
            String query = "SELECT schema_name from information_schema.schemata";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (!databaseName.equals("information_schema") && !databaseName.equals("mysql") && !databaseName.equals("performance_schema") && !databaseName.equals("sys") && !databaseName.equals("loonflownew")) {
                    result.add(databaseName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取表名
    public List<String> getTable(String database) {
        List<String> result = new ArrayList<>();
        try {
            String query = "select table_name from information_schema.tables where table_schema= '" + database + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                result.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取字段名
    public List<String> getColumn(String database, String table) {
        List<String> result = new ArrayList<>();
        try {
            String query = "select column_name from information_schema.columns where table_schema='" + database + "' and table_name='" + table + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String columnName = resultSet.getString(1);
//                System.out.println(columnName);
                result.add(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取字段内容
    public List<String> getContent(String database, String table, String column) {
        List<String> result = new ArrayList<>();
        try {
            String query = "select " + column + " from " + database + "." + table;
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String content = resultSet.getString(1);
                result.add(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取所有行数据
    public List<String> getLine(String database, String table) {
        List<String> result = new ArrayList<>();
        try {
            String query = "select * from " + database + "." + table;
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    row.append(value).append(" ");
                }
                result.add(row.toString().trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}