package consulado.rescate.tramite;

import consulado.rescate.bitacora.Bitacora;

/** Single responsibility: persist the traveler's case file. */
public class RepositorioExpedientes {

    private static final String ACTOR = "RepositorioExpedientes";

    private final Bitacora bitacora;

    public RepositorioExpedientes(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    public void guardar(String viajero) {
        bitacora.registrar(ACTOR, "Guardando expediente de " + viajero);
    }
}
