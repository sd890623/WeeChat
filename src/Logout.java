

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
	private final String server;

       
    /**
     * @throws IOException 
     * @see HttpServlet#HttpServlet()
     */
    public Logout() throws IOException {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
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
				JsonObject account = dbClient.find(JsonObject.class, session.getAttribute("loginEmail").toString());
				if(account.get("accessToken").toString().replace("\"", "").equals(session.getAttribute("accessToken"))){
					account.remove("accessToken");
					dbClient.update(account);
					session.removeAttribute("accessToken");
					response.sendRedirect(server);
				}else{
					response.sendRedirect("Error_Page.jsp");
				}
			}else{
				response.sendRedirect("Error_Page.jsp");
			}
		}
	}

}











