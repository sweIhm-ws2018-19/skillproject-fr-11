package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Test;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.LIST_INGREDIENTS;
import static org.junit.Assert.*;

public class SsmlServiceTest {

    @Test
    public void service_Empty_ReturnEmptyString() {
        SsmlService sut = new SsmlService();

        assertEquals("", sut.getSpeechString());
        assertEquals("", sut.getCardString());
    }
    @Test
    public void append_WithDifferentTypes_ReturnCorrectString() {

        final SsmlService sut = new SsmlService().append(5).append(4.0).append(true).append("string");
        final String expected = "54.0truestring";

        assertEquals(expected, sut.getCardString());
        assertEquals(expected, sut.getSpeechString());
    }
    @Test
    public void paragraph_WithBeginEnd_ReturnCorrectString() {

        final String expectedCard = "hallo";
        final String expectedSpeech = "<p>hallo</p>";
        SsmlService sut = new SsmlService().beginParagraph().append(expectedCard).endParagraph();

        assertEquals(expectedCard, sut.getCardString());
        assertEquals(expectedSpeech, sut.getSpeechString());
    }
    @Test
    public void sentence_WithBeginEnd_ReturnCorrectString() {

        final String expectedCard = "hallo";
        final String expectedSpeech = "<s>hallo</s>";
        SsmlService sut = new SsmlService().beginSentence().append(expectedCard).endSentence();

        assertEquals(expectedCard, sut.getCardString());
        assertEquals(expectedSpeech, sut.getSpeechString());
    }
    @Test
    public void inEnglish_WithString_ReturnCorrectString() {

        final String expectedCard = "hello";
        final String expectedSpeech = "<lang xml:lang=\"en-US\">" + expectedCard + "</lang>";
        SsmlService sut = new SsmlService().inEnglish(expectedCard);

        assertEquals(expectedCard, sut.getCardString());
        assertEquals(expectedSpeech, sut.getSpeechString());
    }
    @Test
    public void sayAsNumber_WithString_ReturnCorrectString() {

        final String expectedCard = "5";
        final String expectedSpeech = "<say-as interpret-as=\"number\">" + expectedCard + "</say-as>";
        SsmlService sut = new SsmlService().sayAs(expectedCard, SsmlService.SayAsType.number);

        assertEquals(expectedCard, sut.getCardString());
        assertEquals(expectedSpeech, sut.getSpeechString());
    }
    @Test
    public void enumerate_WithIngredientAmounts_ReturnCorrectString() {
        ShoppingList sl = new ShoppingList();
        sl.addIngredient(new IngredientAmount(150, "ml", "wasser"));
        sl.addIngredient(new IngredientAmount(50, "g", "tomaten"));
        SsmlService sut = new SsmlService().enumerate(sl.getIngredients());
        String actualCard = sut.getCardString();

        final String expected1 = "150 ml wasser, 50 g tomaten";
        final String expected2 = "50 g tomaten, 150 ml wasser";
        assertEquals(actualCard, sut.getSpeechString());
        if(actualCard.startsWith("150")) {
            assertEquals(expected1, actualCard);
        }
        else {
            assertEquals(expected2, actualCard);
        }
    }
    @Test
    public void enumerate_WithHeaderAndIngredients_ReturnCorrectString() {
        final String header = String.format(LIST_INGREDIENTS, 2);
        ShoppingList sl = new ShoppingList();
        sl.addIngredient(new IngredientAmount(150, "ml", "wasser"));
        sl.addIngredient(new IngredientAmount(50, "g", "tomaten"));
        SsmlService sut = new SsmlService().enumerate(header, sl.getIngredients());
        String actualCard = sut.getCardString();

        final String expected1 = header + ": 150 ml wasser, 50 g tomaten";
        final String expected2 = header + ": 50 g tomaten, 150 ml wasser";
        assertEquals(actualCard, sut.getSpeechString());
        if(actualCard.endsWith("tomaten")) {
            assertEquals(expected1, actualCard);
        }
        else {
            assertEquals(expected2, actualCard);
        }
    }
    @Test
    public void enumerate_WithHeaderWithoutIngredients_ReturnCorrectString() {
        final String header = String.format(LIST_INGREDIENTS, 0);
        ShoppingList sl = new ShoppingList();
        SsmlService sut = new SsmlService().enumerate(header, sl.getIngredients());
        String actualCard = sut.getCardString();

        final String expected = header + ".";
        assertEquals(actualCard, sut.getSpeechString());
        assertEquals(expected, actualCard);
    }
}