package com.talk2amareswaran.projects.socialloginapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.talk2amareswaran.projects.socialloginapp.dao.AppRoleDAO;
import com.talk2amareswaran.projects.socialloginapp.dao.AppUserDAO;
import com.talk2amareswaran.projects.socialloginapp.dao.UserRoleDao;
import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;
import com.talk2amareswaran.projects.socialloginapp.entity.AppUser;
import com.talk2amareswaran.projects.socialloginapp.entity.FileDB;
import com.talk2amareswaran.projects.socialloginapp.entity.UserRole;
import com.talk2amareswaran.projects.socialloginapp.entity.Waranty;
import com.talk2amareswaran.projects.socialloginapp.form.AppUserForm;
import com.talk2amareswaran.projects.socialloginapp.repository.FileRepository;
import com.talk2amareswaran.projects.socialloginapp.repository.UserRepository;
import com.talk2amareswaran.projects.socialloginapp.repository.UserRoleRepository;
import com.talk2amareswaran.projects.socialloginapp.repository.WarantyRepository;
import com.talk2amareswaran.projects.socialloginapp.service.FileStorageService;
import com.talk2amareswaran.projects.socialloginapp.utils.EncrytedPasswordUtils;
import com.talk2amareswaran.projects.socialloginapp.utils.SecurityUtil;
import com.talk2amareswaran.projects.socialloginapp.utils.WebUtils;
import com.talk2amareswaran.projects.socialloginapp.validator.AppUserValidator;
import com.talk2amareswaran.projects.socialloginapp.service.AppRoleService;
import com.talk2amareswaran.projects.socialloginapp.service.UserDetailsServiceImpl;


@Controller
@Transactional
public class MainController {

	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	@Autowired
	private UsersConnectionRepository connectionRepository;

	@Autowired
	private AppUserValidator appUserValidator;

	@Autowired
    private UserRepository userRepo;

    @Autowired
    private UserRoleRepository userRoleRepo;

    @Autowired
    private WarantyRepository warantyRepo;

    @Autowired
    private FileStorageService storageService;

    @Autowired 
    AppRoleService appRoleService;

    @Autowired
    UserDetailsServiceImpl userService;

    @Autowired
    private FileRepository fileRepo;

    @Value("${spring.data.cassandra.username}")
    private String admin;

    //private final Logger log = LoggerFactory.getLogger(this.getClass());

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == AppUserForm.class) {
			dataBinder.setValidator(appUserValidator);
		}
	}

	/*@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("message", "Warranty CRUD Application!");
		return "welcomePage";
	}*/

