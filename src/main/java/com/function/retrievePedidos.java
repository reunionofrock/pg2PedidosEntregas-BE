package com.function;

import com.DaoServices.Pedidos;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class retrievePedidos {

  @FunctionName("getPedidos")
  public HttpResponseMessage run(
      @HttpTrigger(name = "req", methods = {
          HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "Pedido/getPedidos") HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context) {
    context.getLogger().info("Ejecutando funcion para obtener pedidos.");
    context.getLogger().info("GET parameters are: " + request.getQueryParameters());
    String idPedido = request.getQueryParameters().getOrDefault("idPedido", "");
    String emailPedido = request.getQueryParameters().getOrDefault("emailPedido", "");
    String estadoPedido = request.getQueryParameters().getOrDefault("estadoPedido", "");
    // Parse query parameter
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(
            new Pedidos(context)
                .getPedidosList(idPedido, emailPedido, estadoPedido))
        .build();
  }

  @FunctionName("detallePedido")
  public HttpResponseMessage run2(
      @HttpTrigger(name = "req", methods = {
          HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "Pedido/detallePedido") HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context) {
    context.getLogger().info("Ejecutando funcion para obtener pedidos.");
    context.getLogger().info("GET parameters are: " + request.getQueryParameters());
    String idPedido = request.getQueryParameters().getOrDefault("idPedido", "");
    // Parse query parameter
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(new Pedidos(context).getDetallePedido(idPedido))
        .build();
  }

  @FunctionName("createPedido")
  public HttpResponseMessage run3(
      @HttpTrigger(name = "req", methods = {
          HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "Pedido/createPedido") HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context) {
    context.getLogger().info("Ejecutando funcion para crear pedido.");

    // Parse query parameter
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(new Pedidos(context).createPedido(request.getBody().get()))
        .build();
  }

   @FunctionName("modifyStatus")
  public HttpResponseMessage run4(
      @HttpTrigger(name = "req", methods = {
          HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "Pedido/modifypedido") 
          HttpRequestMessage<Optional<String>> request,
      final ExecutionContext context) {
    context.getLogger().info("Ejecutando funcion para crear pedido.");

    // Parse query parameter
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(new Pedidos(context).modifyStatusPedido(request.getBody().get()))
        .build();
  }
}
