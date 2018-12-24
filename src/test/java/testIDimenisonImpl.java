import com.qf.analytis.model.basedimension.PlatformDimension;
import com.qf.analytis.transfrom.service.impl.IDimenisonImpl;
import org.junit.Test;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:下午10:45
 * Vision:1.1
 * Description:
 */
public class testIDimenisonImpl {
    @Test
    public void test(){
            int windows = new IDimenisonImpl().getDimensionByObject(new PlatformDimension("ios"));
        System.out.println(windows);
    }
}