//this is the redirect url after login ROLE_USER
	/*@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("User Name: " + userName);
		UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("waranty", new Waranty());
		return "addNewElement.html";    
	}*/

  /*@GetMapping("/admin") //this is the redirect url after login ROLE_ADMIN
  public String adminAdd(Model model,Principal principal) {
    String userName = principal.getName();
		System.out.println("User Name: " + userName);
		UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);
    model.addAttribute("waranty", new Waranty());
    return "adminAddNew.html";
  }*/

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			UserDetails loginedUser = (UserDetails) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = { "/signin" }, method = RequestMethod.GET)
	public String signInPage(Model model) {
		return "redirect:/";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String signupPage(WebRequest request, Model model) {
		ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator,
				connectionRepository);
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		AppUserForm myForm = null;
		if (connection != null) {
			myForm = new AppUserForm(connection);
		} else {
			myForm = new AppUserForm();
		}
		model.addAttribute("myForm", myForm);
		return "registration.html";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public String signupSave(WebRequest request, //
			Model model, //
			@ModelAttribute("myForm") @Validated AppUserForm appUserForm, //
			BindingResult result, //
			final RedirectAttributes redirectAttributes) {
		// Validation error.
		if (result.hasErrors()) {
			return "signupPage";
		}
   
    AppUser appUser = userService.registerUser(appUserForm);
    AppUser user = null;
		try {
      user = userRepo.save(appUser);
      appRoleService.createRoleFor(user);
      /*SendingEmail email = new SendingEmail(javaMailSender);
	        email.sendEmail(registered.getEmail());
	        return "verify.html";*/

		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("errorMessage", "Error " + ex.getMessage());
			return "signupPage";
		}
    
		if (appUserForm.getSignInProvider() != null) {
			ProviderSignInUtils providerSignInUtils //
					= new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
			providerSignInUtils.doPostSignUp(user.getUserName(), request);
		}

    List<String> roleNames = new ArrayList<>();
    roleNames.add(AppRole.ROLE_ADMIN);
    roleNames.add(AppRole.ROLE_USER);
    
		SecurityUtil.logInUser(user, roleNames);

		return "redirect:/";
	}

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "login.html";
  }
  //ADMIN felület
  @GetMapping("/users")
  public String goToAdmin(Model model) {
    List<AppUser> user = userRepo.findAll();
    List<AppUser> finalUser = new ArrayList<>();
    for (AppUser u : user) {
      if (!u.getUserName().equals(admin)) {
        finalUser.add(u);
      }
    }
    model.addAttribute("users", finalUser);

    return "admin.html";
  }

  @GetMapping("/deleteUser/{email}") //törlés előtt kérdes h biztos e benne h törli!!!!
  public String adminDeleteUserAndWarrantys(
    @PathVariable String email,
    Model model
  ) {
    AppUser u = userRepo.findByEmail(email);
    model.addAttribute("user", u);
    return "deleteUser.html";
  }

  @GetMapping("/confirmDeleteUser/{email}") //a törölt adatok külön táblába átmásolása x ideig megőrzése h vissza lehessen állítani az adatokat ha szükséges!
  public String confirmDelete(@PathVariable String email, Model model) {
    String message = "";
    model.addAttribute("username", email);
    AppUser u = userRepo.findByEmail(email);
    List<Waranty> waranty = warantyRepo.foreignKey(email);
    UserRole userRole = userRoleRepo.findUserRoleByAppUserId(u.getUserId());
    //UserRole userRole = userRoleDao.findUserRole(u.getUserId());
    try {
      userRoleRepo.deleteById(userRole.getId());
      userRepo.deleteById(u.getUserId());
      for (Waranty w : waranty) {
        warantyRepo.deleteById(w.getId());
        List<FileDB> db = fileRepo.findFileDB(w.getId());
        for (FileDB f : db) {
          fileRepo.deleteById(f.getId());
        }
      }
      message = "You have successfully deleted user " + email;
      model.addAttribute("message", message);
    } catch (Exception e) {
      message = "Deleted was not successfully with username " + email;
      model.addAttribute("message", message);
      e.printStackTrace();
    }

    return "deleteSuccess.html";
  }

  @GetMapping("/edit/{email}")
  public String adminEdit(@PathVariable String email, Model model) {
    List<Waranty> listWaranty = warantyRepo.findAllWarrantys(email);
    model.addAttribute("listWaranty", listWaranty);
    model.addAttribute("username", email);
    return "adminEditWarrantys.html";
  }

  @GetMapping("adminAddNewGet")
  public String adminAdd(Model model,HttpServletRequest request) {
    model.addAttribute("waranty", new Waranty());
	  HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    List<Waranty> waranty = warantyRepo.findExpired(foreignKey);
    waranty.forEach(w ->{
      w.setExpired("lejart");
    });
    return "adminAddNew.html";
  }

  @PostMapping("adminAddNew")
  public String adminAddPost(
    Model model,
    Waranty waranty,
    HttpServletRequest request,
    @RequestParam("file") MultipartFile[] file
  )
    throws IOException {
    HttpSession session = request.getSession(true);
    String username = session.getAttribute("userName").toString();
    waranty.setExpired("ervenyes");
    waranty.setForeignKey(username);
    Waranty waranty2 = warantyRepo.save(waranty);
    storageService.store(file, waranty2.getId());
    model.addAttribute("waranty", waranty2);
    return "adminAddNewSuccess";
  }
  @GetMapping("/adminValidWarrantys")
  public String adminListWarantys(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    List<Waranty> listWaranty = warantyRepo.findValidWarantys(foreignKey);
    model.addAttribute("listWaranty", listWaranty);
    List<FileDB> files = storageService.getAllActiveImages();
    model.addAttribute("files", files);

    return "adminValidWarrantys.html";
  }

  @GetMapping("/adminExpiredWarrantys")
  public String adminExpiredListWarantys(
    Model model,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    List<Waranty> listWaranty = warantyRepo.findExpired(foreignKey);
    model.addAttribute("listWaranty", listWaranty);
    List<FileDB> files = storageService.getAllActiveImages();
    model.addAttribute("files", files);

    return "adminExpiredWarrantys.html";
  }

  @GetMapping("/deleteWarrantyLink/{id}") //megerősítő kérdés frontend oldalon a usernek!!!
  public String adminDeleteWarranty(@PathVariable long id, Model model)
    throws IOException {
    Optional<Waranty> waranty = warantyRepo.findById(id);
    if (waranty.isPresent()) {
      waranty.get();
      List<Waranty> list = new ArrayList<>();
      list.add(waranty.get());
      model.addAttribute("waranty", list);
      return "deleteWarranty.html";
    } else {
      throw new RuntimeException("Warranty not found for the id " + id);
    }
  }

  @GetMapping("/confirmDeleteWarranty")
  public String confirmDeleteWarranty(
    @RequestParam("id") long id,
    Model model
  ) {
    String message = "";
    List<FileDB> file = fileRepo.findFileByWarantyId(id);
    try {
      warantyRepo.deleteById(id);
      for (FileDB f : file) {
        fileRepo.deleteById(f.getId());
      }
      message = "You have successfully deleted warranty!";
      model.addAttribute("message", message);
    } catch (Exception e) {
      message = "You have not successfully deleted warranty!";
      model.addAttribute("message", message);

      e.printStackTrace();
    }
    return "deleteSuccessWarranty.html";
  }
   //Download
   @GetMapping("/download/{id}")
   //@ResponseBody
   public String showImage2(
	 @PathVariable Long id,
	 HttpServletResponse response,
	 Optional<FileDB> imageGallery,
	 Model model
   )
	 throws ServletException, IOException {
	 //log.info("Id :: " + id);
	 Optional<FileDB> images = fileRepo.findById(id);
	 model.addAttribute("images", images);
	 byte[] files = null;
	 //model.addAttribute("images",images);
	 if (images.isPresent()) {
	   files = images.get().getData();
	   response.setContentType(
		 "image/jpeg,image/png,image/gif,text/rtf,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
	   );
	   String headerKey = "Content-Disposition";
	   String headerValue = "attachment; filename =" + images.get().getName();
	   response.setHeader(headerKey, headerValue);
	   response.getOutputStream().write(images.get().getData());
	   response.getOutputStream().flush();
	   response.getOutputStream().close();
	 }
	 /*if(files==null){	
			 return "noFileforDownload.html";
			 }*/
 
	 return "downloadFailed.html";
   }
 
   @GetMapping(
	 value = "/displayImage/{id}",
	 produces = MediaType.IMAGE_JPEG_VALUE
   )
   public void fromDatabaseAsHttpServResp(
	 @PathVariable Long id,
	 Model model,
	 HttpServletRequest request,
	 HttpServletResponse response
   )
	 throws SQLException, IOException {
	 Optional<FileDB> files = fileRepo.findById(id);
	 model.addAttribute("images", files);
	 if (files.isPresent()) {
	   response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
	   response.getOutputStream().write(files.get().getData());
     response.getOutputStream().flush();
	   response.getOutputStream().close();
	 }
   }
    // add new element process
  @PostMapping("/addNewElement")
  public String addNew(
    Model model,
    Waranty waranty,
    HttpServletRequest request,
    @RequestParam("file") MultipartFile[] file
  )
    throws IOException {
    HttpSession session = request.getSession(true);
    String username = session.getAttribute("userName").toString();
    waranty.setExpired("ervenyes");
    waranty.setForeignKey(username);
    try {
      Waranty waranty2 = warantyRepo.save(waranty);
      model.addAttribute("waranty", waranty2);
      storageService.store(file, waranty2.getId());
      return "addNewSuccess";
    } catch (Exception e) {
      e.printStackTrace();
      return "failureFileUpload";
    }
  }

  //User felület
  @GetMapping("/addNew")
  public String goToAddNewElement(Model model,HttpServletRequest request) {
    model.addAttribute("waranty", new Waranty());
	  HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    List<Waranty> waranty = warantyRepo.findExpired(foreignKey);
    waranty.forEach(w ->{
      w.setExpired("lejart");
    });
    return "addNewElement.html";
  }

  //after login it loads the warantys
  @GetMapping("/warantys")
  public String listWarantys(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    List<Waranty> listWaranty = warantyRepo.findValidWarantys(foreignKey);
    model.addAttribute("listWaranty", listWaranty);
    List<FileDB> files = storageService.getAllActiveImages();
    model.addAttribute("files", files);

    return "warantys";
  }

  @GetMapping("/forgotPassword")
  public String forgotPassword(Model model) {
    model.addAttribute("user", new AppUserForm());
    return "forgotPassword.html";
  }

  @PostMapping("/forgotPassword-process")
  public String update(AppUserForm u,HttpServletRequest request,Model model) {
    //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encrytedPassword = EncrytedPasswordUtils.encrytePassword(u.getPassword());
      AppUser user = userRepo.findByEmail(u.getUserName());
			user.setEncrytedPassword("{bcrypt}"+encrytedPassword);
      userRepo.update(u.getUserName(), user.getEncrytedPassword()); //beilleszt még egy admin UserRole-t ki kell javitani!!!
    /*SendingEmail email = new SendingEmail(javaMailSender);
	        email.sendEmail(user.getEmail());
	        return "verify.html";*/
      String message = "Updated successfully!";
      model.addAttribute("message",message);
      return "login.html";
  } //@RequestParam("password") Null, mert AppUser class ban a password nullable=false*/


  @GetMapping("/expired")
  public String goExp(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    //List<Waranty> listWaranty2 = new ArrayList<Waranty>();
    List<Waranty> listWaranty = warantyRepo.findExpired(foreignKey);
    //this method adds the user infos to the table in client side
    model.addAttribute("listExpiredWaranty", listWaranty);
    return "expiredWarantys.html";
  }

  @GetMapping("/update/{id}")
  public String updateHtml(
    @PathVariable long id,
    Model model,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    Optional<Waranty> waranty = warantyRepo.findById(id);
    String message = "";
    if (foreignKey.equals(admin)) {
      if (waranty.isPresent()) {
        waranty.get();
        model.addAttribute("waranty", waranty);
        return "adminUpdateWarranty.html";
      } else {
        message = "Cannot get datas from database!";
        model.addAttribute("message", message);
        return "adminUpdateWarranty.html";
      }
    } else if (!foreignKey.equals(admin)) {
      if (waranty.isPresent()) {
        waranty.get();
        model.addAttribute("waranty", waranty);
        return "update.html";
      } else {
        message = "Cannot get datas from database!";
        model.addAttribute("message", message);
        return "update.html";
      }
    }

    return "error.html";
  }

  @PostMapping("/updateTrial")
  public String update(
    Waranty war,
    @RequestParam("id") Long id,
    Model model,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    try {
      warantyRepo.save(war); //Waranty war a th:action = "/updateTrial" template-járől szedi ki a Waranty objektum elemeit azokat, amik th:field-ben szerepelnek. EZ fontos, mert csak ezek kapnak értéket, ami nincs ott az null lesz.
      model.addAttribute("warantyId", id);
      if (foreignKey.equals(admin)) {
        return "adminUpdateSuccess.html";
      }
      return "updateSuccess.html";
    } catch (Exception e) {
      String message = "Update was not successfull";
      model.addAttribute("message", message);
      return "adminUpdateWarranty.html";
    }
  }

  @PostMapping("/fileupdate")
  public String updateFile(
    HttpServletRequest request,
    @RequestParam("file") MultipartFile[] file,
    @RequestParam("id") Long id,
    Model model,
    RedirectAttributes attributes,
    HttpServletResponse response
  )
    throws IOException {
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    String message = "";
    model.addAttribute("message", message);
    try {
      if (foreignKey.equals(admin)) {
        storageService.updateStore(file, id);
        return "adminUpdateFileSuccess.html";
      } else {
        storageService.updateStore(file, id);
        return "fileUploadSuccess";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "failureFileUpload";
    }
  }
  @GetMapping("/imageDownload/{id}")
  public String goToDownload(
    @PathVariable long id,
    Model model,
    HttpServletRequest request
  )
    throws IOException, SQLException {
    List<FileDB> images = fileRepo.getImage(id);
    HttpSession session = request.getSession(true);
    String foreignKey = session.getAttribute("userName").toString();
    model.addAttribute("warrantyId", id);
    if (images != null) {
      //images.get();
      model.addAttribute("images", images);
      if (foreignKey.equals(admin)) {
        return "adminImageDownload.html";
      }
      return "imageDownload.html";
    }
    if (images == null) {
      return "noFileforDownload.html";
    } else {
      throw new RuntimeException("Warranty not found for the id " + id);
    }
  }


}
