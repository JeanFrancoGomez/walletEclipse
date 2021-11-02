package demo1.servlet;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo1.enity.Users;
import demo1.enity.UsersStatus;
import demo1.manager.PasswordManager;
import demo1.manager.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		
		if ( accUser!=null && PasswordManager.validatePassword(password, accUser.getPassword() )) {
			message = accUser.getName() + " " + accUser.getSurname();
			if( accUser.getStateUser()==UsersStatus.TO_CONFIRM ) {
				request.setAttribute("message", message);
				request.getRequestDispatcher("messageConfirmUser.jsp").forward(request, response);
			}
			else if( accUser.getStateUser()==UsersStatus.TO_DELETE ) {
				request.setAttribute("message", message);
				request.getRequestDispatcher("messageUserDeleted.jsp").forward(request, response);
			}
			request.setAttribute("message", message);
			request.getRequestDispatcher("main.jsp").forward(request, response);
		} else {
			request.setAttribute("message", "ERROR: No user found! :(");
			request.getRequestDispatcher("notFoundUser.jsp").forward(request, response);
		}

	}

}
