package demo1.servlet;

import java.io.IOException;

import demo1.enity.Users;
import demo1.manager.PasswordManager;
import demo1.manager.UsersManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/changePassword")
public class ChangePasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UsersManager userManager;
	
	public ChangePasswordServlet() {
		super();
		this.userManager = new UsersManager();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		
		String email = request.getParameter("emailIn");
		String newPass = request.getParameter("inPassword");
		String repPass = request.getParameter("repPassword");
		
		Users user = this.userManager.findByEmail(email);
		if(newPass.equals(repPass)) {
			user.setPassword(PasswordManager.createHash(newPass));
			this.userManager.merge(user);
			request.setAttribute( "name", user.getName() );
			request.getServletContext().getRequestDispatcher("/messagePasswordChange.jsp").forward(request, response);
		}
		else {
			request.getServletContext().getRequestDispatcher("/passDif_changePass.jsp").include(request, response);
		}
		
	}

}