package demo1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	//private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServlet.class);
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("inputEmail");
        String inputPassword = request.getParameter("inputPassword");
        String repeatPassword = request.getParameter("repeatPassword");  
        
        if(inputPassword.equals(repeatPassword)){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wallet?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234");

                String cipherPass = PasswordManager.createHash(inputPassword);

                String query = "INSERT INTO users (email, name, surname, password) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, firstName);
                statement.setString(3, lastName);
                statement.setString(4, cipherPass);
                statement.executeUpdate();
                
                //LOGGER.info("New user,is join -> "+email);

            } catch (ClassNotFoundException | SQLException e) {
            	//LOGGER.error("Error during register: ", e);
            }
            request.setAttribute("nameNewUser",firstName);
            request.getServletContext().getRequestDispatcher("/registerSuccess.jsp" ).include(request,response);
        }
        else{
            request.getServletContext().getRequestDispatcher("/passwordDif.jsp" ).include(request,response);
        }
    }

}