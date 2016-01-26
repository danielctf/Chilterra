package cl.a2r.sip.model;

import java.io.Serializable;
import java.util.Date;

public class InyeccionTB implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer usuarioId;
	private Integer ganadoID;
	private Integer ganadoDiio;
	private Integer mangada;
	private Integer tuboPPDId;
	private Integer tuboPPDSerie;
	private String lecturaTB;
	private Integer instancia;
	private Integer fundoId;
	private Date fecha_dosis;
	private Date fecha_lectura;
	private String sincronizado;
	
	public Integer getGanadoID() {
		return ganadoID;
	}
	
	public void setGanadoID(Integer ganadoID) {
		this.ganadoID = ganadoID;
	}
	
	public Integer getGanadoDiio() {
		return ganadoDiio;
	}
	
	public void setGanadoDiio(Integer ganadoDiio) {
		this.ganadoDiio = ganadoDiio;
	}

	public Integer getTuboPPDId() {
		return tuboPPDId;
	}

	public void setTuboPPDId(Integer tuboPPDId) {
		this.tuboPPDId = tuboPPDId;
	}

	public Integer getTuboPPDSerie() {
		return tuboPPDSerie;
	}

	public void setTuboPPDSerie(Integer tuboPPDSerie) {
		this.tuboPPDSerie = tuboPPDSerie;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Date getFecha_dosis() {
		return fecha_dosis;
	}

	public void setFecha_dosis(Date fecha_dosis) {
		this.fecha_dosis = fecha_dosis;
	}

	public Integer getInstancia() {
		return instancia;
	}

	public void setInstancia(Integer instancia) {
		this.instancia = instancia;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public Integer getFundoId() {
		return fundoId;
	}

	public void setFundoId(Integer fundoId) {
		this.fundoId = fundoId;
	}
	
	public String toString(){
		return "DIIO: " + Integer.toString(this.ganadoDiio)
				+ "   Tubo: " + Integer.toString(this.tuboPPDSerie);
	}

	public String getLecturaTB() {
		return lecturaTB;
	}

	public void setLecturaTB(String lecturaTB) {
		this.lecturaTB = lecturaTB;
	}

	public Date getFecha_lectura() {
		return fecha_lectura;
	}

	public void setFecha_lectura(Date fecha_lectura) {
		this.fecha_lectura = fecha_lectura;
	}

	public Integer getMangada() {
		return mangada;
	}

	public void setMangada(Integer mangada) {
		this.mangada = mangada;
	}

}
