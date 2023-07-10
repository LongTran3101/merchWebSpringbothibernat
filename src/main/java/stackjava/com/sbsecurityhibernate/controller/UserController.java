package stackjava.com.sbsecurityhibernate.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import stackjava.com.sbsecurityhibernate.dao.CallAPi;
import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.ImageMerch;
import stackjava.com.sbsecurityhibernate.entities.Product;
import stackjava.com.sbsecurityhibernate.entities.ProductDTOVIEW;
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

	@RequestMapping("/updateAccountMerch")
	@ResponseBody
	public String updateAccountMerch(HttpSession session, Model model, HttpServletRequest request) {
		try {

			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String ip = request.getParameter("ip");
			String path = request.getParameter("path");

			AccountMerch merch = userDAO.getAccountMerchByID(Integer.parseInt(id));
			merch.setName(name);
			merch.setEmail(email);
			merch.setIp(ip);
			merch.setPath(path);
			userDAO.saveOrUpdate(merch);

			return "00";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	@RequestMapping("/updateImageAll")
	@ResponseBody
	public String updateImageAll(HttpSession session, Model model, HttpServletRequest request,
			@RequestParam(value = "checkItem[]") List<String> myArray) {
		try {

			String idAccount = request.getParameter("idAccount");
			AccountMerch merch = userDAO.getAccountMerchByID(Integer.parseInt(idAccount));
			if (myArray != null && !myArray.isEmpty()) {
				for (String string : myArray) {
					try {
						uploadFile file = userDAO.getuploadFileID(Integer.parseInt(string));
						file.setIdAccount(merch.getId());
						file.setIp(merch.getIp());
						file.setProfile(merch.getPath());
						file.setNameAccount(merch.getName());
						file.setNameuser("1");
						userDAO.saveOrUpdateuploadFile(file);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}

			return "00";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	@RequestMapping("/updateImage")
	@ResponseBody
	public String updateImage(HttpSession session, Model model, HttpServletRequest request) {
		try {

			String id = request.getParameter("id");
			String brand = request.getParameter("brand");
			String bullet1 = request.getParameter("bullet1");
			String bullet2 = request.getParameter("bullet2");
			String title = request.getParameter("title");
			String idAccount = request.getParameter("idAccount");
			AccountMerch merch = userDAO.getAccountMerchByID(Integer.parseInt(idAccount));
			uploadFile file = userDAO.getuploadFileID(Integer.parseInt(id));
			file.setIdAccount(merch.getId());
			file.setBrand(brand);
			file.setTitle(title);
			file.setDes1(bullet1);
			file.setDes2(bullet2);
			file.setIp(merch.getIp());
			// dto.setNameAccount(merch.getName());
			file.setProfile(merch.getPath());
			file.setNameAccount(merch.getName());

			// file.setDay(new Date());
			// file.setStatus("0");
			// file.setUsername(user.getUsername());
			// file.setName(fileName);
			file.setNameuser("1");
			userDAO.saveOrUpdateuploadFile(file);

			return "00";

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

	@RequestMapping("/checkproduct")
	@ResponseBody
	public String checkproduct(HttpSession session, Model model, HttpServletRequest request) {
		try {
			String id = request.getParameter("id");

			AccountMerch merch = userDAO.getAccountMerchByID(Integer.parseInt(id));

			ObjectMapper objectMapper = new ObjectMapper();
			String req = objectMapper.writeValueAsString(merch);
			CallAPi callApi = new CallAPi();
			try {
				String rep = callApi.callAPIPost("http://" + merch.getIp() + ":8080/checkProduct", req);
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

	@GetMapping("/uploadMultifile")
	public String uploadMultifile(HttpSession session, HttpServletRequest request, Model model) {
		User user = (User) session.getAttribute("user");
		List<AccountMerch> lst = userDAO.getAllUser(user.getUsername());
		model.addAttribute("lst", lst);
		return "dashboard/uploadMuti";
	}

	@PostMapping("/saveMultifile")
	public String saveMutifile(@ModelAttribute uploadFile dto, @RequestParam("image") MultipartFile[] multipartFile,
			HttpSession session, RedirectAttributes ra, HttpServletRequest request) {
		try {
			User user = (User) session.getAttribute("user");
			String home = System.getProperty("user.home");
			// read and write the file to the local folder
			Arrays.asList(multipartFile).stream().forEach(file -> {

				String uploadDir = home + "/Downloads/" + user.getUsername() + "/";
				try {
					saveFile(uploadDir, file.getOriginalFilename(), file);
				} catch (Exception e) {
					// TODO: handle exception
				}

			});

			MessageHelper.addSuccessAttribute(ra, "Thanh cong");

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "redirect:/user/upload";
	}

	@GetMapping("/upload")
	public String saveUser(HttpSession session, HttpServletRequest request, Model model) {
		User user = (User) session.getAttribute("user");
		List<AccountMerch> lst = userDAO.getAllUser(user.getUsername());
		model.addAttribute("lst", lst);
		return "dashboard/uploadMuti";
	}

	@GetMapping("/imageupload")
	public String imageupload(HttpSession session, Model model, HttpServletRequest request) {
		String daySeach = request.getParameter("daySearch");
		String status = request.getParameter("status");
		String idAccountSearch = request.getParameter("idAccountSearch");

		// String daySearchTo=request.getParameter("daySearchTo");
		User user = (User) session.getAttribute("user");
		List<AccountMerch> lstacc = userDAO.getAllUser(user.getUsername());
		model.addAttribute("lstacc", lstacc);
		model.addAttribute("daySeach", daySeach);
		model.addAttribute("status", status);
		model.addAttribute("idAccountSearch", idAccountSearch);
		// model.addAttribute("daySearchTo", daySearchTo);
		if (daySeach != null && daySeach != "") {

			List<uploadFile> lst = userDAO.getAllUploadFileSearch(daySeach, daySeach, user.getUsername(), status,
					idAccountSearch);
			model.addAttribute("lst", lst);
			return "dashboard/imageupload";
		}

		model.addAttribute("lst", null);
		return "dashboard/imageupload";
	}

	@GetMapping("/product")
	public String product(HttpSession session, Model model, HttpServletRequest request) {

		String status = request.getParameter("status");
		String page=request.getParameter("page");
		String idAccountSearch = request.getParameter("idAccountSearch");
		User user = (User) session.getAttribute("user");
		List<AccountMerch> lstacc = userDAO.getAllUser(user.getUsername());
		model.addAttribute("lstacc", lstacc);
		model.addAttribute("status", status);
		int totalPage= 1;
		if(page==null|| page.isEmpty())
		{
			page="1";
		}
		model.addAttribute("page", page);
		model.addAttribute("idAccountSearch", idAccountSearch);
		if(idAccountSearch!=null && !idAccountSearch.isEmpty())
		{
			model.addAttribute("idAcc", Integer.parseInt(idAccountSearch));
		}

		List<ProductDTOVIEW> lst = userDAO.getAllProductSearch(user.getUsername(), status, idAccountSearch,page);
		if(lst!=null && !lst.isEmpty())
		{
			for (ProductDTOVIEW product : lst) {
				product.setBase64("data:image/png;base64," + Base64.getEncoder().encodeToString(product.getBobImage()));
			}
			int total=userDAO.getCountProductSearch(user.getUsername(), status, idAccountSearch);
		
			int count = total / 250;
			int phandu = total % 250;
			if (phandu > 0) {
				count = count + 1;
				totalPage=count;
			}
			if ( total <= 250) {
				totalPage = 1;
			}
		}
		
		model.addAttribute("totalPage", totalPage);
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String a = objectMapper.writeValueAsString(lst);
			System.out.println(a);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("lst", lst);
		return "dashboard/product";

	}

	@ResponseBody
	@PostMapping("/deleteimageupload")
	public String deleteimageupload(@RequestParam String checkItem, HttpSession session, Model model,
			HttpServletRequest request, RedirectAttributes ra) {
		System.out.println(checkItem);
		User user = (User) session.getAttribute("user");
		try {
			List<String> convertedCountriesList = Stream.of(checkItem.split(",", -1)).collect(Collectors.toList());
			List<Integer> ids = new ArrayList<Integer>();
			for (String string : convertedCountriesList) {
				ids.add(Integer.parseInt(string));
			}

			userDAO.deleteUploadFileFromlistID(ids);

			// lst.add(dtonew);
			// MessageHelper.addSuccessAttribute(ra, "Thanh cong");
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

	@PostMapping("/deleteproduct")
	public String deleteproduct(@RequestParam String checkItem, HttpSession session, Model model,
			HttpServletRequest request, RedirectAttributes ra) {
		System.out.println(checkItem);
		User user = (User) session.getAttribute("user");
		try {
			List<String> convertedCountriesList = Stream.of(checkItem.split(",", -1)).collect(Collectors.toList());
			List<Integer> ids = new ArrayList<Integer>();
			for (String string : convertedCountriesList) {
				ids.add(Integer.parseInt(string));
			}

			List<Product> lst = userDAO.getAllProductFromlistID(ids);
			List<String> lstIp = new ArrayList<String>();
			ObjectMapper objectMapper = new ObjectMapper();
			CallAPi callApi = new CallAPi();
			for (Product uploadFile : lst) {
				if (!lstIp.contains(uploadFile.getIp())) {
					lstIp.add(uploadFile.getIp());
				}
			}
			for (String string : lstIp) {
				List<Product> resul = new ArrayList<Product>();
				for (Product uploadFile : lst) {
					if (string.equalsIgnoreCase(uploadFile.getIp())) {
						uploadFile.setBase64(null);
						uploadFile.setBobImage(null);
						resul.add(uploadFile);
					}
				}
				try {

					String reqlst = objectMapper.writeValueAsString(resul);
					String reul = callApi.callAPIPostNotReport("http://" + string + ":8080/deleteproduct", reqlst);
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
			MessageHelper.addSuccessAttribute(ra, "Thanh cong");
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

	@PostMapping("/imageupload")
	public String imageuploadpost(@RequestParam String checkItem, HttpSession session, Model model,
			HttpServletRequest request, RedirectAttributes ra) {
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
			MessageHelper.addSuccessAttribute(ra, "Thanh cong");
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
			HttpSession session, RedirectAttributes ra, HttpServletRequest request) {
		try {

			String typeUpload = request.getParameter("typeUpload");
			if (typeUpload != null && typeUpload.equalsIgnoreCase("1")) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				System.out.println();
				User user = (User) session.getAttribute("user");
				// User savedUser = repo.save(user);
				String home = System.getProperty("user.home");
				AccountMerch merch = userDAO.getAccountMerchByID(dto.getIdAccount());
				dto.setIp(merch.getIp());
				// dto.setNameAccount(merch.getName());
				dto.setProfile(merch.getPath());

				dto.setNameAccount(merch.getName());

				dto.setDay(new Date());
				dto.setStatus("0");
				dto.setUsername(user.getUsername());
				dto.setName(fileName);
				dto.setNameuser("1");
				uploadFile dtonew = userDAO.saveOrUpdateuploadFile(dto);
				// File file = new File(home+"/Downloads/" + fileName + ".txt");
				// FileOutputStream fos = new FileOutputStream(home+"/Downloads/" +fileName);
				String uploadDir = home + "/Downloads/" + user.getUsername() + "/";
				try {
					saveFile(uploadDir, fileName, multipartFile);
				} catch (Exception e) {
					// TODO: handle exception
				}

				MessageHelper.addSuccessAttribute(ra, "Thanh cong");
			} else {

				// String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				System.out.println();
				User user = (User) session.getAttribute("user");
				// User savedUser = repo.save(user);
				// String home = System.getProperty("user.home");
				AccountMerch merch = userDAO.getAccountMerchByID(dto.getIdAccount());
				// List<uploadFile> tempStudentList = new ArrayList<uploadFile>();
				XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
				XSSFSheet worksheet = workbook.getSheetAt(0);

				for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
					try {
						uploadFile upload = new uploadFile();
						upload = dto;

						XSSFRow row = worksheet.getRow(i);
						upload.setIp(merch.getIp());
						// dto.setNameAccount(merch.getName());
						upload.setProfile(merch.getPath());
						Calendar now = Calendar.getInstance();
						now.set(Calendar.HOUR, 0);
						now.set(Calendar.MINUTE, 0);
						now.set(Calendar.SECOND, 0);
						now.set(Calendar.HOUR_OF_DAY, 0);
						upload.setNameAccount(merch.getName());

						upload.setDay(new Date());
						upload.setStatus("0");
						upload.setUsername(user.getUsername());
						upload.setName(row.getCell(0).getStringCellValue());
						upload.setTitle(row.getCell(1).getStringCellValue());
						upload.setBrand(row.getCell(2).getStringCellValue());
						upload.setDes1(getDataCell(row.getCell(3)));
						upload.setDes2(getDataCell(row.getCell(4)));
						upload.setTypeShirtUpLoad(getDataCellNotNumeric(row.getCell(5)));

						upload.setTypeTshirt(getDataCellNotNumeric(row.getCell(6)));
						upload.setMau(getDataCellNotNumeric(row.getCell(7)));
						upload.setPrice(getDataCell(row.getCell(8)));

						upload.setTypeTshirtPre(getDataCellNotNumeric(row.getCell(9)));
						upload.setMaupre(getDataCellNotNumeric(row.getCell(10)));
						upload.setPricePre(getDataCell(row.getCell(11)));

						upload.setMauVneck(getDataCellNotNumeric(row.getCell(12)));
						upload.setPriceVneck(getDataCell(row.getCell(13)));

						upload.setMauTank(getDataCellNotNumeric(row.getCell(14)));
						upload.setPriceTank(getDataCell(row.getCell(15)));
						upload.setMauLong(getDataCellNotNumeric(row.getCell(16)));
						upload.setPriceLong(getDataCell(row.getCell(17)));

						upload.setPriceRaglan(getDataCell(row.getCell(18)));

						upload.setMauSweat(getDataCellNotNumeric(row.getCell(19)));
						upload.setPriceSweat(getDataCell(row.getCell(20)));

						upload.setMauPullover(getDataCellNotNumeric(row.getCell(21)));
						upload.setPricePullover(getDataCell(row.getCell(22)));

						upload.setMauZip(getDataCellNotNumeric(row.getCell(23)));
						upload.setPriceZip(getDataCell(row.getCell(24)));

						dto.setNameuser("2");
						userDAO.saveOrUpdateuploadFile(dto);
						MessageHelper.addSuccessAttribute(ra, "Thanh cong");
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return "redirect:/user/upload";
	}

	public static String getDataCell(Cell cell) {
		String value = "";
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				value = String.valueOf(cell.getNumericCellValue());
			} else {
				value = cell.getStringCellValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;

	}

	public static String getDataCellNotNumeric(Cell cell) {
		String value = "";
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				value = String.valueOf(cell.getNumericCellValue()).replaceAll(".0", "");
			} else {
				value = cell.getStringCellValue();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;

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
