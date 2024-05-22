package sopaletras;

import java.util.ArrayList;
import java.util.List;

public class Sopa {
    //-------------------------------------------------------------------------
    //DEVUELVE UN NÚMERO ENTRE 0 Y limite-1
    private static final int[][] DIRECCIONES = {
        {-1, 0},    // Arriba
        {1, 0},     // Abajo
        {0, -1},    // Izquierda
        {0, 1},     // Derecha
        {-1, -1},   // Arriba-Izquierda
        {-1, 1},    // Arriba-Derecha
        {1, -1},    // Abajo-Izquierda
        {1, 1}      // Abajo-Derecha
    };


    public static int azar(int limite) {
        return (int) Math.floor(Math.random() * limite);
    }
    //-------------------------------------------------------------------------

    //getter de posiciones

    public static void mostrarMatriz(String[][] M) {
        for (int f = 0; f < M.length; f++) { //M.length = rows
            mostrarFila(M, f);
        }
    }
    //-------------------------------------------------------------------------

    public static void mostrarFila(String[][] M, int f) {
        int columnas = M[0].length;// Vemos la longitud de la primera fila
        for (int c = 0; c < columnas; c++) {

            System.out.print(M[f][c] + " ");

        }
        System.out.println();
    }
    //-------------------------------------------------------------------------
    //Coloca la palabra almacenada en word de izquierda a derecha

    public static boolean colocarPalabra0(String[] word, String[][] M, int[][] H) {
    //Seleccionar una fila al azar de 0 a número de M.length-1
    int f = azar(M.length);
    //Seleccionar una columna al azar de 0 a número de columnas menos la longitud de word
    int L = word.length;
    int c = azar(M[0].length - L + 1);

    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        //Si el hueco está ocupado por una letra distinta de la que quiero colocar
        if ((H[f][c + t] == 1) && (!M[f][c + t].equals(word[t]))) {
            PERMITIDO = false;
        }
    }
    if (PERMITIDO) {
        //Colocamos la palabra en la matriz M
        for (int t = 0; t < L; t++) {
            M[f][c + t] = word[t];
            H[f][c + t] = 1;
        }
    }
    return PERMITIDO;
}

public static boolean colocarPalabra1(String[] word, String[][] M, int[][] H) {
    int L = word.length;
    int f = azar(M.length);
    int c = azar(M[0].length);
    //Colocamos la palabra en la matriz M
    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        //si me salgo de los límites del array
        if ((f + t >= M.length) || (c + t >= M[0].length)) {
            PERMITIDO = false;
        } else { //si el hueco está ocupado por una letra distinta de la quiero colocar
            if ((H[f + t][c + t] == 1) && (!M[f + t][c + t].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f + t][c + t] = word[t];
            H[f + t][c + t] = 1;
        }
    }
    return PERMITIDO;
}

public static boolean colocarPalabra2(String[] word, String[][] M, int[][] H) {
    //Seleccionar una fila al azar de 0 a número de filas menos la longitud de word
    int L = word.length;
    int f = azar(M.length - L + 1);
    //Seleccionar una columna al azar de 0 a número de M[0].length-1 que es el numero de columnas -1
    int c = azar(M[0].length);
    //Colocamos la palabra en la matriz M

    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[t]))) {
            PERMITIDO = false;
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f + t][c] = word[t];
            H[f + t][c] = 1;
        }
    }
    return PERMITIDO;
}

public static boolean colocarPalabra3(String[] word, String[][] M, int[][] H) {
    int L = word.length;
    int f = azar(M.length);
    int c = azar(M[0].length);
    //Colocamos la palabra en la matriz M
    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        if ((f + t >= M.length) || (c - t < 0)) {
            PERMITIDO = false;
        } else { //si el hueco está ocupado por una letra distinta de la quiero colocar
            if ((H[f + t][c - t] == 1) && (!M[f + t][c - t].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f + t][c - t] = word[t];
            H[f + t][c - t] = 1;
        }
    }
    return PERMITIDO;
}
public static boolean colocarPalabra4(String[] word, String[][] M, int[][] H) {
    int f = azar(M.length);
    int L = word.length;
    int c = azar(M[0].length - L + 1);

    //Colocamos la palabra en la matriz M
    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        if ((H[f][c + t] == 1) && (!M[f][c + t].equals(word[L - t - 1]))) {
            PERMITIDO = false;
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f][c + t] = word[L - t - 1]; //coloca la palabra al revés
            H[f][c + t] = 1;
        }
    }
    return PERMITIDO;
}

public static boolean colocarPalabra5(String[] word, String[][] M, int[][] H) {
    int L = word.length;
    int f = azar(M.length);
    int c = azar(M[0].length);

    //Colocamos la palabra en la matriz M
    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        if ((f + t >= M.length) || (c + t >= M[0].length)) {
            PERMITIDO = false;
        } else {
            if ((H[f + t][c + t] == 1) && (!M[f + t][c + t].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f + t][c + t] = word[L - t - 1];
            H[f + t][c + t] = 1;
        }
    }
    return PERMITIDO;
}

public static boolean colocarPalabra6(String[] word, String[][] M, int[][] H) {
    int L = word.length;
    int f = azar(M.length - L + 1);
    int c = azar(M[0].length);

    //Colocamos la palabra en la matriz M
    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        if ((H[f + t][c] == 1) && (!M[f + t][c].equals(word[L - t - 1]))) {
            PERMITIDO = false;
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f + t][c] = word[L - t - 1];
            H[f + t][c] = 1;
        }
    }
    return PERMITIDO;
}

