import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Umar on 07-Mar-16.
 */
public class LocalSpiderTest extends TestCase {

        LocalSpider c= new LocalSpider();
    public void testLoad() throws Exception {

        c.startcrawling("F:\\Movies");
        assertNotNull(c.l);
        c.startindexing();
        assertNotNull(c.index.get("Movie4k.to"));
    }

}