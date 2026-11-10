package consulado.rescate.visa;

/**
 * Evaluates a visa request. The rules live in {@link TipoVisa}, so there is no
 * if-chain to edit when a new type appears.
 */
public class EvaluadorVisa {

    public ResultadoEvaluacion evaluar(TipoVisa tipo, int edad) {
        if (tipo.admiteEdad(edad)) {
            return new ResultadoEvaluacion(DecisionVisa.APROBADA,
                    "Edad " + edad + " cumple el requisito (" + tipo.descripcionEdad() + ")");
        }
        return new ResultadoEvaluacion(DecisionVisa.RECHAZADA,
                "Edad " + edad + " fuera del rango permitido para " + tipo + " (" + tipo.descripcionEdad() + ")");
    }

    /**
     * Compatibility entry point for callers that still hold a legacy numeric code.
     *
     * @throws TipoVisaInvalidoException when the code does not match any type
     */
    public ResultadoEvaluacion evaluarPorCodigo(int codigo, int edad) {
        return evaluar(TipoVisa.desdeCodigo(codigo), edad);
    }
}
