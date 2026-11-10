package consulado;

import consulado.heredado.CalculadoraDeTarifas;
import consulado.heredado.EvaluadorMagico;
import consulado.heredado.OficinaDeTodo;
import consulado.heredado.SolicitudAcoplada;
import consulado.rescate.bitacora.Bitacora;
import consulado.rescate.bitacora.BitacoraConsola;
import consulado.rescate.solicitud.ObservadorCitas;
import consulado.rescate.solicitud.ObservadorCorreo;
import consulado.rescate.solicitud.ObservadorPagos;
import consulado.rescate.solicitud.Solicitud;
import consulado.rescate.tarifas.CalculadoraTarifas;
import consulado.rescate.tarifas.ConfiguracionTarifas;
import consulado.rescate.tramite.NotificadorCorreo;
import consulado.rescate.tramite.ProcesadorCobro;
import consulado.rescate.tramite.RepositorioExpedientes;
import consulado.rescate.tramite.TramitadorVisa;
import consulado.rescate.tramite.ValidadorPasaporte;
import consulado.rescate.visa.EvaluadorVisa;
import consulado.rescate.visa.ResultadoEvaluacion;
import consulado.rescate.visa.TipoVisa;
import consulado.rescate.visa.TipoVisaInvalidoException;

import java.util.List;
import java.util.Locale;

/**
 * Test harness: runs the 5 required cases, always showing the LEGACY version and the
 * RESCUED version one after the other.
 */
public class Main {

    private static final String DOBLE_LINEA = "=".repeat(86);
    private static final String LINEA = "-".repeat(86);

    // Demo data (named constants: no magic values in the harness either).
    private static final String VIAJERO_DEMO = "Ana Gomez";
    private static final String PASAPORTE_DEMO = "AB123456";
    private static final double PRECIO_DEMO = 110.0;
    private static final int MESES_DEMO = 6;
    private static final double NUEVA_TARIFA_BASE = 60.0;
    private static final int CODIGO_ESTUDIANTE_HEREDADO = 2;
    private static final int EDAD_ESTUDIANTE_DEMO = 20;
    private static final int CODIGO_DESCONOCIDO = 9;

    /** One row of the final summary table (case 5). */
    private record Captura(String antipatron, String sintoma, String solucion) { }

    public static void main(String[] args) {
        caso1GodObject();
        caso2CopyPaste();
        caso3MagicNumbers();
        caso4AcoplamientoFuerte();
        caso5Integracion();
    }

    // ---------------------------------------------------------------- Case 1
    private static void caso1GodObject() {
        encabezado("CASO 1 - God Object", "Tramitar la visa de un viajero: OficinaDeTodo vs clases separadas");
        Bitacora bitacora = new BitacoraConsola();

        System.out.println("HEREDADA (una sola clase hace todo):");
        boolean heredada = new OficinaDeTodo().procesar(VIAJERO_DEMO, PASAPORTE_DEMO, PRECIO_DEMO);

        System.out.println("RESCATADA (una clase por responsabilidad):");
        TramitadorVisa tramitador = new TramitadorVisa(
                new ValidadorPasaporte(bitacora),
                new ProcesadorCobro(bitacora),
                new RepositorioExpedientes(bitacora),
                new NotificadorCorreo(bitacora));
        boolean rescatada = tramitador.tramitar(VIAJERO_DEMO, PASAPORTE_DEMO, PRECIO_DEMO);

        System.out.println(LINEA);
        System.out.println("Resultado heredada : " + (heredada ? "TRAMITE APROBADO" : "TRAMITE RECHAZADO"));
        System.out.println("Resultado rescatada: " + (rescatada ? "TRAMITE APROBADO" : "TRAMITE RECHAZADO"));
        System.out.println("Quien trabajo -> heredada: 1 actor (OficinaDeTodo/Ramiro) | rescatada: 4 actores distintos");
        System.out.println();
    }

    // ---------------------------------------------------------------- Case 2
    private static void caso2CopyPaste() {
        encabezado("CASO 2 - Copy-Paste Programming",
                "Subir la tarifa base de " + ConfiguracionTarifas.PRECIO_BASE_POR_MES_INICIAL
                        + " a " + NUEVA_TARIFA_BASE + " (" + MESES_DEMO + " meses)");

        CalculadoraDeTarifas clonada = new CalculadoraDeTarifas();
        ConfiguracionTarifas configuracion = new ConfiguracionTarifas();
        CalculadoraTarifas strategy = new CalculadoraTarifas(configuracion);

        System.out.println("Precios ANTES del aumento:");
        imprimirPrecios(clonada, strategy);

        // Legacy: the base fee must be edited in each of the three copies.
        clonada.setBaseTurista(NUEVA_TARIFA_BASE);
        clonada.setBaseEstudiante(NUEVA_TARIFA_BASE);
        clonada.setBaseTrabajo(NUEVA_TARIFA_BASE);
        // Strategy: one single place.
        configuracion.setPrecioBasePorMes(NUEVA_TARIFA_BASE);

        System.out.println("Precios DESPUES del aumento:");
        imprimirPrecios(clonada, strategy);
        System.out.println(LINEA);
        System.out.println("Lugares editados -> clonada: " + clonada.getEdiciones()
                + " | Strategy: " + configuracion.getCambiosRealizados());

        // What happened in the real consulate: one copy was forgotten.
        CalculadoraDeTarifas olvidadiza = new CalculadoraDeTarifas();
        olvidadiza.setBaseTurista(NUEVA_TARIFA_BASE);
        olvidadiza.setBaseEstudiante(NUEVA_TARIFA_BASE);
        System.out.println("Si se olvida UNA de las copias (visa de trabajo sin actualizar):");
        System.out.println("   TRABAJO heredada = " + dinero(olvidadiza.visaTrabajo(MESES_DEMO))
                + " | correcto (Strategy) = "
                + dinero(strategy.calcular(TipoVisa.TRABAJO.estrategiaTarifa(), MESES_DEMO))
                + "  -> PRECIOS INCONSISTENTES");
        System.out.println();
    }

