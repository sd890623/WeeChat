
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mail.JavaMail;

import org.apache.commons.lang3.RandomStringUtils;
import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Reset
 */
@WebServlet("/Reset")
public class Reset extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reset() {
        super();
        // TODO Auto-generated constructor stub
        dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("loginEmail")==null || session.getAttribute("accessToken")==null){
			response.sendRedirect("Error_Page.jsp");
		}else{
			if(dbClient.contains(session.getAttribute("loginEmail").toString())){
				JsonObject userAccount = dbClient.find(JsonObject.class, session.getAttribute("loginEmail").toString());
				if(userAccount.get("accessToken").toString().replace("\"", "").equals(session.getAttribute("accessToken"))){
					String tempPassword = RandomStringUtils.randomAlphabetic(13);
					userAccount.remove("password");
					userAccount.addProperty("password", tempPassword);
					dbClient.update(userAccount);
					
					JavaMail javaMail = new JavaMail();
					String subject = "Your new password has been generated";
					String content = "Dear customer,"
							+ "<br/><br/> The password for your proj1 account is: "
							+ tempPassword+"<br/>Keep it secret, keep it safe.";
					javaMail.sendEmail(subject, content, session.getAttribute("loginEmail").toString());
					request.getRequestDispatcher("/WEB-INF/view/success.jsp?action=reset").forward(request, response);
				}else{
					request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
				}
			}else{
				request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
			}
		}
	}

}
