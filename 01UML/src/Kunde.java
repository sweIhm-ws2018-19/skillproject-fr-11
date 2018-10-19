import java.util.List;

public class Kunde {

    private List<Konto> konto;

    private Kunde(Kunde... konto) {
	this.konto.addAll(konto);
    }
}
