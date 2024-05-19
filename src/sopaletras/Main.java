package sopaletras;

public class Main {
    public static void main(String[] args) {
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
        
        // Hacer visible la vista
        vista.setVisible(true);
    }
}
