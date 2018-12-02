package edu.hm.cs.seng.hypershop;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypershopStreamHandlerTest {

    @Test
    public void isSkillIdValid() {
        assertEquals("amzn1.ask.skill.8c7a9786-c97e-4c48-a685-37a80ddd262e", Constants.SKILL_ID);
    }

    @Test
    public void isDynamoDbTableValid() {
        assertEquals("hypershopData", Constants.DYNAMO_TABLE_NAME);
    }
}