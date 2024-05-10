package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDocumento;
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
			ps = conexion.prepareStatement("SELECT identificador, tipo_documento, descripcion, nombre, telefono, correo, direccion "
					+ "FROM proveedores prov, tipo_documento td "
					+ "WHERE prov.tipo_documento = td.codigo "
					+ "AND UPPER(nombre) LIKE ?");
			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				String identificador = rs.getString("identificador");
				String tipoDocumento = rs.getString("tipo_documento");
				String descripcion = rs.getString("descripcion");
				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				TipoDocumento td = new TipoDocumento(tipoDocumento, descripcion);
				
				proveedor = new Proveedor(identificador, td, nombre, telefono, correo, direccion);
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
	
	public void insertar(Proveedor proveedor) throws Exception{
		Connection conexion = null; 
		
		try {
			conexion = ConexionBDD.obtenerConexion();
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO proveedores(identificador, tipo_documento, nombre, telefono, correo, direccion) "
					+ "VALUES(?,?,?,?,?,?)");
			
			ps.setString(1, proveedor.getIdentificador());
			ps.setString(2, proveedor.getTipoDocumento().getCodigo());
			ps.setString(3, proveedor.getNombre());
			ps.setString(4, proveedor.getTelefono());
			ps.setString(5, proveedor.getCorreo());
			ps.setString(6, proveedor.getDireccion());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al insertar el cliente");
		} catch (Exception e) {
			throw e;
		} finally {
			if(conexion != null) {
				conexion.close();
			}
		}
	}
	
}
