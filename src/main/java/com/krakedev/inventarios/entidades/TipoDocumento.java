package com.krakedev.inventarios.entidades;

public class TipoDocumento {
	private String codigo;
	private String descripcion;
	
	public TipoDocumento() {
		
	}
	public TipoDocumento(String codigo, String descripcion) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "TipoDocumentos [codigo=" + codigo + ", descripcion=" + descripcion + "]";
	}
	
}
