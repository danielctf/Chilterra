package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class TrasladoV2 implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer m_movement_id;
	private Integer nro_documento;
	private Date fecha;
	private Predio origen;
	private Predio destino;
	private String description;
	private String estado;
	private Integer transportistaId;
	private Integer choferId;
	private Integer camionId;
	private Integer acopladoId;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Predio getOrigen() {
		return origen;
	}
	
	public void setOrigen(Predio origen) {
		this.origen = origen;
	}
	
	public Predio getDestino() {
		return destino;
	}
	
	public void setDestino(Predio destino) {
		this.destino = destino;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
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
	
}
