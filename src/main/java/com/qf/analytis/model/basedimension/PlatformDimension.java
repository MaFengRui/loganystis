package com.qf.analytis.model.basedimension;

import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.apache.commons.lang.StringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午8:00
 * Vision:1.1
 * Description:平台维度类
 */
public class PlatformDimension extends BaseDimension{
    private int id;
    private String platformName;

    public PlatformDimension() {
        super();
    }

    public PlatformDimension(String platformName) {
        super();
        this.platformName = platformName;
    }

    public PlatformDimension(int id, String platformName) {
        super();
        this.id = id;
        this.platformName = platformName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public int getId() {
        return id;
    }

    public String getPlatformName() {
        return platformName;
    }


    @Override
    public int compareTo(BaseDimension o) {
        if (this == o){
            return 0;
        }
        PlatformDimension other = (PlatformDimension) o ;
        int tmp = Integer.compare(this.id, other.id);
        if(tmp != 0){
            return tmp;
        }
        tmp = this.platformName.compareTo(other.platformName);
        return tmp;

    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.id);
        dataOutput.writeUTF(this.platformName);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readInt();
        this.platformName = dataInput.readUTF();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformDimension that = (PlatformDimension) o;
        return id == that.id &&
                Objects.equals(platformName, that.platformName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, platformName);
    }
    public static PlatformDimension getInstance(String platformName){
        if(StringUtils.isEmpty(platformName)){
            platformName = PropertiesManagerUtil.getPropertyValue(CommonConstants.DEFAULT_IP_INFO);
        }
        return new PlatformDimension(platformName);
    }
}