    private static void imprimirPrecios(CalculadoraDeTarifas clonada, CalculadoraTarifas strategy) {
        for (TipoVisa tipo : TipoVisa.values()) {
            double heredado = precioHeredado(clonada, tipo);
            double nuevo = strategy.calcular(tipo.estrategiaTarifa(), MESES_DEMO);
            System.out.println("   " + String.format("%-11s", tipo) + " heredada=" + dinero(heredado)
                    + "  strategy=" + dinero(nuevo) + "  coinciden=" + (heredado == nuevo ? "SI" : "NO"));
        }
    }

    /** Demo-only adapter: maps each type to the matching cloned legacy method. */
    private static double precioHeredado(CalculadoraDeTarifas calculadora, TipoVisa tipo) {
        return switch (tipo) {
            case TURISTA -> calculadora.visaTurista(MESES_DEMO);
            case ESTUDIANTE -> calculadora.visaEstudiante(MESES_DEMO);
            case TRABAJO -> calculadora.visaTrabajo(MESES_DEMO);
        };
    }

    // ---------------------------------------------------------------- Case 3
    private static void caso3MagicNumbers() {
        encabezado("CASO 3 - Magic Numbers", "Tipo 2 con edad 20 vs TipoVisa.ESTUDIANTE; luego el tipo 9");
        EvaluadorMagico magico = new EvaluadorMagico();
        EvaluadorVisa evaluador = new EvaluadorVisa();

        String heredada = magico.evaluar(CODIGO_ESTUDIANTE_HEREDADO, EDAD_ESTUDIANTE_DEMO);
        ResultadoEvaluacion rescatada = evaluador.evaluar(TipoVisa.ESTUDIANTE, EDAD_ESTUDIANTE_DEMO);
        System.out.println("HEREDADA  evaluar(" + CODIGO_ESTUDIANTE_HEREDADO + ", " + EDAD_ESTUDIANTE_DEMO
                + ")                  -> " + heredada + "  (que significa el 2? nadie lo dice)");
        System.out.println("RESCATADA evaluar(TipoVisa.ESTUDIANTE, " + EDAD_ESTUDIANTE_DEMO + ") -> "
                + rescatada.decision() + "  (" + rescatada.motivo() + ")");
        System.out.println("Mismo resultado: " + (heredada.equals(rescatada.decision().name()) ? "SI" : "NO"));

        System.out.println(LINEA);
        System.out.println("Probando el tipo " + CODIGO_DESCONOCIDO + ":");
        System.out.println("HEREDADA  -> " + magico.evaluar(CODIGO_DESCONOCIDO, EDAD_ESTUDIANTE_DEMO)
                + "  (rechazo en silencio: sin motivo, sin error)");
        try {
            evaluador.evaluarPorCodigo(CODIGO_DESCONOCIDO, EDAD_ESTUDIANTE_DEMO);
        } catch (TipoVisaInvalidoException e) {
            System.out.println("RESCATADA -> ERROR CLARO: " + e.getMessage());
        }
        System.out.println();
    }

    // ---------------------------------------------------------------- Case 4
    private static void caso4AcoplamientoFuerte() {
        encabezado("CASO 4 - Acoplamiento fuerte", "Cancelar en SolicitudAcoplada vs Solicitud con Observer");
        Bitacora bitacora = new BitacoraConsola();

        System.out.println("HEREDADA (la solicitud llama a mano a cada servicio):");
        SolicitudAcoplada acoplada = new SolicitudAcoplada();
        acoplada.cancelar();
        System.out.println("   Correo avisado : " + acoplada.getCorreo().isAvisoEnviado());
        System.out.println("   Cupo liberado  : " + acoplada.getCitas().isCupoLiberado());
        System.out.println("   Reembolso      : " + acoplada.getPagos().isReembolsado()
                + (acoplada.getPagos().isReembolsado() ? "" : "  <-- OLVIDADO! el viajero pierde su reembolso"));

        System.out.println("RESCATADA (la solicitud solo publica un evento):");
        ObservadorCorreo correo = new ObservadorCorreo(bitacora);
        ObservadorCitas citas = new ObservadorCitas(bitacora);
        ObservadorPagos pagos = new ObservadorPagos(bitacora);
        Solicitud solicitud = new Solicitud("S-001", VIAJERO_DEMO, PASAPORTE_DEMO,
                TipoVisa.TURISTA, EDAD_ESTUDIANTE_DEMO, MESES_DEMO);
        solicitud.suscribir(correo);
        solicitud.suscribir(citas);
        solicitud.suscribir(pagos);
        solicitud.cancelar();
        System.out.println("   Avisos de correo      : " + correo.getAvisosEnviados());
        System.out.println("   Cupos liberados       : " + citas.getCuposLiberados());
        System.out.println("   Reembolsos procesados : " + pagos.getReembolsosProcesados());
        System.out.println("   (la clase Solicitud no conoce ni importa correo, citas ni pagos)");
        System.out.println();
    }

