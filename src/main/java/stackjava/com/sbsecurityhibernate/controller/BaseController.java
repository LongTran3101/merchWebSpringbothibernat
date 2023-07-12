package stackjava.com.sbsecurityhibernate.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import stackjava.com.sbsecurityhibernate.dao.UserDAO;
import stackjava.com.sbsecurityhibernate.entities.AccountMerch;
import stackjava.com.sbsecurityhibernate.entities.Image;
import stackjava.com.sbsecurityhibernate.entities.ImageMerch;
import stackjava.com.sbsecurityhibernate.entities.ListPoductDTO;
import stackjava.com.sbsecurityhibernate.entities.Product;
import stackjava.com.sbsecurityhibernate.entities.SaleMerch;
import stackjava.com.sbsecurityhibernate.entities.User;
import stackjava.com.sbsecurityhibernate.entities.uploadFile;

@Controller
public class BaseController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ServletContext servletContext;

	@RequestMapping(value = { "/", "/login" })
	public String login(@RequestParam(required = false) String message, final Model model) {
		if (message != null && !message.isEmpty()) {
			if (message.equals("logout")) {
				model.addAttribute("message", "Logout!");
			}
			if (message.equals("error")) {
				model.addAttribute("message", "Login Failed!");
			}
		}
		return "login";
	}

	@RequestMapping
	public String test2() {
		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "/saveCheckSale", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String test(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {

		try {
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			ObjectMapper objectMapper = new ObjectMapper();
			SaleMerch mech = objectMapper.readValue(req, SaleMerch.class);
			Date saleImage = mech.getDay();
			Calendar saleImagenow = Calendar.getInstance();
			saleImagenow.setTime(saleImage);
			saleImagenow.set(Calendar.HOUR, 0);
			saleImagenow.set(Calendar.MINUTE, 0);
			saleImagenow.set(Calendar.SECOND, 0);
			saleImagenow.set(Calendar.HOUR_OF_DAY, 0);
			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.HOUR_OF_DAY, 0);
			mech.setDay(now.getTime());
			mech.setIp(formatter.format(new Date()));
			// System.out.println(formatter.format(new Date()));
			List<ImageMerch> LstimageMerch = new ArrayList<>();
			LstimageMerch = mech.getLstimageMerch();
			if (LstimageMerch != null && !LstimageMerch.isEmpty()) {
				try {
					System.out.println(saleImage.toString() + " -----saleImage");
					// System.out.println(saleImage.get);
					userDAO.deleteImage(LstimageMerch, saleImagenow.getTime());
				} catch (Exception e) {
					// TODO: handle exception
				}
				userDAO.saveOrUpdate(LstimageMerch, saleImage);
			}
			try {
				try {
					userDAO.deleteSaleMerch(mech.getUsername(), mech.getName(), now.getTime());
				} catch (Exception e) {
					// TODO: handle exception
				}

				// userDAO.saveOrUpdate(mech);
			} catch (Exception e) {
				// TODO: handle exception
			}
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

	@ResponseBody
	@RequestMapping(value = "/saveProduct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String saveProduct(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {

		try {
			Boolean check = false;
			ObjectMapper objectMapper = new ObjectMapper();
			ListPoductDTO mech = objectMapper.readValue(req, ListPoductDTO.class);

			Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.HOUR_OF_DAY, 0);
			List<Product> lst = mech.getList();
			// System.out.println(formatter.format(new Date()));

			if (lst != null && !lst.isEmpty()) {
					try {
						int kq=userDAO.deleteProductStatusdelete(lst.get(0).getAcc());
						 kq=userDAO.updateDayCheckProduct(lst.get(0).getAcc(), now.getTime());
							for (Product product : lst) {
								product.setDayUpdate(new Date());
								try {
									kq=userDAO.deleteProduct(product.getAsin());
									java.net.URL url = new java.net.URL(product.getUrlPreview());
									InputStream is = url.openStream();
									byte[] imageBytes = org.apache.commons.io.IOUtils.toByteArray(is);
									BufferedImage bImageFromConvert = ImageIO.read(new ByteArrayInputStream(imageBytes));
									int type = bImageFromConvert.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
											: bImageFromConvert.getType();
									BufferedImage resizeImageJpg = resizeImage(bImageFromConvert, type, 80, 108);
									ByteArrayOutputStream os = new ByteArrayOutputStream();
									ImageIO.write(resizeImageJpg, "png", os);
									product.setBobImage(os.toByteArray());
									product.setDayUpdate(new Date());
								} catch (Exception e) {
									// TODO: handle exception
								}
								userDAO.saveOrUpdateProduct(product);
							}

					} catch (Exception e) {
						// TODO: handle exception
					}
					
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}
	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT) {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();

		return resizedImage;
	}

	@ResponseBody
	@RequestMapping(value = "/updateStatusProduct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String updateStatusProduct(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {

		try {
			Boolean check = false;
			ObjectMapper objectMapper = new ObjectMapper();
			Product mech = objectMapper.readValue(req, Product.class);
			if (mech != null) {
				try {

					userDAO.updateStatusProduct(mech.getAsin());
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			if (check) {
				return "00";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	@ResponseBody
	@RequestMapping(value = "/saveImageUpLoad", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String saveImageUpload(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			/*
			 * List<uploadFile> mechlst = objectMapper.readValue(req, new
			 * TypeReference<List<uploadFile>>() { });
			 */
			uploadFile mech = objectMapper.readValue(req, uploadFile.class);
			userDAO.saveOrUpdateuploadFile(mech);

			// model.addAttribute("lst", lst);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	@ResponseBody
	@PostMapping("/uploadMultifileExcel")
	public String uploadMultifileExcel(@RequestBody String req, HttpServletRequest request, HttpServletResponse resp) {
		try {

			String home = System.getProperty("user.home");
			ObjectMapper objectMapper = new ObjectMapper();

			Image mech = objectMapper.readValue(req, Image.class);

			String base64ImageString = mech.getBaseFile().replace("data:image/png;base64,", "");
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64ImageString);
			InputStream is = new ByteArrayInputStream(imageBytes);
			String uploadDir = home + "/Downloads/" + mech.getUsernam() + "/";
			try {
				Path uploadPath = Paths.get(uploadDir);

				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				Path filePath = uploadPath.resolve(mech.getName());
				Files.deleteIfExists(filePath);
				Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ok";
	}

	@GetMapping("/download2")
	public ResponseEntity<ByteArrayResource> downloadFile2(@RequestParam int imageid) {
		try {
			MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, "solo levelling.jpg");
			// System.out.println("fileName: " + fileName);
			// System.out.println("mediaType: " + mediaType);
			uploadFile up = userDAO.getFileUploadByid(imageid);

			String home = System.getProperty("user.home");
			// File file = new File(home+"/Downloads/" + fileName + ".txt");
			// FileOutputStream fos = new FileOutputStream(home+"/Downloads/" +fileName);
			Path path = Paths.get(home + "/Downloads/" + up.getUsername() + "/" + up.getName());
			byte[] data = Files.readAllBytes(path);
			ByteArrayResource resource = new ByteArrayResource(data);

			return ResponseEntity.ok()
					// Content-Disposition
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
					// Content-Type
					.contentType(mediaType) //
					// Content-Lengh
					.contentLength(data.length) //
					.body(resource);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	@ResponseBody
	@RequestMapping(value = "/getallaccfromip", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	private String getAllAcc(HttpServletRequest request, HttpServletResponse resp) {

		try {
			String ip = request.getRemoteAddr();
			System.out.println("ip---------" + ip);
			ObjectMapper objectMapper = new ObjectMapper();
			List<AccountMerch> lstacc = userDAO.getAccountMerchByip(ip);
			String req = objectMapper.writeValueAsString(lstacc);
			return req;

			// model.addAttribute("lst", lst);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "01";

	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/upload", consumes =
	 * MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) private String Upload( @RequestBody String
	 * req,HttpServletRequest request, HttpServletResponse resp) {
	 * 
	 * try {
	 * 
	 * 
	 * ObjectMapper objectMapper = new ObjectMapper(); uploadFile
	 * mech=objectMapper.readValue(req, uploadFile.class);
	 * System.out.println(mech.getName());
	 * 
	 * return "ok";
	 * 
	 * //model.addAttribute("lst", lst);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return"01";
	 * 
	 * 
	 * }
	 */

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {

			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/";
	}

	@ResponseBody
	@RequestMapping("/api")
	public String api() {
		return "api";
	}

	@ResponseBody
	@RequestMapping("/signup")
	public String signup(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		User acc = new User();
		acc.setPassword(bCryptPasswordEncoder.encode(pass));
		acc.setUsername(name);
		acc.setEnabled(1);
		userDAO.saveOrUpdateUser(acc);

		return "00";
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}
}
