

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Delete() {
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
			request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
		}else{
			if(dbClient.contains(session.getAttribute("loginEmail").toString())){
				JsonObject account = dbClient.find(JsonObject.class, session.getAttribute("loginEmail").toString());
				if(account.get("accessToken").toString().replace("\"", "").equals(session.getAttribute("accessToken"))){
					dbClient.remove(account);
					session.removeAttribute("accessToken");
					request.getRequestDispatcher("/WEB-INF/view/success.jsp?action=delete").forward(request, response);
				}else{
					request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
				}
			}else{
				request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
			}
		}
	}

}
