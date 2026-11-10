package consulado.rescate.tarifas;

/** Work visa: 50% surcharge. */
public class TarifaTrabajo extends TarifaPorFactor {

    private static final double FACTOR_RECARGO_TRABAJO = 1.5;

    public TarifaTrabajo() {
        super(FACTOR_RECARGO_TRABAJO);
    }
}
