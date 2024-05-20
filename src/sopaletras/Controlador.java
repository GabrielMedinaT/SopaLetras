package sopaletras;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

public class Controlador {

    private Modelo modelo;
    private Vista vista;
    static String[][] M; // Variable global
    static int[][] H; // Variable global array de huecos

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        boolean generar = false;

        // Configurar los listeners de los botones en la vista
        vista.addAddButtonListener(e -> agregarPalabra());
        vista.addDeleteButtonListener(e -> eliminarPalabra());
        vista.addConsultButtonListener(e -> consultarPalabra());
        vista.addGenerateButtonListener(e -> generarSopa());
        vista.setGenerateButtonEnabled(generar);
    }

    private void agregarPalabra() {
        String palabra = vista.getTextFieldValue().trim(); // Eliminar espacios en blanco al inicio y al final
        String palabraFinal = palabra.toUpperCase();

        // Validar que la palabra no tenga caracteres especiales ni espacios y tenga un máximo de 15 caracteres
        if (!palabra.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+")) {
            JOptionPane.showMessageDialog(vista, "La palabra solo puede contener letras sin espacios ni caracteres especiales.", "Error", JOptionPane.WARNING_MESSAGE);

        } else if (palabra.length() > 15) {
            JOptionPane.showMessageDialog(vista, "La palabra no puede contener más de 15 caracteres. Elimine una si quiere agregar otra", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            modelo.agregarPalabra(palabra);

            if (modelo.isDuplicateEntryError()) {
                // Manejar la acción cuando se detecta una entrada duplicada
                JOptionPane.showMessageDialog(vista, "La palabra ya existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (modelo.esLimite() == true) {
                JOptionPane.showMessageDialog(vista, "No se pueden agregar mas palabras .", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                vista.addWordToList(palabraFinal);
                vista.clearTextField();
            }
        }
    }

    private void eliminarPalabra() {
        // Obtener la palabra seleccionada desde la vista
        String palabra = vista.getSelectedWord();
        boolean habilitar = true;

        // Verificar si se seleccionó una palabra
        if (palabra != null) {
            // Eliminar la palabra del modelo
            modelo.eliminarPalabra(palabra);

            // Eliminar la palabra de la vista
            vista.removeWordFromList(palabra);
        }

        // Obtener las palabras ingresadas después de eliminar la seleccionada
        String[] palabrasIngresadas = vista.getPalabras();

        // Deshabilitar el botón de generar sopa si no hay palabras ingresadas
        if (palabrasIngresadas.length <= 0) {
            habilitar = false;
        }

        // Actualizar el estado del botón de generar sopa
        vista.setGenerateButtonEnabled(habilitar);

    }

    private void consultarPalabra() {
        // Leer las palabras desde la base de datos
        modelo.leerPalabras();

        // Obtener la lista de palabras leídas
        List<String> palabras = modelo.obtenerPalabras();

        // Actualizar la lista de palabras en la vista
        vista.setWordList(palabras);

        // Actualizar el estado del botón de generar sopa
        actualizarEstadoBotonGenerar();
    }

    private void actualizarEstadoBotonGenerar() {
        // Obtener las palabras ingresadas desde la vista
        String[] palabrasIngresadas = vista.getPalabras();

        // Habilitar o deshabilitar el botón de generar sopa según si hay palabras ingresadas
        vista.setGenerateButtonEnabled(palabrasIngresadas != null && palabrasIngresadas.length > 0);
    }

    private void generarSopa() {
        // Obtener las palabras ingresadas desde la vista
        String[] palabrasIngresadas = vista.getPalabras();

        if (palabrasIngresadas != null && palabrasIngresadas.length > 0) { // Verificar si se ingresaron palabras
            // Inicializar las matrices M y H
            M = new String[18][29];
            H = new int[18][29];

            // Generar la sopa de letras con las palabras ingresadas
            colocarTodas(palabrasIngresadas);

            // Mostrar la matriz en la vista
            vista.setTextAreaContent(convertirMatrizAString(M));
        } else {
            // Si no hay palabras, generar letras aleatorias
            inicializaMatrices(); // Llena la matriz con letras aleatorias

            // Mostrar la matriz en la vista
            vista.setTextAreaContent(convertirMatrizAString(M));
        }
    }

    // Métodos de colocación de palabras, inicialización de matrices y otras utilidades aquí...
    private String convertirMatrizAString(String[][] matriz) {
        StringBuilder sb = new StringBuilder();
        for (String[] fila : matriz) {
            for (String letra : fila) {
                sb.append(letra != null ? letra : " ").append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Métodos de colocación de palabras, inicialización de matrices y otras utilidades aquí...
    public static int azar(int limite) {
        Random random = new Random();
        return random.nextInt(limite);
    }

    public static void colocarTodas(String[] palabras) {
        int orientacion;
        String[] word;
        int longPalabra = palabras.length;
        int cuenta = 0;
        boolean COLOCADO = false;

        // Colocar todas las palabras
        do {
            word = palabras[cuenta].split("");
            orientacion = azar(8);
            COLOCADO = false;
            switch (orientacion) {
                case 0:
                    COLOCADO = colocarPalabra0(word);
                    break;
                case 1:
                    COLOCADO = colocarPalabra1(word);
                    break;
                case 2:
                    COLOCADO = colocarPalabra2(word);
                    break;
                case 3:
                    COLOCADO = colocarPalabra3(word);
                    break;
                case 4:
                    COLOCADO = colocarPalabra4(word);
                    break;
                case 5:
                    COLOCADO = colocarPalabra5(word);
                    break;
                case 6:
                    COLOCADO = colocarPalabra6(word);
                    break;
                case 7:
                    COLOCADO = colocarPalabra7(word);
                    break;
            }
            if (COLOCADO) {
                cuenta++;
            }
        } while (cuenta < longPalabra);

        // Rellenar los espacios restantes con letras aleatorias
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                if (M[i][j] == null) {
                    M[i][j] = generarLetraAleatoria();
                }
            }
        }
    }

    public static String generarLetraAleatoria() {
        String letras = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        return letras.charAt(azar(letras.length())) + "";
    }

    //-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de izquierda a derecha
    public static boolean colocarPalabra0(String[] word) {
        // Seleccionar una fila al azar de 0 a número de M.length-1
        int f = azar(M.length);
        // Seleccionar una columna al azar de 0 a número de columnas menos la longitud de word
        int L = word.length;
        int c = azar(M[0].length - L + 1);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f][c + t] == 1) && (!M[f][c + t].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f][c + t] = word[t];
                H[f][c + t] = 1;
            }
        }
        return PERMITIDO;
    }

