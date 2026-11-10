package consulado.rescate.solicitud;

import consulado.rescate.bitacora.Bitacora;

/** Observer: issues the refund. It can no longer be "forgotten" by the request. */
public class ObservadorPagos implements ObservadorSolicitud {

    private static final String ACTOR = "ObservadorPagos";

    private final Bitacora bitacora;
    private int reembolsosProcesados = 0;

    public ObservadorPagos(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    @Override
    public void alCancelarse(EventoCancelacion evento) {
        bitacora.registrar(ACTOR, "Reembolso procesado para " + evento.viajero());
        reembolsosProcesados++;
    }

    public int getReembolsosProcesados() { return reembolsosProcesados; }
}
