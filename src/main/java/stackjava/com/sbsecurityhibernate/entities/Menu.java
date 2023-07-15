package stackjava.com.sbsecurityhibernate.entities;

public class Menu {
	private String name;
	private String path;
	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Menu(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}
	
	
}
