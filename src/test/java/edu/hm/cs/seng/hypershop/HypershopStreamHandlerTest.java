package edu.hm.cs.seng.hypershop;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypershopStreamHandlerTest {

    @Test
    public void isSkillIdValid() {
        assertEquals("amzn1.ask.skill.83f45138-33db-4af6-874d-c78af275fa85", Constants.SKILL_ID);
    }

    @Test
    public void isDynamoDbTableValid() {
        assertEquals("hypershopData", Constants.DYNAMO_TABLE_NAME);
    }
}