package com.function;

import com.DaoServices.Products;
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
public class retrieveProducts {

  /**
   * This function listens at endpoint "/api/HttpExample". Two ways to invoke it
   * using "curl" command in bash:
   * 1. curl -d "HTTP Body" {your host}/api/HttpExample
   * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
   */
  @FunctionName("getProducts")
  public HttpResponseMessage run(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.GET },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "products/getproducts"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para obtener productos.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Products().getProducts())
      .build();
  }

  @FunctionName("setProducts")
  public HttpResponseMessage run2(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "products/setproducts"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para ingresar productos");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Products().setProducts(request.getBody().get()))
      .build();
  }
  @FunctionName("modifyProduct")
  public HttpResponseMessage run3(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "products/modifyProduct"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para ingresar productos");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Products().modifyProduct(request.getBody().get()))
      .build();
  }
  @FunctionName("modifyPic")
  public HttpResponseMessage run4(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "products/modifyPic"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para ingresar productos");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Products().modifyPic(request.getBody().get()))
      .build();
  }
    @FunctionName("delProduct")
  public HttpResponseMessage run5(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.GET },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "products/delProduct"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    String id = request.getQueryParameters().get("id");
    context.getLogger().info("Ejecutando funcion para ingresar productos");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Products().delProduct(id))
      .build();
  }
}
