package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResultadoBrucelosis implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Date fecha_envio;
	private Date fecha_resultado;
	private String brucelosis;
	private Integer protocolo_sag;
	private Integer instancia;
	private List<Ganado> ganado;
	
	public ResultadoBrucelosis(){
		ganado = new ArrayList<Ganado>();
	}
	
	public List<Ganado> getGanado(){
		return this.ganado;
	}
	
	public Integer getUsuarioId() {
		return usuarioId;
	}
	
	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	public Date getFecha_envio() {
		return fecha_envio;
	}
	
	public void setFecha_envio(Date fecha_envio) {
		this.fecha_envio = fecha_envio;
	}
	
	public Date getFecha_resultado() {
		return fecha_resultado;
	}
	
	public void setFecha_resultado(Date fecha_resultado) {
		this.fecha_resultado = fecha_resultado;
	}
	
	public String getBrucelosis() {
		return brucelosis;
	}
	
	public void setBrucelosis(String brucelosis) {
		this.brucelosis = brucelosis;
	}
	
	public Integer getProtocolo_sag() {
		return protocolo_sag;
	}
	
	public void setProtocolo_sag(Integer protocolo_sag) {
		this.protocolo_sag = protocolo_sag;
	}
	
	public Integer getInstancia() {
		return instancia;
	}
	
	public void setInstancia(Integer instancia) {
		this.instancia = instancia;
	}
	
}
