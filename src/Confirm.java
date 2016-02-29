

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mail.JavaMail;

import org.apache.commons.lang3.RandomStringUtils;
import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Confirmation
 */
@WebServlet("/Confirm")
public class Confirm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Confirm() {
        super();
        // TODO Auto-generated constructor stub
        dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = new String();
		String token = new String();
		if(request.getParameter("email")!=null)
			email = request.getParameter("email");
		if(request.getParameter("token")!=null)
			token = request.getParameter("token");
		if(dbClient.contains(email)){
			JsonObject userAccount = dbClient.find(JsonObject.class, email);
			if (userAccount.get("token").toString().replace("\"", "").equals(token)){
				String tempPassword = userAccount.get("password").getAsString();
				userAccount.remove("confirm");
				userAccount.addProperty("confirm", true);
				dbClient.update(userAccount);
				
				JavaMail javaMail = new JavaMail();
				String subject = "You've already finished signing up";
				String content = "Dear customer,"
						+ "<br/><br/> The password for your chatting system account is: "
						+ tempPassword+"<br/>Keep it secret, keep it safe.";
				javaMail.sendEmail(subject, content, email);
				request.getRequestDispatcher("/WEB-INF/view/success.jsp?action=confirm").forward(request, response);
			}else
				request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
		}
		else
			request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
