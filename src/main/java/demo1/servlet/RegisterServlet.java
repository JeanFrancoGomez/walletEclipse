package demo1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.enity.Users;
import demo1.enity.UsersRole;
import demo1.enity.UsersStatus;
import demo1.manager.PasswordManager;
import demo1.manager.SendEmailManager;
import demo1.manager.UsersManager;
import demo1.utility.ApplicationProperties;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Base64;

@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServlet.class);
	private ApplicationProperties appProp;
	private UsersManager userManager;
	private SendEmailManager emailManager;

	public RegisterServlet() {
		super();
		this.appProp = ApplicationProperties.getInstance();
		this.userManager = new UsersManager();
		this.emailManager = new SendEmailManager();
	}

	private boolean confirmUser(String secretKey) {
		Users result = null;
		result = this.userManager.findUserBySecretKey(secretKey);
		if ( result != null ) {
			result.setStateUser( UsersStatus.CONFIRMED );
			this.userManager.merge(result);
			return true;
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String secretKey = request.getParameter("secret");

		if ( secretKey!=null && confirmUser(secretKey) ) {
			request.getServletContext().getRequestDispatcher("/index.jsp").include(request, response);
		} else {
			throw new RuntimeException("Wrong secret key");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(128);
			SecretKey secretKey = keyGen.generateKey();
			String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			
			String name = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("inputEmail");
			String pass = request.getParameter("inputPassword");
			String repPass = request.getParameter("repeatPassword");


			if ( this.userManager.findByEmail(email)!=null ) {
				request.setAttribute("emailRep", email);
				String info = "The email " + email + " attempted to log in";
				request.getServletContext().getRequestDispatcher("/existingEmail.jsp").forward(request, response);
				LOGGER.info(info);
				//throw new Exception(info);
			}

			if (pass.equals(repPass)) {

				Users newUser = new Users(UsersRole.USER, email, name, lastName, PasswordManager.createHash(pass), 
						key, new Date(), UsersStatus.TO_CONFIRM);
				this.userManager.create(newUser);

				String htmlMessage = "<h3>By clicking on the link you accept the processing of your data for access to our service, thank you.</h3>"
						+ "<a href='" + appProp.getUriServer() + "/register?secret=" + key + "'>"
						+ "Confirm registration" + "</a>";

				this.emailManager.send("Confirm user", email,htmlMessage);

				request.setAttribute("nameNewUser", name);
				request.getServletContext().getRequestDispatcher("/registerSuccess.jsp").include(request, response);
			} else {
				request.getServletContext().getRequestDispatcher("/passwordDif.jsp").include(request, response);
			}

		} catch ( NoSuchAlgorithmException | MessagingException e1) {
			e1.printStackTrace();
		}
	}

}