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
<title>Error page</title>
</head>
<body>
	
<div data-role="dialog" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="false" class="ui-corner-all" data-close-btn="none">
    <div data-role="header" data-theme="a" class="ui-corner-top">
        <h1>Account management</h1>
    </div>
    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
        <h3 class="ui-title">There is no current active session to do this action!!!</h3>
        <p><a href="./" data-role="button" data-inline="true" data-theme="c" data-ajax="false">Back to homepage</a></p>
    </div>
</div>
</body>
</html>