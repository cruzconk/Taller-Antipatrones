package consulado.heredado;

/**
 * LEGACY CODE - kept on purpose as evidence.
 * ANTIPATTERN: Copy-Paste Programming. The same formula (base * months + 10) is
 * duplicated in three methods.
 *
 * In the handout the base fee (50) is a literal repeated in each method. Here each
 * copy is a field with its own setter so the demo can COUNT how many places must be
 * edited when the base fee changes (see {@link #getEdiciones()}).
 */
public class CalculadoraDeTarifas {

    // One independent copy of the base fee per method (mirrors the three "50" literals).
    private double baseTurista = 50;
    private double baseEstudiante = 50;
    private double baseTrabajo = 50;

    // Counts how many places had to be edited to change the base fee.
    private int ediciones = 0;

    public void setBaseTurista(double valor) { baseTurista = valor; ediciones++; }
    public void setBaseEstudiante(double valor) { baseEstudiante = valor; ediciones++; }
    public void setBaseTrabajo(double valor) { baseTrabajo = valor; ediciones++; }
    public int getEdiciones() { return ediciones; }

    public double visaTurista(int meses) {
        double total = baseTurista * meses;
        total = total + 10;
        return total;
    }

    public double visaEstudiante(int meses) {
        double total = baseEstudiante * meses;
        total = total + 10;
        return total * 0.5;
    }

    public double visaTrabajo(int meses) {
        double total = baseTrabajo * meses;
        total = total + 10;
        return total * 1.5;
    }
}
