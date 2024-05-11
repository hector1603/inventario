package com.krakedev.inventarios.entidades;

import java.math.BigDecimal;

public class Producto {
	private int codigo;
	private String nombre;
	private UnidadDeMedida udm;
	private BigDecimal precioVenta;
	private boolean tieneIva;
	private BigDecimal coste;
	private Categoria categoria;
	private int stock;
	
	public Producto() {
		
	}

	public Producto(int codigo, String nombre, UnidadDeMedida udm, BigDecimal precioVenta, boolean tieneIva,
			BigDecimal coste, Categoria categoria, int stock) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.udm = udm;
		this.precioVenta = precioVenta;
		this.tieneIva = tieneIva;
		this.coste = coste;
		this.categoria = categoria;
		this.stock = stock;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public UnidadDeMedida getUdm() {
		return udm;
	}

	public void setUdm(UnidadDeMedida udm) {
		this.udm = udm;
	}

	public BigDecimal getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(BigDecimal precioVenta) {
		this.precioVenta = precioVenta;
	}

	public boolean getTieneIva() {
		return tieneIva;
	}

	public void setTieneIva(boolean tieneIva) {
		this.tieneIva = tieneIva;
	}

	public BigDecimal getCoste() {
		return coste;
	}

	public void setCoste(BigDecimal coste) {
		this.coste = coste;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Producto [codigo=" + codigo + ", nombre=" + nombre + ", udm=" + udm + ", precioVenta=" + precioVenta
				+ ", tieneIva=" + tieneIva + ", coste=" + coste + ", categoria=" + categoria + ", stock=" + stock + "]";
	}
	
}
