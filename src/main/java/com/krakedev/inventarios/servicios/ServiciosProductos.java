package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.ProductosBDD;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;

@Path("productos")
public class ServiciosProductos {
	@Path("buscar/{sub}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("sub")String subcadena) {
		ProductosBDD prod = new ProductosBDD();
		ArrayList<Producto> productos = null;
		
		try {
			productos = prod.buscar(subcadena);
			return Response.ok(productos).build();
		} catch(KrakeDevExcepcion e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("insertar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertar(Producto producto) {
		System.out.println(">>>>>> " + producto);
		
		ProductosBDD prod = new ProductosBDD();
		try {
			prod.insertar(producto);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
