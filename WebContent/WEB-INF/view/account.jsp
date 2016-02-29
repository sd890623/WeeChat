<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
	<%
	String username=(String)session.getAttribute("loginEmail");
	%>
	
</head>
<body>
<script>
var username="<%=username %>";
sessionStorage.setItem("username",username);
</script>
	<div data-role="page" data-theme="b" data-content-theme="b" id="account"> 
        <div data-role="header" data-theme="e"> 
			<h1>WeeChat</h1> 
			<a href="Logout" data-icon="delete" class="ui-btn-right" data-ajax="false">Logout</a>
		</div>
		
		<div data-role="navbar" class="ui-body-b">
			<ul>
				<li><a href="Account" data-icon="home" data-role="button" data-iconpos="up">Home</a></li>
				<li><a href="Account#groups" data-icon="grid" data-role="button" data-iconpos="up" data-ajax="false">Groups</a></li>
			</ul>
		</div>
        
        <div data-role="content">
        <h3 align="left">Welcome to WeeChat, <i style="color: blue"><%= session.getAttribute("loginEmail") %></i></h3>
        <br/>
        <script>
        	
        </script>
        <h4 align="left">You can reset your password or delete your account</h4>
			<div data-role="controlgroup">
				<a href="Reset" data-role="button" data-icon="refresh">Reset password</a>
				<a href="Delete" data-role="button" data-icon="delete">Delete account</a>
			</div>
			
			<p>
				
			</p>
		</div>
	</div>
	
	<div data-role="page" data-theme="b" data-content-theme="b" id="groups"> 
        <div data-role="header" data-theme="e"> 
			<h1>WeeChat</h1> 
			<a href="Logout" data-icon="delete" class="ui-btn-right" data-ajax="false">Logout</a>
		</div>
		
		<div data-role="navbar" class="ui-body-b">
			<ul>
				<li><a href="Account" data-icon="home" data-role="button" data-iconpos="up">Home</a></li>
				<li><a href="#groups" data-icon="grid" data-role="button" data-iconpos="up">Groups</a></li>
			</ul>
		</div>
        
        <div data-role="content">
        	<input type="text" data-clear-btn="true" name="newGroupName" id="newGroupName" value="" placeholder="Enter new group name">
			<br/>
			<div data-role="collapsible" data-theme="c" data-content-theme="d" data-inset="true" id="memberGroups">
				<h2>Group as member</h2>
				<ul data-role="listview" data-filter="true" data-inset="true" data-theme="d" class="ui-icon-alt" id="memberGroups"
					data-filter-placeholder="Search for group..." data-divider-theme="d">
					<c:forEach var="group" items="${memberGroups}">
						<li id="${group.getGroupID()}">
							<label style="border-top-width: 0px;margin-top: 0px;border-bottom-width: 0px;margin-bottom: 0px;border-left-width: 0px;border-right-width: 0px;" data-corners="false">
								<input type="checkbox" name="groups_checkbox" data-mini="true" value="${group.getGroupID()}"/>
								<label>${group.getGroupName()}</label>	
								
							</label>
						</a></li>
					</c:forEach>
				</ul>
			</div>
			<div data-role="collapsible" data-theme="c" data-content-theme="d" data-inset="true">
				<h2>Group as owner</h2>
				<ul data-role="listview" data-filter="true" data-inset="true" data-theme="d" class="ui-icon-alt" id="ownGroups"
					data-filter-placeholder="Search for group..." data-divider-theme="d">
					<c:forEach var="group" items="${ownGroups}">
						<li id="${group.getGroupID()}"><a>
							<label style="border-top-width: 0px;margin-top: 0px;border-bottom-width: 0px;margin-bottom: 0px;border-left-width: 0px;border-right-width: 0px;" data-corners="false">
								<input type="checkbox" id="group_checkbox" name="groups_checkbox" data-mini="true" value="${group.getGroupID()}"/>
								<label>${group.getGroupName()}</label>	
							</label>
						</a></li>
					</c:forEach>
				</ul>
			</div>
			
			<div id="inviteToChatGroup">
				<br/><h3>Invite to chat group</h3>
				<label for="inviteEmail" class="ui-hidden-accessible">Email:</label>
				<input type="email" name="inviteEmail" id="inviteEmail" value="" placeholder="enter email">
				<h3>To this group</h3>
				<select name="select-group" id="select-group" data-theme="a" data-native-menu="false">
					<c:forEach var="group" items="${ownGroups}">
					  	<option value='${group.getGroupID()}'>
					   		${group.getGroupName()}
					   	</option>
					</c:forEach>							 
				</select>
			</div>
			
			<div data-role="footer" data-position="fixed">
			    <button data-icon="plus" id="createGroupButton">Create group</button>
			    <button data-icon="delete" id="deleteGroupButton">Delete group</button>
			    <button data-icon="delete" id="deleteMemberButton">C/Delete member</button>
			    <button data-icon="star" id="inviteButton">Invite</button>
			    <button data-icon="refresh" id="refreshButton">Refresh</button>
			    <a href="#popupHelp" data-rel="popup" data-position-to="origin"
			    	data-role="button" data-icon="info" data-theme="a" data-transition="pop">Help</a>
			    <button data-icon="forward" id="GoChat">GoToChat </button>
			</div>
			
			<div data-role="popup" id="popupInfo" class="ui-content" data-theme="e" style="max-width:350px;">
			  <p>Group name cannot be empty to create a new group chat</p>
			</div>
			
			<div data-role="popup" id="deletePopup" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:400px;" class="ui-corner-all">
			    <div data-role="header" data-theme="a" class="ui-corner-top">
			        <h1>Delete Page?</h1>
			    </div>
			    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
			        <h3 class="ui-title">Are you sure you want to delete or withdraw the group(s)?</h3>
			        <p>Groups which you are owner of will be deleted and you will be withdrawed from groups you are member of.</p>
			        <a href="#" data-role="button" data-inline="true" data-rel="back" data-theme="c">Cancel</a>
			        <a href="#" data-role="button" data-inline="true" data-rel="back" data-theme="b" id="deleteConfirmButton">Delete</a>
			    </div>
			</div>
			
			<div data-role="popup" id="deleteMember" data-theme="c" style="max-width:400px;class="ui-corner-all">
				<div data-role="header" data-theme="a" class="ui-corner-top">
				<h3 class="ui-title">Delete members</h3>
				
				</div>
				<div data-role="content" data-theme="e" class="ui-corner-bottom ui-content">
				<h3 class="ui-title">You can choose one member to delete from the group</h3>
				<div data-role="fieldcontain">
				<select name="select-member" id="select-member" data-theme="a" >
					<option selected="selected">Choose a member</option>
				</select>
				</div>
				<button data-inline="true" id="confirmDeleteMemberButton">DeleteMember</button>
				<a href="#" data-role="button" data-inline="true" data-rel="back" data-theme="c">Cancel</a>
				
				</div>
			</div>
			
			<div data-role="popup" id="popupHelp" class="ui-content" data-theme="e" style="max-width:350px;" align="left">
			  <p>The groups page shows 2 collapsible lists of groups where you are member of and the owner.
			  You can click on each group to enter the chat page, as well as the member list of that group</p><br/>
			  <p>To create a new group, enter the group name in the group text field and click create. The group name 
			  must contain only alpha numeric characters, with no space or punctuation. It must be uniquer with respect 
			  to the group owner.</p><br/>
			  <p>To delete or withdraw from a group, click the checkbox of this group and click Delete.
			   Delete only the group when you are owner of. You can delete or withdraw multiple groups.</p><br/>
			   <p>To invite someone to a chat group, enter their email, select the group to invite and click Invite</p><br/>
			   <p>Click refresh to update information from the server</p>
			</div>
		</div>
	</div>

</body>
</html>






