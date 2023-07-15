package stackjava.com.sbsecurityhibernate.config;

import java.util.ArrayList;
import java.util.List;

import stackjava.com.sbsecurityhibernate.entities.Menu;

public class Constain {
	public  List<Menu> lstadmin;
	
	public  List<Menu> lstuser;
	
	public Constain() {
		lstadmin=new ArrayList<>();
		lstadmin.add(new Menu("Tài khoản", "/user/allAcc"));
		lstadmin.add(new Menu("Thống kê", "/user/checkSale"));
		lstadmin.add(new Menu("Quản lý upload", "/user/imageupload"));
		lstadmin.add(new Menu("Upload", "/user/upload"));
		lstadmin.add(new Menu("Quản lý product", "/user/product"));
		lstadmin.add(new Menu("Quản lý người dùng", "/admin/alluser"));
		lstuser=new ArrayList<>();
		lstuser.add(new Menu("Tài khoản", "/user/allAcc"));
		lstuser.add(new Menu("Thống kê", "/user/checkSale"));
		lstuser.add(new Menu("Quản lý upload", "/user/imageupload"));
		lstuser.add(new Menu("Upload", "/user/upload"));
		lstuser.add(new Menu("Quản lý product", "/user/product"));
		// TODO Auto-generated constructor stub
	}

	public List<Menu> getLstadmin() {
		return lstadmin;
	}

	public List<Menu> getLstuser() {
		return lstuser;
	}

	public void setLstadmin(List<Menu> lstadmin) {
		this.lstadmin = lstadmin;
	}

	public void setLstuser(List<Menu> lstuser) {
		this.lstuser = lstuser;
	}
}
