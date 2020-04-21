package com.datasw.flume;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author jinsen
 * @create 2020-04-02 15:02
 */
public class LogUtil {

    public static boolean validateStart(String log){

        return false;
    }
    public static ArrayList<String> selectDb() {
        Connection c = null;
        Statement stmt = null;
        ArrayList<String> arrayList = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(
                    "jdbc:postgresql://172.18.2.18:5832/zxvmax", "postgres",
                    "U_tywg_2013");
            c.setAutoCommit(false);
            System.out.println("连接数据库成功！");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select inner_name from t_module_info WHERE status = 1");
            arrayList = new ArrayList<String>();
            while(rs.next()){
                String name = rs.getString("inner_name");
                System.out.println(name);
                arrayList.add(name);
            }
            System.out.println(arrayList.toString());
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("查询数据成功！");
        return arrayList;
    }
}
