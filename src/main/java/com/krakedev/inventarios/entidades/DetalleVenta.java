package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class DetalleVenta {
	private int codigo;
	private Venta venta;
	private Producto producto;
	private int cantidad;
	private Producto precioVenta;
	private BigDecimal subtotal;
	private BigDecimal subtotalConIva;
	
	public DetalleVenta() {
		
	}

	public DetalleVenta(int codigo, Venta venta, Producto producto, int cantidad, Producto precioVenta,
			BigDecimal subtotal, BigDecimal subtotalConIva) {
		super();
		this.codigo = codigo;
		this.venta = venta;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioVenta = precioVenta;
		this.subtotal = subtotal;
		this.subtotalConIva = subtotalConIva;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Producto getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Producto precioVenta) {
		this.precioVenta = precioVenta;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getSubtotalConIva() {
		return subtotalConIva;
	}

	public void setSubtotalConIva(BigDecimal subtotalConIva) {
		this.subtotalConIva = subtotalConIva;
	}
	
}
