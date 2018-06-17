package controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ProjectControllerTest.class, UploadControllerTest.class,
		AccountControllerTest.class, HashtagControllerTest.class,CategoryControllerTest.class, LikeControllerTest.class, DemoControllerTest.class})
public class AllTests {

}
