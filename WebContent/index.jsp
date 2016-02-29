<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<meta http-equiv="content-type" content="text/html" />
	<meta name="author" content="tester" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0;"  />
	<meta name="apple-mobile-web-app-capable" content="yes" />
    <link rel="stylesheet" href="css/style.css"/>
    <script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="jquery/jquery.js"></script>
    <script type="text/javascript" src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css"/>
	<title>Registration System</title>
</head>

<body>
	<% 
	String access=(String)session.getAttribute("accessToken");
	if(access!=null) {
		request.getRequestDispatcher("/WEB-INF/view/account.jsp").forward(request, response);
	}
	
	%>
	<!-- main page -->
    <div data-role="page" data-theme="b" data-content-theme="b"> 
        <div data-role="header" data-theme="e"> 
			<h1>WeeChat</h1> 
		</div>
		
		<div data-role="navbar" class="ui-body-b">
			<ul>
				<li><a href="#" data-icon="home" data-role="button" data-iconpos="up">Home</a></li>
				<li><a href="#" data-icon="grid" data-role="button" data-iconpos="up">Groups</a></li>
			</ul>
		</div>
        
        <div data-role="content">
        <h3 align="left">Use WeeChat to create chat groups and invite others to join.</h3>
        <h4 align="left">Register to sign up, Reminder if you forgot your password, Login if you've got an account</h4>
        <br/>
         
        <form id="emailActionsForm" name="emailActionsForm" method="post" action="">
        	<div data-role="fieldcontain">
         		<label for="email">Email: </label>
   				<input data-clear-btn="true" name="email" id="email" value="" type="text"/>
   			</div>
   			<div data-role="fieldcontain">
   				<label for="password">Password: </label>
     			<input data-clear-btn="true" name="password" id="password" value="" autocomplete="off" type="password">
    		</div>
    		<br/><h4 align="left">Enter a valid email and password to register, use Reminder if you forgot your password, or enter email and 
    		password to login</h4>
    		<h2>Select a mode</h2>
    		<fieldset data-role="controlgroup" data-type="horizontal" name="radio_mode" id="radio_mode">
			    <input name="radio_mode" id="radio_mode_register" value="register" checked="checked" type="radio" data-theme="a"/>
			    <label for="radio_mode_register">Register</label>
			    <input name="radio_mode" id="radio_mode_reminder" value="reminder" type="radio" data-theme="a"/>
			    <label for="radio_mode_reminder">Reminder</label>
			    <input name="radio_mode" id="radio_mode_login" value="login" type="radio" data-theme="a"/>
			    <label for="radio_mode_login">Login</label>
			</fieldset>
			<br/>
			<input type="submit" name="submit" id="submit" value="Submit"/>
		</form>
		<!-- 
		<form id="simpleFrom" method="post" action="SimpleServer">
			<input name="username" class="form-login" title="Username" value="" size="30" maxlength="2048" />
  			<input name="password" type="password" class="form-login" title="Password" value="" size="30" maxlength="2048" />
  			<input type="submit" value="Connect">
		</form>
		-->
		
		<!-- popup info -->
		<div data-role="popup" id="popupInfo" class="ui-content" data-theme="e" style="max-width:350px;">
		  <p>Email cannot be empty.</p>
		</div>
		
        </div>
        
    </div>
    
    <!-- dialog -->
    <div data-role="dialog" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="false" class="ui-corner-all" data-close-btn="none">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>Email already registered</h1>
	    </div>
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <h3 class="ui-title">Sorry this email is already registered</h3>
	        <p>If you forget your password, use "Reminder" function.</p>
	        <a href="./" data-role="button" data-inline="true" data-ajax="false" data-theme="c">Back</a>
	    </div>
	</div>
</body>
</html>








