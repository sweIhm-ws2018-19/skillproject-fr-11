package edu.hm.cs.seng.hypershop;

public class SpeechTextConstants {

    private static final String RECIPE_NOT_FOUND = "Tut mir leid, ich konnte das Rezept %s nicht finden.";
    private static final String RECIPE_NAME_ERROR = "Tut mir leid, ich konnte den Rezeptnamen nicht verstehen.";
    private static final String RECIPE_NAME_REPROMPT = "Wie lautet der Rezeptnamen?";
    private static final String INGREDIENT_ERROR = "Tut mir leid, ich konnte die angegebene Zutat nicht verstehen.";
    private static final String INGREDIENT_NUMBER_ERROR = "Tut mir leid, ich konnte die angegebene Zutatenmenge nicht verstehen.";
    private static final String INGREDIENT_UNIT_ERROR = "Tut mir leid, ich konnte die angegebene Einheit nicht finden.";

    private SpeechTextConstants() {
        // hide public ctor
    }

    public static final String INGREDIENTS_ADD_SUCCESS = "%s wurde zu deiner Einkaufsliste hinzugefügt.";
    public static final String INGREDIENTS_ADD_ERROR = INGREDIENT_ERROR;
    public static final String INGREDIENTS_ADD_NUMBER_ERROR = INGREDIENT_NUMBER_ERROR;
    public static final String INGREDIENTS_ADD_UNIT_ERROR = INGREDIENT_UNIT_ERROR;

    public static final String INGREDIENTS_ADD_REPROMPT = "Was willst du hinzufügen?";

    public static final String INGREDIENTS_REMOVE_REPROMPT = "Tut mir leid, ich konnte die angegebene Zutat nicht verstehen, kannst du sie wiederholen?";
    public static final String INGREDIENTS_REMOVE_ERROR = "Tut mir leid, ich konnte die Zutat nicht finden.";
    public static final String INGREDIENTS_REMOVE_SUCCESS = "%s wurde aus deiner Einkaufsliste gelöscht.";
    public static final String INGREDIENTS_REMOVE_RECIPE_SUCCESS = "%s wurde von deinem Rezept gelöscht.";

    public static final String STOP_TEXT = "Bis bald.";

    public static final String FALLBACK_TEXT = "Tut mir leid, das weiss ich nicht. Sage einfach Hilfe.";

    public static final String HELP_TEXT = "Ich bin dein smarter Einkaufszettel, du kannst Zutaten hinzufügen. " +
            "Sage zum Beispiel: <s>Füge ein Kilogramm Kartoffeln hinzu.</s> " +
            "Zur Ausgabe deines Einkaufszettels, sage einfach: <s>Liste Zutaten auf!</s>";
    public static final String HELP_REPROMPT = "Hilfe Nachfrage";

    public static final String LAUNCH_TEXT = "Willkommen bei <lang xml:lang=\"en-US\">Hypershop</lang>.";
    public static final String LAUNCH_REPROMPT = "Was willst du tun?";

    public static final String RECIPES_LIST = "Du hast %d Rezepte gespeichert";

    public static final String RECIPE_CREATE_SUCCESS = "Das Rezept %s wurde erstellt. Du kannst jetzt Zutaten hinzufügen und entfernen.";
    public static final String RECIPE_CREATE_ERROR = RECIPE_NAME_ERROR;
    public static final String RECIPE_CREATE_REPROMPT = RECIPE_NAME_REPROMPT;
    public static final String RECIPE_CREATE_INVALID_INTENT = "Tut mir leid, das kannst du hier nicht machen, du bearbeitest gerade ein Rezept.";
    public static final String RECIPE_CREATE_DUPLICATE = "Tut mir leid, das Rezept %s konnte nicht erstellt werden, es existiert bereits.";

    public static final String RECIPE_ADD_SUCCESS = "Das Rezept %s wurde zu deiner Einkaufsliste hinzugefügt.";
    public static final String RECIPE_ADD_MULTI_SUCCESS = "%d Portionen %s wurden zu deiner Einkaufsliste hinzugefügt.";
    public static final String RECIPE_ADD_ERROR = RECIPE_NAME_ERROR;
    public static final String RECIPE_ADD_ERROR_AMOUNT = "Tut mir leid, ich konnte die Anzahl der Portionen nicht verstehen.";
    public static final String RECIPE_ADD_NOT_FOUND = RECIPE_NOT_FOUND;
    public static final String RECIPE_ADD_REPROMPT = RECIPE_NAME_REPROMPT;

    public static final String RECIPE_EDIT_SUCCESS = "Du bearbeitest jetzt das Rezept %s.";
    public static final String RECIPE_EDIT_ERROR = RECIPE_NAME_ERROR;
    public static final String RECIPE_EDIT_REPROMPT = "Welches Rezept möchtest du bearbeiten?";
    public static final String RECIPE_EDIT_NOT_FOUND = RECIPE_NOT_FOUND;


    public static final String RECIPE_LIST_NOT_FOUND = RECIPE_NOT_FOUND;
    public static final String LIST_RECIPE_INGREDIENTS = "Du hast %d Zutaten in deinem Rezept";
    public static final String LIST_INGREDIENTS = "Du hast %d Zutaten in deiner Einkaufsliste";
    public static final String LIST_RECIPES = "Du hast %d Rezepte in deiner Einkaufsliste";

    public static final String INGREDIENTS_ADD_RECIPE_SUCCESS = "%s wurde zu deinem Rezept hinzugefügt.";    ;

    public static final String RECIPE_REMOVE_NOT_FOUND = RECIPE_NOT_FOUND;
    public static final String RECIPE_REMOVE_REPROMPT = "Welches Rezept möchtest du von der Einkaufsliste entfernen?";
    public static final String RECIPE_REMOVE_SUCCESS = "%s wurde von deiner Einkaufsliste entfernt.";

    public static final String RECIPE_DELETE_NOT_FOUND = RECIPE_NOT_FOUND;
    public static final String RECIPE_DELETE_REPROMPT = "Welches Rezept möchtest du löschen?";
    public static final String RECIPE_DELETE_SUCCESS = "Das Rezept %s wurde gelöscht.";

    public static final String LIST_CLEAR_SUCCESS = "Deine Einkaufsliste wurde geleert.";

    public static final String BACK_OK = "Ok.";
}
