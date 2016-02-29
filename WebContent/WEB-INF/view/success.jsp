<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0;"  />
<meta name="apple-mobile-web-app-capable" content="yes" />
<link rel="stylesheet" href="css/style.css"/>
<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="jquery/jquery.js"></script>
<script type="text/javascript" src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css"/>
<title>Notification page</title>
</head>
<body>
<%
	String notification = "";
	if(request.getParameter("action")!=null){
			String action = request.getParameter("action");
			if(action.equals("delete"))
				notification = "Delete account successfully.";
			else if(action.equals("register"))
				notification = "Register successfully. Remember to check for confimation email to activate this email.";
			else if(action.equals("confirm"))
				notification = "Confirmation complete.";
			else if(action.equals("reset"))
				notification = "Your password has been reset, check your inbox to see new password.";
			else if(action.equals("reminder"))
				notification = "An email with your password is sent to your email address.";
			else if(action.equals("invite"))
				notification = "Invitation accepted. Login to chat with your friends";
			else
				notification = "";
	}
%>
	
<div data-role="dialog" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="false" class="ui-corner-all" data-close-btn="none">
    <div data-role="header" data-theme="a" class="ui-corner-top">
        <h1>Account management</h1>
    </div>
    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
        <h3 class="ui-title"><% out.print(notification); %></h3>
        <% if(request.getParameter("action")!=null && !request.getParameter("action").equals("reset")) { %>
        	<a href="./" data-role="button" data-inline="true" data-theme="c" data-ajax="false">Back to homepage</a>
        <%} else { %>
			<a href="./" data-role="button" data-inline="true" data-theme="c" data-ajax="false">Login again using new password</a>
		<%} %>
    </div>
</div>
</body>
</html>