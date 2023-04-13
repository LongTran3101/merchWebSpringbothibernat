package stackjava.com.sbsecurityhibernate.entities;

import java.util.Base64;

public class Image {
	private String name;
	private String usernam;
	private String baseFile;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsernam() {
		return usernam;
	}
	public void setUsernam(String usernam) {
		this.usernam = usernam;
	}
	public String getBaseFile() {
		return baseFile;
	}
	public void setBaseFile(String baseFile) {
		this.baseFile = baseFile;
	}
}
