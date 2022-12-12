package stackjava.com.sbsecurityhibernate.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "upload_file")
public class uploadFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	private String name;
	private String username;
	private String status;
	private String title;
	private String brand;
	private String des1;
	private String des2;
	private String mau;
	private String profile;
	private String typeTshirt;
	private Integer idAccount;
	private String price;
	private String ip;
	private Date day;
	private String nameAccount;
	private String nameuser;

	private String typeTshirtPre;
	private String pricePre;
	private String maupre;
	private String TypeShirtUpLoad;

	private String priceVneck;

	private String mauVneck;

	private String priceTank;

	private String mauTank;
	private String mauLong;

	private String priceLong;
	private String priceRaglan;
	private String mauSweat;

	private String priceSweat;
	
	private String pricePullover;
	private String mauPullover;
	private String priceZip;
	private String mauZip;
	
	
	
	

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	// private Integer idImage;

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public String getNameAccount() {
		return nameAccount;
	}

	public void setNameAccount(String nameAccount) {
		this.nameAccount = nameAccount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(Integer idAccount) {
		this.idAccount = idAccount;
	}

	public String getTypeTshirt() {
		return typeTshirt;
	}

	public void setTypeTshirt(String typeTshirt) {
		this.typeTshirt = typeTshirt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getDes1() {
		return des1;
	}

	public void setDes1(String des1) {
		this.des1 = des1;
	}

	public String getDes2() {
		return des2;
	}

	public void setDes2(String des2) {
		this.des2 = des2;
	}

	public String getMau() {
		return mau;
	}

	public void setMau(String mau) {
		this.mau = mau;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNameuser() {
		return nameuser;
	}

	public void setNameuser(String nameuser) {
		this.nameuser = nameuser;
	}

	public String getTypeTshirtPre() {
		return typeTshirtPre;
	}

	public void setTypeTshirtPre(String typeTshirtPre) {
		this.typeTshirtPre = typeTshirtPre;
	}

	public String getPricePre() {
		return pricePre;
	}

	public void setPricePre(String pricePre) {
		this.pricePre = pricePre;
	}

	public String getMaupre() {
		return maupre;
	}

	public void setMaupre(String maupre) {
		this.maupre = maupre;
	}

	public String getTypeShirtUpLoad() {
		return TypeShirtUpLoad;
	}

	public void setTypeShirtUpLoad(String typeShirtUpLoad) {
		TypeShirtUpLoad = typeShirtUpLoad;
	}

	public String getPriceVneck() {
		return priceVneck;
	}

	public void setPriceVneck(String priceVneck) {
		this.priceVneck = priceVneck;
	}

	public String getMauVneck() {
		return mauVneck;
	}

	public void setMauVneck(String mauVneck) {
		this.mauVneck = mauVneck;
	}

	public String getPriceTank() {
		return priceTank;
	}

	public void setPriceTank(String priceTank) {
		this.priceTank = priceTank;
	}

	public String getMauTank() {
		return mauTank;
	}

	public void setMauTank(String mauTank) {
		this.mauTank = mauTank;
	}

	public String getMauLong() {
		return mauLong;
	}

	public void setMauLong(String mauLong) {
		this.mauLong = mauLong;
	}

	public String getPriceLong() {
		return priceLong;
	}

	public void setPriceLong(String priceLong) {
		this.priceLong = priceLong;
	}

	public String getPriceRaglan() {
		return priceRaglan;
	}

	public void setPriceRaglan(String priceRaglan) {
		this.priceRaglan = priceRaglan;
	}

	public String getMauSweat() {
		return mauSweat;
	}

	public void setMauSweat(String mauSweat) {
		this.mauSweat = mauSweat;
	}

	public String getPriceSweat() {
		return priceSweat;
	}

	public void setPriceSweat(String priceSweat) {
		this.priceSweat = priceSweat;
	}

	public String getPricePullover() {
		return pricePullover;
	}

	public void setPricePullover(String pricePullover) {
		this.pricePullover = pricePullover;
	}

	public String getMauPullover() {
		return mauPullover;
	}

	public void setMauPullover(String mauPullover) {
		this.mauPullover = mauPullover;
	}

	public String getPriceZip() {
		return priceZip;
	}

	public void setPriceZip(String priceZip) {
		this.priceZip = priceZip;
	}

	public String getMauZip() {
		return mauZip;
	}

	public void setMauZip(String mauZip) {
		this.mauZip = mauZip;
	}

}
