package sopaletras;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Vista extends JFrame {

    private final JTextField textField;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JButton generarSopa;
    private final JButton consultarBaseDatos;
    private final JTextPane textArea;
    private final JButton vaciarLista;
    private final JList<String> wordList; // Cambiado a JList<String>
    private final DefaultListModel<String> listModel;
    private final JButton botonSolucion;
    private Modelo modelo;
    private int lastSelectedIndex = -1;

    public Vista() {
        // Configuración de la ventana principal
        setTitle("Sopa de Letras");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        

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

        textArea = new JTextPane();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 28));
        textArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        
        rightPanel.add(textScrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        

        add(rightPanel, BorderLayout.CENTER);

        
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTextPane source = (JTextPane) e.getSource();
                int offset = source.viewToModel2D(e.getPoint());

                try {
                    StyledDocument doc = source.getStyledDocument();
                    Element element = doc.getCharacterElement(offset);
                    AttributeSet as = element.getAttributes();
                    Color currentColor = StyleConstants.getForeground(as);

                    // Create a new style
                    SimpleAttributeSet newStyle = new SimpleAttributeSet();
                    if (Color.RED.equals(currentColor)) {
                        // Revert to default color
                        StyleConstants.setForeground(newStyle, Color.BLACK);
                    } else {
                        // Change to red color
                        StyleConstants.setForeground(newStyle, Color.RED);
                    }

                    // Apply the new style to the letter
                    doc.setCharacterAttributes(offset, 1, newStyle, false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        
        
        
        

        
        
    }

    
    
    
public String[] getPalabras() {
    // Obtiene todas las palabras en la lista
    ListModel<String> listModel = wordList.getModel();
    String[] palabras = new String[listModel.getSize()];

    // Itera sobre el modelo de lista y agrega cada palabra al array
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

    
    public void setGenerateButtonEnabled(boolean generar){
        generarSopa.setEnabled(generar);
        
        }

    public String getTextFieldValue() {
        return textField.getText();
    }

    public void setTextFieldValue(String value) {
        textField.setText(value);
    }

    public void setTextAreaContent(String content) {
        textArea.setText(content);
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
    //al cerrar se vacia la basededatos
    
    // onclick de vaciarlista 

    
}
