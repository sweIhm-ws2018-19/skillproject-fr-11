package edu.hm.cs.seng.hypershop.service;

public class SsmlService {
    private final StringBuilder cardBuilder;
    private final StringBuilder speechBuilder;
    enum SayAsType {
        NUMBER, UNIT;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    public SsmlService() {
        cardBuilder = new StringBuilder();
        speechBuilder = new StringBuilder();
    }

    public SsmlService append(String s) {
        cardBuilder.append(s);
        speechBuilder.append(s);
        return this;
    }
    public SsmlService append(int i) {
        cardBuilder.append(i);
        speechBuilder.append(i);
        return this;
    }
    public SsmlService append(double d) {
        cardBuilder.append(d);
        speechBuilder.append(d);
        return this;
    }
    public SsmlService append(boolean b) {
        cardBuilder.append(b);
        speechBuilder.append(b);
        return this;
    }
    public SsmlService append(Object o) {
        cardBuilder.append(o);
        speechBuilder.append(o);
        return this;
    }
    public SsmlService append(SsmlService ssml) {
        cardBuilder.append(ssml.cardBuilder);
        speechBuilder.append(ssml.speechBuilder);
        return this;
    }
    public SsmlService beginParagraph() {
        speechBuilder.append("<p>");
        return this;
    }
    public SsmlService endParagraph() {
        speechBuilder.append("</p>");
        return this;
    }
    public SsmlService newLine() {
        append("\n");
        return this;
    }
    public SsmlService beginSentence() {
        speechBuilder.append("<s>");
        return this;
    }
    public SsmlService endSentence() {
        speechBuilder.append("</s>");
        return this;
    }
    public SsmlService inEnglish(String s) {
        speechBuilder.append("<lang xml:lang=\"en-US\">").append(s).append("</lang>");
        cardBuilder.append(s);
        return this;
    }
    public SsmlService enumerate(Iterable iterable) {
        boolean isFirst = true;
        for (Object o : iterable) {
            if(!isFirst)
                append(", ");
            else
                isFirst = false;
            append(o);
        }
        return this;
    }
    public SsmlService enumerate(String header, Iterable iterable) {
        this.append(header);
        if(iterable.iterator().hasNext()) {
            append(": ");
            enumerate(iterable);
        }
        else {
            append(".");
        }
        return this;
    }
    public SsmlService sayAs(String s, SayAsType type) {
        speechBuilder.append("<say-as interpret-as=\"").append(type).append("\">").append(s).append("</say-as>");
        cardBuilder.append(s);
        return this;
    }
    public String getCardString() {
        return cardBuilder.toString();
    }
    public String getSpeechString() {
        return speechBuilder.toString();
    }
}
