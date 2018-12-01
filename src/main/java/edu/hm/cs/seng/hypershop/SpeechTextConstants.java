package edu.hm.cs.seng.hypershop;

public class SpeechTextConstants {

    public static final String INGREDIENTS_ADD_SUCCESS = "%s wurde zu deiner Einkaufsliste hinzugefügt.";
    public static final String INGREDIENTS_ADD_ERROR = "Tut mir leid, ich habe die Zutat nicht verstanden, kannst du sie wiederholen?";
    public static final String INGREDIENTS_ADD_NUMBER_ERROR = "Tut mir leid, ich habe die angegebene Zutatenmenge nicht verstanden, kannst du sie wiederholen?";

    public static final String INGREDIENTS_ADD_REPROMPT = "Was willst du hinzufügen?";

    public static final String INGREDIENTS_REMOVE_REPROMPT = "Tut mir leid, ich habe die angegebene Zutat nicht verstanden, kannst du sie wiederholen?";
    public static final String INGREDIENTS_REMOVE_ERROR = "Tut mir leid, ich konnte die Zutat nicht finden.";
    public static final String INGREDIENTS_REMOVE_SUCCESS = "%s wurde aus deiner Einkaufsliste gelöscht.";

    public static final String STOP_TEXT = "Bis bald.";

    public static final String FALLBACK_TEXT = "Tut mir leid, das weiss ich nicht. Sage einfach Hilfe.";

    public static final String HELP_TEXT = "Ich bin dein smarter Einkaufszettel, du kannst Zutaten hinzufügen. " +
            "Sage zum Beispiel: <s>Füge ein Kilogramm Kartoffeln hinzu.</s> " +
            "Zur Ausgabe deines Einkaufszettels, sage einfach: <s>Liste Zutaten auf!</s>";
    public static final String HELP_REPROMPT = "Hilfe Nachfrage";

    public static final String LAUNCH_TEXT = "Willkommen bei <lang xml:lang=\"en-US\">Hypershop</lang>.";
    public static final String LAUNCH_REPROMPT = "Was willst du tun?";
}
