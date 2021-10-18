package demo1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Received login request");      
        
        response.setContentType("text/html");
        String message;
        
        
        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wallet?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "1234");
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() && PasswordManager.validatePassword(password, resultSet.getString("password"))) {
                message = resultSet.getString("name") + " " + resultSet.getString("surname");
                request.setAttribute("message", message);
                request.getRequestDispatcher("main.jsp").forward(request, response);
            }
            else {
                request.setAttribute("message", "ERROR: No user found! :(");
                request.getRequestDispatcher("notFoundUser.jsp").forward(request, response);
            }

        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Error during login: ", e);
        }

    }

}
