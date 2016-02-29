import mail.JavaMail;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Reminder
 */
@WebServlet("/Reminder")
public class Reminder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reminder() {
        super();
        // TODO Auto-generated constructor stub
        dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JsonObject info = new JsonObject();
		
		String email = new String();
		if(request.getParameter("email")!=null){
			email = request.getParameter("email");
			if(dbClient.contains(email)){
				JsonObject userAccount = dbClient.find(JsonObject.class, email);
				if(!userAccount.get("confirm").getAsBoolean()){
					info.addProperty("status", "reminderUnsuccessfully");
				}else{
				JavaMail javaMail = new JavaMail();
				String subject = "This is your password";
				String content = "Dear customer,"
						+ "<br/><br/> The password for your proj1 account is: "
						+ userAccount.get("password").toString().replace("\"", "")+"<br/>Keep it secret, keep it safe.";
				javaMail.sendEmail(subject, content, email);
				
				info.addProperty("status", "reminderSuccessfully");
				}
			}else{
				info.addProperty("status", "reminderUnsuccessfully");
			}
		}
		
		out.print(info);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
