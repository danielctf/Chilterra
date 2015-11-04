package cl.a2r.sip.model;

import java.io.Serializable;

public class RegistroParto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer predioId;
	private Integer ganadoId;
	private Integer tipoPartoId;
	private Integer subTipoParto;
	private String estado;
	private String sexo;
	private Integer collarId;
	
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getPredioId() {
		return predioId;
	}
	
	public void setPredioId(Integer predioId) {
		this.predioId = predioId;
	}
	
	public Integer getGanadoId() {
		return ganadoId;
	}
	
	public void setGanadoId(Integer ganadoId) {
		this.ganadoId = ganadoId;
	}
	
	public Integer getTipoPartoId() {
		return tipoPartoId;
	}
	
	public void setTipoPartoId(Integer tipoPartoId) {
		this.tipoPartoId = tipoPartoId;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getSexo() {
		return sexo;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public Integer getCollarId() {
		return collarId;
	}
	
	public void setCollarId(Integer collarId) {
		this.collarId = collarId;
	}

	public Integer getSubTipoParto() {
		return subTipoParto;
	}

	public void setSubTipoParto(Integer subTipoParto) {
		this.subTipoParto = subTipoParto;
	}
}
