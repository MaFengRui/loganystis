package com.qf.analytis.transfrom.service.impl;

import com.qf.analytis.model.basedimension.*;
import com.qf.analytis.transfrom.service.IDimension;
import com.qf.analytis.utils.JdbcUtils;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:下午5:04
 * Vision:1.1
 * Description:获取基础维度Id的实现
 */
public class IDimenisonImpl implements IDimension {
    //需要定义一个缓存，来存放已经获取到的维度－维度id
    private Map<String,Integer> cache = new LinkedHashMap<String, Integer>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return this.size()>5000;
        }
    };
    //1根据维度对象里面的属性，赋值给对应的sql,然后查询，如果有则返回对应的维度id
    //如果没有，则先添加到数据库然后返回新增对的id

    @Override
    public int getDimensionByObject(BaseDimension baseDimension) {

        Connection conn = null;

        //构造cachekey
        String cachekey = buildCachekey(baseDimension);
        //查看缓存中是否存在
        try {

            if (this.cache.containsKey(cachekey)) {
                return this.cache.get(cachekey);
            }
            //代码走到这说明缓存已经存在
            String sqls[] = null;
            if (baseDimension instanceof KpiDimension) {
                sqls = buildKpiSqls(baseDimension);
            } else if (baseDimension instanceof PlatformDimension) {
                sqls = buildPlatformSqls(baseDimension);
            } else if (baseDimension instanceof DateDimension) {
                sqls = buildDateSqls(baseDimension);
            } else if (baseDimension instanceof BrowserDimension) {
                sqls = buildBrowserSqls(baseDimension);
//            } else if(dimension instanceof LocationDimension){
//                sqls = buildLocalSqls(dimension);
//            } else if(dimension instanceof EventDimension){
//                sqls = buildEventSqls(dimension);
            }
            //
            conn = JdbcUtils.getConn();
            int id = -1;
            synchronized (this) {
                id = this.executSql(conn, sqls, baseDimension);
            }
            //将结果存储在cache中
            this.cache.put(cachekey, id);
            return id;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JdbcUtils.close(conn,null,null);
        }
        throw  new RuntimeException("插入基础表错误");


    }


    private int executSql(Connection conn, String[] sqls, BaseDimension baseDimension) {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            //获取查询的sql
            String selectSql = sqls[1];
            preparedStatement =  conn.prepareStatement(selectSql);
            //为查询语句赋值
            this.setArgs(baseDimension,preparedStatement);
             rs = preparedStatement.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }
            //如果没有查出来
            preparedStatement = conn.prepareStatement(sqls[0],Statement.RETURN_GENERATED_KEYS);
            this.setArgs(baseDimension,preparedStatement);
            preparedStatement.executeUpdate();
             rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                return  rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(null,preparedStatement,rs);
        }
        return -1;
    }

    private void setArgs(BaseDimension dimension, PreparedStatement ps) {
        try {
            int i = 0;
            if(dimension instanceof KpiDimension){
                KpiDimension kpi = (KpiDimension)dimension;
                ps.setString(++i,kpi.getKpiName());
//                System.out.println(i);
            } else if(dimension instanceof DateDimension){
                DateDimension date = (DateDimension)dimension;
                ps.setInt(++i,date.getYear());
                ps.setInt(++i,date.getSeason());
                ps.setInt(++i,date.getMonth());
                ps.setInt(++i,date.getWeek());
                ps.setInt(++i,date.getDay());
                ps.setString(++i,date.getType());
                ps.setDate(++i,new Date(date.getCalendar().getTime()));
            }  else if(dimension instanceof PlatformDimension){
                PlatformDimension platform = (PlatformDimension)dimension;
                ps.setString(++i,platform.getPlatformName());
            }  else if(dimension instanceof BrowserDimension){
                BrowserDimension browser = (BrowserDimension)dimension;
                ps.setString(++i,browser.getBrowserName());
                ps.setString(++i,browser.getBrowserVersion());
//            } else if(dimension instanceof LocationDimension){
//                LocationDimension local = (LocationDimension) dimension;
//                ps.setString(++i,local.getCountry());
//                ps.setString(++i,local.getProvince());
//                ps.setString(++i,local.getCity());
//            } else if(dimension instanceof EventDimension){
//                EventDimension event = (EventDimension) dimension;
//                ps.setString(++i,event.getCategory());
//                ps.setString(++i,event.getAction());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 构建kpi的插入和查询sql语句
     * @param baseDimension
     * @return
     */

    private String[] buildKpiSqls(BaseDimension baseDimension) {
        String insertSql = "INSERT INTO`dimension_kpi`(kpi_name)VALUES(?)";
        String selectSql="SELECT id FROM `dimension_kpi` WHERE kpi_name = ?";
        return new String[]{insertSql,selectSql};
    }
    private String[] buildEventSqls(BaseDimension dimension) {
        String query = "select id from `dimension_event` where `category` = ? and `action` = ? ";
        String insert = "insert into `dimension_event`(`category` , `action` ) values(?,?)";
        return new String[]{insert,query};
    }
    private String[] buildLocalSqls(BaseDimension dimension) {
        String query = "select id from `dimension_location` where `country` = ? and `province` = ? and `city` = ? ";
        String insert = "insert into `dimension_location`(`country` , `province` , `city`) values(?,?,?)";
        return new String[]{insert,query};
    }
    private String[] buildBrowserSqls(BaseDimension dimension) {
        String insertSql = "INSERT INTO `dimension_browser`(`browser_name`, `browser_version`) VALUES(?,?)";
        String selectSql = "SELECT `id` FROM `dimension_browser` WHERE `browser_name` = ? AND `browser_version` = ?";
        return new String[]{insertSql,selectSql};
    }
    private String[] buildDateSqls(BaseDimension dimension) {
        String insertSql = "INSERT INTO `dimension_date`(`year`, `season`, `month`, `week`, `day`, `type`, `calendar`) VALUES(?, ?, ?, ?, ?, ?, ?)";
        String selectSql = "SELECT `id` FROM `dimension_date` WHERE `year` = ? AND `season` = ? AND `month` = ? AND `week` = ? AND `day` = ? AND `type` = ? AND `calendar` = ?";
        return new String[]{insertSql,selectSql};
    }
    private String[] buildPlatformSqls(BaseDimension dimension) {
        String insertSql = "insert into `dimension_platform`(platform_name) values(?)";
        String selectSql = "select id from `dimension_platform` where platform_name = ?";
        return new String[]{insertSql,selectSql};
    }



    private String buildCachekey(BaseDimension baseDimension) {
        StringBuffer sb = new StringBuffer();

        if (baseDimension instanceof BrowserDimension) {
            sb.append("browser_");
            BrowserDimension browswer = (BrowserDimension) baseDimension;
            sb.append(browswer.getBrowserName());
            sb.append(browswer.getBrowserVersion());
        } else if (baseDimension instanceof KpiDimension) {
            sb.append("kpi_");
            KpiDimension kpiDimension = (KpiDimension) baseDimension;
            sb.append(kpiDimension.getKpiName());
        } else if (baseDimension instanceof DateDimension) {
            sb.append("date_");
            DateDimension date = (DateDimension) baseDimension;
            sb.append(date.getYear());
            sb.append(date.getSeason());
            sb.append(date.getMonth());
            sb.append(date.getWeek());
            sb.append(date.getDay());
            sb.append(date.getType());
        } else if (baseDimension instanceof PlatformDimension) {
            sb.append("platform_");
            PlatformDimension platform = (PlatformDimension) baseDimension;
            sb.append(platform.getPlatformName());
        }
//        else if(baseDimension instanceof LocationDimension){
//            LocationDimension local = (LocationDimension) dimension;
//            sb.append("local_");
//            sb.append(local.getCountry());
//            sb.append(local.getProvince());
//            sb.append(local.getCity());
//        } else if(dimension instanceof EventDimension){
//            EventDimension event = (EventDimension) dimension;
//            sb.append("event_");
//            sb.append(event.getCategory());
//            sb.append(event.getAction());
//        }?
        return sb != null ? sb.toString() : null;
    }

}
