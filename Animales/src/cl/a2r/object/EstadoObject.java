package cl.a2r.object;

public class EstadoObject {
	
	private String id;
	private String estado;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String toString(){
		return this.estado;
	}
	
}
