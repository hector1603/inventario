package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.TiposDocumentosBDD;
import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;

@Path("tiposdocumento")
public class ServiciosTiposDocumentos {
	@Path("recuperar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperar() throws KrakeDevExcepcion{
		TiposDocumentosBDD doc = new TiposDocumentosBDD();
		ArrayList<TipoDocumento> documentos = null;
		
		try {
			documentos = doc.recuperarTodos();
			return Response.ok(documentos).build();
		} catch(KrakeDevExcepcion e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
