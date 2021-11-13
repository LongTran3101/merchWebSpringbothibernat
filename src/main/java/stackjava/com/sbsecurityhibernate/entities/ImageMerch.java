package stackjava.com.sbsecurityhibernate.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "image_merch")
public class ImageMerch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "url")
	private String url;
	@Basic(optional = false)
	@Column(name = "day")
	@Temporal(TemporalType.DATE)
	private Date day;
	
	@Column(name = "acc")
	private String acc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public String getAcc() {
		return acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}
}
