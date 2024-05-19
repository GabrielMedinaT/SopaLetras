package sopaletras;

import java.util.ArrayList;
import java.util.List;
import org.mariadb.jdbc.Connection;
import java.sql.*;

public class Modelo {

    private List<String> palabras;
    private Connection connection;

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

    public void agregarPalabra(String palabra) {
        try {
            // Preparar la consulta SQL para insertar la palabra en la tabla correspondiente
            String sql = "INSERT INTO palabras (palabra) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, palabra);

            // Ejecutar la consulta SQL para insertar la palabra
            statement.executeUpdate();
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
