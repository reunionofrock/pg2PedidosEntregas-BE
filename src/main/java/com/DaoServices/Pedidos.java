package com.DaoServices;

import com.ApiResponse.ApiResponse;
import com.conexion.Conexion;
import com.microsoft.azure.functions.ExecutionContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class Pedidos {

  Conexion conn = new Conexion();
  ApiResponse apiRes = new ApiResponse();
  final ExecutionContext context;

  // private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(
  // this.getClass().getName());

  public Pedidos(ExecutionContext context) {
    this.context = context;
  }

  // valorIdPedido, valorEmailPedido, valorEstadoPedido
  public String getPedidosList(
      String valorIdPedido,
      String valorEmailPedido,
      String valorEstadoPedido) {
    try {
      this.apiRes.setFunctionName("retrievePedidos");
      JSONArray arr = getPedidosQuery(
          valorIdPedido,
          valorEmailPedido,
          valorEstadoPedido);
      if (arr.length() != 0) {
        this.apiRes.setRes("Pedidos", arr);
      } else {
        this.apiRes.Errort();
        this.apiRes.setDescription("Sin datos");
      }
    } catch (Exception ex) {
      this.apiRes.Errort();
      this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
    }
    this.context.getLogger()
        .info(
            "{Request: idPedido=" +
                valorIdPedido +
                " valorEmailPedido= " +
                valorEmailPedido +
                " valorEstadoPedido= " +
                valorEstadoPedido +
                "\n" +
                "Response: " +
                this.apiRes.ParseResponse().replaceAll(" ", "") +
                "}");
    return this.apiRes.ParseResponse();
  }

  // valorIdPedido, valorEmailPedido, valorEstadoPedido
  public JSONArray getPedidosQuery(
      String valorIdPedido,
      String valorEmailPedido,
      String valorEstadoPedido) {
    JSONArray arr = new JSONArray();
    try {
      this.apiRes.setFunctionName("retrievePedidos");
      String query = "SELECT * FROM PEDIDOS";
      if (!valorIdPedido.isEmpty() ||
          !valorEmailPedido.isEmpty() ||
          !valorEstadoPedido.isEmpty()) {
        StringBuilder queryBuilder = new StringBuilder(query + " WHERE ");
        if (!valorIdPedido.isEmpty()) {
          queryBuilder
              .append("id_pedido = '")
              .append(valorIdPedido)
              .append("'");
        }
        if (!valorEmailPedido.isEmpty()) {
          if (!queryBuilder.toString().endsWith(" WHERE ")) {
            queryBuilder.append(" AND ");
          }
          queryBuilder
              .append("email_cliente = '")
              .append(valorEmailPedido)
              .append("'");
        }
        if (!valorEstadoPedido.isEmpty()) {
          if (!queryBuilder.toString().endsWith(" WHERE ")) {
            queryBuilder.append(" AND ");
          }
          queryBuilder
              .append("estado = '")
              .append(valorEstadoPedido)
              .append("'");
        }
        query = queryBuilder.toString();
      }
      this.conn.Conectar();
      ResultSet rs = this.conn.executeQuery(query);
      while (rs.next()) {
        JSONObject obj = new JSONObject();
        obj.put("idPedido", rs.getInt("id_pedido"));
        obj.put("estado", rs.getString("estado"));
        obj.put("comentario", rs.getString("comentario"));
        obj.put("fechaPedido", rs.getTimestamp("fecha_pedido"));
        obj.put("fecha_entrega", rs.getTimestamp("fecha_entrega"));
        obj.put("total", rs.getFloat("total"));
        obj.put("nombre", rs.getString("nombre_cliente"));
        obj.put("telefono", rs.getString("telefono_cliente"));
        obj.put("direccion", rs.getString("direccion_entrega"));
        obj.put("email", rs.getString("email_cliente"));
        obj.put("metodoPago", rs.getString("metodo_pago"));
        obj.put("tipoEntrega", rs.getString("tipo_entrega"));
        arr.put(obj);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e);
    } finally {
      this.conn.closeConnection();
    }
    return arr;
  }

  public String getDetallePedido(String idPedido) {
    try {
      this.apiRes.setFunctionName("retrievePedidos");
      JSONArray arr = getDetalleQuery(idPedido);
      if (arr.length() != 0) {
        this.apiRes.setRes("DetallePedido", arr);
      } else {
        this.apiRes.Errort();
        this.apiRes.setDescription("Sin datos");
      }
    } catch (Exception ex) {
      this.apiRes.Errort();
      this.apiRes.setDescription("Ha ocurrido un error al obtener datos");
    }
    this.context.getLogger()
        .info(
            "{Request: idPedido=" +
                idPedido +
                "\n" +
                "Response: " +
                this.apiRes.ParseResponse().replaceAll(" ", "") +
                "}");
    return this.apiRes.ParseResponse();
  }

  public JSONArray getDetalleQuery(String idPedido) {
    JSONArray arr = new JSONArray();
    try {
      String query = "SELECT pe.id_pedido, p.nombre, dp.cantidad, dp.subtotal, p.id_producto, p.precio \r\n" + //
          "FROM detallespedido dp\r\n" + //
          "INNER JOIN Productos p ON dp.id_producto = p.id_producto\r\n" + //
          "INNER JOIN pedidos pe ON dp.id_pedido = pe.id_pedido\r\n" + //
          "WHERE pe.id_pedido = ?";
      this.conn.Conectar();
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setInt(1, Integer.parseInt(idPedido));
      ResultSet rs = stmt.executeQuery();
      // id_pedido descripcion cantidad subtotal id_producto precio
      while (rs.next()) {
        JSONObject obj = new JSONObject();
        obj.put("idPedido", rs.getInt("id_pedido"));
        obj.put("idProducto", rs.getInt("id_producto"));
        obj.put("cantidad", rs.getInt("cantidad"));
        obj.put("subtotal", rs.getFloat("subtotal"));
        obj.put("nombre", rs.getString("nombre"));
        obj.put("precio", rs.getFloat("precio"));
        arr.put(obj);
      }
    } catch (Exception e) {
      System.err.println("Exception: " + e);
    } finally {
      this.conn.closeConnection();
    }
    return arr;
  }

  public String createPedido(String content) {
    try {
      this.apiRes.setFunctionName("retrievePedidos");
      JSONObject json = new JSONObject(content);
      // String idCliente = json.getJSONObject("Pedido").getString("ClienteID");
      String idVendedor = json.getJSONObject("Pedido").getString("VendedorID");
      String idRepartidor = json
          .getJSONObject("Pedido")
          .getString("RepartidorID");
      String estado = json.getJSONObject("Pedido").getString("Estado");
      String comentario = json.getJSONObject("Pedido").getString("Comentario");
      String total = json.getJSONObject("Pedido").getString("total");
      String nombreCliente = json
          .getJSONObject("Pedido")
          .getString("nombreCliente");
      String direccionEntrega = json
          .getJSONObject("Pedido")
          .getString("direccionEntrega");
      String emailCliente = json
          .getJSONObject("Pedido")
          .getString("emailCliente");
      String ciudadEntrega = json
          .getJSONObject("Pedido")
          .getString("ciudadEntrega");
      String telefonoCliente = json
          .getJSONObject("Pedido")
          .getString("telefonoCliente");
      String metodoPago = json.getJSONObject("Pedido").getString("metodoPago");
      String tipoEntrega = json
          .getJSONObject("Pedido")
          .getString("tipoEntrega");

      JSONArray detallePedido = json
          .getJSONObject("Pedido")
          .getJSONArray("DetallePedido");
      this.conn.Conectar();
      this.conn.startTransaction();
      // long idCLiente = generateIdCLiente();
      long idCLiente = 1;
      int firstInsert = insertPedido(
          idCLiente,
          idVendedor,
          idRepartidor,
          estado,
          comentario,
          total,
          nombreCliente,
          direccionEntrega,
          emailCliente,
          ciudadEntrega,
          telefonoCliente,
          metodoPago,
          tipoEntrega);
      if (firstInsert > 0) {
        int secondInsert = insertDetallePedido(firstInsert, detallePedido);
        if (secondInsert > 0) {

          this.apiRes.setDescription("Pedido generado de forma exitosa");
          this.apiRes.setRes("ID", firstInsert);
          this.conn.commitTransaction();
        } else {
          this.conn.rollbackTransaction();
          this.apiRes.Errort();
          this.apiRes.setDescription("Error al generar detalle");
        }
      } else {
        this.conn.rollbackTransaction();
        this.apiRes.Errort();
        this.apiRes.setDescription("Error al generar pedido");
      }
    } catch (Exception e) {
      this.conn.rollbackTransaction();
      System.err.println("Exception: " + e);
      this.apiRes.Errort();
      this.apiRes.setDescription("Error al obtener datos");
      this.conn.closeConnection();
    }
    this.conn.closeConnection();
    return this.apiRes.ParseResponse();
  }

  public long generateIdCLiente() {
    return System.nanoTime() / 1000000;
  }

  public int insertPedido(
      Long idCliente,
      String idVendedor,
      String idRepartidor,
      String estado,
      String comentario,
      String total,
      String nombreCliente,
      String direccionEntrega,
      String emailCliente,
      String ciudadEntrega,
      String telefonoCliente,
      String metodoPago,
      String tipoEntrega) {
    int response = 0;
    try {
      String query = "INSERT INTO Pedidos (id_cliente, id_vendedor, id_repartidor, estado, comentario, total, nombre_cliente, direccion_entrega, email_cliente, ciudad_entrega, telefono_cliente, metodo_pago, tipo_entrega) "
          +
          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
          "RETURNING ID_Pedido";
      // Crear una sentencia preparada
      PreparedStatement statement = this.conn.prepareStatement(query);
      // Establecer valores para los parámetros
      statement.setLong(1, idCliente);
      statement.setInt(2, Integer.parseInt(idVendedor));
      statement.setInt(3, Integer.parseInt(idRepartidor));
      statement.setString(4, estado);
      statement.setString(5, comentario);
      statement.setFloat(6, Float.parseFloat(total));
      statement.setString(7, nombreCliente);
      statement.setString(8, direccionEntrega);
      statement.setString(9, emailCliente);
      statement.setString(10, ciudadEntrega);
      statement.setString(11, telefonoCliente);
      statement.setString(12, metodoPago);
      statement.setString(13, tipoEntrega);
      // Ejecutar la inserción
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        response = rs.getInt(1); // Obtenemos el valor de la primera columna generada
        System.out.println("ID del Pedido Generado: " + response);
      } else {
        this.conn.rollbackTransaction();
        System.out.println("No se pudo obtener el ID del Pedido Generado.");
      }
    } catch (Exception e) {
      this.conn.rollbackTransaction();
      System.err.println("Exception: " + e);
      System.err.println("Ocurrio un error al completar el pedido: ");
    }
    return response;
  }

  public int insertDetallePedido(int idPedido, JSONArray arr) {
    int response = 0;
    try {
      for (int i = 0; i < arr.length(); i++) {
        String query = "INSERT INTO DetallesPedido (id_pedido, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = this.conn.prepareStatement(query);
        preparedStatement.setInt(1, idPedido);
        preparedStatement.setInt(
            2,
            arr.getJSONObject(i).getInt("id"));
        preparedStatement.setInt(
            3,
            arr.getJSONObject(i).getInt("cantidad"));
        preparedStatement.setDouble(
            4, arr.getJSONObject(i).getDouble("subtotal"));
        preparedStatement.executeUpdate();
        response++;
      }
    } catch (Exception e) {
      this.conn.rollbackTransaction();
      System.err.println("Exception: " + e);
    }
    return response;
  }

  public String modifyStatusPedido(String content) {
    try {
      this.apiRes.setFunctionName("Pedido");
      JSONObject json = new JSONObject(content);
      String status = json.getString("status");
      String id_pedido = json.getString("id_pedido");

      int rowsAffected = modifyStatusPedidoQuery(id_pedido, status);
      if (rowsAffected > 0) {
        this.apiRes.setDescription("Pedido modificado de forma exitosa");
      } else {
        this.apiRes.Errort();
        this.apiRes.setDescription("No se modifico pedido");
      }
    } catch (Exception e) {
      this.apiRes.Errort();
      this.apiRes.setDescription("Error al obtener datos");
      System.err.println("Exception: " + e);
    }
    return apiRes.ParseResponse();
  }

  public int modifyStatusPedidoQuery(String id, String status) {
    int response = 0;
    try {
      String query = "UPDATE Pedidos SET estado = ? WHERE id_pedido = ?";
      this.conn.Conectar();
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, status);
      stmt.setInt(2, Integer.parseInt(id));
      response = stmt.executeUpdate();
    } catch (Exception e) {
      System.err.println("Exception: " + e);
    }
    return response;
  }

}
