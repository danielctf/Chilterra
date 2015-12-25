package cl.a2r.sip.model;

import java.util.ArrayList;
import java.util.List;

public class Salvataje {

	private Integer grupoId;
	private String nombreGrupo;
	private List<Ganado> ganado;
	
	public Salvataje(){
		ganado = new ArrayList<Ganado>();
	}
	
	public List<Ganado> getGanado(){
		return this.ganado;
	}
	
	public String toString(){
		return this.nombreGrupo;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public Integer getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Integer grupoId) {
		this.grupoId = grupoId;
	}
	
}
