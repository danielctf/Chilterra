package cl.a2r.sap.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Medicion implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String activa;
	private Integer usuarioId;
	private String correo;
	private Integer fundoId;
	private Integer potreroId;
	private Integer numeroPotrero;
	private Integer clickInicial;
	private Integer clickFinal;
	private double click;
	private Integer muestras;
	private Integer materiaSeca;
	private Integer medidorId;
	private String nombreMedidor;
	private Integer tipoMuestraId;
	private Integer animales;
	private String tipoMuestraNombre;
	private String sincronizado;
	private double superficie;
	private Date actualizado;
	private Date fecha;
	private Integer sqliteId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActiva() {
		return activa;
	}

	public void setActiva(String activa) {
		this.activa = activa;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Integer getFundoId() {
		return fundoId;
	}

	public void setFundoId(Integer fundoId) {
		this.fundoId = fundoId;
	}

	public Integer getPotreroId() {
		return potreroId;
	}
	
	public void setPotreroId(Integer potreroId) {
		this.potreroId = potreroId;
	}
	
	public Integer getClickInicial() {
		return clickInicial;
	}
	
	public void setClickInicial(Integer clickInicial) {
		this.clickInicial = clickInicial;
	}
	
	public Integer getClickFinal() {
		return clickFinal;
	}
	
	public void setClickFinal(Integer clickFinal) {
		this.clickFinal = clickFinal;
	}

	public double getClick() {
		return click;
	}

	public void setClick(double click) {
		this.click = click;
	}

	public Integer getMuestras() {
		return muestras;
	}
	
	public void setMuestras(Integer muestras) {
		this.muestras = muestras;
	}
	
	public Integer getMateriaSeca() {
		return materiaSeca;
	}
	
	public void setMateriaSeca(Integer materiaSeca) {
		this.materiaSeca = materiaSeca;
	}
	
	public Integer getMedidorId() {
		return medidorId;
	}

	public void setMedidorId(Integer medidorId) {
		this.medidorId = medidorId;
	}

	public String getNombreMedidor() {
		return nombreMedidor;
	}
	
	public void setNombreMedidor(String nombreMedidor) {
		this.nombreMedidor = nombreMedidor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Integer getTipoMuestraId() {
		return tipoMuestraId;
	}

	public void setTipoMuestraId(Integer tipoMuestraId) {
		this.tipoMuestraId = tipoMuestraId;
	}

	/**
	 * @return the actualizado
	 */
	public Date getActualizado() {
		return actualizado;
	}

	/**
	 * @param actualizado the actualizado to set
	 */
	public void setActualizado(Date actualizado) {
		this.actualizado = actualizado;
	}

	public String getTipoMuestraNombre() {
		return tipoMuestraNombre;
	}

	public Integer getAnimales() {
		return animales;
	}

	public void setAnimales(Integer animales) {
		this.animales = animales;
	}

	public double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}

	public void setTipoMuestraNombre(String tipoMuestraNombre) {
		this.tipoMuestraNombre = tipoMuestraNombre;
	}

	public Integer getSqliteId() {
		return sqliteId;
	}

	public void setSqliteId(Integer sqliteId) {
		this.sqliteId = sqliteId;
	}

	public String getSincronizado() {
		return sincronizado;
	}

	public void setSincronizado(String sincronizado) {
		this.sincronizado = sincronizado;
	}

	public Integer getNumeroPotrero() {
		return numeroPotrero;
	}

	public void setNumeroPotrero(Integer numeroPotrero) {
		this.numeroPotrero = numeroPotrero;
	}

	public String toString(){
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		return "Potrero: " + this.potreroId.toString() + "\n" +
				"Click: " + Double.toString(this.click) + "\n" + 
				"Materia Seca: " + this.materiaSeca.toString() + "\n" + 
				"Fecha: " + df.format(this.fecha) + "\n";
				
	}
	
}
