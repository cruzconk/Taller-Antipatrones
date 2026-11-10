package consulado.rescate.visa;

/** Decision plus the human-readable reason behind it. */
public record ResultadoEvaluacion(DecisionVisa decision, String motivo) {

    public boolean aprobada() {
        return decision == DecisionVisa.APROBADA;
    }
}
