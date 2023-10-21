package com.DaoServices;

import com.ApiResponse.ApiResponse;
import com.conexion.Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Products {

    Conexion conn = new Conexion();
    ApiResponse apiRes = new ApiResponse();
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(
            this.getClass().getName());

    public Products() {
    }

    public String getProducts() throws JSONException {
        this.apiRes.setFunctionName("Products");
        try {
            JSONArray queryList = getQueryList();
            if (queryList.length() != 0) {
                this.apiRes.setRes("Productos", queryList);
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Sin datos");
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            this.apiRes.Errort();
            this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
        }
        // return this.apiRes.ParseResponse();
        System.out.println(this.apiRes.ParseResponse());
        return this.apiRes.ParseResponse();
    }

    public JSONArray getQueryList() {
        JSONArray queryArray = new JSONArray();
        try {
            this.conn.Conectar();
            String query = "SELECT id_producto, nombre, descripcion, precio, disponible, COALESCE(foto::text, 'No foto') AS foto FROM Productos";
            ResultSet rs = this.conn.executeQuery(query);

            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("id", rs.getInt("id_Producto"));
                json.put("nombre", rs.getString("nombre"));
                json.put("descrripcion", rs.getString("descripcion"));
                json.put("precio", rs.getFloat("precio"));
                json.put("disponible", rs.getBoolean("disponible"));
                json.put("imagen", rs.getString("foto"));
                queryArray.put(json);
            }
        } catch (Exception ex) {
            System.err.println("Exception: " + ex);
            logger.error("Exception: ", ex);
        } finally {
            this.conn.closeConnection();
        }
        return queryArray;
    }

    public String setProducts(String content) {
        this.apiRes.setFunctionName("Products");
        try {
            JSONObject json = new JSONObject(content);
            String nombre = json.getJSONObject("Products").getString("Nombre");
            String descripcion = json.getJSONObject("Products").getString("Descripcion");
            String precio = json.getJSONObject("Products").getString("Precio");
            boolean estado = json.getJSONObject("Products").getBoolean("Estado");
            String fotoByte = json.getJSONObject("Products").getString("Foto");
            // byte[] byteFoto = stringToByteArray(fotoByte);
            int rowsAffected = setProductQuery(nombre, descripcion, precio, estado, fotoByte);
            if (rowsAffected > 0) {
                this.apiRes.setDescription("Producto ingresado con exito");
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Error en la insercion de producto");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public int setProductQuery(String nombre, String descripcion, String precio, boolean estado, String foto) {
        int response = 0;
        try {
            String query = "INSERT INTO productos (nombre, descripcion, precio, disponible, foto) VALUES(?,?,?,?,?)";
            this.conn.Conectar();
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setFloat(3, Float.parseFloat(precio));
            ps.setBoolean(4, estado);
            ps.setString(5, foto);
            response = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return response;
    }

    public byte[] stringToByteArray(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            return null;
        }
    }

    public String modifyPic(String content) {
        this.apiRes.setFunctionName("Products");
        try {
            JSONObject json = new JSONObject(content);
            String id = json.getJSONObject("Products").getString("idProducto");
            String fotoByte = json.getJSONObject("Products").getString("Foto");
            byte[] byteFoto = stringToByteArray(fotoByte);
            if (byteFoto == null) {
                this.apiRes.Errort();
                this.apiRes.setDescription("Error imagen corrupta");
            } else {
                int rowsAffected = modifyPicQuery(id, byteFoto);
                if (rowsAffected > 0) {
                    this.apiRes.setDescription("Imagen actualizada con exito");
                } else {
                    this.apiRes.Errort();
                    this.apiRes.setDescription("Error en al actualizar la imagen");
                }
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public int modifyPicQuery(String id, byte[] foto) {
        int response = 0;
        try {
            String query = "UPDATE productos SET foto = ? WHERE id_producto = ?";
            this.conn.Conectar();
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setInt(2, Integer.parseInt(id));
            ps.setBytes(1, foto);
            response = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return response;
    }

    public String modifyProduct(String content) {
        this.apiRes.setFunctionName("Products");
        try {
            JSONObject json = new JSONObject(content);
            String id = json.getJSONObject("Products").getString("idProducto");
            String nombre = json.getJSONObject("Products").getString("Nombre");
            String descripcion = json.getJSONObject("Products").getString("Descripcion");
            String precio = json.getJSONObject("Products").getString("Precio");
            boolean estado = json.getJSONObject("Products").getBoolean("Estado");
            String imagen = json.getJSONObject("Products").getString("Imagen");
            int rowsAffected = modifyProductQuery(id, nombre, descripcion, precio, estado, imagen);
            if (rowsAffected > 0) {
                this.apiRes.setDescription("Producto actualizado con exito");
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Error en la actualizacion de producto");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public int modifyProductQuery(String id, String nombre, String descripcion, String precio, boolean estado, String imagen) {
        int response = 0;
        try {
            String query = "UPDATE productos SET nombre = ?, descripcion = ?, precio= ?, disponible =?, foto= ? WHERE id_producto = ?";
            this.conn.Conectar();
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setFloat(3, Float.parseFloat(precio));
            ps.setBoolean(4, estado);
            ps.setString(5, imagen);
            ps.setInt(6, Integer.parseInt(id));
            response = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return response;
    }

    public String delProduct(String id) {

        
        this.apiRes.setFunctionName("Products");
        try {
            // JSONObject json = new JSONObject(content);
            // String id = json.getJSONObject("Products").getString("idProducto");
            int rowsAffected = delProductcQuery(id);
            if (rowsAffected > 0) {
                this.apiRes.setDescription("Producto eliminado con exito");
            } else {
                this.apiRes.Errort();
                this.apiRes.setDescription("Error al eliminar producto");
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            this.apiRes.Errort();
            this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
        }
        return this.apiRes.ParseResponse();
    }

    public int delProductcQuery(String id) {
        int response = 0;
        try {
            String query = "DELETE FROM productos WHERE id_producto = ?";
            this.conn.Conectar();
            PreparedStatement ps = this.conn.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(id));
            response = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        } finally {
            this.conn.closeConnection();
        }
        return response;
    }
}
