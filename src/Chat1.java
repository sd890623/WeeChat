

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class Chat
 */
@WebServlet("/Chat1")
public class Chat1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient db=new CouchDbClient("chat-record",true,"http","127.0.0.1",5984,null,null);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chat1() {
        super();
        // TODO Auto-generated constructor stub
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
		JsonObject chatObj=new JsonObject();
		
		JsonArray chats=new JsonArray();
		response.setContentType("application/json");
		PrintWriter out=response.getWriter();
		String groupID=request.getParameter("groupID");
		if(request.getParameter("status").equals("new"))
			{
				chatObj=db.find(JsonObject.class, groupID);
				chats=chatObj.get("chats").getAsJsonArray();
				JsonObject singleChat=new JsonObject();
				singleChat.addProperty("participant", request.getParameter("participant"));
				singleChat.addProperty("chat", request.getParameter("chat"));
				singleChat.addProperty("time", new Timestamp(new Date().getTime()).toString());
				chats.add(singleChat);
				chatObj.remove("chats");
				chatObj.add("chats", chats);
				db.update(chatObj);
				
			}
		
		else if(request.getParameter("status").equals("initialize"))
			{
				System.out.print("verify");
				CouchDbClient infoDB=new CouchDbClient("db-groups",true,"http","127.0.0.1",5984,null,null);
				JsonObject groupInfo=infoDB.find(JsonObject.class, request.getParameter("groupID"));
				out.print(groupInfo);
			}
		else {
			int countInChat=Integer.parseInt(request.getParameter("status"));
			
			if(db.contains(groupID))
				{
					chatObj=db.find(JsonObject.class, groupID);
					chats=chatObj.get("chats").getAsJsonArray();
					if(countInChat<chats.size())
						{
							out.print(chats);
							System.out.print(chats.size());
							
						}
					
				}
			else 
				{
					chatObj.addProperty("_id", groupID);
					chatObj.add("chats", chats);
					db.save(chatObj);
					

					
				}
		}
		
		
		out.close();
	}

}
