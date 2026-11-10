# La Oficina de Visas del Caos (Java)

**Taller para la Casa - Antipatrones** | Modelos de Programacion | Docente: Bocanegra
Universidad Distrital Francisco Jose de Caldas - Ingenieria de Sistemas

**Integrantes:** _(completar nombres y codigos)_

Se rescata el sistema heredado del "Consulado Caotico" refactorizando cuatro antipatrones
(God Object, Copy-Paste Programming, Magic Numbers y Acoplamiento fuerte) con
responsabilidad unica, Strategy, enum con constantes y Observer. El codigo esta comentado en ingles.

## Como compilar y ejecutar (JDK 17 o superior)

```bash
./run.sh
# o manualmente:
javac -d out $(find src -name '*.java')
java -cp out consulado.Main
```

Con Maven: `mvn package` y luego `java -cp target/classes consulado.Main`.

## Estructura de paquetes

```
src/main/java/consulado
|-- Main.java                      # ejecuta los 5 casos (heredada vs rescatada)
|-- heredado/                      # codigo legado, conservado a proposito como evidencia
|   |-- OficinaDeTodo              # God Object
|   |-- CalculadoraDeTarifas       # Copy-Paste
|   |-- EvaluadorMagico            # Magic Numbers
|   |-- SolicitudAcoplada          # Acoplamiento fuerte
|   `-- CorreoServicio, CitasServicio, PagosServicio
`-- rescate/
    |-- bitacora/                  # Bitacora (interfaz) + BitacoraConsola
    |-- tramite/                   # God Object -> responsabilidad unica
    |   |-- ValidadorPasaporte, ProcesadorCobro, RepositorioExpedientes, NotificadorCorreo
    |   `-- TramitadorVisa         # solo coordina, no hace el trabajo
    |-- tarifas/                   # Copy-Paste -> Strategy
    |   |-- EstrategiaTarifa, TarifaPorFactor
    |   |-- TarifaTurista, TarifaEstudiante, TarifaTrabajo
    |   `-- ConfiguracionTarifas (unico punto de cambio del precio base), CalculadoraTarifas
    |-- visa/                      # Magic Numbers -> enum + constantes + error claro
    |   |-- TipoVisa, DecisionVisa, ResultadoEvaluacion
    |   `-- EvaluadorVisa, TipoVisaInvalidoException
    `-- solicitud/                 # Acoplamiento -> Observer
        |-- Solicitud (sujeto), ObservadorSolicitud, EventoCancelacion, EstadoSolicitud
        `-- ObservadorCorreo, ObservadorCitas, ObservadorPagos
