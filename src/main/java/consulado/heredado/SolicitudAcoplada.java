package consulado.heredado;

/**
 * LEGACY CODE - kept on purpose as evidence.
 * ANTIPATTERN: Tight coupling. The request creates its collaborators with "new" and
 * notifies them one by one, by hand. Someone forgot the payments service, so the
 * refund is never issued.
 *
 * Only change versus the original handout: getters so the demo can inspect what
 * actually happened after cancelling.
 */
public class SolicitudAcoplada {

    private String estado = "ACTIVA";
    private CorreoServicio correo = new CorreoServicio();
    private CitasServicio citas = new CitasServicio();
    private PagosServicio pagos = new PagosServicio();

    public void cancelar() {
        estado = "CANCELADA";
        correo.avisarCancelacion();
        citas.liberarCupo();
        // BUG: pagos.reembolsar() was forgotten -> the traveler loses the refund.
    }

    public String getEstado() { return estado; }
    public CorreoServicio getCorreo() { return correo; }
    public CitasServicio getCitas() { return citas; }
    public PagosServicio getPagos() { return pagos; }
}
