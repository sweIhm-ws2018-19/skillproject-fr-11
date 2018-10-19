import java.util.List;

public class Konto {

    private String bezeichnung;
    private List<Kunde> zeichnungsberechtigter;

    private Konto(Kunde... zeichnungsberechtigter) throws IllegalArgumentException {
        if(zeichnungsberechtigter.length < 1) {
            throw new IllegalArgumentException("at least one zeichnungsberechtigter has to be supplied");
        }
	    this.zeichnungsberechtigter.addAll(zeichnungsberechtigter);
    }

    public GeldBetrag saldo(){

    }

    public void einzahlen(GeldBetrag eingBetrag){

    }

}
