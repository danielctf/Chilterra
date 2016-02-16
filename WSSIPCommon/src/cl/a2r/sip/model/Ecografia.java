package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class Ecografia implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer usuarioId;
	private Integer fundoId;
	private Date fecha;
	private Ganado gan;
	private Integer dias_prenez;
	private Integer ecografistaId;
	private Integer inseminacionId;
	private Integer estadoId;
	private Integer problemaId;
	private Integer notaId;
	private Integer mangada;
	private String sincronizado;
	
	public Ecografia(){
		gan = new Ganado();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void setGanado(Ganado gan){
		this.gan = gan;
	}

	public Ganado getGan() {
		return gan;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public Integer getEstadoId() {
		return estadoId;
	}

	public void setEstadoId(Integer estadoId) {
		this.estadoId = estadoId;
	}

	public Integer getInseminacionId() {
		return inseminacionId;
	}

	public void setInseminacionId(Integer inseminacionId) {
		this.inseminacionId = inseminacionId;
	}

	public Integer getEcografistaId() {
		return ecografistaId;
	}

	public void setEcografistaId(Integer ecografistaId) {
		this.ecografistaId = ecografistaId;
	}

	public Integer getDias_prenez() {
		return dias_prenez;
	}

	public void setDias_prenez(Integer dias_prenez) {
		this.dias_prenez = dias_prenez;
	}

	public Integer getProblemaId() {
		return problemaId;
	}

	public void setProblemaId(Integer problemaId) {
		this.problemaId = problemaId;
	}

	public Integer getNotaId() {
		return notaId;
	}

	public void setNotaId(Integer notaId) {
		this.notaId = notaId;
	}

	public Integer getMangada() {
		return mangada;
	}

	public void setMangada(Integer mangada) {
		this.mangada = mangada;
	}

	public String toString(){
		if (this.dias_prenez != null){
			return "DIIO: " + Integer.toString(this.getGan().getDiio()) + "   Dias Preñez: "
					+ Integer.toString(this.dias_prenez);
		} else {
			return "DIIO: " + Integer.toString(this.getGan().getDiio()) + "   Dias Preñez: ";
		}
	}
	
}
