import mail.JavaMail;
import model.*;

import java.io.*;

import java.net.URLEncoder;
import java.util.*;


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
 * Servlet implementation class FrontController
 */
@WebServlet({ "/createGroup", "/deleteGroup", "/deleteMember", "/confirmInvitation", "/invite" ,"/Account", "/goChat"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CouchDbClient dbClient;
    private final String server;
    /**
     * @throws IOException 
     * @see HttpServlet#HttpServlet()
     */
    public FrontController() throws IOException {
        super();
        // TODO Auto-generated constructor stub
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("global.properties");
        // ...
        Properties properties = new Properties();
        properties.load(input);
        server=properties.getProperty("serverUrl");
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String userPath = request.getServletPath();
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userPath = request.getServletPath();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if(userPath.equals("/Account")){
			dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
			String loginEmail = new String();
			String accessToken = new String();
			if(request.getSession().getAttribute("loginEmail").toString() != null)
				loginEmail = request.getSession().getAttribute("loginEmail").toString();
			if(request.getSession().getAttribute("accessToken").toString() != null)
				accessToken = request.getSession().getAttribute("accessToken").toString();
			if(dbClient.contains(loginEmail)){
				JsonObject account = dbClient.find(JsonObject.class, loginEmail);
				if(account.get("accessToken").toString().replaceAll("\"", "").equals(accessToken)){
					ArrayList<Group> ownGroups = new ArrayList<Group>();
					ArrayList<Group> memberGroups = new ArrayList<Group>();
					CouchDbClient newDbClient = new CouchDbClient("db-groups", true, "http", "127.0.0.1", 5984, null, null);
					List<JsonObject> allGroups = newDbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
					for(int i=0;i<allGroups.size();++i){
						if(allGroups.get(i).get("owner").toString().replaceAll("\"", "").equals(loginEmail))
						{
							Group group = new Group(allGroups.get(i).get("_id").toString().replaceAll("\"", "")
									,allGroups.get(i).get("group_name").toString().replaceAll("\"", ""),
									allGroups.get(i).get("owner").toString().replaceAll("\"", ""));
							ownGroups.add(group);
						}else{
							JsonArray memberList = allGroups.get(i).getAsJsonArray("memberList");
							for(int j=0;j<memberList.size();j++){
								if(((JsonObject) memberList.get(j)).get("email").toString().replaceAll("\"", "").equals(loginEmail)
									&& ((JsonObject) memberList.get(j)).get("confirm").getAsBoolean()==true){
									Group group = new Group(allGroups.get(i).get("_id").toString().replaceAll("\"", "")
											,allGroups.get(i).get("group_name").toString().replaceAll("\"", ""),
											allGroups.get(i).get("owner").toString().replaceAll("\"", ""));
									memberGroups.add(group);
								
									break;
								}
							}
						}
					}
					
					request.setAttribute("ownGroups", ownGroups);
					request.setAttribute("memberGroups", memberGroups);
					request.getRequestDispatcher("/WEB-INF/view/account.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
				}
			}else{
				request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
			}
		}else if(userPath.equals("/createGroup")){


			String newGroupName = request.getParameter("newGroupName");
			String loginEmail = request.getSession().getAttribute("loginEmail").toString();
			dbClient = new CouchDbClient("db-groups", true, "http", "127.0.0.1", 5984, null, null);
			
			boolean isGroupExist = false;
			List<JsonObject> allGroups = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
			for(int i=0;i<allGroups.size();++i){
				if(allGroups.get(i).get("owner").toString().replaceAll("\"", "").equals(loginEmail)
					&& allGroups.get(i).get("group_name").toString().replaceAll("\"", "").equals(newGroupName)){
						isGroupExist = true;
						break;
				}
			}
			
			JsonObject data = new JsonObject();
			JsonObject groupJson = new JsonObject();
			if(isGroupExist==false){
				Collections.sort(allGroups, new Comparator<JsonObject>(){
					@Override
					public int compare(JsonObject o1, JsonObject o2) {
						// TODO Auto-generated method stub
						int counter1 = Integer.parseInt(o1.get("_id").toString().replaceAll("\"", ""));
						int counter2 = Integer.parseInt(o2.get("_id").toString().replaceAll("\"", ""));
						return counter1-counter2;
					}
				});
				
				int currentID = 0;
				JsonObject lastGroup = new JsonObject();
				if(allGroups.size()>0){
					lastGroup = (JsonObject)(allGroups.get(allGroups.size()-1));
					currentID = Integer.parseInt(lastGroup.get("_id").toString().replaceAll("\"", ""));
				}
				groupJson.addProperty("_id", String.valueOf(currentID+1));
				groupJson.addProperty("group_name", newGroupName);
				groupJson.addProperty("owner",loginEmail);
				JsonArray memberList = new JsonArray();
				groupJson.add("memberList", memberList);
				data.addProperty("isGroupExist", "false");
				data.add("groupJson", groupJson);
				dbClient.save(groupJson);
			}else{
				data.addProperty("isGroupExist", "true");
			}
					
			out.print(data);
			
		}else if(userPath.equals("/deleteGroup")){
			String[] deleteGroups = request.getParameterValues("checkedGroups[]");
			String loginEmail = request.getSession().getAttribute("loginEmail").toString();
			dbClient = new CouchDbClient("db-groups", true, "http", "127.0.0.1", 5984, null, null);
			JsonArray groupsJson = new JsonArray();
			for(int i=0;i<deleteGroups.length;++i){
				if(dbClient.contains(deleteGroups[i])){
					JsonObject groupToDelete = dbClient.find(JsonObject.class, deleteGroups[i]);
					groupsJson.add(groupToDelete);
					if(groupToDelete.get("owner").toString().replaceAll("\"", "").equals(loginEmail)){
						dbClient.remove(groupToDelete);
						CouchDbClient chatDB= new CouchDbClient("chat-record", true, "http", "127.0.0.1", 5984, null, null);
						if(chatDB.contains(deleteGroups[i]))
							{
						JsonObject chatToDelete=chatDB.find(JsonObject.class, deleteGroups[i]);
						chatDB.remove(chatToDelete);
							}
					}else{
						JsonArray tempMemberList = new JsonArray();
						JsonArray memberList = groupToDelete.getAsJsonArray("memberList");
						for(int j=0;j<memberList.size();++j){
							JsonObject member = (JsonObject) memberList.get(i);
							if(member.get("email").toString().replaceAll("\"", "").equals(loginEmail)==false){
								tempMemberList.add(member);
							}
						}
						groupToDelete.remove("memberList");
						groupToDelete.add("memberList", tempMemberList);
						dbClient.update(groupToDelete);
					}
				}
			}
			out.print(groupsJson);
		}else if(userPath.equals("/invite")){
			String inviteEmail = request.getParameter("inviteEmail");
			String groupID = request.getParameter("select-group");
			
			dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
			JsonObject inviteStatus = new JsonObject();
			if(dbClient.contains(inviteEmail)){
				JsonObject account = dbClient.find(JsonObject.class, inviteEmail);
				if(account.get("confirm").getAsBoolean()==true){
					CouchDbClient groupDbClient = new CouchDbClient("db-groups", true, "http", "127.0.0.1", 5984, null, null);
					JsonObject group = groupDbClient.find(JsonObject.class, groupID);
					String groupName = group.get("group_name").toString().replaceAll("\"", "");
					JsonArray memberList = group.getAsJsonArray("memberList");
					boolean invitationSent = false;
					for(int i=0;i<memberList.size();++i){
						JsonObject member = (JsonObject) memberList.get(i);
						if(member.get("email").toString().replaceAll("\"", "").equals(inviteEmail)){
							invitationSent = true;
							break;
						}
					}
					
					if(invitationSent==false){
						JsonObject groupMember = new JsonObject();
						groupMember.addProperty("email", account.get("_id").toString().replaceAll("\"", ""));
						groupMember.addProperty("confirm", false);
						memberList.add(groupMember);
						group.remove("memberList");
						group.add("memberList", memberList);
						groupDbClient.update(group);
						
						String confirmationURL = server+"confirmInvitation?group_id="
								+groupID+"&email="+URLEncoder.encode(inviteEmail, "UTF-8");
						
						JavaMail javaMail = new JavaMail();
						String subject = "Invitation to chat group";
						String content = "Dear customer,"
								+ "<br/><br/> You have been invited to this chat group: "+groupName
								+ ". Click the following link to confirm registration: <br/><a href='"+confirmationURL+"'>"+confirmationURL+"</a>";
						javaMail.sendEmail(subject, content, inviteEmail);
		
						inviteStatus.addProperty("status", "successful");
					}else{
						inviteStatus.addProperty("status", "sent");
					}
				}else{
					inviteStatus.addProperty("status", "failed");
				}
			}else{
				inviteStatus.addProperty("status", "failed");
			}
			
			out.print(inviteStatus);
		}else if(userPath.equals("/confirmInvitation")){
			String groupID = request.getParameter("group_id");
			String email = request.getParameter("email");
			dbClient = new CouchDbClient("db-register-email", true, "http", "127.0.0.1", 5984, null, null);
			if(dbClient.contains(email)){
				CouchDbClient groupDbClient = new CouchDbClient("db-groups", true, "http", "127.0.0.1", 5984, null, null);
				if(groupDbClient.contains(groupID)){
					JsonObject group = groupDbClient.find(JsonObject.class, groupID);
					JsonArray memberList = group.getAsJsonArray("memberList");
					boolean foundMember = false;
					for(int i=0;i<memberList.size();++i){
						JsonObject member = (JsonObject) memberList.get(i);
						if(member.get("email").toString().replaceAll("\"", "").equals(email) && member.get("confirm").getAsBoolean()==false){
							foundMember = true;
							member.remove("confirm");
							member.addProperty("confirm", true);
							group.remove("memberList");
							group.add("memberList", memberList);
							groupDbClient.update(group);
							request.getRequestDispatcher("/WEB-INF/view/success.jsp?action=invite").forward(request, response);
						}
					}
					if(foundMember==false){
						request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
					}
				}else{
					request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
				}
			}else{
				request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
			}
		}
		else if(userPath.equals("/goChat"))
			{

				String participant=request.getParameter("participant");
				String groupID=request.getParameter("groupID");
				HttpSession session=request.getSession();
				session.setAttribute("member", participant);
				session.setAttribute("groupID", groupID);
				
				
				System.out.print(participant+" "+groupID);

				dbClient=new CouchDbClient("chat-record",true,"http","127.0.0.1",5984,null,null);
				JsonObject groupChat=new JsonObject();
				if(dbClient.contains(groupID))
					{

					}
				else 
					{
						groupChat.addProperty("_id", groupID);
						JsonArray chats=new JsonArray();
						groupChat.add("chats", chats);
						
						dbClient.save(groupChat);
					}
				
				
				
				
			}
		
		else if(userPath.equals("/deleteMember"))
			{

				dbClient=new CouchDbClient("db-groups",true,"http","127.0.0.1",5984,null,null);
				JsonObject group=new JsonObject();
				JsonArray members=new JsonArray();

				if (request.getParameter("status").equals("listMember"))
					{
						group=dbClient.find(JsonObject.class, request.getParameter("groupID"));
						System.out.print(group.get("group_name").getAsString());
						members=group.get("memberList").getAsJsonArray();
						System.out.print(members.size());
						out.print(members);
					}
				else if (request.getParameter("status").equals("delete"))	
					{
						System.out.print(request.getParameter("groupID"));

						String member=request.getParameter("member");
						int position = 0;
						group=dbClient.find(JsonObject.class, request.getParameter("groupID"));
						members=group.get("memberList").getAsJsonArray();
						JsonArray tempMembers=new JsonArray();
						for (int i=0;i<members.size();i++)
							{
								JsonObject each;
								each=members.get(i).getAsJsonObject();
								if (each.get("email").getAsString().equals(member))
									{
										position=i;
									}
							}
						for (int i=0;i<position;i++)
							{
								tempMembers.add(members.get(i).getAsJsonObject());
							}
						for (int i=position+1;i<members.size();i++)
							{
								tempMembers.add(members.get(i).getAsJsonObject());

							}
						System.out.print("213123");
						group.remove("memberList");
						group.add("memberList", tempMembers);
						dbClient.update(group);
						JsonObject info=new JsonObject();
						info.addProperty("status", "success");
						out.print(info);
					}
			}
		
		out.close();
	}

}
