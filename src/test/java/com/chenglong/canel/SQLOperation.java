package com.chenglong.canel;
import java.sql.*;
public class SQLOperation {
    public static void main(String[] args) {
        String sql="update example.user set  id = '6', orderId = '6', username = '哈哈', password = '6', age = '6' where id=5";
        try {
            operateSQL(sql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void operateSQL(String sql) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");//加载驱动

        String jdbc="jdbc:mysql://127.0.0.1:3306/example?characterEncoding=UTF-8";
        Connection conn=DriverManager.getConnection(jdbc, "root", "123456");//链接到数据库

        Statement state=conn.createStatement();   //容器
        state.executeUpdate(sql);         //将sql语句上传至数据库执行

        conn.close();//关闭通道
    }
}
