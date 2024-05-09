package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;
import com.krakedev.inventarios.utils.ConexionBDD;

public class TiposDocumentosBDD {
	public ArrayList<TipoDocumento> recuperarTodos() throws KrakeDevExcepcion{
		ArrayList<TipoDocumento> documentos = new ArrayList<TipoDocumento>();
		Connection conexion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TipoDocumento tipoDoc = null;
		try {
			conexion = ConexionBDD.obtenerConexion();
			ps = conexion.prepareStatement("SELECT codigo, descripcion FROM tipo_documento");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String codigo = rs.getString("codigo");
				String descripcion = rs.getString("descripcion");
				tipoDoc = new TipoDocumento(codigo, descripcion);
				documentos.add(tipoDoc);
			}
		} catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al consultar. Detalle: " + e.getMessage());
		}
		
		return documentos;
	}
}
