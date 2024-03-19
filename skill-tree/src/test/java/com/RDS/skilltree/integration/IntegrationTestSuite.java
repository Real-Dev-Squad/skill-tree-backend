package com.RDS.skilltree.integration;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(IntegrationTest.class)
@Suite.SuiteClasses({
    EndorsementsIntegrationTests.class,
    SkillsIntegrationTests.class,
    SecurityContextIntegrationTest.class
})
public class IntegrationTestSuite {}
