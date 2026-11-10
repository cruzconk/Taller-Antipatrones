package consulado.rescate.tarifas;

/**
 * Shared behavior of every strategy that simply scales the subtotal by a factor.
 * The multiplication lives here ONCE; subclasses only provide a named factor.
 */
public abstract class TarifaPorFactor implements EstrategiaTarifa {

    private final double factor;

    protected TarifaPorFactor(double factor) {
        this.factor = factor;
    }

    @Override
    public double aplicar(double subtotal) {
        return subtotal * factor;
    }
}
