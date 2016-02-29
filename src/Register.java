

import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

import mail.JavaMail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lightcouch.CouchDbClient;
import org.apache.commons.lang3.RandomStringUtils;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
	private final String server;
       
    /**
     * @throws IOException 
     * @see HttpServlet#HttpServlet()
     */
    public Register() throws IOException {
        super();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("global.properties");
        Properties properties = new Properties();
        properties.load(input);
        server=properties.getProperty("serverUrl");
        // TODO Auto-generated constructor stub
        dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JsonObject info = new JsonObject();
		
		String email = new String();
		String password=new String();
		if(request.getParameter("email")!=null){
			email = request.getParameter("email");
		}
		if(request.getParameter("password")!=null){
			password = request.getParameter("password");
		}
		
		if(email.length()>0){
			if(dbClient.contains(email)){
				JsonObject userAccount = dbClient.find(JsonObject.class, email);
				if(userAccount.get("confirm").getAsBoolean()){
					info.addProperty("status", "emailConfirmed");
				}
				else{
					String lastRequest = userAccount.get("lastRequest").toString().replace("\"", "");
					long lastRequestLong = (Timestamp.valueOf(lastRequest)).getTime();
					long now = (new Date()).getTime();
					int diffInMins = (int)( (now - lastRequestLong) 
			                 / (1000 * 60) );
					if(diffInMins>5){
						userAccount.remove("lastRequest");
						userAccount.addProperty("lastRequest", (new Timestamp(now)).toString());
						dbClient.update(userAccount);
						String confirmationURL = server+"Confirm?token="
								+userAccount.get("token").toString().replace("\"", "")+"&email="
								+URLEncoder.encode(email, "UTF-8");
						
						JavaMail javaMail = new JavaMail();
						String subject = "Thanks for signing up";
						String content = "Dear customer,"
								+ "<br/><br/> Somebody, probably you, has registered your email address."
								+ " Click the following link to confirm registration: <br/><a href='"+confirmationURL+"'>"+confirmationURL+"</a>";
						javaMail.sendEmail(subject, content, email);
					}
					
					info.addProperty("status", "emailNotConfirmed");
				}
			}
			else{
				JsonObject newAccount = new JsonObject();
				newAccount.addProperty("_id", email);
				newAccount.addProperty("password", password);
				newAccount.addProperty("confirm", false);
				newAccount.addProperty("lastRequest", (new Timestamp((new Date()).getTime())).toString());
				String token = RandomStringUtils.randomAlphabetic(30);
				newAccount.addProperty("token", token);
				dbClient.save(newAccount);
				
				String confirmationURL = server+"Confirm?token="
						+newAccount.get("token").toString().replace("\"", "")+"&email="
						+URLEncoder.encode(email, "UTF-8");
				JavaMail javaMail = new JavaMail();
				String subject = "Thanks for signing up";
				String content = "Dear customer,"
						+ "<br/><br/> Somebody, probably you, has registered your email address."
						+ " Click the following link to confirm registration: <br/><a href='"+confirmationURL+"'>"+confirmationURL+"</a>";
				javaMail.sendEmail(subject, content, email);
				
				info.addProperty("status", "registerSuccessfully");
			}
				
		}
		out.print(info);
		out.close();
	}

}















