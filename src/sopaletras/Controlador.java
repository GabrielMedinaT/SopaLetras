package sopaletras;

import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Controlador {

    private Modelo modelo;
    private Sopa sopa;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.sopa = new Sopa(15, 20); // Inicializar la sopa con el tamaño adecuado

        // Configurar los listeners de los botones en la vista
        vista.addAddButtonListener(e -> agregarPalabra());
        vista.addDeleteButtonListener(e -> eliminarPalabra());
        vista.addConsultButtonListener(e -> consultarPalabra());
        vista.addGenerateButtonListener(e -> generarSopa());
        vista.setGenerateButtonEnabled(false);
        vista.addVaciarListaListener(e -> vaciarTabla());
        vista.addSolucionButtonListener(e -> mostrarSolucion());
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

        // Obtener la lista de palabras
        List<String> palabras = modelo.obtenerPalabras();

        // Inicializar la matriz con letras aleatorias
        sopa.inicializaMatrices();

        // Colocar las palabras en la sopa de letras
        sopa.colocarTodas(palabras);

        // Mostrar la matriz en la vista
        vista.setTextAreaContent(convertirMatrizAString(sopa.getMatrizLetras()));
    }

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

    private void mostrarSolucion() {
        List<String> palabras = modelo.obtenerPalabras();
        StyledDocument doc = vista.areaSopa.getStyledDocument();

        // Eliminar cualquier resaltado previo
        limpiarResaltado(doc);

        // Obtener el contenido del JTextPane
        String contenido = vista.areaSopa.getText();

        // Iterar sobre cada palabra en la lista de palabras
        for (String palabra : palabras) {
            // Buscar y resaltar cada ocurrencia de la palabra en el contenido del JTextPane
            resaltarOcurrenciasEnTexto(palabra, contenido, doc);
        }
    }

    private void limpiarResaltado(StyledDocument doc) {
        SimpleAttributeSet defaultStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        StyleConstants.setBold(defaultStyle, false);
        StyleConstants.setItalic(defaultStyle, false);
        doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
    }

    private void resaltarOcurrenciasEnTexto(String palabra, String contenido, StyledDocument doc) {
        SimpleAttributeSet resaltado = new SimpleAttributeSet();
        StyleConstants.setForeground(resaltado, Color.RED);
        StyleConstants.setBold(resaltado, true);
        StyleConstants.setItalic(resaltado, true);

        int index = contenido.indexOf(palabra);
        while (index != -1) {
            doc.setCharacterAttributes(index, palabra.length(), resaltado, true);
            index = contenido.indexOf(palabra, index + palabra.length());
        }
    }
}
