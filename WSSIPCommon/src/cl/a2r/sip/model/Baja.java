package cl.a2r.sip.model;

import java.io.Serializable;

public class Baja implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer userId;
	private Integer predioId;
	private Integer ganadoId;
	private Integer motivoId;
	private Integer causaId;
	
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
	
	public Integer getMotivoId() {
		return motivoId;
	}
	
	public void setMotivoId(Integer motivoId) {
		this.motivoId = motivoId;
	}
	
	public Integer getCausaId() {
		return causaId;
	}
	
	public void setCausaId(Integer causaId) {
		this.causaId = causaId;
	}
	
}
