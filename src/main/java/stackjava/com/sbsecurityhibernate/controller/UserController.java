package stackjava.com.sbsecurityhibernate.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import stackjava.com.sbsecurityhibernate.dao.CallAPi;
import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.ImageMerch;
import stackjava.com.sbsecurityhibernate.entities.SaleMerch;
import stackjava.com.sbsecurityhibernate.entities.User;
import stackjava.com.sbsecurityhibernate.entities.uploadFile;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserDAO userDAO;

	@RequestMapping("/allAcc")
	public String user(HttpSession session, HttpServletRequest request, Model model) {
		try {
			User user = (User) session.getAttribute("user");
			List<AccountMerch> lst = userDAO.getAllUser(user.getUsername());
			model.addAttribute("lst", lst);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return "dashboard/allAccount";
	}

	@RequestMapping("/checkSale")
	public String checkSaleMerch(HttpSession session, Model model, HttpServletRequest request) {
		String daySeach = request.getParameter("daySearch");
		// String daySearchTo=request.getParameter("daySearchTo");
		User user = (User) session.getAttribute("user");

		model.addAttribute("daySeach", daySeach);
		// model.addAttribute("daySearchTo", daySearchTo);
		if (daySeach != null && daySeach != "") {

			List<SaleMerch> lst = userDAO.getAllSaleMerch(daySeach, daySeach, user.getUsername());
			model.addAttribute("lst", lst);
			return "dashboard/checkSaleMerch";
		}

		model.addAttribute("lst", null);
		return "dashboard/checkSaleMerch";
	}

	@RequestMapping("/saveAccountMerch")
	@ResponseBody
	public String saveAccountMerch(@ModelAttribute AccountMerch acc, HttpSession session, Model model,
			HttpServletRequest request) {
		try {
			AccountMerch accSave = acc;
			User user = (User) session.getAttribute("user");
			accSave.setUsername(user.getUsername());
			accSave.setDay(new Date());
			accSave.setIp(acc.getIp().trim());
			accSave.setPath(acc.getPath().trim());
			AccountMerch accRP = userDAO.saveOrUpdate(accSave);
			if (accRP != null && accRP.getId() != null) {
				return "00";
			}
			// model.addAttribute("lst", lst);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	@RequestMapping("/deleteAccountMerch")
	@ResponseBody
	public String deleteAccountMerch(HttpSession session, Model model, HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			boolean check = userDAO.deleteAcoutMerch(Integer.parseInt(id));
			if (check) {
				return "00";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	@RequestMapping("/checkSaleById")
	@ResponseBody
	public String checkSaleById(HttpSession session, Model model, HttpServletRequest request) {
		try {
			String id = request.getParameter("id");

			AccountMerch merch = userDAO.getAccountMerchByID(Integer.parseInt(id));

			ObjectMapper objectMapper = new ObjectMapper();
			String req = objectMapper.writeValueAsString(merch);
			CallAPi callApi = new CallAPi();
			try {
				String rep = callApi.callAPIPost("http://" + merch.getIp() + ":8080/checksalemerchtest", req);
				// model.addAttribute("lst", lst);
				if (rep != null && rep.equalsIgnoreCase("00")) {
					return "00";
				}
			} catch (Exception e) {
				return "02";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "01";

		}
		return "01";

	}

	@RequestMapping("/showDS")
	public String showDS(HttpSession session, Model model, HttpServletRequest request) {
		try {
			String id = request.getParameter("id");

			SaleMerch merch = userDAO.getSaleMerchByID(Integer.parseInt(id));
			List<ImageMerch> lst = userDAO.getImageMerchFromSaleMerch(merch);
			model.addAttribute("lst", lst);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "dashboard/fmDS :: channelEdit";

	}

	@GetMapping("/upload")
	public String saveUser(HttpSession session, HttpServletRequest request, Model model) {
		User user = (User) session.getAttribute("user");
		List<AccountMerch> lst = userDAO.getAllUser(user.getUsername());
		model.addAttribute("lst", lst);
		return "dashboard/upload.html";
	}

	@GetMapping("/imageupload")
	public String imageupload(HttpSession session, Model model, HttpServletRequest request) {
		String daySeach = request.getParameter("daySearch");
		String status = request.getParameter("status");
		// String daySearchTo=request.getParameter("daySearchTo");
		User user = (User) session.getAttribute("user");

		model.addAttribute("daySeach", daySeach);
		model.addAttribute("status", status);
		// model.addAttribute("daySearchTo", daySearchTo);
		if (daySeach != null && daySeach != "") {

			List<uploadFile> lst = userDAO.getAllUploadFileSearch(daySeach, daySeach, user.getUsername(),status);
			model.addAttribute("lst", lst);
			return "dashboard/imageupload";
		}

		model.addAttribute("lst", null);
		return "dashboard/imageupload";
	}

	@PostMapping("/imageupload")
	public String imageuploadpost(@RequestParam String checkItem, HttpSession session, Model model,
			HttpServletRequest request) {
		System.out.println(checkItem);
		User user = (User) session.getAttribute("user");
		try {
			List<String> convertedCountriesList = Stream.of(checkItem.split(",", -1)).collect(Collectors.toList());
			List<Integer> ids = new ArrayList<Integer>();
			for (String string : convertedCountriesList) {
				ids.add(Integer.parseInt(string));
			}

			List<uploadFile> lst = userDAO.getAllUploadFileFromlistID(ids);
			List<String> lstIp = new ArrayList<String>();
			ObjectMapper objectMapper = new ObjectMapper();
			CallAPi callApi = new CallAPi();
			for (uploadFile uploadFile : lst) {
				if (!lstIp.contains(uploadFile.getIp())) {
					lstIp.add(uploadFile.getIp());
				}
			}
			for (String string : lstIp) {
				List<uploadFile> resul = new ArrayList<uploadFile>();
				for (uploadFile uploadFile : lst) {
					if (string.equalsIgnoreCase(uploadFile.getIp())) {
						resul.add(uploadFile);
					}
				}
				try {

					String reqlst = objectMapper.writeValueAsString(resul);
					String reul = callApi.callAPIPostNotReport("http://" + string + ":8080/uploadMerchMulti", reqlst);
					// model.addAttribute("lst", lst);
					if (reul != null && reul.equalsIgnoreCase("00")) {
						// return "00";
					}
				} catch (Exception e) {
					continue;
					// TODO: handle exception
				}

			}
			// lst.add(dtonew);

		} catch (Exception e) {
			return "02";
		}

		String daySeach = request.getParameter("daySearch");
		if (daySeach != null && daySeach != "") {

			List<uploadFile> lst = userDAO.getAllUploadFile(daySeach, daySeach, user.getUsername());
			model.addAttribute("lst", lst);
			return "dashboard/imageupload";
		}
		model.addAttribute("daySeach", daySeach);
		model.addAttribute("lst", null);
		return "dashboard/imageupload";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute uploadFile dto, @RequestParam("image") MultipartFile multipartFile,
			HttpSession session, RedirectAttributes ra) {
		try {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			System.out.println();
			User user = (User) session.getAttribute("user");
			// User savedUser = repo.save(user);
			String home = System.getProperty("user.home");
			AccountMerch merch = userDAO.getAccountMerchByID(dto.getIdAccount());
			dto.setIp(merch.getIp());
			// dto.setNameAccount(merch.getName());
			dto.setProfile(merch.getPath());
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.HOUR_OF_DAY, 0);
			dto.setNameAccount(merch.getName());

			dto.setDay(new Date());
			dto.setStatus("0");
			dto.setUsername(user.getUsername());
			dto.setName(fileName);
			uploadFile dtonew = userDAO.saveOrUpdateuploadFile(dto);
			// File file = new File(home+"/Downloads/" + fileName + ".txt");
			// FileOutputStream fos = new FileOutputStream(home+"/Downloads/" +fileName);
			String uploadDir = home + "/Downloads/" + user.getUsername() + "/";
			try {
				saveFile(uploadDir, fileName, multipartFile);
			}catch (Exception e) {
				// TODO: handle exception
			}
			

			// AccountMerch merch = userDAO.getAccountMerchByID(Integer.parseInt(id));

//			ObjectMapper objectMapper = new ObjectMapper();
//
//			String req = objectMapper.writeValueAsString(dtonew);
//			CallAPi callApi = new CallAPi();
//			try {
//				String rep = callApi.callAPIPost("http://" + dtonew.getIp() + ":8080/uploadMerch", req);
//				// model.addAttribute("lst", lst);
//				if (rep != null && rep.equalsIgnoreCase("00")) {
//					return "00";
//				}
//			} catch (Exception e) {
//				MessageHelper.addErrorAttribute(ra, "Đã có lỗi xảy ra!");
//				e.printStackTrace();
//				return "redirect:/user/upload";
//				// return "02";
//			}
			MessageHelper.addSuccessAttribute(ra, "Thanh cong");
//			try {
//				List<uploadFile> lst = new ArrayList<uploadFile>();
//				lst.add(dtonew);
//				String reqlst = objectMapper.writeValueAsString(lst);
//				String reul = callApi.callAPIPostNotReport("http://" + dtonew.getIp() + ":8080/uploadMerchMulti", reqlst);
//				// model.addAttribute("lst", lst);
//				if (reul != null && reul.equalsIgnoreCase("00")) {
//					return "00";
//				}
//
//			} catch (Exception e) {
//				return "02";
//			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "redirect:/user/upload";
	}

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
		try {
			InputStream inputStream = multipartFile.getInputStream();
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			Path filePath = uploadPath.resolve(fileName);
			 Files.deleteIfExists(filePath);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/checkSaleAllAcc")
	@ResponseBody
	public String checkSaleAllAcc(HttpSession session, Model model, HttpServletRequest request) {
		try {
			User user = (User) session.getAttribute("user");
			List<AccountMerch> lst = userDAO.getAllUser(user.getUsername());
			for (AccountMerch merch : lst) {
				ObjectMapper objectMapper = new ObjectMapper();
				String req = objectMapper.writeValueAsString(merch);
				CallAPi callApi = new CallAPi();
				try {
					String rep = callApi.callAPIPost("http://" + merch.getIp() + ":8080/checksalemerchtest", req);

				} catch (Exception e) {
					continue;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "01";

		}
		return "00";

	}

	@ResponseBody
	@RequestMapping(value = "/saveCheckSale", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String test(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			SaleMerch mech = objectMapper.readValue(req, SaleMerch.class);
			mech.setDay(new Date());
			SaleMerch rp = userDAO.saveOrUpdate(mech);
			if (rp != null && rp.getId() != null) {
				return "00";
			}

			// model.addAttribute("lst", lst);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

}
