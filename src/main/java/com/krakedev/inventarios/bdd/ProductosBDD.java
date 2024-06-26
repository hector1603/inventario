package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.UnidadDeMedida;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProductosBDD {
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevExcepcion{
		ArrayList<Producto> productos = new ArrayList<Producto>();
		Connection conexion = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto producto = null;
		try {
			conexion = ConexionBDD.obtenerConexion();
			ps = conexion.prepareStatement("SELECT prod.codigo_prod, prod.nombre AS nombre_producto, udm.nombre AS nombre_udm, "
					+ "udm.descripcion AS descripcion_udm, CAST(prod.precio_venta AS DECIMAL(5,2)), prod.tiene_iva, "
					+ "CAST(prod.coste AS DECiMAL(5,2)), prod.categoria, cat.nombre AS nombre_categoria, stock "
					+ "FROM productos prod, unidades_medida udm, categorias cat "
					+ "WHERE prod.udm = udm.nombre "
					+ "AND prod.categoria = cat.codigo_cat "
					+ "AND UPPER(prod.nombre) LIKE ?");
			
			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int codigoProd = rs.getInt("codigo_prod");
				String nombreProd = rs.getString("nombre_producto");
				String nombreUDM = rs.getString("nombre_udm");
				String descripcionUDM = rs.getString("descripcion_udm");
				BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
				boolean tieneIVA = rs.getBoolean("tiene_iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int categoria = rs.getInt("categoria");
				String nombreCategoria = rs.getString("nombre_categoria");
				int stock = rs.getInt("stock");
				
				UnidadDeMedida udm = new UnidadDeMedida(nombreUDM, descripcionUDM);
				
				Categoria cat = new Categoria(categoria, nombreCategoria);
				
				producto = new Producto();
				producto.setCodigo(codigoProd);
				producto.setNombre(nombreProd);
				producto.setUdm(udm);
				producto.setPrecioVenta(precioVenta);
				producto.setTieneIva(tieneIVA);
				producto.setCoste(coste);
				producto.setCategoria(cat);
				producto.setStock(stock);
				
				productos.add(producto);
			}
		} catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al consultar. Detalle: " + e.getMessage());
		}
		return productos;
	}
	
	
	public Producto buscarPorIdentificador(String identificador) throws KrakeDevExcepcion {
	    Connection conexion = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    Producto producto = null;
	    try {
	        conexion = ConexionBDD.obtenerConexion();
	        String sql = "SELECT prod.codigo_prod, prod.nombre AS nombre_producto, "
	                   + "udm.nombre AS nombre_udm, udm.descripcion AS descripcion_udm, "
	                   + "CAST(prod.precio_venta AS DECIMAL(5,2)), prod.tiene_iva, "
	                   + "CAST(prod.coste AS DECIMAL(5,2)), prod.categoria, "
	                   + "cat.nombre AS nombre_categoria, prod.stock "
	                   + "FROM productos prod "
	                   + "JOIN unidades_medida udm ON prod.udm = udm.nombre "
	                   + "JOIN categorias cat ON prod.categoria = cat.codigo_cat "
	                   + "WHERE prod.codigo_prod = ?::integer";
	        ps = conexion.prepareStatement(sql);
	        ps.setString(1, identificador);

	        System.out.println("Ejecutando consulta: " + sql);
	        System.out.println("Con parámetro: " + identificador);

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            int codigoProd = rs.getInt("codigo_prod");
	            String nombreProd = rs.getString("nombre_producto");
	            String nombreUDM = rs.getString("nombre_udm");
	            String descripcionUDM = rs.getString("descripcion_udm");
	            BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
	            boolean tieneIVA = rs.getBoolean("tiene_iva");
	            BigDecimal coste = rs.getBigDecimal("coste");
	            int categoria = rs.getInt("categoria");
	            String nombreCategoria = rs.getString("nombre_categoria");
	            int stock = rs.getInt("stock");

	            UnidadDeMedida udm = new UnidadDeMedida(nombreUDM, descripcionUDM);
	            Categoria cat = new Categoria(categoria, nombreCategoria);

	            producto = new Producto();
	            producto.setCodigo(codigoProd);
	            producto.setNombre(nombreProd);
	            producto.setUdm(udm);
	            producto.setPrecioVenta(precioVenta);
	            producto.setTieneIva(tieneIVA);
	            producto.setCoste(coste);
	            producto.setCategoria(cat);
	            producto.setStock(stock);
	        }
	    } catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al consultar. Detalle: " + e.getMessage());
		}
	    return producto;
	}

	
	public void insertar(Producto producto) throws Exception{
		Connection conexion = null; 
		
		try {
			conexion = ConexionBDD.obtenerConexion();
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO productos(nombre, udm, precio_venta, tiene_iva, "
					+ "coste, categoria, stock) "
					+ "VALUES(?,?,?,?,?,?,?)");
			
			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getUdm().getNombre());
			ps.setBigDecimal(3, producto.getPrecioVenta());
			ps.setBoolean(4, producto.getTieneIva());
			ps.setBigDecimal(5, producto.getCoste());
			ps.setInt(6, producto.getCategoria().getCodigo());
			ps.setInt(7, producto.getStock());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al insertar el producto");
		} catch (Exception e) {
			throw e;
		} finally {
			if(conexion != null) {
				conexion.close();
			}
		}
	}

	public void actualizar(Producto producto) throws KrakeDevExcepcion {
	    Connection conexion = null; 
	    PreparedStatement psProducto = null; 
	    
	    try {
	        conexion = ConexionBDD.obtenerConexion();
	        
	        psProducto = conexion.prepareStatement("UPDATE productos SET nombre = ?, udm = ?, precio_venta = ?,"
	        		+ "tiene_iva = ?, coste = ?, categoria = ?, stock = ? WHERE codigo_prod = ?");
	        psProducto.setString(1, producto.getNombre());
	        psProducto.setString(2, producto.getUdm().getNombre());
	        psProducto.setBigDecimal(3, producto.getPrecioVenta());
	        psProducto.setBoolean(4, producto.getTieneIva());
	        psProducto.setBigDecimal(5, producto.getCoste());
	        psProducto.setInt(6, producto.getCategoria().getCodigo());
	        psProducto.setInt(7, producto.getStock());

	        psProducto.setInt(8, producto.getCodigo());
			
	        psProducto.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevExcepcion("Error al actualizar el producto. Detalle: " + e.getMessage());
	    } catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} 
	}

}
