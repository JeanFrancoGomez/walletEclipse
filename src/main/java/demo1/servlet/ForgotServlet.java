package demo1.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import demo1.enity.Users;
import demo1.manager.SendEmailManager;
import demo1.manager.UsersManager;
import demo1.utility.ApplicationProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/forgotPassword")
public class ForgotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ApplicationProperties appProp;
	private UsersManager userManager;
	private SendEmailManager emailManager;

	public ForgotServlet() {
		super();
		this.appProp = ApplicationProperties.getInstance();
		this.userManager = new UsersManager();
		this.emailManager = new SendEmailManager();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws NoResultException {
		try {
			String key = request.getParameter("secret");
			Users user = this.userManager.findUserBySecretKey(key);

			if (key != null && user != null) {
				request.setAttribute( "name", user.getName() );
				request.setAttribute( "emailTest", user.getEmail() );
				request.getServletContext().getRequestDispatcher("/newPassword.jsp").include(request, response);
			} else {
				throw new RuntimeException("Wrong secret key");
			}
		} catch ( ServletException | IOException e1) {
			e1.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			String email = request.getParameter("email");
			Users user = this.userManager.findByEmail(email);

			if (user == null) {
				request.setAttribute("emailRep", email);
				request.getServletContext().getRequestDispatcher("/notFoundUser.jsp").forward(request, response);
			} else {
				KeyGenerator keyGen = KeyGenerator.getInstance("AES");
				keyGen.init(128);
				SecretKey secretKey = keyGen.generateKey();
				String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
				
				user.setSecretKey(key);
				this.userManager.merge(user);
				
				String htmlMessage = "<h3>By clicking on the link you can change the current password.</h3>" + "<a href='"
						+ appProp.getUriServer() + "/forgotPassword?secret=" + key + "'>" + "Reset password" + "</a>";

				this.emailManager.send("Reset password", email, htmlMessage);
				request.setAttribute("emailRep", email);
				request.getServletContext().getRequestDispatcher("/messageRecoverPassword.jsp").forward(request, response);
			}

		} catch (NoSuchAlgorithmException | MessagingException e1) {
			e1.printStackTrace();

		}
	}

}
