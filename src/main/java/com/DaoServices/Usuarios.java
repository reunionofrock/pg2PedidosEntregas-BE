package com.DaoServices;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

import com.ApiResponse.ApiResponse;
import com.conexion.Conexion;
import com.microsoft.azure.functions.ExecutionContext;

public class Usuarios {
    Conexion conn = new Conexion();
    ApiResponse apiRes = new ApiResponse();
    final ExecutionContext context;

    public Usuarios(ExecutionContext context) {
        this.context = context;
    }

    public String login(String content) {
        try {
            this.apiRes.setFunctionName("login");
            JSONObject json = new JSONObject(content);
            String user = json.getJSONObject("login").getString("user");
            String pass = json.getJSONObject("login").getString("pass");
            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                this.apiRes.Errort();
                this.apiRes.setDescription("Credenciales invalidas");
            } else {
                JSONObject userQuery = getUserQuery(user, pass);
                if (userQuery.toString().equals("{}")) {
                    this.apiRes.Errort();
                    this.apiRes.setDescription("Usuario no existe");
                } else {
                    this.apiRes.setDescription("Inicio de sesion exitoso");
                    this.apiRes.setRes("usuario", userQuery);
                }
            }

        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Error en el servidor");
        }
        return this.apiRes.ParseResponse();
    }

    JSONObject getUserQuery(String user, String pass) {
        JSONObject json = new JSONObject();
        try {
            String query = "select id_usuario, nombre, correoelectronico, tipo  from usuarios where correoelectronico = ? and contraseña = ?";
            this.conn.Conectar();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                json.put("id", rs.getInt("id_usuario"));
                json.put("nombre", rs.getString("nombre"));
                json.put("email", rs.getString("correoelectronico"));
                json.put("rol", rs.getString("tipo"));
            }
        } catch (Exception e) {
            System.err.println("Error en la consulta: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return json;
    }

    public String newUser(String content) {
        try {
            this.apiRes.setFunctionName("retrieveUsuarios");
            JSONObject json = new JSONObject(content);
            String nombre = json.getJSONObject("retrieveUsuarios").getString("Nombres");
            String apellidos = json.getJSONObject("retrieveUsuarios").getString("Apellidos");
            String nombreCompleto = nombre + " " + apellidos;
            String email = json.getJSONObject("retrieveUsuarios").getString("Email");
            String pass = json.getJSONObject("retrieveUsuarios").getString("Pass");
            String tipoUsuario = json.getJSONObject("retrieveUsuarios").getString("TipoUsuario");
            JSONObject user = getUserQuery(email);
            if (user.toString().equals("{}")) {
                int rowsAffected = queryUserInsert(nombreCompleto, email, pass, tipoUsuario);
                if (rowsAffected > 0) {
                    this.apiRes.setDescription("Usuario creado de forma exitosa");
                } else {
                    this.apiRes.Errort();
                    this.apiRes.setDescription("Error al crear usuario");
                }
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Correo electronico ya registrado");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public String getUser(String email) {
        try {
            this.apiRes.setFunctionName("retrieveUsuarios");
            JSONObject user = getUserQuery(email);
            if (!user.toString().equals("{}")) {
                this.apiRes.setDescription("Usuario encontrado");
                this.apiRes.setRes("Usuario", user);
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("No se encontro usuario");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public String modifyUser(String content) {
        try {
            this.apiRes.setFunctionName("retrieveUsuarios");
            JSONObject json = new JSONObject(content);
            String email = json.getJSONObject("retrieveUsuarios").optString("Email");
            String nombre = json.getJSONObject("retrieveUsuarios").optString("Nombre");
            String tipo = json.getJSONObject("retrieveUsuarios").optString("Tipo");

            JSONObject user = getUserQuery(email);
            if (user.toString().equals("{}")) {
                int rowsAffected = queryUserModify(email, nombre, tipo);
                if (rowsAffected > 0) {
                    this.apiRes.setDescription("Usuario modificado de forma exitosa");
                } else {
                    this.apiRes.Errort();
                    this.apiRes.setDescription("Error al modificar usuario");
                }
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Correo");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public String modifyUserP(String content) {
        try {
            this.apiRes.setFunctionName("retrieveUsuarios");
            JSONObject json = new JSONObject(content);
            String pass = json.getJSONObject("retrieveUsuarios").optString("Password");
            String email = json.getJSONObject("retrieveUsuarios").optString("Email");

            JSONObject user = getUserQuery(email);
            if (user.toString().equals("{}")) {
                int rowsAffected = queryUserPModify(email, pass);
                if (rowsAffected > 0) {
                    this.apiRes.setDescription("Usuario modificado de forma exitosa");
                } else {
                    this.apiRes.Errort();
                    this.apiRes.setDescription("Error al modificar usuario");
                }
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Correo");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public int queryUserPModify(String email, String pass) {
        int response = 0;
        try {
            this.conn.Conectar();
            String query = "UPDATE usuarios SET contraseña = ? WHERE correoelectronico = ?";
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setString(1, pass);
            ps.setString(2, email);
            response = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
        return response;
    }

    public int queryUserModify(String email, String nombre, String tipo) {
        int response = 0;
        try {
            this.conn.Conectar();
            String query = "UPDATE usuarios SET nombre = ?, correoelectronico = ?, tipo) VALUES(?,?,?)";
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, tipo);
            response = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
        return response;
    }

    public String delUser(String email) {
        try {
            JSONObject json = getUserQuery(email);
            if (!json.toString().equals("{}")) {
                int rowsAffected = delUserQuery(email);
                if (rowsAffected > 0) {
                    this.apiRes.setDescription("Usuario eliminado con exito");
                } else {
                    this.apiRes.Errort();
                    this.apiRes.setDescription("Error al borrar usuario");
                }
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Usuario no encontrado");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public int queryUserInsert(String nombre, String email, String pass, String tipoUsuario) {
        int response = 0;
        try {
            this.conn.Conectar();
            String query = "INSERT INTO usuarios (nombre, correoelectronico, contraseña, tipo) VALUES(?,?,?,?)";
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.setString(4, tipoUsuario);
            ps.executeUpdate();
            response++;
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
        return response;
    }

    public JSONObject getUserQuery(String email) {
        JSONObject response = new JSONObject();
        try {
            String query = "SELECT id_usuario, nombre, tipo FROM usuarios where correoelectronico='" + email + "'";
            this.conn.Conectar();
            ResultSet rs = this.conn.executeQuery(query);
            if (rs.next()) {
                response.put("Id", rs.getInt("id_usuario"));
                response.put("Nombre", rs.getInt("nombre"));
                response.put("Email", rs.getInt("correoelectronico"));
                response.put("Tipo", rs.getInt("tipo"));
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return response;
    }

    public int delUserQuery(String email) {
        int response = 0;
        try {
            String query = "DELETE FROM usuarios WHERE correoelectronico=?";
            this.conn.Conectar();
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setString(1, email);
            ps.executeQuery();
            response++;
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return response;
    }
}
