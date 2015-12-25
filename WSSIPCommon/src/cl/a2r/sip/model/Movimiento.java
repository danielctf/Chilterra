package cl.a2r.sip.model;

import java.io.Serializable;

public class Movimiento implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer g_movimiento_id;
	private Integer m_movement_id;
	private Integer nro_documento;
	private Integer fundoOrigenId;
	private Integer fundoDestinoId;
	private String fundoOrigen;
	private String fundoDestino;
	
	public Integer getG_movimiento_id() {
		return g_movimiento_id;
	}
	
	public void setG_movimiento_id(Integer g_movimiento_id) {
		this.g_movimiento_id = g_movimiento_id;
	}
	
	public Integer getNro_documento() {
		return nro_documento;
	}
	
	public void setNro_documento(Integer nro_documento) {
		this.nro_documento = nro_documento;
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

	public Integer getM_movement_id() {
		return m_movement_id;
	}

	public void setM_movement_id(Integer m_movement_id) {
		this.m_movement_id = m_movement_id;
	}
	
	public String getFundoOrigen() {
		return fundoOrigen;
	}

	public void setFundoOrigen(String fundoOrigen) {
		this.fundoOrigen = fundoOrigen;
	}

	public String getFundoDestino() {
		return fundoDestino;
	}

	public void setFundoDestino(String fundoDestino) {
		this.fundoDestino = fundoDestino;
	}

	public String toString(){
		return "Guia Despacho: " + Integer.toString(nro_documento) + "\n" +
				"Origen: " + fundoOrigen + "\n" +
				"Destino: " + fundoDestino + "\n";
	}
	
}