public static boolean colocarPalabra7(String[] word, String[][] M, int[][] H) {
    int L = word.length;
    int f = azar(M.length);
    int c = azar(M[0].length);

    //Colocamos la palabra en la matriz M
    boolean PERMITIDO = true;
    for (int t = 0; t < L; t++) {
        if ((f + t >= M.length) || (c - t < 0)) {
            PERMITIDO = false;
        } else {
            if ((H[f + t][c - t] == 1) && (!M[f + t][c - t].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
    }
    if (PERMITIDO) {
        for (int t = 0; t < L; t++) {
            M[f + t][c - t] = word[L - t - 1];
            H[f + t][c - t] = 1;
        }
    }
    return PERMITIDO;
}

    //-------------------------------------------------------------------------
    //Coloca la palabra almacenada en word SE


    //-------------------------------------------------------------------------



    public static void colocarTodas(String[] palabras, String[][] M, int[][] H) {
        int orientacion;
        boolean COLOCADO;
        System.out.println("Colocando ");
        
        for (String p : palabras) {
            String[] palabra = p.split("");
            do {
                orientacion = azar(8);
                COLOCADO = false;
                switch (orientacion) {
                    case 0:
                        COLOCADO = colocarPalabra0(palabra, M, H);
                        break;
                    case 1:
                        COLOCADO = colocarPalabra1(palabra, M, H);
                        break;
                    case 2:
                        COLOCADO = colocarPalabra2(palabra, M, H);
                        break;
                    case 3:
                        COLOCADO = colocarPalabra3(palabra, M, H);
                        break;
                    case 4:
                        COLOCADO = colocarPalabra4(palabra, M, H);
                        break;
                    case 5:
                        COLOCADO = colocarPalabra5(palabra, M, H);
                        break;
                    case 6:
                        COLOCADO = colocarPalabra6(palabra, M, H);
                        break;
                    case 7:
                        COLOCADO = colocarPalabra7(palabra, M, H);
                        break;
                }
            } while (!COLOCADO);
        }
    }

    public static void rellenarHuecos(String[][] M, int[][] H) {
        String cadena = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String[] letra = cadena.split("");
        for (int f = 0; f < M.length; f++) {
            for (int c = 0; c < M[0].length; c++) {
                if (M[f][c] == null) {
                    M[f][c] = letra[azar(27)];
                    H[f][c] = 0;
                }
                //imprimir el valor de las posiciones 
                System.out.print(M[f][c] + " ");
                
            }
        }
    }

    // Aquí van los métodos colocarPalabraX, inicializaMatrices, y las variables globales M y H como las tienes implementadas.



    //--------------------------------------------------------------------------

    public static void inicializaMatrices() {
        String cadena = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String[] letra;
        letra = cadena.split("");
        int f, c;
        for (f = 0; f < M.length; f++) { //M.length = rows
            //Rellena de letras al azar la fila f
            for (c = 0; c < M[0].length; c++) {//M[0].length = cols
                M[f][c] = letra[azar(27)];
                H[f][c] = 0;
            }
        }
    }

public List<int[]> buscarPalabra(String palabra, String[][] M) {
    List<int[]> posiciones = new ArrayList<>();

    for (int i = 0; i < M.length; i++) {
        for (int j = 0; j < M[0].length; j++) {
            // Si la primera letra de la palabra coincide con la letra en M[i][j], intenta encontrar la palabra en todas las direcciones
            if (M[i][j].equals(String.valueOf(palabra.charAt(0)))) {
                // Buscar la palabra en todas las direcciones
                buscarEnTodasDirecciones(palabra, M, i, j, posiciones);
            }
        }
    }

    return posiciones;
}

private void buscarEnTodasDirecciones(String palabra, String[][] M, int fila, int columna, List<int[]> posiciones) {
    // Direcciones: horizontal, vertical, diagonal, etc.
    int[][] direcciones = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    for (int[] direccion : direcciones) {
        int dx = direccion[0];
        int dy = direccion[1];
        int x = fila;
        int y = columna;

        int i;
        for (i = 1; i < palabra.length(); i++) {
            x += dx;
            y += dy;

            // Verificar si la posición está dentro de los límites de la matriz
            if (x < 0 || x >= M.length || y < 0 || y >= M[0].length) {
                break; // Salir del bucle si estamos fuera de los límites de la matriz
            }

            // Verificar si la letra en la posición actual coincide con la siguiente letra de la palabra
            if (!M[x][y].equals(String.valueOf(palabra.charAt(i)))) {
                break; // Salir del bucle si no coincide
            }
        }

        // Si llegamos al final de la palabra, la palabra fue encontrada en esta dirección
        if (i == palabra.length()) {
            // Agregar la posición inicial y final de la palabra a la lista de posiciones
            int[] inicio = {fila, columna};
            int[] fin = {x, y};
            posiciones.add(inicio);
            posiciones.add(fin);
            return; // No es necesario buscar en otras direcciones
        }
    }
}

    //--------------------------------------------------------------------------
    static String[][] M; //Variable global
    static int[][] H; //Variable global array de huecos


}
