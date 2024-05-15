package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.Pedido;
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
				psDet = conexion.prepareStatement("INSERT INTO detalle_pedidos(cabecera_pedido, producto, cantidad_solicitada, subtotal, cantidad_recibida) "
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

}
