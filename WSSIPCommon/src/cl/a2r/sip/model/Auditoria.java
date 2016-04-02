package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Auditoria implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer fundoId;
	private Date fecha_inicio;
	private Date fecha_termino;
	private String estado;
	private List<Ganado> ganList;
	
	public Auditoria(){
		ganList = new ArrayList<Ganado>();
	}
	
	public List<Ganado> getGanList() {
		return ganList;
	}

	public void setGanList(List<Ganado> ganList) {
		this.ganList = ganList;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getFundoId() {
		return fundoId;
	}
	
	public void setFundoId(Integer fundoId) {
		this.fundoId = fundoId;
	}
	
	public Date getFecha_inicio() {
		return fecha_inicio;
	}
	
	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	
	public Date getFecha_termino() {
		return fecha_termino;
	}
	
	public void setFecha_termino(Date fecha_termino) {
		this.fecha_termino = fecha_termino;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
