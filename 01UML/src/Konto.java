import java.util.List;

public class Konto {

    private String bezeichnung;
    private List<Kunde> zeichnungsberechtigter;

    private Konto(Kunde... zeichnungsberechtigter) {
	this.zeichnungsberechtigter.addAll(zeichnungsberechtigter);
    }

    public Geldbetrag saldo(){

    }

    public void einzahlen(Geldbetrag eingBetrag){

    }

}
