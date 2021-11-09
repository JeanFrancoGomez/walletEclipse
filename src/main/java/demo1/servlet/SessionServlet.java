package demo1.servlet;

import java.io.IOException;

import demo1.utility.StartupListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SessionServlet
 */
@WebServlet("/sessionServlet")
public class SessionServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    /**
     * Default constructor. 
     */
    public SessionServlet() {
    	super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		String sessionKey=null;
		String email=null;		
		
		for(Cookie c : cookies) {
			if(c.getName().equals("email")) {
				email=c.getValue();
			}
			else if(c.getName().equals("sessionKey")) {
				sessionKey=c.getValue();
			}
		}
		
		if ( sessionKey!=null ) {
			
			String keySessionUser = StartupListener.getCache(email);
			String mes=null;
			
			if( keySessionUser!=null && keySessionUser.equals(sessionKey) ) {
				mes= "THE COOKIE IS IN THE CACHE";
				request.setAttribute("message", mes);
				request.getRequestDispatcher("./sessionExample.jsp").forward(request, response);
			}
			else {
				mes="COOKIE NOT FOUND SEARCH IN DATABASE";
				request.setAttribute("message", mes);
				request.getRequestDispatcher("/sessionExample.jsp").forward(request, response);
			}
			
		}
		else {
			request.getRequestDispatcher("/").forward(request, response);		
		}
	}

}
