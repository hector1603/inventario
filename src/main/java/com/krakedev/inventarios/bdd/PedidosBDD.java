package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.EstadoPedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {
	public void crear(Pedido pedido) throws KrakeDevExcepcion{
		Connection conexion = null; 
		PreparedStatement psDet = null;
		ResultSet rsClave = null;
		int codigoCabecera = 0;
		
		Date fechaActual = new Date();
		java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
		try {
			conexion = ConexionBDD.obtenerConexion();
			PreparedStatement ps = conexion.prepareStatement("INSERT INTO cabecera_pedidos(proveedor, fecha, estado) "
					+ "VALUES(?,?,?);", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");
			
			ps.executeUpdate();
			
			rsClave = ps.getGeneratedKeys();
			
			if(rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}
			
			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
			DetallePedido detalle;
			
			for(int i = 0; i < detallesPedido.size(); i++) {
				detalle = detallesPedido.get(i);
				psDet = conexion.prepareStatement("INSERT INTO detalle_pedidos(cabecera_pedido, producto, "
						+ "cantidad_solicitada, subtotal, cantidad_recibida) "
						+ "VALUES(?,?,?,?,?)");
				
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, detalle.getProducto().getCodigo());
				psDet.setInt(3, detalle.getCantidadSolicitada());
				
				BigDecimal pv = detalle.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(detalle.getCantidadSolicitada());
				BigDecimal subtotal = pv.multiply(cantidad);
				
				psDet.setBigDecimal(4, subtotal);
				psDet.setInt(5, 0);
				
				psDet.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevExcepcion("Error al insertar el producto. Detalle: " + e.getMessage());
		} catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} 
	}
	
	public void recibirPedido(Pedido pedido) throws KrakeDevExcepcion {
	    Connection conexion = null; 
	    PreparedStatement psCabecera = null;
	    PreparedStatement psDetalle = null;
	    PreparedStatement psHistorialStock = null; 
	    
	    Date fechaActual = new Date();
	    Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());
	    
	    try {
	        conexion = ConexionBDD.obtenerConexion();
	        
	        psCabecera = conexion.prepareStatement("UPDATE cabecera_pedidos SET estado = ? WHERE numero = ?");
	        psCabecera.setString(1, "R");
	        psCabecera.setInt(2, pedido.getNumero());
	        psCabecera.executeUpdate();
	        
	        for (DetallePedido detalleRecibido : pedido.getDetalles()) {
	            psDetalle = conexion.prepareStatement("UPDATE detalle_pedidos SET cantidad_recibida = ?, subtotal = ? WHERE codigo = ?");
	            psDetalle.setInt(1, detalleRecibido.getCantidadRecibida());
	            
	            BigDecimal pv = detalleRecibido.getProducto().getPrecioVenta();
	            BigDecimal cantidad = new BigDecimal(detalleRecibido.getCantidadRecibida());
	            BigDecimal subtotal = pv.multiply(cantidad);
	            
	            psDetalle.setBigDecimal(2, subtotal);
	            psDetalle.setInt(3, detalleRecibido.getCodigo());
	            
	            psDetalle.executeUpdate();
	            
	            psHistorialStock = conexion.prepareStatement("INSERT INTO historial_stock (fecha, referencia, producto, cantidad) "
	            		+ "VALUES (?, ?, ?, ?)");
	            psHistorialStock.setTimestamp(1, fechaHoraActual);
	            psHistorialStock.setString(2, "Pedido " + pedido.getNumero());
	            psHistorialStock.setInt(3, detalleRecibido.getProducto().getCodigo());
	            psHistorialStock.setInt(4, detalleRecibido.getCantidadRecibida());
	            
	            psHistorialStock.executeUpdate();
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevExcepcion("Error al recibir el pedido. Detalle: " + e.getMessage());
	    } catch (KrakeDevExcepcion e) {
			e.printStackTrace();
			throw e;
		} 
	}

	
	public ArrayList<Pedido> buscarPedidoPorProveedor(String subcadena) throws KrakeDevExcepcion {
	    ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
	    Connection conexion = null;
	    PreparedStatement psPedido = null;
	    ResultSet rs = null;
	    Pedido pedido = null;
	    try {
	        conexion = ConexionBDD.obtenerConexion();
	        String sql = "SELECT prov.tipo_documento, prov.nombre AS nombre_proveedor, "
	                   + "cp.fecha, cp.estado, dp.cantidad_solicitada, dp.subtotal, dp.cantidad_recibida "
	                   + "FROM proveedores prov "
	                   + "JOIN tipo_documento td ON prov.tipo_documento = td.codigo "
	                   + "JOIN cabecera_pedidos cp ON prov.identificador = cp.proveedor "
	                   + "JOIN detalle_pedidos dp ON cp.numero = dp.cabecera_pedido "
	                   + "WHERE prov.nombre LIKE ?";
	        psPedido = conexion.prepareStatement(sql);
	        psPedido.setString(1, "%" + subcadena + "%");
	        
	        // Imprimir la consulta y el parámetro para depuración
	        System.out.println("Ejecutando consulta: " + sql);
	        System.out.println("Con parámetro: " + "%" + subcadena + "%");
	        
	        rs = psPedido.executeQuery();

	        while (rs.next()) {
	            String nombreProveedor = rs.getString("nombre_proveedor");
	            //String tipoDocumento = rs.getString("tipo_documento"); // Descomentar si es necesario
	            Date fecha = rs.getDate("fecha");
	            String estado = rs.getString("estado");
	            int cantidadSolicitada = rs.getInt("cantidad_solicitada");
	            String subtotalString = rs.getString("subtotal");
	            BigDecimal subtotal = new BigDecimal(subtotalString.replace(",", "."));
	            int cantidadRecibida = rs.getInt("cantidad_recibida");

	            Proveedor proveedor = new Proveedor();
	            proveedor.setNombre(nombreProveedor);
	            //proveedor.setTipoDocumento(tipoDocumento); // Descomentar si es necesario

	            pedido = new Pedido();
	            pedido.setProveedor(proveedor);
	            pedido.setFecha(fecha);
	            pedido.setEstado(new EstadoPedido(estado));

	            DetallePedido detalle = new DetallePedido();
	            detalle.setCantidadSolicitada(cantidadSolicitada);
	            detalle.setSubtotal(subtotal);
	            detalle.setCantidadRecibida(cantidadRecibida);

	            ArrayList<DetallePedido> detalles = new ArrayList<>();
	            detalles.add(detalle);
	            pedido.setDetalles(detalles);

	            pedidos.add(pedido);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevExcepcion("Error al consultar. Detalle: " + e.getMessage());
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psPedido != null) psPedido.close();
	            if (conexion != null) conexion.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new KrakeDevExcepcion("Error al cerrar la conexión. Detalle: " + e.getMessage());
	        }
	    }
	    return pedidos;
	}

	
}
