package com.qf.analytis.utils;

import com.qf.analytis.common.CommonConstants;

import java.sql.*;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:下午8:14
 * Vision:1.1
 * Description:获取jdbc的连接
 */
public class JdbcUtils {
    //静态加载驱动
    static {
        try {
            Class.forName(PropertiesManagerUtil.getPropertyValue(CommonConstants.JDBC_DRIVER));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(PropertiesManagerUtil.getPropertyValue(CommonConstants.MYSQL_URL),PropertiesManagerUtil.getPropertyValue(CommonConstants.MYSQL_USER),PropertiesManagerUtil.getPropertyValue(CommonConstants.MYSQL_PWD));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭连接
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                //do nothing
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                //do nothing
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                //do nothing
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(JdbcUtils.getConn());

    }
}
