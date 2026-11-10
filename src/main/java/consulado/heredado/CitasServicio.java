package consulado.heredado;

/** LEGACY helper service: appointment calendar. */
public class CitasServicio {

    private boolean cupoLiberado = false;

    public void liberarCupo() {
        System.out.println("   [CitasServicio] Cupo de la cita liberado");
        cupoLiberado = true;
    }

    public boolean isCupoLiberado() { return cupoLiberado; }
}
