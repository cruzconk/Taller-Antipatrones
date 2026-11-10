package consulado.rescate.solicitud;

/** Immutable event published when a request is cancelled. */
public record EventoCancelacion(String idSolicitud, String viajero) {
}
