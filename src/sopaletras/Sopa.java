package sopaletras;

import java.util.List;
import java.util.Random;

public class Sopa {

    private String[][] M; // Matriz de letras
    private String[][] H; // Matriz de palabras

    public Sopa(int filas, int columnas) {
        M = new String[filas][columnas];
        H = new String[filas][columnas];
        inicializaMatrices();
    }

    // Devuelve un número entre 0 y limite-1
    public static int azar(int limite) {
        return (int) Math.floor(Math.random() * limite);
    }

    public void mostrarMatriz() {
        for (int f = 0; f < M.length; f++) {
            mostrarFila(f);
        }
    }

    public void mostrarFila(int f) {
        int columnas = M[0].length;
        for (int c = 0; c < columnas; c++) {
            System.out.print(M[f][c] + " ");
        }
        System.out.println();
    }

    // Coloca la palabra almacenada en word de izquierda a derecha
    public boolean colocarPalabra0(String[] word) {
        int f = azar(M.length);
        int L = word.length;
        int c = azar(M[0].length - L + 1);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((H[f][c + t] != null) && (!H[f][c + t].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f][c + t] = word[t];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word de arriba hacia abajo
    public boolean colocarPalabra2(String[] word) {
        int L = word.length;
        int f = azar(M.length - L + 1);
        int c = azar(M[0].length);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((H[f + t][c] != null) && (!H[f + t][c].equals(word[t]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f + t][c] = word[t];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word de derecha a izquierda
    public boolean colocarPalabra4(String[] word) {
        int f = azar(M.length);
        int L = word.length;
        int c = azar(M[0].length - L + 1);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((H[f][c + t] != null) && (!H[f][c + t].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f][c + t] = word[L - t - 1];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word de abajo hacia arriba
    public boolean colocarPalabra6(String[] word) {
        int L = word.length;
        int f = azar(M.length - L + 1);
        int c = azar(M[0].length);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((H[f + t][c] != null) && (!H[f + t][c].equals(word[L - t - 1]))) {
                PERMITIDO = false;
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f + t][c] = word[L - t - 1];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word diagonal SE
    public boolean colocarPalabra1(String[] word) {
        int L = word.length;
        int f = azar(M.length);
        int c = azar(M[0].length);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((f + t >= M.length) || (c + t >= M[0].length)) {
                PERMITIDO = false;
            } else {
                if ((H[f + t][c + t] != null) && (!H[f + t][c + t].equals(word[t]))) {
                    PERMITIDO = false;
                }
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f + t][c + t] = word[t];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word diagonal NO
    public boolean colocarPalabra5(String[] word) {
        int L = word.length;
        int f = azar(M.length);
        int c = azar(M[0].length);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((f + t >= M.length) || (c + t >= M[0].length)) {
                PERMITIDO = false;
            } else {
                if ((H[f + t][c + t] != null) && (!H[f + t][c + t].equals(word[L - t - 1]))) {
                    PERMITIDO = false;
                }
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f + t][c + t] = word[L - t - 1];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word diagonal SO
    public boolean colocarPalabra3(String[] word) {
        int L = word.length;
        int f = azar(M.length);
        int c = azar(M[0].length);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((f + t >= M.length) || (c - t < 0)) {
                PERMITIDO = false;
            } else {
                if ((H[f + t][c - t] != null) && (!H[f + t][c - t].equals(word[t]))) {
                    PERMITIDO = false;
                }
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f + t][c - t] = word[t];
            }
        }
        return PERMITIDO;
    }

    // Coloca la palabra almacenada en word diagonal NE
    public boolean colocarPalabra7(String[] word) {
        int L = word.length;
        int f = azar(M.length);
        int c = azar(M[0].length);

        boolean PERMITIDO = true;
        for (int t = 0; t < L; t++) {
            if ((f + t >= M.length) || (c - t < 0)) {
                PERMITIDO = false;
            } else {
                if ((H[f + t][c - t] != null) && (!H[f + t][c - t].equals(word[L - t - 1]))) {
                    PERMITIDO = false;
                }
            }
        }
        if (PERMITIDO) {
            for (int t = 0; t < L; t++) {
                H[f + t][c - t] = word[L - t - 1];
            }
        }
        return PERMITIDO;
    }

    // Coloca todas las palabras en la sopa de letras
    public void colocarTodas(List<String> palabras) {
        String[] palabraArray = palabras.toArray(new String[0]);
        int orientacion;
        String[] word;
        int longPalabra = palabraArray.length;
        int cuenta = 0;
        boolean COLOCADO;
        do {
            word = palabraArray[cuenta].split("");
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
    }

    public void inicializaMatrices() {
        String cadena = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        String[] letra = cadena.split("");
        int f, c;
        for (f = 0; f < M.length; f++) {
            for (c = 0; c < M[0].length; c++) {
                M[f][c] = letra[azar(27)];
                H[f][c] = null;
            }
        }
    }

    public String[][] getMatrizLetras() {
        return M;
    }

    public String[][] getMatrizPalabras() {
        return H;
    }

    public String[][] combinarMatrices() {
        String[][] matrizCombinada = new String[M.length][M[0].length];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                if (H[i][j] != null) {
                    matrizCombinada[i][j] = H[i][j];
                } else {
                    matrizCombinada[i][j] = M[i][j];
                }
            }
        }
        return matrizCombinada;
    }
}
