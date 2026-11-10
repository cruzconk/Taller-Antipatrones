package consulado.rescate.bitacora;

/** {@link Bitacora} implementation that prints to the console. */
public class BitacoraConsola implements Bitacora {

    @Override
    public void registrar(String actor, String mensaje) {
        System.out.println("   [" + actor + "] " + mensaje);
    }
}
