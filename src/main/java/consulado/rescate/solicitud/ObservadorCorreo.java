package consulado.rescate.solicitud;

import consulado.rescate.bitacora.Bitacora;

/** Observer: e-mails the traveler about the cancellation. */
public class ObservadorCorreo implements ObservadorSolicitud {

    private static final String ACTOR = "ObservadorCorreo";

    private final Bitacora bitacora;
    private int avisosEnviados = 0;

    public ObservadorCorreo(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    @Override
    public void alCancelarse(EventoCancelacion evento) {
        bitacora.registrar(ACTOR, "Aviso de cancelacion enviado a " + evento.viajero());
        avisosEnviados++;
    }

    public int getAvisosEnviados() { return avisosEnviados; }
}
