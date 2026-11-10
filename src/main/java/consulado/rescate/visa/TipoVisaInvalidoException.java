package consulado.rescate.visa;

/** Thrown when a visa type code does not exist (instead of rejecting it silently). */
public class TipoVisaInvalidoException extends IllegalArgumentException {

    public TipoVisaInvalidoException(String mensaje) {
        super(mensaje);
    }
}
