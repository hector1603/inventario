package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProveedoresBDD {
	public ArrayList<Proveedor> buscar(String subcadena) throws KrakeDevExcepcion{
		ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
		Connection conexion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Proveedor proveedor = null;
		try {
			conexion = ConexionBDD.obtenerConexion();
			ps = conexion.prepareStatement("SELECT identificador, tipo_documento, nombre, telefono, correo, direccion "
					+ "FROM proveedores "
					+ "WHERE UPPER(nombre) LIKE ?");
			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String identificador = rs.getString("identificador");
				String tipoDocumento = rs.getString("tipo_documento");
				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				
				proveedor = new Proveedor(identificador, tipoDocumento, nombre, telefono, correo, direccion);
				proveedores.add(proveedor);
			}
		} catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al consultar. Detalle: " + e.getMessage());
		}
		return proveedores;
	}
}
