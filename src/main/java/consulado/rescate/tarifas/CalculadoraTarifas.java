package consulado.rescate.tarifas;

/**
 * Computes a visa price. The common part (base * months + fixed fee) is written once;
 * the visa-specific part is delegated to the {@link EstrategiaTarifa} received.
 */
public class CalculadoraTarifas {

    private final ConfiguracionTarifas configuracion;

    public CalculadoraTarifas(ConfiguracionTarifas configuracion) {
        this.configuracion = configuracion;
    }

    public double calcular(EstrategiaTarifa estrategia, int meses) {
        if (meses <= 0) {
            throw new IllegalArgumentException("Los meses deben ser positivos: " + meses);
        }
        double subtotal = configuracion.getPrecioBasePorMes() * meses + configuracion.getCargoFijo();
        return estrategia.aplicar(subtotal);
    }
}
