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

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@Entity
@Table(name = "product")
public class ProductDTOVIEW {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	private Integer acc;
	private String asin;
	private String urlPreview;
	private String title;
	private String brand;
	@Basic(optional = false)
	@Column(name = "createDate")
	@Temporal(TemporalType.DATE)
	private Date createDate;
	private String userDelete;
	@Basic(optional = false)
	@Column(name = "dayUpdate")
	@Temporal(TemporalType.DATE)
	private Date dayUpdate;
	private String username;
	private String pathProfile;
	private String typeProduct;
	private String price;
	private String accName;
	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	private String status;
	@Transient
	private String base64;
	private String pathFile;
	@Lob
	@Column(name = "BobImage", columnDefinition = "LONGBLOB")
	byte[] BobImage;
	private BigDecimal royaltie ;
	private int sold;
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

	public byte[] getBobImage() {
		return BobImage;
	}

	public void setBobImage(byte[] bobImage) {
		BobImage = bobImage;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTypeProduct() {
		return typeProduct;
	}

	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}

	public String getMkt() {
		return mkt;
	}

	public void setMkt(String mkt) {
		this.mkt = mkt;
	}

	private String ip;
	private String mkt;

	public Integer getAcc() {
		return acc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAcc(Integer acc) {
		this.acc = acc;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getUrlPreview() {
		return urlPreview;
	}

	public void setUrlPreview(String urlPreview) {
		this.urlPreview = urlPreview;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserDelete() {
		return userDelete;
	}

	public void setUserDelete(String userDelete) {
		this.userDelete = userDelete;
	}

	public Date getDayUpdate() {
		return dayUpdate;
	}

	public void setDayUpdate(Date dayUpdate) {
		this.dayUpdate = dayUpdate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPathProfile() {
		return pathProfile;
	}

	public void setPathProfile(String pathProfile) {
		this.pathProfile = pathProfile;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
