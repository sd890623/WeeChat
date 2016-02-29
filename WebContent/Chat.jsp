<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html" />
	<meta name="author" content="tester" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0;"  />
	<meta name="apple-mobile-web-app-capable" content="yes" />
    <link rel="stylesheet" href="css/style.css"/>
    <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="jquery/ChatJS.js"></script>
    <script type="text/javascript" src="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js"></script>
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css"/>
	<title>Registration System</title>
<title>Insert title here</title>

</head>
<body>
<h1 align="center";position:relative;>GroupChatBox</h1>


<div data-role="page" data-theme="b" data-content-theme="b">
	<div data-role="header" data-theme="e" data-position="fixed">
	<h1>GroupChatBox</h1>
	<a href="Logout" data-icon="delete" class="ui-btn-right" data-ajax="false">Logout</a>
	<div align="left">
		<h3 >Welcome to GroupChatBox, <i> <%=request.getParameter("participant") %></i></h3>
		
		<div id="info"></div>
	</div>
	

	</div>
	<div data-role="content">
	
		<div id="output"></div>

	</div>
	<div data-role="footer" data-position="fixed">
					<a data-rel="back"   data-icon="forward" class="ui-btn-right">Back to account</a>
	
	<div
		align="center" background-color: orange;">
		<span>Chat:</span> 
		<span> 
			<input type=text; id="chat" ; size="50"> </input>
		</span> <span>
			<button data-icon="forward" data-inline="true">Send</button>
		</span>
	</div>
	</div>
	

</div>



</body>
</html>