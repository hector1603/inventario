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

import com.krakedev.inventarios.entidades.DetalleVenta;
import com.krakedev.inventarios.entidades.Venta;
import com.krakedev.inventarios.excepciones.KrakeDevExcepcion;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VentasBDD {
	
	public void crear(Venta venta) throws KrakeDevExcepcion {
	    Connection conexion = null;
	    PreparedStatement psCabecera = null;
	    PreparedStatement psDetalle = null;
	    ResultSet rsClave = null;
	    int codigoCabecera = 0;

	    Date fechaActual = new Date();
	    Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());

	    try {
	        conexion = ConexionBDD.obtenerConexion();
	        psCabecera = conexion.prepareStatement("INSERT INTO cabecera_ventas(fecha, total_sin_iva, iva, total) "
	        		+ "VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

	        psCabecera.setTimestamp(1, fechaHoraActual);
	        psCabecera.setBigDecimal(2, BigDecimal.ZERO);
	        psCabecera.setBigDecimal(3, BigDecimal.ZERO);
	        psCabecera.setBigDecimal(4, BigDecimal.ZERO);

	        psCabecera.executeUpdate();

	        rsClave = psCabecera.getGeneratedKeys();

	        if (rsClave.next()) {
	            codigoCabecera = rsClave.getInt(1);
	        }

	        ArrayList<DetalleVenta> detallesVenta = venta.getDetalles();
	        DetalleVenta detalle;

	        BigDecimal totalSinIva = BigDecimal.ZERO;
	        BigDecimal totalConIva = BigDecimal.ZERO;

	        for (int i = 0; i < detallesVenta.size(); i++) {
	            detalle = detallesVenta.get(i);
	            psDetalle = conexion.prepareStatement("INSERT INTO detalle_ventas(cabecera_ventas, producto, "
	            		+ "cantidad, precio_venta, subtotal, subtotal_con_iva) "
	            		+ "VALUES(?,?,?,?,?,?)");

	            psDetalle.setInt(1, codigoCabecera);
	            psDetalle.setInt(2, detalle.getProducto().getCodigo());
	            psDetalle.setInt(3, detalle.getCantidad());
	            psDetalle.setBigDecimal(4, detalle.getProducto().getPrecioVenta());

	            BigDecimal pv = detalle.getProducto().getPrecioVenta();
	            BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
	            BigDecimal subtotal = pv.multiply(cantidad);

	            psDetalle.setBigDecimal(5, subtotal);

	            BigDecimal subConIVA = subtotal;
	            if (detalle.getProducto().getTieneIva()) {
	                subConIVA = subtotal.multiply(new BigDecimal("1.12"));
	                psDetalle.setBigDecimal(6, subConIVA);
	                totalConIva = totalConIva.add(subConIVA);
	                totalSinIva = totalSinIva.add(subtotal);
	            } else {
	                psDetalle.setBigDecimal(6, subtotal);
	                totalSinIva = totalSinIva.add(subtotal);
	                totalConIva = totalConIva.add(subtotal);
	            }

	            psDetalle.executeUpdate();

	            String sqlHistorialStock = "INSERT INTO historial_stock(producto, fecha, referencia, cantidad) "
	            		+ "VALUES (?, ?, ?, ?)";
	            PreparedStatement psHistorialStock = conexion.prepareStatement(sqlHistorialStock, Statement.RETURN_GENERATED_KEYS);
	            psHistorialStock.setInt(1, detalle.getProducto().getCodigo());
	            psHistorialStock.setTimestamp(2, fechaHoraActual);
	            psHistorialStock.setString(3, "Venta " + codigoCabecera);
	            psHistorialStock.setInt(4, -detalle.getCantidad());
	            psHistorialStock.executeUpdate();
	        }

	        BigDecimal iva = totalConIva.subtract(totalSinIva);

	        String sqlActualizarCabecera = "UPDATE cabecera_ventas SET total_sin_iva = ?, iva = ?, total = ? "
	        		+ "WHERE codigo = ?";
	        PreparedStatement psActualizarCabecera = conexion.prepareStatement(sqlActualizarCabecera);
	        psActualizarCabecera.setBigDecimal(1, totalSinIva);
	        psActualizarCabecera.setBigDecimal(2, iva);
	        psActualizarCabecera.setBigDecimal(3, totalConIva);
	        psActualizarCabecera.setInt(4, codigoCabecera);
	        psActualizarCabecera.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new KrakeDevExcepcion("Error al registrar la venta. Detalle: " + e.getMessage());
	    } catch (KrakeDevExcepcion e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
}
