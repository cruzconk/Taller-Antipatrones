package consulado.rescate.tramite;

import consulado.rescate.bitacora.Bitacora;

/** Single responsibility: decide whether a passport number is acceptable. */
public class ValidadorPasaporte {

    private static final String ACTOR = "ValidadorPasaporte";
    private static final int LONGITUD_MINIMA = 6;

    private final Bitacora bitacora;

    public ValidadorPasaporte(Bitacora bitacora) {
        this.bitacora = bitacora;
    }

    public boolean esValido(String pasaporte) {
        boolean valido = pasaporte != null && pasaporte.length() >= LONGITUD_MINIMA;
        bitacora.registrar(ACTOR, valido ? "Pasaporte valido" : "Pasaporte invalido");
        return valido;
    }
}
