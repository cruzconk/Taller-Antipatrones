package consulado.heredado;

/** LEGACY helper service: payments desk. */
public class PagosServicio {

    private boolean reembolsado = false;

    public void reembolsar() {
        System.out.println("   [PagosServicio] Reembolso procesado");
        reembolsado = true;
    }

    public boolean isReembolsado() { return reembolsado; }
}
