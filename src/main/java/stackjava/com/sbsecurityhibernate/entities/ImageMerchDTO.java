package stackjava.com.sbsecurityhibernate.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "image_merch")
public class ImageMerchDTO {
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
	@Basic(optional = false)
	@Column(name = "dayMerch")
	@Temporal(TemporalType.DATE)
	private Date dayMerch;
	@Column(name = "royaltie")
	private BigDecimal royaltie;
	@Column(name = "sold")
	private int sold;
	@Lob
	@Column(name = "BobImage", columnDefinition = "LONGBLOB")
	byte[] BobImage;
	
	@Transient
	private String base64;
	
	public BigDecimal getRoyaltie() {
		return royaltie;
	}

	public void setRoyaltie(BigDecimal royaltie) {
		this.royaltie = royaltie;
	}

	public int getSold() {
		return sold;
	}

	public void setSold(int sold) {
		this.sold = sold;
	}

	public Date getDayMerch() {
		return dayMerch;
	}

	public void setDayMerch(Date dayMerch) {
		this.dayMerch = dayMerch;
	}

	@Column(name = "acc")
	private String acc;
	@Column(name = "asin")
	private String asin;
	

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

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

	public byte[] getBobImage() {
		return BobImage;
	}

	public void setBobImage(byte[] bobImage) {
		BobImage = bobImage;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
}
