package sopaletras;

import java.util.ArrayList;
import java.util.List;
import org.mariadb.jdbc.Connection;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;

public class Modelo {

    private List<String> palabras;
    private Connection connection;
    private boolean duplicateEntryError;
    private boolean limite;
    //array de 20 palabras para el juego
    private String[] palabrasJuego = (new String[]{"CASA", "PERRO", "GATO", "COCHE", "MOTO", "BICI", "MESA", "SILLA", "CAMA", "PUERTA", "VENTANA", "TELEVISOR", "ORDENADOR", "LAPIZ", "BOLIGRAFO", "LIBRO", "CUADERNO", "MOVIL", "RELOJ", "PANTALLA", "COCINA", "BAÑO", "SALON", "DORMITORIO", "JARDIN", "PISCINA", "GARAJE", "TRASTERO", "TERRAZA", "BALCON", "AZOTEA", "PATIO", "PASILLO", "SOTANO", "BUHARDILLA", "COCINA", "BAÑO", "SALON", "DORMITORIO", "JARDIN", "PISCINA", "GARAJE", "TRASTERO", "TERRAZA", "BALCON", "AZOTEA", "PATIO", "PASILLO", "SOTANO", "BUHARDILLA"});

    public Modelo() {
        palabras = new ArrayList<>(); // Inicializar la lista de palabras

        try {
            // Cargar el controlador JDBC
            Class.forName("org.mariadb.jdbc.Driver");

            // Establecer la conexión con la base de datos (reemplaza los valores con los de tu propia base de datos)
            String url = "jdbc:mariadb://localhost:3306/sopaletras";
            String usuario = "root";
            String contraseña = "root1234";
            connection = (Connection) DriverManager.getConnection(url, usuario, contraseña);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void crearTabla() {
        try {
            // Preparar la consulta SQL para crear la tabla correspondiente
            String sql = "CREATE TABLE IF NOT EXISTS palabras (palabra VARCHAR(15) UNIQUE)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Ejecutar la consulta SQL para crear la tabla
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //crear trigger
    public void insertarPalabrasInicio() {
        List<String> listaPalabras = Arrays.asList(palabrasJuego);
        Collections.shuffle(listaPalabras);
        List<String> palabrasSeleccionadas = listaPalabras.subList(0, 10);
    
        System.out.println("Palabras seleccionadas: " + palabrasSeleccionadas); // Imprimir las palabras seleccionadas
    
        try {
            // Preparar la consulta SQL para insertar las palabras en la tabla correspondiente
            String sql = "INSERT INTO palabras (palabra) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
    
            // Insertar solo las primeras 10 palabras seleccionadas
            for (String palabra : palabrasSeleccionadas) {
                statement.setString(1, palabra);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                // Manejar el error específico de entrada duplicada
                System.out.println("Error: Entrada duplicada para la palabra '" + e.getMessage().split("'")[1] + "'");
            } else if (e.getErrorCode() == 1644) {
                // Manejar el error específico de límite máximo de elementos en la tabla
                System.out.println("Se ha alcanzado el límite máximo de elementos en la tabla.");
                limite = true;
            } else {
                e.printStackTrace();
            }
        }
    }
    
    
    
    public void insertarPalabras() {
        try {
            // Preparar la consulta SQL para insertar las palabras en la tabla correspondiente
            String sql = "INSERT INTO palabras (palabra) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (String palabra : palabrasJuego) {
                statement.setString(1, palabra);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDuplicateEntryError() {
        return duplicateEntryError;
    }

    public boolean esLimite() {
        return limite;
    }

    public void agregarPalabra(String palabra) {
        try {
            // Reiniciar la variable booleana antes de la operación
            duplicateEntryError = false;
            limite = false;
            String palabraFinal = palabra.toUpperCase();

            // Preparar la consulta SQL para insertar la palabra en la tabla correspondiente
            String sql = "INSERT INTO palabras (palabra) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, palabraFinal);

            // Ejecutar la consulta SQL para insertar la palabra
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getErrorCode() == 1062) {
                // Capturar la excepción específica de entrada duplicada
                duplicateEntryError = true;
            } else {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1644) {
                // Manejar el error específico de límite máximo de elementos en la tabla
                System.out.println("Se ha alcanzado el límite máximo de elementos en la tabla.");
                limite = true;
            } else {
                e.printStackTrace();
            }

        }
    }

//vaciar las palabras de la tabla
    public void vaciarPalabras() {
        try {
            // Preparar la consulta SQL para vaciar la tabla correspondiente
            String sql = "DELETE FROM palabras";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Ejecutar la consulta SQL para vaciar la tabla
            statement.executeUpdate();
            
            
            palabras.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarPalabra(String palabra) {
        try {
            // Preparar la consulta SQL para eliminar la palabra en la tabla correspondiente
            String sql = "DELETE FROM palabras WHERE palabra = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, palabra);

            // Ejecutar la consulta SQL para eliminar la palabra
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void leerPalabras() {
        palabras.clear(); // Limpiar la lista de palabras antes de leer las nuevas
        try {
            // Preparar la consulta SQL para leer todas las palabras de la tabla correspondiente
            String sql = "SELECT palabra FROM palabras";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Leer todas las palabras y agregarlas a la lista de palabras
            while (resultSet.next()) {
                palabras.add(resultSet.getString("palabra"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> obtenerPalabras() {
        return palabras;
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 
}
/*

    DELIMITER //
    CREATE TRIGGER before_insert_mi_tabla
    BEFORE INSERT ON palabras
    FOR EACH ROW
    BEGIN
        DECLARE row_count INT;
        SELECT COUNT(*) INTO row_count FROM palabras;
        IF row_count >= 15 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Se ha alcanzado el límite máximo de elementos en la tabla';
        END IF;
    END;
    //
    DELIMITER ;

*/
