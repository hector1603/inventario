package com.krakedev.inventarios.entidades;

public class UnidadDeMedida {
	private String nombre;
	private String descipcion;
	private CategoriaUDM categoriaUDM;
	
	public UnidadDeMedida() {
		
	}
	
	public UnidadDeMedida(String nombre, String descipcion) {
		super();
		this.nombre = nombre;
		this.descipcion = descipcion;
	}

	public UnidadDeMedida(String nombre, String descipcion, CategoriaUDM categoriaUDM) {
		super();
		this.nombre = nombre;
		this.descipcion = descipcion;
		this.categoriaUDM = categoriaUDM;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescipcion() {
		return descipcion;
	}

	public void setDescipcion(String descipcion) {
		this.descipcion = descipcion;
	}

	public CategoriaUDM getCategoriaUDM() {
		return categoriaUDM;
	}

	public void setCategoriaUDM(CategoriaUDM categoriaUDM) {
		this.categoriaUDM = categoriaUDM;
	}

	@Override
	public String toString() {
		return "UnidadDeMedida [nombre=" + nombre + ", descipcion=" + descipcion + ", categoriaUDM=" + categoriaUDM
				+ "]";
	}
}
