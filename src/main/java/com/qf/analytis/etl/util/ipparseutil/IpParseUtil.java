package com.qf.analytis.etl.util.ipparseutil;

import com.alibaba.fastjson.JSONObject;
import com.qf.analytis.bean.RegionInfo;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * 解析IP工具类
 * */
public class IpParseUtil{
    private static Logger logger = Logger.getLogger(IpParseUtil.class);
    public static RegionInfo regionInfo = new RegionInfo();
    /**
     * 淘宝解析IP
     * @param url
     * @return
     */
    public static RegionInfo ipTaoBaoParser2(String url, String charset){
        try {
            if(url == null || !url.startsWith("http")){
                throw new RuntimeException("url格式错误");
            }
            //代码走到这里
            //获取http的客户端对象
            HttpClient client = new HttpClient();
            //获取发送get请求的对象
            GetMethod getMethod = new GetMethod(url);
            //添加浏览器头信息
            if(null!=charset){
                getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
            }else {
                getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + "utf-8");
            }
            //执行get请求
            int statuscode = client.executeMethod(getMethod);
            if(statuscode != HttpStatus.SC_OK){

                System.out.println("failed message:"+getMethod.getStatusLine());
            }
            byte [] responseBody = getMethod.getResponseBodyAsString().getBytes(charset);
            //根据字节数组获取字符串
            String response = new String(responseBody,"utf-8");
            //返回json对象
            JSONObject jo = JSONObject.parseObject(response);
            JSONObject jo1= JSONObject.parseObject(jo.getString("data"));
            regionInfo.setCountry(jo1.getString("country"));
            regionInfo.setProvince(jo1.getString("region"));
            regionInfo.setCity(jo1.getString("city"));
        } catch (IOException e) {
            logger.error("解析IP异常",e);
        }
        return regionInfo;
    }
    /**
     * 纯真数据库解析ip地址的方法
     */
    public static RegionInfo ipParser(String ip){
        if(StringUtils.isNotEmpty(ip)){
            String country = IPSeeker.getInstance().getCountry(ip);
            if(StringUtils.isNotEmpty(country.trim())){
                if(country.equals("局域网")){
                    regionInfo.setCountry("中国");
                    regionInfo.setProvince("北京市");
                    regionInfo.setCity("昌平区");
                }else {
                    int index = country.indexOf("省");
                    if(index>0){
                        regionInfo.setCountry("中国");
                        regionInfo.setProvince(country.substring(0,index+1));
                        int  index2=country.indexOf("市");
                        if(index2>0){
                            regionInfo.setCity(country.substring(index+1,index2+1));
                        }
                    }else {
                        String flag = country.substring(0,2);
                        switch (flag){
                            case "内蒙":
                                regionInfo.setProvince("内蒙古");
                                country.substring(3);
                                index=country.indexOf("市");
                                if(index>0){
                                    regionInfo.setCity(country.substring(0,index+1));
                                }
                                break;
                            case "宁夏":
                            case "广西":
                            case "新疆":
                            case "西藏":
                                regionInfo.setProvince(flag+"省");
                                country.substring(2);
                                index=country.indexOf("市");
                                if(index>0){
                                    regionInfo.setCity(country.substring(0,index+1));
                                }
                                break;
                            case "北京":
                            case "天津":
                            case "上海":
                            case "重庆":
                                regionInfo.setProvince(flag + "市");
                                country.substring(2);
                                index = country.indexOf("区");
                                if (index > 0) {
                                    char ch = country.charAt(index - 1);
                                    if (ch != '小' || ch != '校' || ch != '军') {
                                        regionInfo.setCity(country.substring(0, index + 1));
                                    }
                                }

                                index = country.indexOf("县");
                                if (index > 0) {
                                    regionInfo.setCity(country.substring(0, index + 1));
                                }
                                break;
                            case "香港":
                            case "澳门":
                            case "台湾":
                                regionInfo.setProvince(flag + "特别行政区");
                                break;
                            default:
                                break;

                        }
                    }
                }
            }
        }
        return regionInfo;
    }


}