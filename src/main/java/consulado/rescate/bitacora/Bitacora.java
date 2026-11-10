package consulado.rescate.bitacora;

/**
 * Tiny logging abstraction. Each rescued class reports what it did through this
 * interface, so the console output shows WHICH class performed each task, and
 * output can be redirected without touching the business classes.
 */
public interface Bitacora {

    void registrar(String actor, String mensaje);
}
