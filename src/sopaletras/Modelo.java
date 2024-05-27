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
    // Array de palabras predefinidas para el juego
    private String[] palabrasJuego = {
        "CASA", "PERRO", "GATO", "COCHE", "MOTO", "BICI", "MESA", "SILLA", "CAMA", 
        "PUERTA", "VENTANA", "TELEVISOR", "ORDENADOR", "LAPIZ", "BOLIGRAFO", "LIBRO", 
        "CUADERNO", "MOVIL", "RELOJ", "PANTALLA", "COCINA", "BAÑO", "SALON", 
        "DORMITORIO", "JARDIN", "PISCINA", "GARAJE", "TRASTERO", "TERRAZA", "BALCON", 
        "AZOTEA", "PATIO", "PASILLO", "SOTANO", "BUHARDILLA"
    };

    public Modelo() {
        palabras = new ArrayList<>(); // Inicializar la lista de palabras

        try {
            // Cargar el controlador JDBC
            Class.forName("org.mariadb.jdbc.Driver");

            // Establecer la conexión con la base de datos
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
    
 public void crearTrigger() {
        String dropTriggerSql = "DROP TRIGGER IF EXISTS limitar_numero_palabras";
        String createTriggerSql = 
            "CREATE TRIGGER limitar_numero_palabras " +
            "BEFORE INSERT ON palabras " +
            "FOR EACH ROW " +
            "BEGIN " +
            "DECLARE total_palabras INT; " +
            "SELECT COUNT(*) INTO total_palabras FROM palabras; " +
            "IF total_palabras >= 15 THEN " +
            "SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se permiten más de 15 palabras en la tabla'; " +
            "END IF; " +
            "END";

        try (Statement stmt = connection.createStatement()) {
            // Eliminar el trigger existente si existe
            stmt.execute(dropTriggerSql);
            // Crear el nuevo trigger
            stmt.execute(createTriggerSql);
        } catch (SQLException e) {
            System.err.println("Error al crear el trigger: " + e.getMessage());
        }
    }

    public void insertarPalabrasInicio() {
        List<String> listaPalabras = Arrays.asList(palabrasJuego);
        Collections.shuffle(listaPalabras);
        List<String> palabrasSeleccionadas = listaPalabras.subList(0, 6);

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

    // Vaciar las palabras de la tabla
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

    public List<String> obtenerPalabrasParaSopa() {
        leerPalabras();
        if (palabras.size() > 10) {
            return palabras.subList(0, 10); // Devuelve las primeras 10 palabras
        } else {
            return new ArrayList<>(palabras); // Devuelve todas las palabras si hay menos de 10
        }
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
