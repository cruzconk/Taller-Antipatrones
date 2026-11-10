package consulado.heredado;

/**
 * LEGACY CODE - kept on purpose as evidence (identical to the handout).
 * ANTIPATTERN: Magic Numbers. What is "tipo == 3"? Why 17, 31 or 66?
 * Unknown types fall through and are rejected silently, with no explanation.
 */
public class EvaluadorMagico {

    public String evaluar(int tipo, int edad) {
        if (tipo == 1 && edad > 17) return "APROBADA";
        if (tipo == 2 && edad > 15 && edad < 31) return "APROBADA";
        if (tipo == 3 && edad > 17 && edad < 66) return "APROBADA";
        return "RECHAZADA";
    }
}
