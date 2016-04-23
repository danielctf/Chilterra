package cl.a2r.sip.model;

import java.io.Serializable;

public class Instancia implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer superInstanciaId;
	private Integer usuarioId;
	private Integer fundoId;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getSuperInstanciaId() {
		return superInstanciaId;
	}

	public void setSuperInstanciaId(Integer superInstanciaId) {
		this.superInstanciaId = superInstanciaId;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public Integer getFundoId() {
		return fundoId;
	}
	
	public void setFundoId(Integer fundoId) {
		this.fundoId = fundoId;
	}
	
}
