package dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountDaoTest.class, ProjectDaoTest.class, UploadDaoTest.class, HashtagDaoTest.class, CategoryDaoTest.class, FeedbackDaoTest.class, SpotDaoTest.class})
public class AllTests {}