package consulado.heredado;

/** LEGACY helper service: e-mail notifications. */
public class CorreoServicio {

    private boolean avisoEnviado = false;

    public void avisarCancelacion() {
        System.out.println("   [CorreoServicio] Aviso de cancelacion enviado");
        avisoEnviado = true;
    }

    public boolean isAvisoEnviado() { return avisoEnviado; }
}
