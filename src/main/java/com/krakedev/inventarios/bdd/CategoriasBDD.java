package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;
import com.krakedev.inventarios.utils.ConexionBDD;

public class CategoriasBDD {
	public void crear(Categoria categoria) throws KrakeDevExcepcion{
		Connection conexion = null;
		PreparedStatement psCategoria = null;
		
		try {
			conexion = ConexionBDD.obtenerConexion();
			psCategoria = conexion.prepareStatement("INSERT INTO categorias(nombre, categoria_padre) "
					+ "VALUES(?,?)");
			psCategoria.setString(1, categoria.getNombre());
			psCategoria.setInt(2, categoria.getCategoriaPadre().getCodigo());
			
			psCategoria.executeUpdate();			
		} catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevExcepcion("Error al insertar la categoria. Detalle: " + e.getMessage());
	    } catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void actualizar(Categoria categoria) throws KrakeDevExcepcion{
		Connection conexion = null;
		PreparedStatement psCategoria = null;
		
		try {
			conexion = ConexionBDD.obtenerConexion();
			psCategoria = conexion.prepareStatement("UPDATE categorias SET nombre = ?, categoria_padre = ? "
					+ "WHERE codigo_cat = ?");
			psCategoria.setString(1, categoria.getNombre());
			psCategoria.setInt(2, categoria.getCategoriaPadre().getCodigo());
			psCategoria.setInt(3, categoria.getCodigo());
			
			psCategoria.executeUpdate()	;		
		} catch(SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al actualizar la categoria. Detalle: "+ e.getMessage());
		}
	}
	
	
	public ArrayList<Categoria> recuperarTodas() throws KrakeDevExcepcion{
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		Connection conexion = null;
		PreparedStatement psCategoria = null;
		ResultSet rsCategoria = null;
		Categoria cat = null;
		
		try {
			conexion = ConexionBDD.obtenerConexion();
			psCategoria = conexion.prepareStatement("SELECT codigo_cat, nombre, categoria_padre FROM categorias");
			rsCategoria = psCategoria.executeQuery();
			
			while(rsCategoria.next()) {
				int codigo = rsCategoria.getInt("codigo_cat");
				String nombre = rsCategoria.getString("nombre");
				int  categoriaPadre = rsCategoria.getInt("categoria_padre");
				cat = new Categoria(codigo, nombre, new Categoria(categoriaPadre));
				
				categorias.add(cat);
			}
		} catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al consultar. Detalle: " + e.getMessage());
		}
		
		return categorias;
	}
	
}
