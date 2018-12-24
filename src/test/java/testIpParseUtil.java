import com.qf.analytis.bean.RegionInfo;
import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.etl.util.ipparseutil.IpParseUtil;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.junit.Test;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-18
 * Time:下午5:06
 * Vision:1.1
 * Description:
 */
public class testIpParseUtil {
    @Test
    public void IpParseUtiltest(){

        RegionInfo regionInfo = IpParseUtil.ipTaoBaoParser2(PropertiesManagerUtil.getPropertyValue(CommonConstants.TOBAO_REQUEST_IPPREX)+"102.113.115.114","utf-8");

    }
}
