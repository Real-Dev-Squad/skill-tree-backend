package com.RDS.skilltree;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

public class EndorsementsIntegrationTests {
    @Before
    public void setup(){
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Disabled
    public void testAPIReturnsAllEndorsements(){

    }

    @Test
    @Disabled
    public void testAPIReturnsEndorsementsGivenStatus(){

    }

    @Test
    @Disabled
    public void testAPIReturns400OnInvalidStatusPassed(){

    }

    @Test
    @Disabled
    public void testAPIReturns200OnEndorsementCreation(){

    }

    @Test
    @Disabled
    public void testAPIReturns204OnEndorsementsUpdation(){

    }

    @Test
    @Disabled
    public void testAPIReturns400OnInvalidParameterPassed(){

    }

    @Test
    @Disabled
    public void testAPIReturnsEndorsementGivenId(){

    }

    @Test
    @Disabled
    public void testAPIReturn400OnInvalidIdPassed(){

    }

    @Test
    @Disabled
    public void testAPIReturn404OnEndorsementNotFound(){

    }
}
