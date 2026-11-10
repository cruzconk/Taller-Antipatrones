package consulado.rescate.visa;

import consulado.rescate.tarifas.EstrategiaTarifa;
import consulado.rescate.tarifas.TarifaEstudiante;
import consulado.rescate.tarifas.TarifaTrabajo;
import consulado.rescate.tarifas.TarifaTurista;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Replaces the magic numbers (tipo == 1/2/3, ages 17/31/66, ...).
 * Each constant carries its own legacy code, its allowed age range and its pricing
 * strategy. Adding a new visa type = one new constant here (plus its strategy class).
 */
public enum TipoVisa {

    TURISTA(1, Edades.MAYORIA_DE_EDAD, Edades.SIN_LIMITE, new TarifaTurista()),
    ESTUDIANTE(2, Edades.EDAD_MINIMA_ESTUDIANTE, Edades.EDAD_MAXIMA_ESTUDIANTE, new TarifaEstudiante()),
    TRABAJO(3, Edades.MAYORIA_DE_EDAD, Edades.EDAD_MAXIMA_TRABAJO, new TarifaTrabajo());

    /**
     * Named age limits (inclusive). They translate the legacy comparisons:
     * "edad > 17" -> 18, "edad > 15" -> 16, "edad < 31" -> 30, "edad < 66" -> 65.
     */
    private static final class Edades {
        static final int MAYORIA_DE_EDAD = 18;
        static final int EDAD_MINIMA_ESTUDIANTE = 16;
        static final int EDAD_MAXIMA_ESTUDIANTE = 30;
        static final int EDAD_MAXIMA_TRABAJO = 65;
        static final int SIN_LIMITE = Integer.MAX_VALUE;
    }

    private final int codigoHeredado;
    private final int edadMinima;
    private final int edadMaxima;
    private final EstrategiaTarifa estrategiaTarifa;

    TipoVisa(int codigoHeredado, int edadMinima, int edadMaxima, EstrategiaTarifa estrategiaTarifa) {
        this.codigoHeredado = codigoHeredado;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.estrategiaTarifa = estrategiaTarifa;
    }

    public boolean admiteEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }

    public String descripcionEdad() {
        if (edadMaxima == Edades.SIN_LIMITE) {
            return "desde " + edadMinima + " anios";
        }
        return "entre " + edadMinima + " y " + edadMaxima + " anios";
    }

    public EstrategiaTarifa estrategiaTarifa() {
        return estrategiaTarifa;
    }

    public int codigoHeredado() {
        return codigoHeredado;
    }

    /**
     * Translates a legacy numeric code into a type.
     *
     * @throws TipoVisaInvalidoException with a clear message when the code is unknown
     */
    public static TipoVisa desdeCodigo(int codigo) {
        for (TipoVisa tipo : values()) {
            if (tipo.codigoHeredado == codigo) {
                return tipo;
            }
        }
        String validos = Arrays.stream(values())
                .map(t -> t.codigoHeredado + "=" + t.name())
                .collect(Collectors.joining(", "));
        throw new TipoVisaInvalidoException(
                "Tipo de visa desconocido: " + codigo + ". Codigos validos: " + validos);
    }
}
