package stackjava.com.sbsecurityhibernate.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
@RequestMapping("/restApi")
public class UpLoadController {

	@RequestMapping(value = "/upload", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String hello(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ConnectDB con = new ConnectDB();

		subMitClass subMitClass = gson.fromJson(req, subMitClass.class);

		String adress = con.getAdress(subMitClass.getKey()); 
		String dressSubmid = subMitClass.getAddress();
		String[] adressAR = adress.split(",");
		boolean check = true;
		for (int i = 0; i < adressAR.length; i++) {

			if (!dressSubmid.toLowerCase().contains(adressAR[i].toLowerCase())) { 
				check = false;
			} else {
				check = true;
			}
		}
		if (check == false) {
			return "";
		}
		List<Image> lstNew = new ArrayList<Image>();
		List<Image> lst = subMitClass.getLstImage();
		for (Image image : lst) {
			String a = image.getUrl();
			String b = a.replace("https://ih1.redbubble.net/", "");
			int firstIndex = b.indexOf('/');
			String abc = "https://ih1.redbubble.net/" + b.substring(0, firstIndex) + "/--.u1.png";
			image.setUrlpng(abc);
			lstNew.add(image);
		}
		subMitClass.setLstImage(lstNew);
		if(con.getCon()!=null)
		{
			try {
				con.getCon().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return gson.toJson(subMitClass);

	}

	@RequestMapping(value = "/checkkey", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String checkkey(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ConnectDB con = new ConnectDB();
		subMitClass subMitClass = gson.fromJson(req, subMitClass.class);
		con.update(subMitClass.getKey(), subMitClass.getAddress());
		String adress = con.getAdress(subMitClass.getKey());
		String dressSubmid = subMitClass.getAddress();
		if (adress != null && !adress.isEmpty()) {
				String[] adressAR = adress.split(",");
				boolean check = true;
				for (int i = 0; i < adressAR.length; i++) {

					if (!dressSubmid.toLowerCase().contains(adressAR[i].toLowerCase())) {
						check = false;
					} else {
						check = true;
					}
				}
				if (check == false) {
					return "";
				} else {

				}
				subMitClass newClass = new subMitClass();
				newClass.setKey("00");
				if(con.getCon()!=null)
				{
					try {
						con.getCon().close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return gson.toJson(newClass);
			

		}
		
		if(con.getCon()!=null)
		{
			try {
				con.getCon().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";

	}

	@RequestMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String insert(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ConnectDB con = new ConnectDB();
		subMitClass subMitClass = gson.fromJson(req, subMitClass.class);
		con.insert(subMitClass.getKey(), subMitClass.getAddress());
		subMitClass newClass = new subMitClass();
		newClass.setKey("00");
		if(con.getCon()!=null)
		{
			try {
				con.getCon().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return gson.toJson(newClass);

	}

	// @RequestMapping(value = "/updatetimeadress", consumes =
	// MediaType.APPLICATION_JSON_VALUE, produces =
	// MediaType.APPLICATION_JSON_VALUE)
	// private String updatetimeadress(@RequestBody String req, HttpServletRequest
	// request, HttpServletResponse resp) {
	// Gson gson = new GsonBuilder().setPrettyPrinting().create();
	// ConnectDB con = new ConnectDB();
	// subMitClass subMitClass = gson.fromJson(req, subMitClass.class);
	// con.update(subMitClass.getKey(), subMitClass.getAddress());
	// subMitClass newClass= new subMitClass();
	// newClass.setKey("00");
	// return gson.toJson(newClass);
	//
	// }
	//

/*	@RequestMapping(value = "/insert")
	private String insert() {
		ConnectDB con = new ConnectDB();
		con.insert("123", "123");
		return "";

	}*/

	@RequestMapping(value = "/test")
	private String test() {
		ConnectDB con = new ConnectDB();
		con.update("470259770", "123");
		return "";

	}
}
