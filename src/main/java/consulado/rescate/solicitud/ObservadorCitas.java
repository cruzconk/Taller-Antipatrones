package consulado.rescate.solicitud;

import consulado.rescate.bitacora.Bitacora;

/** Observer: frees the appointment slot of the cancelled request. */
public class ObservadorCitas implements ObservadorSolicitud {

    private static final String ACTOR = "ObservadorCitas";

    private final Bitacora bitacora;
    private int cuposLiberados = 0;

    public ObservadorCitas(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    @Override
    public void alCancelarse(EventoCancelacion evento) {
        bitacora.registrar(ACTOR, "Cupo liberado para la solicitud " + evento.idSolicitud());
        cuposLiberados++;
    }

    public int getCuposLiberados() { return cuposLiberados; }
}
