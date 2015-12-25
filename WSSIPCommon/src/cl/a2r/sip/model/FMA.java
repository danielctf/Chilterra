package cl.a2r.sip.model;

import java.io.Serializable;

public class FMA implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Integer g_movimiento_id;
	private Integer fundoOrigenId;
	private Integer fundoDestinoId;
	
	public Integer getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public Integer getG_movimiento_id() {
		return g_movimiento_id;
	}
	
	public void setG_movimiento_id(Integer g_movimiento_id) {
		this.g_movimiento_id = g_movimiento_id;
	}
	
	public Integer getFundoOrigenId() {
		return fundoOrigenId;
	}
	
	public void setFundoOrigenId(Integer fundoOrigenId) {
		this.fundoOrigenId = fundoOrigenId;
	}
	
	public Integer getFundoDestinoId() {
		return fundoDestinoId;
	}
	
	public void setFundoDestinoId(Integer fundoDestinoId) {
		this.fundoDestinoId = fundoDestinoId;
	}
	
}
