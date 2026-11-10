package consulado.rescate.tarifas;

/**
 * THE single place where the base price lives. Raising the fee means calling
 * {@link #setPrecioBasePorMes(double)} once; every visa type picks it up.
 * It also counts the changes so the demo can compare against the cloned version.
 */
public class ConfiguracionTarifas {

    public static final double PRECIO_BASE_POR_MES_INICIAL = 50.0;
    public static final double CARGO_FIJO = 10.0;

    private double precioBasePorMes = PRECIO_BASE_POR_MES_INICIAL;
    private int cambiosRealizados = 0;

    public void setPrecioBasePorMes(double nuevoPrecio) {
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio base debe ser positivo: " + nuevoPrecio);
        }
        this.precioBasePorMes = nuevoPrecio;
        this.cambiosRealizados++;
    }

    public double getPrecioBasePorMes() { return precioBasePorMes; }
    public double getCargoFijo() { return CARGO_FIJO; }
    public int getCambiosRealizados() { return cambiosRealizados; }
}
