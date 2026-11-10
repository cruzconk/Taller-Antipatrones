package consulado.heredado;

/**
 * LEGACY CODE - kept on purpose as evidence.
 * ANTIPATTERN: God Object. One class validates the passport, charges the fee,
 * stores the file and sends the email (4 responsibilities).
 *
 * Only change versus the original handout: it returns a boolean and prefixes its
 * output with a tag so the demo can show WHO performed each task.
 */
public class OficinaDeTodo {

    private static final String TAG = "   [OficinaDeTodo/Ramiro] ";

    public boolean procesar(String nombre, String pasaporte, double precio) {
        if (pasaporte == null || pasaporte.length() < 6) {
            System.out.println(TAG + "Pasaporte invalido");
            return false;
        }
        System.out.println(TAG + "Cobrando $" + precio + " a " + nombre);
        System.out.println(TAG + "Guardando expediente de " + nombre);
        System.out.println(TAG + "Correo para " + nombre + ": su visa va en camino");
        return true;
    }
}
