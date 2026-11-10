package consulado.rescate.tarifas;

/**
 * Strategy: how the subtotal (base fee * months + fixed fee) is adjusted for one
 * kind of visa. Each visa type supplies its own implementation.
 */
public interface EstrategiaTarifa {

    double aplicar(double subtotal);
}
