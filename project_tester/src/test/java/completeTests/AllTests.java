package completeTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sun.print.resources.serviceui;

@RunWith(Suite.class)
@SuiteClasses({controller.AllTests.class, dao.AllTests.class, service.AllTests.class})
public class AllTests {

}
