package demo1.servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.enity.Users;
import demo1.enity.UsersStatus;
import demo1.manager.PasswordManager;
import demo1.manager.UsersManager;
import demo1.utility.StartupListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);
	private UsersManager userManager;

	public LoginServlet() {
		super();
		this.userManager = new UsersManager();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("Received login request");

		String message;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Users accUser = this.userManager.findByEmail(email);
		if ( accUser!=null && PasswordManager.validatePassword( password, accUser.getPassword() ) ) {
			if( accUser.getStateUser()==UsersStatus.TO_CONFIRM ) {
				message = accUser.getName() + " " + accUser.getSurname() + "user to confirm, look your email.";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/notFoundUser.jsp").forward(request, response);
			}
			else if( accUser.getStateUser()==UsersStatus.TO_DELETE ) {
				message = accUser.getName() + " " + accUser.getSurname() + "we working to delete your email";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/notFoundUser.jsp").forward(request, response);
			}
			else {
				KeyGenerator keyGen = null;
				try { keyGen = KeyGenerator.getInstance("AES");	}
				catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
				
				keyGen.init(128);
				SecretKey strongKey = keyGen.generateKey();
				String sessionKey = Base64.getEncoder().encodeToString(strongKey.getEncoded());
				
				//Save in database
				accUser.setSessionKey(sessionKey);
				accUser.setDateStartSession(new Date());
				this.userManager.merge(accUser);
				
				//Save in cache
				StartupListener.putCache(email,sessionKey);
				
				Cookie myCookieKey = new Cookie("sessionKey", sessionKey);
				myCookieKey.setMaxAge(20);
				response.addCookie(myCookieKey);
				
				Cookie myCookieEmail = new Cookie("email", email);
				response.addCookie(myCookieEmail);
				
				
				message = accUser.getName() + " " + accUser.getSurname() + " WELCOME TO SESSION";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/sessionExample.jsp").forward(request, response);
			}
		}
		else {
				request.setAttribute("message", "ERROR: User not found!");
				request.getRequestDispatcher("/notFoundUser.jsp").forward(request, response);
		}
		
	}
	
}
