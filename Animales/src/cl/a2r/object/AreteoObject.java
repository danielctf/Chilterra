package cl.a2r.object;

public class AreteoObject {

	private Integer id;
	private String nombre;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String toString() {
		return this.nombre;
	}
	
}
