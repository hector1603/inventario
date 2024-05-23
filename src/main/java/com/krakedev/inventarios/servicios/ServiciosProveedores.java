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

import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;

@Path("proveedores")
public class ServiciosProveedores{
	@Path("buscar/{sub}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("sub")String subcadena) {
		ProveedoresBDD provBDD = new ProveedoresBDD();
		ArrayList<Proveedor> proveedores = null;
		
		try {
			proveedores = provBDD.buscar(subcadena);
			return Response.ok(proveedores).build();
		} catch(KrakeDevExcepcion e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("buscarprov/{sub}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarProv(@PathParam("sub")String subcadena) {
		ProveedoresBDD provBDD = new ProveedoresBDD();
		ArrayList<Proveedor> proveedores = null;
		
		try {
			proveedores = provBDD.buscarPorIdentificador(subcadena);
			return Response.ok(proveedores).build();
		} catch(KrakeDevExcepcion e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("insertar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertar(Proveedor proveedor) {
		System.out.println(">>>>>> " + proveedor);
		
		ProveedoresBDD prov = new ProveedoresBDD();
		try {
			prov.insertar(proveedor);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
