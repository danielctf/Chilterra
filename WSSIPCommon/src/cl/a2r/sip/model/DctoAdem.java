package cl.a2r.sip.model;

import java.io.Serializable;

public class DctoAdem implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer iddocto;
	private Integer idtipodocto;
	private String nrodocto;
	private Integer g_movimiento_id;
	
	public Integer getIddocto() {
		return iddocto;
	}
	
	public void setIddocto(Integer iddocto) {
		this.iddocto = iddocto;
	}
	
	public Integer getIdtipodocto() {
		return idtipodocto;
	}
	
	public void setIdtipodocto(Integer idtipodocto) {
		this.idtipodocto = idtipodocto;
	}
	
	public String getNrodocto() {
		return nrodocto;
	}
	
	public void setNrodocto(String nrodocto) {
		this.nrodocto = nrodocto;
	}

	public Integer getG_movimiento_id() {
		return g_movimiento_id;
	}

	public void setG_movimiento_id(Integer g_movimiento_id) {
		this.g_movimiento_id = g_movimiento_id;
	}
	
}
