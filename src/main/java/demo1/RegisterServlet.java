package demo1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.Properties;
import java.time.LocalDateTime;


@WebServlet(value = "/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ApplicationProperties appProp = ApplicationProperties.getInstance();    
	private final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
    private boolean insertData(String secret) throws ClassNotFoundException, IOException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(appProp.getUriMySql(), appProp.getUserMySql(), appProp.getPswMySql());
        String query = "SELECT secretKey FROM users WHERE secretKey=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, secret);
        ResultSet rs = statement.executeQuery();

        try {
	        if( rs.next() && secret.equals(rs.getString("secretKey")) ) {
	        	//User insert correctly
	        	return true;
	        }
	        else {
	        	cancelSecretKey(secret);
	        }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secret = request.getParameter("secret");
        //
        if(secret != null) {
            try {
                if(insertData(secret)) {
                    request.getServletContext().getRequestDispatcher("/login.jsp" ).include(request,response);
                }
                else {
                	 request.getServletContext().getRequestDispatcher("/notFoundUser.jsp" ).include(request,response);
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        else {
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
	
	        Properties properties = System.getProperties();
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.port", "587");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	
	        Session messageSession = Session.getDefaultInstance(properties, new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                try {
	                    return new PasswordAuthentication(appProp.getUserMail(), appProp.getPswMail());
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                return null;
	            }
	        });
	
	        if(pass.equals(repPass)){
	            
                MimeMessage mimeMessage = new MimeMessage(messageSession);
                mimeMessage.setSubject("Confirm user");
                mimeMessage.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(appProp.getUriMySql(), appProp.getUserMySql(), appProp.getPswMySql());
                String query = "INSERT INTO users (email, name, surname, password, secretKey, dataKey) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, name);
                statement.setString(3, lastName);
                statement.setString(4, PasswordManager.createHash(pass));
                statement.setString(5, key);
                statement.setDate(6, new Date(System.currentTimeMillis()) );
                statement.executeUpdate();

                String htmlCod =
                        "<h3>By clicking on the link you accept the processing of your data for access to our service, thank you.</h3>"
                        +"<a href='"+ appProp.getUriServer()+"/register?secret=" + key + "'>"
                                +"Confirm registration" +
                        "</a>";
                
                mimeMessage.setContent(htmlCod, "text/html; charset=utf-8");
                Transport.send(mimeMessage);
		            
	            request.setAttribute("nameNewUser",name);
	            request.getServletContext().getRequestDispatcher("/registerSuccess.jsp").include(request,response);	            
	        }
	        else{
	            request.getServletContext().getRequestDispatcher("/passwordDif.jsp" ).include(request,response);
	        }
	        
		} catch (MessagingException | NoSuchAlgorithmException | ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
    }

	private void cancelSecretKey(String key) throws ClassNotFoundException, SQLException, IOException {
			
		Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(appProp.getUriMySql(), appProp.getUserMySql(), appProp.getPswMySql());
        
        
        String query = "SELECT * FROM users WHERE secretKey=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, key);
        ResultSet rs = statement.executeQuery();
        
        String dbSecretKey = rs.getString("secretKey");
        int dbMin = rs.getInt("dataKey");
        int dbCurr = LocalDateTime.now().getMinute();       
        
        query = "DELETE FROM users WHERE secretKey=?";
    	PreparedStatement preparedStatement = connection.prepareStatement(query);
    	preparedStatement.setString(1, key);
    	preparedStatement.executeUpdate();
        
	}

}