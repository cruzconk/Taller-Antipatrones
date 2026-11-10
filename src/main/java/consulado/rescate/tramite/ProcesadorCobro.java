package consulado.rescate.tramite;

import consulado.rescate.bitacora.Bitacora;

/** Single responsibility: charge the visa fee. */
public class ProcesadorCobro {

    private static final String ACTOR = "ProcesadorCobro";

    private final Bitacora bitacora;

    public ProcesadorCobro(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    public void cobrar(String viajero, double monto) {
        bitacora.registrar(ACTOR, "Cobrando $" + monto + " a " + viajero);
    }
}