```

## Parte 1 - Caceria de antipatrones

| Fragmento | Antipatron | Sintoma que lo delata | Consecuencia en el consulado |
|---|---|---|---|
| 1. `OficinaDeTodo` (ventanilla de Ramiro) | **God Object** | Una sola clase valida el pasaporte, cobra, guarda el expediente y envia el correo (4 responsabilidades). | Si Ramiro (la clase) falla o cambia, todo el consulado se detiene; no se puede probar, reutilizar ni modificar una tarea sin riesgo de romper las otras. |
| 2. `CalculadoraDeTarifas` (tarifa clonada) | **Copy-Paste Programming** | Los tres metodos repiten la misma formula (`50 * meses + 10`) y solo cambia el factor final. | Al subir la tarifa base se corrigieron 2 de 3 copias: la visa de trabajo quedo con el precio viejo y los precios son inconsistentes. |
| 3. `EvaluadorMagico` (numeros misteriosos) | **Magic Numbers** | Valores sin nombre: `tipo == 3`, `edad > 17`, `edad < 31`, `edad < 66`, `0.5`. | Nadie sabe que significan ni si el 3 es de verdad "trabajo"; un tipo invalido (9) se rechaza en silencio y los cambios de regla son peligrosos. |
| 4. `SolicitudAcoplada` (cancelacion en cadena) | **Acoplamiento fuerte** | La solicitud crea con `new` y llama a mano a correo, citas y pagos. | Un viernes se olvidaron de la caja (`pagos`): el viajero perdio su reembolso, y agregar un aviso nuevo obliga a editar la solicitud. |

## Parte 2 - El rescate

| Antipatron | Solucion | Donde mirar |
|---|---|---|
| God Object | Responsabilidad unica: una clase por tarea; `TramitadorVisa` solo coordina (dependencias inyectadas). | `rescate/tramite` |
| Copy-Paste | Strategy: la formula comun esta una sola vez en `CalculadoraTarifas`; cada visa solo aporta su factor. El precio base vive solo en `ConfiguracionTarifas`. | `rescate/tarifas` |
| Magic Numbers | `enum TipoVisa` con codigo, rango de edad y estrategia de tarifa; constantes con nombre; `DecisionVisa` y `EstadoSolicitud` reemplazan los textos sueltos; un tipo invalido lanza `TipoVisaInvalidoException` con mensaje claro. | `rescate/visa`, `rescate/solicitud` |
| Acoplamiento fuerte | Observer: `Solicitud` publica un `EventoCancelacion`; correo, citas y pagos se suscriben y ella no los conoce. | `rescate/solicitud` |

## Parte 3 - Salida en consola de los 5 casos

```text
======================================================================================
CASO 1 - God Object
Tramitar la visa de un viajero: OficinaDeTodo vs clases separadas
======================================================================================
HEREDADA (una sola clase hace todo):
   [OficinaDeTodo/Ramiro] Cobrando $110.0 a Ana Gomez
   [OficinaDeTodo/Ramiro] Guardando expediente de Ana Gomez
   [OficinaDeTodo/Ramiro] Correo para Ana Gomez: su visa va en camino
RESCATADA (una clase por responsabilidad):
   [ValidadorPasaporte] Pasaporte valido
   [ProcesadorCobro] Cobrando $110.0 a Ana Gomez
   [RepositorioExpedientes] Guardando expediente de Ana Gomez
   [NotificadorCorreo] Correo para Ana Gomez: su visa va en camino
--------------------------------------------------------------------------------------
Resultado heredada : TRAMITE APROBADO
Resultado rescatada: TRAMITE APROBADO
Quien trabajo -> heredada: 1 actor (OficinaDeTodo/Ramiro) | rescatada: 4 actores distintos

======================================================================================
CASO 2 - Copy-Paste Programming
Subir la tarifa base de 50.0 a 60.0 (6 meses)
======================================================================================
Precios ANTES del aumento:
   TURISTA     heredada=310.00  strategy=310.00  coinciden=SI
   ESTUDIANTE  heredada=155.00  strategy=155.00  coinciden=SI
   TRABAJO     heredada=465.00  strategy=465.00  coinciden=SI
Precios DESPUES del aumento:
   TURISTA     heredada=370.00  strategy=370.00  coinciden=SI
   ESTUDIANTE  heredada=185.00  strategy=185.00  coinciden=SI
   TRABAJO     heredada=555.00  strategy=555.00  coinciden=SI
--------------------------------------------------------------------------------------
Lugares editados -> clonada: 3 | Strategy: 1
Si se olvida UNA de las copias (visa de trabajo sin actualizar):
   TRABAJO heredada = 465.00 | correcto (Strategy) = 555.00  -> PRECIOS INCONSISTENTES

======================================================================================
CASO 3 - Magic Numbers
Tipo 2 con edad 20 vs TipoVisa.ESTUDIANTE; luego el tipo 9
======================================================================================
HEREDADA  evaluar(2, 20)                  -> APROBADA  (que significa el 2? nadie lo dice)
RESCATADA evaluar(TipoVisa.ESTUDIANTE, 20) -> APROBADA  (Edad 20 cumple el requisito (entre 16 y 30 anios))
Mismo resultado: SI
--------------------------------------------------------------------------------------
Probando el tipo 9:
HEREDADA  -> RECHAZADA  (rechazo en silencio: sin motivo, sin error)
RESCATADA -> ERROR CLARO: Tipo de visa desconocido: 9. Codigos validos: 1=TURISTA, 2=ESTUDIANTE, 3=TRABAJO

