package cl.a2r.sip.model;

import java.io.Serializable;

public class Ganado implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer diio;
	private String eid;
	private String activa;
	private Integer predio;
	private String sexo;
	private Integer mangada;
	private String observacion;
	private String brucelosis;
	private Integer tipoGanadoId;
	private Integer estadoLecheId;
	private String sincronizado;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getDiio() {
		return diio;
	}
	
	public void setDiio(Integer diio) {
		this.diio = diio;
	}
	
	public String getActiva() {
		return activa;
	}
	
	public void setActiva(String activa) {
		this.activa = activa;
	}
	
	public Integer getPredio() {
		return predio;
	}
	
	public void setPredio(Integer predio) {
		this.predio = predio;
	}
	
	public String toString(){
		if (this.diio != null){
			return Integer.toString(this.diio);
		} else {
			return this.eid;
		}
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Integer getMangada() {
		return mangada;
	}

	public void setMangada(Integer mangada) {
		this.mangada = mangada;
	}

	public Integer getTipoGanadoId() {
		return tipoGanadoId;
	}

	public void setTipoGanadoId(Integer tipoGanadoId) {
		this.tipoGanadoId = tipoGanadoId;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getBrucelosis() {
		return brucelosis;
	}

	public void setBrucelosis(String brucelosis) {
		this.brucelosis = brucelosis;
	}

	public Integer getEstadoLecheId() {
		return estadoLecheId;
	}

	public void setEstadoLecheId(Integer estadoLecheId) {
		this.estadoLecheId = estadoLecheId;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}
	
}
