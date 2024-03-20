package com.RDS.skilltree.integration;

import com.RDS.skilltree.unit.SkillsServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    EndorsementsIntegrationTests.class,
    SecurityContextIntegrationTest.class,
    SkillsServiceTest.class
})
public class IntegrationTestSuite {}