======================================================================================
CASO 4 - Acoplamiento fuerte
Cancelar en SolicitudAcoplada vs Solicitud con Observer
======================================================================================
HEREDADA (la solicitud llama a mano a cada servicio):
   [CorreoServicio] Aviso de cancelacion enviado
   [CitasServicio] Cupo de la cita liberado
   Correo avisado : true
   Cupo liberado  : true
   Reembolso      : false  <-- OLVIDADO! el viajero pierde su reembolso
RESCATADA (la solicitud solo publica un evento):
   [ObservadorCorreo] Aviso de cancelacion enviado a Ana Gomez
   [ObservadorCitas] Cupo liberado para la solicitud S-001
   [ObservadorPagos] Reembolso procesado para Ana Gomez
   Avisos de correo      : 1
   Cupos liberados       : 1
   Reembolsos procesados : 1
   (la clase Solicitud no conoce ni importa correo, citas ni pagos)

======================================================================================
CASO 5 - Integracion
Viaje completo con la version nueva: solicitar, evaluar, cobrar y cancelar
======================================================================================
1) SOLICITAR
   Solicitud S-002 de Luis Perez para visa ESTUDIANTE (6 meses) -> estado ACTIVA
2) EVALUAR
   APROBADA: Edad 22 cumple el requisito (entre 16 y 30 anios)
3) COBRAR
   Precio calculado con Strategy: 155.00
   [ValidadorPasaporte] Pasaporte valido
   [ProcesadorCobro] Cobrando $155.0 a Luis Perez
   [RepositorioExpedientes] Guardando expediente de Luis Perez
   [NotificadorCorreo] Correo para Luis Perez: su visa va en camino
4) CANCELAR
   [ObservadorCorreo] Aviso de cancelacion enviado a Luis Perez
   [ObservadorCitas] Cupo liberado para la solicitud S-002
   [ObservadorPagos] Reembolso procesado para Luis Perez
   Estado final: CANCELADA
--------------------------------------------------------------------------------------
TABLA FINAL - ANTIPATRONES CAPTURADOS
| Antipatron             | Sintoma                                            | Solucion aplicada                                            |
| God Object             | OficinaDeTodo valida, cobra, guarda y envia correo | Responsabilidad unica: 1 clase por tarea + TramitadorVisa    |
| Copy-Paste Programming | Misma formula copiada 3 veces; se actualizaron 2   | Strategy + ConfiguracionTarifas (1 solo punto de cambio)     |
| Magic Numbers          | tipo == 3, edad > 17, descuento 0.5 sin nombre     | enum TipoVisa + constantes con nombre + error claro          |
| Acoplamiento fuerte    | Solicitud llama a mano a servicios y olvida pagos  | Observer: la solicitud publica un evento y no conoce a nadie |
======================================================================================
```

## Como evita volver a caer en los antipatrones

Cada clase nueva tiene una sola razon para cambiar: validar, cobrar, guardar, notificar, calcular o evaluar,
y `TramitadorVisa` solo coordina sin hacer el trabajo, asi que no aparece un nuevo "Ramiro". La formula de precio
existe una unica vez y el precio base vive en `ConfiguracionTarifas`, por lo que subir la tarifa es un solo cambio
y no se puede "olvidar una copia". Las reglas de cada visa estan en `TipoVisa` con constantes con nombre, y un
codigo desconocido falla con un mensaje claro. Finalmente `Solicitud` solo publica un evento: si manana se agrega
un aviso nuevo basta con crear otro observador y suscribirlo, sin tocar codigo ajeno, y ningun servicio puede
quedar olvidado en una cadena manual. Las dependencias se inyectan por constructor y las clases nuevas se conectan
en un unico lugar (`Main`), lo que evita el acoplamiento.

## Nota sobre el codigo heredado

El paquete `heredado` conserva los fragmentos originales a proposito, como evidencia de los antipatrones.
Solo se hicieron cambios minimos para poder demostrarlos: `OficinaDeTodo.procesar` devuelve `boolean` y etiqueta
su salida, `CalculadoraDeTarifas` guarda la tarifa base en tres campos con contador de ediciones (equivalente a
los tres literales `50`), y `SolicitudAcoplada` y los servicios exponen getters para mostrar el reembolso olvidado.
