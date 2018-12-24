package com.qf.analytis.model.basedimension;

import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.apache.commons.lang.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午9:26
 * Vision:1.1
 * Description:浏览器维度
 */
public class BrowserDimension extends BaseDimension{
    private  int id ; //id
    private String browserName;//名称
    private String browserVersion; //版本

    public BrowserDimension() {
    }
    public BrowserDimension(String browserName, String browserVersion) {
        super();
        this.browserName = browserName;
        this.browserVersion = browserVersion;
    }
    public void clean() {
        this.id = 0;
        this.browserName = "";
        this.browserVersion = "";
    }
    //获得浏览器维度实例
    public static BrowserDimension newInstance(String browserName, String browserVersion) {
        BrowserDimension browserDimension = new BrowserDimension();
        browserDimension.browserName = browserName;
        browserDimension.browserVersion = browserVersion;
        return browserDimension;
    }

    /**
     * 构建多个浏览器维度信息对象集合
     *
     * @param browserName
     * @param browserVersion
     * @return
     */
    public static List<BrowserDimension> buildList(String browserName, String browserVersion) {
        List<BrowserDimension> list = new ArrayList<BrowserDimension>();
        if (StringUtils.isBlank(browserName)) {
            // 浏览器名称为空，那么设置为unknown
            browserName = PropertiesManagerUtil.getPropertyValue(CommonConstants.DEFAULT_IP_INFO);
            browserVersion = PropertiesManagerUtil.getPropertyValue(CommonConstants.DEFAULT_IP_INFO);

        }
        if (StringUtils.isEmpty(browserVersion)) {
            browserVersion = PropertiesManagerUtil.getPropertyValue(CommonConstants.DEFAULT_IP_INFO);

        }
        // list.add(BrowserDimension.newInstance(GlobalConstants.VALUE_OF_ALL,
        // GlobalConstants.VALUE_OF_ALL));
        list.add(BrowserDimension.newInstance(browserName, PropertiesManagerUtil.getPropertyValue(CommonConstants.ALL_OF_VALUE)));
        list.add(BrowserDimension.newInstance(browserName, browserVersion));
        return list;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public int getId() {
        return id;
    }

    public String getBrowserName() {
        return browserName;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }

        BrowserDimension other = (BrowserDimension) o;
        int tmp = Integer.compare(this.id, other.id);
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.browserName.compareTo(other.browserName);
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.browserVersion.compareTo(other.browserVersion);
        return tmp;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeUTF(this.browserName);
        out.writeUTF(this.browserVersion);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.browserName = in.readUTF();
        this.browserVersion = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrowserDimension that = (BrowserDimension) o;
        return id == that.id &&
                Objects.equals(browserName, that.browserName) &&
                Objects.equals(browserVersion, that.browserVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, browserName, browserVersion);
    }

    @Override
    public String toString() {
        return "BrowserDimension{" +
                "id=" + id +
                ", browserName='" + browserName + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                '}';
    }
}

