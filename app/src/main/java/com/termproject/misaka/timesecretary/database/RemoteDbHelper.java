package com.termproject.misaka.timesecretary.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoteDbHelper {

    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://106.2.23.8/chuxu";
    private static String user = "mysql";
    private static String password = "mysql";

    private static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回单个结果值，如count\min\max等
     *
     * @param sql
     * @param args
     * @return object
     * @throws SQLException
     */
    public Object Select2Object(String sql, Object... args) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            closeAll(conn,ps,rs);
        }
    }

    /**
     * 执行insert update delete语句
     *
     * @param sql
     */
    public int ExecuteSQL(String sql, Object... args) {
        int result=0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            System.out.println(ps.toString());
            result=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn,ps,null);
        }
        return result;
    }
}