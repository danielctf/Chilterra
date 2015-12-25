package cl.a2r.sip.model;

import java.io.Serializable;

public class GanadoLogs implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Integer ganadoId;
	private Integer diio;
	private String eid;
	private Integer logId;
	
	public Integer getGanadoId() {
		return ganadoId;
	}
	
	public void setGanadoId(Integer ganadoId) {
		this.ganadoId = ganadoId;
	}
	
	public Integer getDiio() {
		return diio;
	}
	
	public void setDiio(Integer diio) {
		this.diio = diio;
	}
	
	public Integer getLogId() {
		return logId;
	}
	
	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	
}
