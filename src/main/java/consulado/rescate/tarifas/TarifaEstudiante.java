package consulado.rescate.tarifas;

/** Student visa: 50% discount. */
public class TarifaEstudiante extends TarifaPorFactor {

    private static final double FACTOR_DESCUENTO_ESTUDIANTE = 0.5;

    public TarifaEstudiante() {
        super(FACTOR_DESCUENTO_ESTUDIANTE);
    }
}
