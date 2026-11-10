package consulado.rescate.tramite;

/**
 * Coordinator of the visa procedure. Its ONLY job is to run the steps in order;
 * every step is delegated to a class that owns exactly one responsibility.
 * Collaborators are injected, so nothing here is created with "new".
 */
public class TramitadorVisa {

    private final ValidadorPasaporte validador;
    private final ProcesadorCobro cobro;
    private final RepositorioExpedientes expedientes;
    private final NotificadorCorreo correo;

    public TramitadorVisa(ValidadorPasaporte validador,
                          ProcesadorCobro cobro,
                          RepositorioExpedientes expedientes,
                          NotificadorCorreo correo) {
        this.validador = validador;
        this.cobro = cobro;
        this.expedientes = expedientes;
        this.correo = correo;
    }

    /** @return true when the procedure was approved and completed. */
    public boolean tramitar(String viajero, String pasaporte, double precio) {
        if (!validador.esValido(pasaporte)) {
            return false;
        }
        cobro.cobrar(viajero, precio);
        expedientes.guardar(viajero);
        correo.avisarVisaEnCamino(viajero);
        return true;
    }
}
