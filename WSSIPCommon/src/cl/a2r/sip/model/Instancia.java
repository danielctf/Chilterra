package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.List;

public class Instancia implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Instancia instancia;
	private Integer usuarioId;
	private Integer fundoId;
	private String estado;
	
	private TrasladoV2 traslado;
	
	private List<Ganado> ganList;
	
	public void iniciarInstancia(){
		instancia = new Instancia();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Instancia getInstancia() {
		return instancia;
	}

	public void setInstancia(Instancia instancia) {
		this.instancia = instancia;
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

	public TrasladoV2 getTraslado() {
		return traslado;
	}

	public void setTraslado(TrasladoV2 traslado) {
		this.traslado = traslado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Ganado> getGanList() {
		return ganList;
	}

	public void setGanList(List<Ganado> ganList) {
		this.ganList = ganList;
	}
	
}
