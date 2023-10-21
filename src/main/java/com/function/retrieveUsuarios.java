package com.function;

import com.DaoServices.Usuarios;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Optional;

public class retrieveUsuarios {

   @FunctionName("login")
  public HttpResponseMessage login(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "user/login"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion de login.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Usuarios(context).login(request.getBody().get()))
      .build();
  }

  @FunctionName("getuser")
  public HttpResponseMessage run(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.GET },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "user/getuser"
    ) HttpRequestMessage<Optional<String>> request,
    @BindingName("email") String email,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para obtener productos.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Usuarios(context).getUser(email))
      .build();
  }

  //////////////////////esto falta
  @FunctionName("newuser")
  public HttpResponseMessage run2(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "user/newuser"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para ingresar usuarios.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Usuarios(context).newUser(request.getBody().get()))
      .build();
  }
  
  @FunctionName("modifyuser")
  public HttpResponseMessage run3(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "user/modifyuser"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para obtener productos.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Usuarios(context).modifyUser(request.getBody().get()))
      .build();
  }
  @FunctionName("modifyuserp")
  public HttpResponseMessage run4(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.POST },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "user/modifyuserp"
    ) HttpRequestMessage<Optional<String>> request,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para obtener productos.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Usuarios(context).modifyUserP(request.getBody().get()))
      .build();
  }
  @FunctionName("deluser")
  public HttpResponseMessage run5(
    @HttpTrigger(
      name = "req",
      methods = { HttpMethod.DELETE },
      authLevel = AuthorizationLevel.ANONYMOUS,
      route = "user/deluser"
    ) HttpRequestMessage<Optional<String>> request,
    @BindingName("email") String email,
    final ExecutionContext context
  ) {
    context.getLogger().info("Ejecutando funcion para eliminar usuario.");

    // Parse query parameter
    return request
      .createResponseBuilder(HttpStatus.OK)
      .body(new Usuarios(context).delUser(email))
      .build();
  }
}
