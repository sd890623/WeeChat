

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		JsonObject info = new JsonObject();
		
		String loginEmail = new String();
		String password = new String();
		if(request.getParameter("email")!=null)
			loginEmail = request.getParameter("email");
		if(request.getParameter("password")!=null)
			password = request.getParameter("password");
		
		JsonObject account = new JsonObject();
		if(loginEmail.length()>0 && password.length()>0){
			if(dbClient.contains(loginEmail)){
				account = dbClient.find(JsonObject.class, loginEmail);
				if(account.get("confirm").getAsBoolean()==false){
					info.addProperty("status", "loginFailed");
				}else{
					if(account.get("password").toString().replace("\"", "").equals(password)){
						String accessToken = RandomStringUtils.randomAlphanumeric(30);
						account.remove("accessToken");
						account.addProperty("accessToken", accessToken);
						dbClient.update(account);
						HttpSession session = request.getSession();
						session.setAttribute("loginEmail", loginEmail);
						session.setAttribute("accessToken", accessToken);
						info.addProperty("status", "loginSuccessfully");
						//request.getRequestDispatcher("/WEB-INF/view/account.jsp").forward(request, response);
					}else{
						info.addProperty("status", "loginFailed");
					}
				}
			}else{
				info.addProperty("status", "loginFailed");
			}
		}
		
		out.print(info);
		out.close();
	}

}