    // ---------------------------------------------------------------- Case 5
    private static void caso5Integracion() {
        encabezado("CASO 5 - Integracion", "Viaje completo con la version nueva: solicitar, evaluar, cobrar y cancelar");
        Bitacora bitacora = new BitacoraConsola();

        // Composition root: the only place where concrete classes are wired together.
        ConfiguracionTarifas configuracion = new ConfiguracionTarifas();
        CalculadoraTarifas calculadora = new CalculadoraTarifas(configuracion);
        EvaluadorVisa evaluador = new EvaluadorVisa();
        TramitadorVisa tramitador = new TramitadorVisa(
                new ValidadorPasaporte(bitacora),
                new ProcesadorCobro(bitacora),
                new RepositorioExpedientes(bitacora),
                new NotificadorCorreo(bitacora));
        ObservadorCorreo correo = new ObservadorCorreo(bitacora);
        ObservadorCitas citas = new ObservadorCitas(bitacora);
        ObservadorPagos pagos = new ObservadorPagos(bitacora);

        System.out.println("1) SOLICITAR");
        Solicitud solicitud = new Solicitud("S-002", "Luis Perez", "XK987654",
                TipoVisa.ESTUDIANTE, 22, MESES_DEMO);
        solicitud.suscribir(correo);
        solicitud.suscribir(citas);
        solicitud.suscribir(pagos);
        System.out.println("   Solicitud " + solicitud.getId() + " de " + solicitud.getViajero()
                + " para visa " + solicitud.getTipoVisa() + " (" + solicitud.getMeses()
                + " meses) -> estado " + solicitud.getEstado());

        System.out.println("2) EVALUAR");
        ResultadoEvaluacion evaluacion = evaluador.evaluar(solicitud.getTipoVisa(), solicitud.getEdad());
        System.out.println("   " + evaluacion.decision() + ": " + evaluacion.motivo());

        System.out.println("3) COBRAR");
        double precio = calculadora.calcular(solicitud.getTipoVisa().estrategiaTarifa(), solicitud.getMeses());
        System.out.println("   Precio calculado con Strategy: " + dinero(precio));
        if (evaluacion.aprobada()) {
            tramitador.tramitar(solicitud.getViajero(), solicitud.getPasaporte(), precio);
        }

        System.out.println("4) CANCELAR");
        solicitud.cancelar();
        System.out.println("   Estado final: " + solicitud.getEstado());

        System.out.println(LINEA);
        System.out.println("TABLA FINAL - ANTIPATRONES CAPTURADOS");
        imprimirTabla(List.of(
                new Captura("God Object", "OficinaDeTodo valida, cobra, guarda y envia correo",
                        "Responsabilidad unica: 1 clase por tarea + TramitadorVisa"),
                new Captura("Copy-Paste Programming", "Misma formula copiada 3 veces; se actualizaron 2",
                        "Strategy + ConfiguracionTarifas (1 solo punto de cambio)"),
                new Captura("Magic Numbers", "tipo == 3, edad > 17, descuento 0.5 sin nombre",
                        "enum TipoVisa + constantes con nombre + error claro"),
                new Captura("Acoplamiento fuerte", "Solicitud llama a mano a servicios y olvida pagos",
                        "Observer: la solicitud publica un evento y no conoce a nadie")));
        System.out.println(DOBLE_LINEA);
    }

    // ---------------------------------------------------------------- helpers
    private static void encabezado(String titulo, String descripcion) {
        System.out.println(DOBLE_LINEA);
        System.out.println(titulo);
        System.out.println(descripcion);
        System.out.println(DOBLE_LINEA);
    }

    private static void imprimirTabla(List<Captura> filas) {
        String formato = "| %-22s | %-50s | %-60s |%n";
        System.out.printf(formato, "Antipatron", "Sintoma", "Solucion aplicada");
        for (Captura fila : filas) {
            System.out.printf(formato, fila.antipatron(), fila.sintoma(), fila.solucion());
        }
    }

    /** Locale-independent money format (always a dot as the decimal separator). */
    private static String dinero(double valor) {
        return String.format(Locale.ROOT, "%.2f", valor);
    }
}
