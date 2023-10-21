package com.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    private static final String url = "jdbc:postgresql://pg2db.postgres.database.azure.com:5432/postgres";
    private static final String username = "dario";
    private static final String password = "Dario+2023";

    private Connection connection;

    public Conexion() {
    }

    public void Conectar() {
        try {
            // Establece la conexión
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexion con la base de datos satisfactoria");
        } catch (SQLException e) {
            System.err.println("Exception: " + e + "Base de datos no conecta");
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sqlQuery) {
        try {
            // Crea una sentencia preparada para la consulta
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            // Ejecuta la consulta
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String sqlQuery) {
        try {
            // Crea una sentencia preparada para la consulta de actualización
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            // Ejecuta la consulta de actualización (INSERT, UPDATE, DELETE, etc.)
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void closeConnection() {
        try {
            // Cierra la conexión
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String sqlQuery) throws SQLException {
        if (connection != null) {
            return connection.prepareStatement(sqlQuery);
        } else {
            // Manejo de error o lanzamiento de una excepción personalizada
            throw new SQLException("La conexión a la base de datos es nula.");
        }
    }

    public void startTransaction() {
        try {
            // Inicia una transacción
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commitTransaction() {
        try {
            // Confirma la transacción
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollbackTransaction() {
        try {
            // Realiza un rollback para deshacer la transacción
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
