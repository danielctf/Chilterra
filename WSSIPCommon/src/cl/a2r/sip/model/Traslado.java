package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Traslado implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Integer fundoOrigenId;
	private Integer fundoDestinoId;
	private String descripcion;
	private Integer tipoTransporteId;
	private Integer transportistaId;
	private Integer choferId;
	private Integer camionId;
	private Integer acopladoId;
	private Integer arrieroId;
	private Integer g_movimiento_id;
	private Integer m_movement_id;
	private Integer nro_documento;
	private List<Ganado> ganado;
	
	public Traslado(){
		ganado = new ArrayList<Ganado>();
	}
	
	public List<Ganado> getGanado() {
		return ganado;
	}

	public void setGanado(List<Ganado> ganado) {
		this.ganado = ganado;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
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
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getTipoTransporteId() {
		return tipoTransporteId;
	}
	
	public void setTipoTransporteId(Integer tipoTransporteId) {
		this.tipoTransporteId = tipoTransporteId;
	}
	
	public Integer getTransportistaId() {
		return transportistaId;
	}
	
	public void setTransportistaId(Integer transportistaId) {
		this.transportistaId = transportistaId;
	}
	
	public Integer getChoferId() {
		return choferId;
	}
	
	public void setChoferId(Integer choferId) {
		this.choferId = choferId;
	}
	
	public Integer getCamionId() {
		return camionId;
	}
	
	public void setCamionId(Integer camionId) {
		this.camionId = camionId;
	}
	
	public Integer getAcopladoId() {
		return acopladoId;
	}
	
	public void setAcopladoId(Integer acopladoId) {
		this.acopladoId = acopladoId;
	}

	public Integer getArrieroId() {
		return arrieroId;
	}

	public void setArrieroId(Integer arrieroId) {
		this.arrieroId = arrieroId;
	}
	
	public Integer getG_movimiento_id() {
		return g_movimiento_id;
	}

	public void setG_movimiento_id(Integer g_movimiento_id) {
		this.g_movimiento_id = g_movimiento_id;
	}

	public Integer getM_movement_id() {
		return m_movement_id;
	}

	public void setM_movement_id(Integer m_movement_id) {
		this.m_movement_id = m_movement_id;
	}

	public Integer getNro_documento() {
		return nro_documento;
	}

	public void setNro_documento(Integer nro_documento) {
		this.nro_documento = nro_documento;
	}
}