//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de arriba abajo
    public static boolean colocarPalabra1(String[] word) {
        // Seleccionar una columna al azar de 0 a número de M[0].length-1
        int c = azar(M[0].length);
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[t];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

// Agrega las demás funciones colocarPalabra con las correcciones...
//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de derecha a izquierda
    public static boolean colocarPalabra2(String[] word) {
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        // Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el número de columnas - 1
        int c = azar(M[0].length);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[L - t - 1];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de arriba abajo y de derecha a izquierda
    public static boolean colocarPalabra3(String[] word) {
        // Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el número de columnas - 1
        int c = azar(M[0].length);
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[t];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

// Agrega las demás funciones colocarPalabra con las correcciones...
//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de derecha a izquierda
    public static boolean colocarPalabra4(String[] word) {
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        // Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el número de columnas - 1
        int c = azar(M[0].length);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[L - t - 1];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de arriba abajo y de derecha a izquierda
    public static boolean colocarPalabra5(String[] word) {
        // Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el número de columnas - 1
        int c = azar(M[0].length);
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[t];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de derecha a izquierda
    public static boolean colocarPalabra6(String[] word) {
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        // Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el número de columnas - 1
        int c = azar(M[0].length);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[L - t - 1];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

//-------------------------------------------------------------------------
// Coloca la palabra almacenada en word de arriba abajo y de derecha a izquierda
    public static boolean colocarPalabra7(String[] word) {
        // Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el número de columnas - 1
        int c = azar(M[0].length);
        // Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
        int L = word.length;
        int f = azar(M.length - L + 1);
        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            // Si el hueco está ocupado por una letra distinta de la que quiero colocar
            if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            // Colocamos la palabra en la matriz M
            for (int t = 0; t < L; t++) {
                M[f + t][c] = word[t];
                H[f + t][c] = 1;
            }
        }
        return PERMITIDO;
    }

    /**
     *
     */
    public static void inicializaMatrices() {
        String cadena = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String[] letra;
        letra = cadena.split("");
        int f, c;
        for (f = 0; f < M.length; f++) {
            // Rellena de letras al azar la fila f
            for (c = 0; c < M[0].length; c++) {
                M[f][c] = letra[azar(27)];
                H[f][c] = 1; // Marcar como ocupada
            }
        }
    }

}
