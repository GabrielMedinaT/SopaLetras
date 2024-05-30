package sopaletras;

import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.StyledDocument;

public class Controlador {

    private Modelo modelo;
    private Sopa sopa;
    private Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.sopa = new Sopa(15, 20); 

        vista.addAddButtonListener(e -> agregarPalabra());
        vista.addDeleteButtonListener(e -> eliminarPalabra());
        vista.addConsultButtonListener(e -> consultarPalabra());
        vista.addGenerateButtonListener(e -> generarSopa());
        vista.setGenerateButtonEnabled(false);
        vista.addVaciarListaListener(e -> vaciarTabla());
        vista.addSolucionButtonListener(e -> mostrarSolucion());
    }

    public void vaciarTabla() {
        modelo.vaciarPalabras();
        consultarPalabra();
    }

    public void iniciarPartida() {
        modelo.crearTabla();
        modelo.crearTrigger();
        modelo.vaciarPalabras();
        modelo.insertarPalabrasInicio();
        generarSopa();
        consultarPalabra();
        vista.setGenerateButtonEnabled(true);
    }

    private void agregarPalabra() {
        String palabra = vista.getTextFieldValue().trim();
        String palabraFinal = palabra.toUpperCase();

        if (!palabra.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+")) {
            JOptionPane.showMessageDialog(vista, "La palabra solo puede contener letras sin espacios ni caracteres especiales.", "Error", JOptionPane.WARNING_MESSAGE);
        } else if (palabra.length() > 15) {
            JOptionPane.showMessageDialog(vista, "La palabra no puede contener más de 10 caracteres. ", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            modelo.agregarPalabra(palabra);

            if (modelo.isDuplicateEntryError()) {
                JOptionPane.showMessageDialog(vista, "La palabra ya existe en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (modelo.esLimite()) {
                JOptionPane.showMessageDialog(vista, "No se pueden agregar más palabras. Elimine una si quiere colocar otra ", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                vista.addWordToList(palabraFinal);
                vista.clearTextField();
            }
        }
    }

    private void eliminarPalabra() {
        String palabra = vista.getSelectedWord();
        boolean habilitar = true;

        if (palabra != null) {
            modelo.eliminarPalabra(palabra);
            vista.removeWordFromList(palabra);
        }

        String[] palabrasIngresadas = vista.getPalabras();

        if (palabrasIngresadas.length <= 0) {
            habilitar = false;
        }

        vista.setGenerateButtonEnabled(habilitar);
    }

    private void consultarPalabra() {
        modelo.leerPalabras();
        List<String> palabras = modelo.obtenerPalabras();
        vista.setWordList(palabras);
        actualizarEstadoBotonGenerar();
    }

    private void actualizarEstadoBotonGenerar() {
        String[] palabrasIngresadas = vista.getPalabras();
        vista.setGenerateButtonEnabled(palabrasIngresadas != null && palabrasIngresadas.length > 0);
    }

    private void generarSopa() {
        modelo.leerPalabras();
        List<String> palabras = modelo.obtenerPalabrasParaSopa();
        sopa.inicializaMatrices();
        sopa.colocarTodas(palabras);
        String[][] matrizCombinada = sopa.combinarMatrices();
        String contenidoMatriz = convertirMatrizAString(matrizCombinada);
        vista.setTextAreaContent(contenidoMatriz);
        consultarPalabra();
    }

    private String convertirMatrizAString(String[][] matriz) {
        StringBuilder sb = new StringBuilder();
        for (String[] fila : matriz) {
            for (String letra : fila) {
                if (letra == null || letra.equals("�")) {
                    letra = " ";
                }
                sb.append(letra).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString().replaceAll("�", " ");
    }

    private void mostrarSolucion() {
        List<String> palabras = modelo.obtenerPalabras();
        StyledDocument doc = vista.areaSopa.getStyledDocument();
        vista.limpiarResaltado(doc);

        String[][] matrizCombinada = sopa.combinarMatrices();
        vista.resaltarPalabras(palabras, matrizCombinada);
    }
}
