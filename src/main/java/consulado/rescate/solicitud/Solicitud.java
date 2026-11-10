package consulado.rescate.solicitud;

import consulado.rescate.visa.TipoVisa;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject of the Observer pattern. When cancelled it publishes ONE event to whoever
 * subscribed. It does not import, create or call the e-mail, appointment or payment
 * services, so adding one more notice never requires touching this class.
 */
public class Solicitud {

    private final String id;
    private final String viajero;
    private final String pasaporte;
    private final TipoVisa tipoVisa;
    private final int edad;
    private final int meses;
    private final List<ObservadorSolicitud> observadores = new ArrayList<>();
    private EstadoSolicitud estado = EstadoSolicitud.ACTIVA;

    public Solicitud(String id, String viajero, String pasaporte, TipoVisa tipoVisa, int edad, int meses) {
        this.id = id;
        this.viajero = viajero;
        this.pasaporte = pasaporte;
        this.tipoVisa = tipoVisa;
        this.edad = edad;
        this.meses = meses;
    }

    public void suscribir(ObservadorSolicitud observador) {
        observadores.add(observador);
    }

    public void cancelar() {
        if (estado == EstadoSolicitud.CANCELADA) {
            throw new IllegalStateException("La solicitud " + id + " ya estaba cancelada");
        }
        estado = EstadoSolicitud.CANCELADA;
        EventoCancelacion evento = new EventoCancelacion(id, viajero);
        observadores.forEach(o -> o.alCancelarse(evento));
    }

    public String getId() { return id; }
    public String getViajero() { return viajero; }
    public String getPasaporte() { return pasaporte; }
    public TipoVisa getTipoVisa() { return tipoVisa; }
    public int getEdad() { return edad; }
    public int getMeses() { return meses; }
    public EstadoSolicitud getEstado() { return estado; }
}
