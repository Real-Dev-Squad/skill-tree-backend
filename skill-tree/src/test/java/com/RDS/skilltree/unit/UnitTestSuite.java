package com.RDS.skilltree.unit;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Suite.SuiteClasses({
    EndorsementServiceTest.class,
    EndorsementListServiceTest.class,
    HealthCheckTest.class,
    SkillsServiceTest.class
})
public class UnitTestSuite {}
