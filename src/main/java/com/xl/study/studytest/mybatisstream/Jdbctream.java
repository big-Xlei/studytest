package com.xl.study.studytest.mybatisstream;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 在test中有mybatis流进行整合查询
 * 还没找到插入的相关信息
 */

public class Jdbctream {
    private String url ="jdbc:mysql://localhost:10089/dor_manage?characterEncoding=utf-8&rewriteBatchedStatements=true";
    private String user ="root";
    private String password ="password";
    @Test
    public void jdbcInsert(){
        Connection conn =null;
        PreparedStatement pstm =null;
        ResultSet rt =null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url,user,password);
            String sql =" insert into permission_info(user_id,table_names,columnss) VALUES(?,?,?) ";
            pstm = (PreparedStatement) conn.prepareStatement(sql);
            long start = System.currentTimeMillis();
            for (int i =0; i <200000; i++) {
                pstm.setInt(1,i);
                pstm.setString(2,"测试");
                pstm.setString(3,"测试"+i);
                pstm.addBatch();
            }
            pstm.executeBatch();
            long end = System.currentTimeMillis();
            System.out.println("用时："+(end-start));
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn!=null){
                try {
                    conn.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstm!=null){
                try {
                    pstm.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
