import java.util.List;

public abstract class Kunde {

    private List<Konto> konto;

    private Kunde(Konto... konto) throws IllegalArgumentException {
        if(konto.length < 1) {
            throw new IllegalArgumentException("at least one konto has to be supplied");
        }
	    this.konto.addAll(konto);
    }
}
