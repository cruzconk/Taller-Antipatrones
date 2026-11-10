package consulado.rescate.tarifas;

/** Tourist visa: standard price, no discount and no surcharge. */
public class TarifaTurista extends TarifaPorFactor {

    private static final double FACTOR_ESTANDAR = 1.0;

    public TarifaTurista() {
        super(FACTOR_ESTANDAR);
    }
}
