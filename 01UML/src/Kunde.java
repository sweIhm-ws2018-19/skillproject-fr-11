import java.util.List;

public abstract class Kunde {

    private List<Konto> konto;

    private Kunde(Konto... konto) {
	this.konto.addAll(konto);
    }
}
