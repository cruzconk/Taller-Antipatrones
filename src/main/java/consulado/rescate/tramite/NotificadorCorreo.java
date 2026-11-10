package consulado.rescate.tramite;

import consulado.rescate.bitacora.Bitacora;

/** Single responsibility: send the "your visa is on its way" e-mail. */
public class NotificadorCorreo {

    private static final String ACTOR = "NotificadorCorreo";

    private final Bitacora bitacora;

    public NotificadorCorreo(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    public void avisarVisaEnCamino(String viajero) {
        bitacora.registrar(ACTOR, "Correo para " + viajero + ": su visa va en camino");
    }
}
