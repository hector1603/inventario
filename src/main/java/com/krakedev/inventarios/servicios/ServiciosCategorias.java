package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.CategoriasBDD;
import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;

@Path("categorias")
public class ServiciosCategorias {
	@Path("crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertar(Categoria categoria) {
		System.out.println(">>>>>> " + categoria);
		
		CategoriasBDD cat = new CategoriasBDD();
		try {
			cat.crear(categoria);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("actualizar")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizar(Categoria categoria) {
		System.out.println("Producto actualizado >>>>>> " + categoria);
		
		CategoriasBDD cat = new CategoriasBDD();
		try {
			cat.actualizar(categoria);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("mostrar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperar() throws KrakeDevExcepcion{
		CategoriasBDD cat = new CategoriasBDD();
		ArrayList<Categoria> categorias = null;
		
		try {
			categorias = cat.recuperarTodas();
			return Response.ok(categorias).build();
		} catch(KrakeDevExcepcion e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

}
