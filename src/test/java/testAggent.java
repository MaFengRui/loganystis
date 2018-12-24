import com.qf.analytis.bean.AgentInfo;
import com.qf.analytis.etl.util.agentparseutil.UserAgentUtil;
import org.junit.Test;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-18
 * Time:下午4:43
 * Vision:1.1
 * Description:
 */
public class testAggent {
@Test
    public void AggentTest(){
    AgentInfo agentInfo = UserAgentUtil.parserUserAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36");
    System.out.printf(agentInfo.toString());
}


}
