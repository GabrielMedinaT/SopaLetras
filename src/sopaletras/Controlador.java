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
        vista.addVaciarListaListener(e -> vaciarTabla());
    }

    // Método vaciar tabla de la base de datos
    public void vaciarTabla() {
        modelo.vaciarPalabras();
        consultarPalabra();
    }

    
    public void iniciarPartida() {
        modelo.crearTabla();
        modelo.vaciarPalabras();
        modelo.insertarPalabrasInicio();
        consultarPalabra();
        generarSopa();
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
            } else if (modelo.esLimite()) {
                JOptionPane.showMessageDialog(vista, "No se pueden agregar más palabras.", "Error", JOptionPane.ERROR_MESSAGE);
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
        // Leer las palabras desde el modelo
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
    // Leer las palabras desde el modelo
    modelo.leerPalabras();

    //imprimir las palabras
    List<String> palabras = modelo.obtenerPalabras();
    System.out.println(Arrays.toString(palabras.toArray()));

    // Inicializar las matrices M y H
    M = new String[10][10]; // Tamaño arbitrario
    H = new int[10][10];

    // Comprueba si la lista de palabras no está vacía
    if (palabras != null && !palabras.isEmpty()) { 
        // Convertir la lista de palabras a un array
        String[] palabrasArray = palabras.toArray(new String[0]);

        // Generar la sopa de letras con las palabras ingresadas
        Sopa sopa = new Sopa();
        sopa.colocarTodas(palabrasArray, M, H);
        
        
        sopa.rellenarHuecos(M,H);
        
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

    // Llena la matriz con letras aleatorias
    private void inicializaMatrices() {
        Random random = new Random();
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                M[i][j] = String.valueOf((char) ('A' + random.nextInt(26)));
            }
        }
    }

    public static int azar(int limite) {
        Random random = new Random();
        return random.nextInt(limite);
    }
}
