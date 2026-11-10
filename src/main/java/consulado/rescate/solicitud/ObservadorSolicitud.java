package consulado.rescate.solicitud;

/**
 * Observer: anything interested in a request being cancelled implements this.
 * The request only knows this interface, never the concrete services.
 */
public interface ObservadorSolicitud {

    void alCancelarse(EventoCancelacion evento);
}
