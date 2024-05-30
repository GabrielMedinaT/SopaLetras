package sopaletras;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class Vista extends JFrame {

    private final JTextField textField;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JButton generarSopa;
    private final JButton consultarBaseDatos;
    public final JTextPane areaSopa;
    private final JButton vaciarLista;
    private final JList<String> wordList; // Cambiado a JList<String>
    private final DefaultListModel<String> listModel;
    private final JButton botonSolucion;
    private final JLabel timerLabel;
    private Timer timer;
    private int secondsElapsed;
    private int elapsedTimeInSeconds;

    private final List<Character> selectedLetters; // Lista para guardar las letras seleccionadas
    private final DocumentFilter filter = new DocumentFilter() {
        private static final int MAX_LENGTH = 10;
        private static final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            if (isValid(string) && fb.getDocument().getLength() + string.length() <= MAX_LENGTH) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            int newLength = fb.getDocument().getLength() - length + text.length();
            if (isValid(text) && newLength <= MAX_LENGTH) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private boolean isValid(String text) {
            for (char c : text.toCharArray()) {
                if (VALID_CHARACTERS.indexOf(c) == -1) {
                    return false;
                }
            }
            return true;
        }
    };

    public Vista() {
        selectedLetters = new ArrayList<>(); // Inicializar la lista
        // Configuración de la ventana principal
        setTitle("Sopa de Letras");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        timerLabel = new JLabel("Tiempo: 0s");
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);

        // Panel izquierdo para la entrada de palabras y botones
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo de texto para ingresar palabras
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        leftPanel.add(new JLabel("Palabra:"), gbc);

        textField = new JTextField(10);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
        gbc.gridy = 1;
        leftPanel.add(textField, gbc);

        // Botón Añadir
        addButton = new JButton("Añadir");
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        leftPanel.add(addButton, gbc);

        // Botón Eliminar
        deleteButton = new JButton("Eliminar");
        gbc.gridx = 1;
        leftPanel.add(deleteButton, gbc);

        // Botón Consultar Palabras
        consultarBaseDatos = new JButton("Consultar Palabras");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        leftPanel.add(consultarBaseDatos, gbc);

        // Botón Vaciar Lista
        vaciarLista = new JButton("Vaciar Lista");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        leftPanel.add(vaciarLista, gbc);

        // Lista de palabras (JList)
        listModel = new DefaultListModel<>();
        wordList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(wordList);
        gbc.gridy = 5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        leftPanel.add(listScrollPane, gbc);

        add(leftPanel, BorderLayout.WEST);

        // Panel derecho para la sopa de letras y botón de generación
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        generarSopa = new JButton("Generar Sopa Letras");
        rightPanel.add(generarSopa, BorderLayout.NORTH);

        botonSolucion = new JButton("Solucion");
        rightPanel.add(botonSolucion, BorderLayout.SOUTH);

        areaSopa = new JTextPane();
        areaSopa.setFont(new Font("Monospaced", Font.PLAIN, 32));
        areaSopa.setEditable(false);

        // Crear un panel intermedio para centrar el JTextPane
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridx = 0;
        centerGbc.gridy = 0;
        centerGbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(areaSopa, centerGbc);
        centerPanel.setBackground(Color.WHITE);

        // Crear un panel de scroll para el JTextPane
        JScrollPane textScrollPane = new JScrollPane(centerPanel);
        rightPanel.add(textScrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        areaSopa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        areaSopa.setCaretColor(getBackground());
        areaSopa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTextPane source = (JTextPane) e.getSource();
                int offset = source.viewToModel2D(e.getPoint());

                try {
                    StyledDocument doc = source.getStyledDocument();
                    Element element = doc.getCharacterElement(offset);
                    AttributeSet as = element.getAttributes();
                    Color currentColor = StyleConstants.getForeground(as);
                    String selectedText = doc.getText(offset, 1).trim(); // Trim to avoid spaces

                    // Create a new style
                    SimpleAttributeSet newStyle = new SimpleAttributeSet();
                    if (Color.RED.equals(currentColor)) {
                        // Revert to default color
                        StyleConstants.setForeground(newStyle, Color.BLACK);
                        StyleConstants.setBold(newStyle, false);
                        StyleConstants.setItalic(newStyle, false);

                        // Remove the letter from the selected list if it's being deselected
                        if (!selectedText.isEmpty()) {
                            selectedLetters.remove((Character) selectedText.charAt(0));
                        }
                    } else {
                        // Change to red color and make it bold and italic
                        StyleConstants.setForeground(newStyle, Color.RED);
                        StyleConstants.setBold(newStyle, true);
                        StyleConstants.setItalic(newStyle, true);

                        // Add the selected letter to the list
                        if (!selectedText.isEmpty()) {
                            selectedLetters.add(selectedText.charAt(0));
                        }
                    }

                    // Apply the new style to the letter
                    doc.setCharacterAttributes(offset, 1, newStyle, false);

                    // Print the current selection
                    StringBuilder sb = new StringBuilder();
                    for (Character c : selectedLetters) {
                        sb.append(c);
                    }
                    System.out.println("Selected letters: " + sb.toString());

                    // Check if the selected letters form a word in the list
                    checkForCompleteWord();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        generarSopa.addActionListener(e -> startNewGame());

    }

    private void resetTimer() {
        if (timer != null) {
            timer.stop();
        }
        secondsElapsed = 0;
        timerLabel.setText("Tiempo: 00:00");
    }

 private void checkIfGameCompleted() {
    boolean allWordsFound = true;
    for (int i = 0; i < listModel.size(); i++) {
        String word = listModel.getElementAt(i);
        if (!word.startsWith("<html><b><strike>")) {
            allWordsFound = false;
            break;
        }
    }

    if (allWordsFound) {
        stopTimer(); // Detener el temporizador al completar el juego
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String timeTaken = String.format("%d minutos y %d segundos", minutes, seconds);

        // Determinar el nivel de dificultad
        String difficultyLevel;
        int numWords = listModel.size();
        if (numWords <= 5) {
            difficultyLevel = "Fácil";
        } else if (numWords <= 10) {
            difficultyLevel = "Medio";
        } else {
            difficultyLevel = "Difícil";
        }

        String message = "¡Juego completado en " + timeTaken + "!\nNivel: " + difficultyLevel;
        JOptionPane.showMessageDialog(this, message, "Felicidades", JOptionPane.INFORMATION_MESSAGE);
    }
}


    private void startTimer() {
        timer = new Timer(1000, e -> {
            secondsElapsed++;
            elapsedTimeInSeconds++; // Incrementar el tiempo transcurrido
            timerLabel.setText("Tiempo: " + formatTime(secondsElapsed));
        });
        timer.start();
        updateTimerLabel();
    }

    private void updateTimerLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        timerLabel.setText("Tiempo: " + String.format("%02d:%02d", minutes, seconds));

        // Determinar el nivel de dificultad
        String nivelDificultad;
        if (listModel.size() <= 5) {
            nivelDificultad = "Fácil";
        } else if (listModel.size() <= 10) {
            nivelDificultad = "Medio";
        } else {
            nivelDificultad = "Difícil";
        }
        timerLabel.setText("Tiempo: " + String.format("%02d:%02d", minutes, seconds) + "   Nivel: " + nivelDificultad);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    private void checkForCompleteWord() {
        StringBuilder sb = new StringBuilder();
        for (Character c : selectedLetters) {
            sb.append(c);
        }
        String formedWord = sb.toString();

        // Check if the formed word is in the list
        for (int i = 0; i < listModel.size(); i++) {
            String word = listModel.getElementAt(i);
            if (word.equals(formedWord) || word.equals("<html><b><strike>" + formedWord + "</strike></b></html>")) {
                // Tachado, en negrita, rojo e itálico en la lista de palabras
                listModel.set(i, "<html><b><strike><font color='red'><i>" + formedWord + "</i></font></strike></b></html>");
                selectedLetters.clear();
                break;

            }
        }

        // Check if all words are found
        checkIfGameCompleted();
    }

    public void limpiarResaltado(StyledDocument doc) {
        SimpleAttributeSet defaultStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        StyleConstants.setBold(defaultStyle, false);
        StyleConstants.setItalic(defaultStyle, false);
        doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
    }

    private void startNewGame() {
        resetTimer();
        startTimer();
        // Lógica para generar una nueva sopa de letras
    }

    public void resaltarOcurrenciasEnMatriz(String palabra, StyledDocument doc, String[][] matriz) {
        SimpleAttributeSet resaltado = new SimpleAttributeSet();
        StyleConstants.setForeground(resaltado, Color.RED);
        StyleConstants.setBold(resaltado, true);
        StyleConstants.setItalic(resaltado, true);

        try {
            int filas = matriz.length;
            int columnas = matriz[0].length;
            int palabraLongitud = palabra.length();

            // Horizontal de izquierda a derecha
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j <= columnas - palabraLongitud; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i][j + k].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = i * (columnas * 2 + 1) + j * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * 2, 1, resaltado, false);
                        }
                    }
                }
            }

            // Horizontal de derecha a izquierda
            for (int i = 0; i < filas; i++) {
                for (int j = palabraLongitud - 1; j < columnas; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i][j - k].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = i * (columnas * 2 + 1) + (j - palabraLongitud + 1) * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * 2, 1, resaltado, false);
                        }
                    }
                }
            }

            // Vertical de arriba hacia abajo
            for (int i = 0; i <= filas - palabraLongitud; i++) {
                for (int j = 0; j < columnas; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i + k][j].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = i * (columnas * 2 + 1) + j * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * (columnas * 2 + 1), 1, resaltado, false);
                        }
                    }
                }
            }

            // Vertical de abajo hacia arriba
            for (int i = palabraLongitud - 1; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i - k][j].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = (i - palabraLongitud + 1) * (columnas * 2 + 1) + j * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * (columnas * 2 + 1), 1, resaltado, false);
                        }
                    }
                }
            }

            // Diagonal de arriba izquierda a abajo derecha
            for (int i = 0; i <= filas - palabraLongitud; i++) {
                for (int j = 0; j <= columnas - palabraLongitud; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i + k][j + k].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = i * (columnas * 2 + 1) + j * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * (columnas * 2 + 3), 1, resaltado, false);
                        }
                    }
                }
            }

            // Diagonal de abajo derecha a arriba izquierda
            for (int i = palabraLongitud - 1; i < filas; i++) {
                for (int j = palabraLongitud - 1; j < columnas; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i - k][j - k].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = (i - palabraLongitud + 1) * (columnas * 2 + 1) + (j - palabraLongitud + 1) * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * (columnas * 2 + 3), 1, resaltado, false);
                        }
                    }
                }
            }

            // Diagonal de abajo izquierda a arriba derecha
            for (int i = palabraLongitud - 1; i < filas; i++) {
                for (int j = 0; j <= columnas - palabraLongitud; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i - k][j + k].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = (i - palabraLongitud + 1) * (columnas * 2 + 1) + j * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * (columnas * 2 - 1), 1, resaltado, false);
                        }
                    }
                }
            }

            // Diagonal de arriba derecha a abajo izquierda
            for (int i = 0; i <= filas - palabraLongitud; i++) {
                for (int j = palabraLongitud - 1; j < columnas; j++) {
                    boolean encontrado = true;
                    for (int k = 0; k < palabraLongitud; k++) {
                        if (!matriz[i + k][j - k].equals(String.valueOf(palabra.charAt(k)))) {
                            encontrado = false;
                            break;
                        }
                    }
                    if (encontrado) {
                        int start = i * (columnas * 2 + 1) + (j - palabraLongitud + 1) * 2;
                        for (int k = 0; k < palabraLongitud; k++) {
                            doc.setCharacterAttributes(start + k * (columnas * 2 - 1), 1, resaltado, false);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resaltarPalabras(List<String> palabras, String[][] matriz) {
        StyledDocument doc = areaSopa.getStyledDocument();
        limpiarResaltado(doc);

        for (String palabra : palabras) {
            resaltarOcurrenciasEnMatriz(palabra, doc, matriz);
        }
    }

    public String[] getPalabras() {
        ListModel<String> listModel = wordList.getModel();
        String[] palabras = new String[listModel.getSize()];

        for (int i = 0; i < listModel.getSize(); i++) {
            palabras[i] = listModel.getElementAt(i);
        }

        return palabras;
    }

    public void setWordList(List<String> palabras) {
        listModel.clear();
        for (String palabra : palabras) {
            listModel.addElement(palabra);
        }
    }

    public void setGenerateButtonEnabled(boolean generar) {
        generarSopa.setEnabled(generar);
    }

    public String getTextFieldValue() {
        return textField.getText();
    }

    public void setTextFieldValue(String value) {
        textField.setText(value);
    }

    public void setTextAreaContent(String content) {
        areaSopa.setText(content);
    }

    public void addWordToList(String word) {
        listModel.addElement(word);
    }

    public void removeWordFromList(String word) {
        listModel.removeElement(word);
    }

    public void clearTextField() {
        textField.setText("");
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addGenerateButtonListener(ActionListener listener) {
        generarSopa.addActionListener(listener);
    }

    public void addConsultButtonListener(ActionListener listener) {
        consultarBaseDatos.addActionListener(listener);
    }

    public String getSelectedWord() {
        return wordList.getSelectedValue();
    }

    public void addVaciarListaListener(ActionListener listener) {
        vaciarLista.addActionListener(listener);
    }

    public void addWindowCloseListener(WindowAdapter windowAdapter) {
        addWindowListener(windowAdapter);
    }

    public void addSolucionButtonListener(ActionListener listener) {
        botonSolucion.addActionListener(listener);
    }
}
