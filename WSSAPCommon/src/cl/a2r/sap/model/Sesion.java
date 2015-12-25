package cl.a2r.sap.model;

import java.io.Serializable;

public class Sesion implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Integer fundoId;
	private Integer appId;
	private String imei;
	
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
	
	public Integer getAppId() {
		return appId;
	}
	
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	
	public String getImei() {
		return imei;
	}
	
	public void setImei(String imei) {
		this.imei = imei;
	}
	
}
