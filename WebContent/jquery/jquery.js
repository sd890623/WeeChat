var info1="<p>e21e21e12e</p>";
var server="http://2016.mydanielsun.com:8080/Project2/";
$(function(){
	console.log("6");
	
	
	//check email pattern
	function checkEmailPattern(email){
		var pattern = /^\b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b$/i;
		if(!pattern.test(email))
			return false;
		else
			return true;
	}

	
	function checkTextboxAlphanumeric(textInput){
		var pattern = /^[A-Za-z0-9]+$/;
		if(!pattern.test(textInput))
			return false;
		else
			return true;
	}
	
	
	
	$("#submit").click(function(){
		
		if($("#email").val().length>0){
			if(checkEmailPattern($("#email").val())==true&&$("#password").val().length>0){
				var radio_mode = $('input[name=radio_mode]:checked').val();
				if(radio_mode=="register"){
					//$.getJSON('Register',{"email":$("#email").val()},function(info)
					$.ajax({
						url:"Register",
						type:"POST",
						dataType:"json",
						data:{"email":$("#email").val(),"password":$("#password").val()},
						beforeSend: function() { 
							$.mobile.loading("show");
						},
			            //complete: function() { $.mobile.loading("hide");},
						success : function(info)
						{
							$.mobile.loading("hide");
							if(info.status=="emailConfirmed"){
								$("#popupDialog h1").text("Email already exists");
								$("#popupDialog h3").text("Sorry this email is already registered");
								$("#popupDialog p").text("If you forget your password, use \"Reminder\" mode.");
								$.mobile.changePage('#popupDialog', {transition: 'pop', role: 'dialog'});
							}else if(info.status=="emailNotConfirmed"){
								$("#popupDialog h1").text("Email is not confirmed");
								$("#popupDialog h3").text("Sorry this email is registered but not confirmed");
								$("#popupDialog p").text("A confirmation email has been sent to your email.");
								$.mobile.changePage('#popupDialog', {transition: 'pop', role: 'dialog'});
							}else if(info.status=="registerSuccessfully"){
								$("#popupDialog h1").text("Register successfully");
								$("#popupDialog h3").text("Congratulations, you have successfully registered");
								$("#popupDialog p").text("A confirmation email is sent to your email");
								$.mobile.changePage('#popupDialog', {transition: 'pop', role: 'dialog'});
							}
						}
					});
				}else if(radio_mode=="reminder"){
					$.getJSON('Reminder',{"email":$("#email").val()},function(info){
						if(info.status=="reminderSuccessfully"){
							$("#popupDialog h1").text("Reminder successfully");
							$("#popupDialog h3").text("Forgot your password");
							$("#popupDialog p").text("An email with your password is sent to your email");
							$.mobile.changePage('#popupDialog', {transition: 'pop', role: 'dialog'});
						}else if(info.status=="reminderUnsuccessfully"){
							$("#popupDialog h1").text("Email is not registered");
							$("#popupDialog h3").text("Sorry this email is not registered yet");
							$("#popupDialog p").text("Please register this email before using this function");
							$.mobile.changePage('#popupDialog', {transition: 'pop', role: 'dialog'});
						}
					});
				}else if(radio_mode=="login"){
					if($("#password").val().length==0){
						$("#popupInfo p").text("Password cannot be empty.");
						$("#popupInfo").popup("open",{positionTo: '#password'});
						return false;
					}else{
						$.getJSON('Login',{"email":$("#email").val(),"password":$("#password").val()},function(info){
							if(info.status=="loginFailed"){
								$("#popupDialog h1").text("Login failed");
								$("#popupDialog h3").text("Incorrect username or password");
								$("#popupDialog p").text("Please check your username and password");
								$.mobile.changePage('#popupDialog', {transition: 'pop', role: 'dialog'});
							}else if(info.status=="loginSuccessfully"){
								$("#emailActionsForm").attr("action","Account");
								$("#emailActionsForm").submit();
							}
						});
					}
				}
			} else if (checkEmailPattern($("#email").val())==false){
				$("#popupInfo p").text("Invalid email. Email should have pattern: user@xxx.xxx");
				$("#popupInfo").popup("open",{positionTo: '#email'});
				return false;
			}
			else {
				$("#popupInfo p").text("Password must be provided to register.");
				$("#popupInfo").popup("open",{positionTo: '#password'});
				return false;
			}
		}else{
			$("#popupInfo p").text("Email cannot be empty.");
			$("#popupInfo").popup("open",{positionTo: '#email'});
			return false;
		}
	});
	
	$("#createGroupButton").click(function(){
		if($("#newGroupName").val().length==0){
			$("#popupInfo p").text("Group name cannot be empty");
			$("#popupInfo").popup("open",{positionTo: '#newGroupName'});
			return false;
		}else{
			if(checkTextboxAlphanumeric($("#newGroupName").val())==true){
				$.getJSON('createGroup',{"newGroupName":$("#newGroupName").val()},function(data){
					if(data.isGroupExist=="false"){
			    		$("#ownGroups").append("<li id='"+data.groupJson._id+"'><a href='#'>" +
			    				"<label style='border-top-width: 0px;margin-top: 0px;border-bottom-width: 0px;margin-bottom: 0px;border-left-width: 0px;border-right-width: 0px;' data-corners='false'>" +
			    				"<input class='newCheckBox' type='checkbox' name='groups_checkbox' data-mini='true' value='"+data.groupJson._id+"'/><label>"
			    				+data.groupJson.group_name+
			    				"</label></label></a></li>");
						$("#ownGroups").listview("refresh").trigger("create");
						
						$("#select-group").append("<option value='"+data.groupJson._id+"'>"+data.groupJson.group_name+"</option>").trigger("create");
						$('#select-group').selectmenu('refresh', true);
						
						$("#popupInfo p").text("You've created new group: "+data.groupJson.group_name);
						$("#popupInfo").popup("open",{positionTo: '#newGroupName'});
					}else if(data.isGroupExist=="true"){
						$("#popupInfo p").text("You've already created a group with the same name.");
						$("#popupInfo").popup("open",{positionTo: '#newGroupName'});
						return false;
					}
				});
			}else{
				$("#popupInfo p").text("Group name must contain only alphanumeric characters.");
				$("#popupInfo").popup("open",{positionTo: '#newGroupName'});
				return false;
			}
		}
	});

	$("#deleteGroupButton").click(function(){
		var checkedGroups = new Array();
	
		
		$("input[name='groups_checkbox']:checked").each(function(){
			 checkedGroups.push($(this).val());
		});
		if(checkedGroups.length>0){
			$("#deletePopup").popup("open");
		}else{
			$("#popupInfo p").text("You must select at least a group to delete");
			$("#popupInfo").popup("open",{positionTo: '#deleteGroupButton'});
		}
	});
	
	$("#deleteMemberButton").click(function(){
		
		var checkedGroups = new Array();
		$("input[name='groups_checkbox']:checked").each(function(){
			 checkedGroups.push($(this).val());
		});
		var groupID=checkedGroups[0];
		if (checkedGroups.length!=1)
			{
					$("#popupInfo p").text("You must choose only one group you owned to continue");
					$("#popupInfo").popup("open");
			}
		
		else {
		if($("#group_checkbox:checked").length==0)
			{
				$.getJSON("deleteMember",
							{
								"status":"listMember",
								"groupID":groupID
							},
							function(members){
								if (members.length==0)
									{
										//$("#select-member").empty();
										$("#select-member").html("<option value='0'>Currently no member</option>");
										//$("#select-member").html("<option value='1'> </option>");
										//$("#select-member").selectmenu('refresh',true);

										$("#select-member").trigger("create");
										$("#select-member").selectmenu('refresh',true);
									}
								else {
									
									$("#select-member").empty();
								$.each(members, function(index, i) {
									//console.log("1");
									
									//$("#select-member").append("<option value='0'>tretret </option>").trigger("create");
									$("#select-member").append("<option value='"+index+"'>"+i.email+"</option>").trigger("create");
								
									
								});
								$("#select-member").selectmenu('refresh',true);
								}
								
							});
				$("#confirmDeleteMemberButton").closest('.ui-btn').hide();

			}
		else {
		
		$.getJSON("deleteMember",
					{
						"status":"listMember",
						"groupID":groupID
					},
					function(members){
						if (members.length==0)
							{
								$("#select-member").empty();
								$("#select-member").html("<option value='0'>Currently no member</option>");
								$("#select-member").trigger("create");
								//$("#select-member").selectMenu('refresh',true);
								$("#select-member").selectmenu('refresh',true);

							}
						else {
							
						
						$.each(members, function(index, i) {
							//console.log("1");
							$("#select-member").empty();
							$("#select-member").append("<option value='"+index+"'>"+i.email+"</option>");
							$("#select-member").trigger("create");
							//$("#select-member").selectMenu('refresh',true);

						});
						$("#select-member").selectmenu('refresh',true);
						}
						
					});
		$("#confirmDeleteMemberButton").closest('.ui-btn').show();

			}
	$("#deleteMember").popup("open");
		}
	});
	
		
		$("#confirmDeleteMemberButton").click(function(){
			var checkedGroups = new Array();
			$("input[name='groups_checkbox']:checked").each(function(){
				 checkedGroups.push($(this).val());
			});
			var groupID=checkedGroups[0];
			if($("#select-member option:selected").text()=="Currently no member")
				{
					setTimeout(function(){$("#deleteMember").popup("close")},1000);
					setTimeout(function(){
					$("#popupInfo p").text("You have no members to delete from the group");
					$("#popupInfo").popup("open")
					},3000);
				}
			else 
			{
			$.getJSON("deleteMember",{
				"status":"delete",
				"member":$("#select-member option:selected").text(),
				"groupID":groupID
			},
			function(info)
			{
				if(info.status=="success")
					{
						setTimeout(function(){$("#deleteMember").popup("close")},1000);
						setTimeout(function(){
						$("#popupInfo p").text("You have successfully deleted a member from this group");
						$("#popupInfo").popup("open")
						},3000);
						
					}
			}
			);
			}
	});
	
	$("#deleteConfirmButton").click(function(){
		var checkedGroups = new Array();
		$("input[name='groups_checkbox']:checked").each(function(){
			 checkedGroups.push($(this).val());
		});
		$.getJSON("deleteGroup",{"checkedGroups":checkedGroups},function(groupsJson){
			$("#popupInfo p").text("Your selected groups were deleted or withdrawed");
			$("#popupInfo").popup("open");
			$("input[name='groups_checkbox']:checked").each(function(){
				 $("li#"+$(this).val()).remove();
				 $("option[value='"+$(this).val()+"']").remove();
				 $('#select-group').selectmenu('refresh', true);
			});
		});
	});
	
	$("#inviteButton").click(function(){
		if($("#inviteEmail").val().length==0){
			$("#popupInfo p").text("Email cannot be empty");
			$("#popupInfo").popup("open",{positionTo: '#inviteEmail'});
			return false;
		}else{
			if(checkEmailPattern($("#inviteEmail").val())==true){
				if($("#select-group").val()>0){
					$.mobile.loading("show");
					$.getJSON("invite",{"inviteEmail":$("#inviteEmail").val(),"select-group":$("#select-group").val()},function(inviteStatus){
						if(inviteStatus.status=="successful"){
							$("#popupInfo p").text("Your invitation has been sent to: "+$("#inviteEmail").val());
							$("#popupInfo").popup("open",{positionTo: '#inviteEmail'});
						}else if(inviteStatus.status=="failed"){
							$("#popupInfo p").text("This email has not registered yet");
							$("#popupInfo").popup("open",{positionTo: '#inviteEmail'});
						}else if(inviteStatus.status=="sent"){
							$("#popupInfo p").text("This email is already in this group");
							$("#popupInfo").popup("open",{positionTo: '#inviteEmail'});
						}
						$.mobile.loading("hide");
					});
				}else{
					$("#popupInfo p").text("You have no group to invite");
					$("#popupInfo").popup("open",{positionTo: '#inviteEmail'});
					return false;
				}
			}else{
				$("#popupInfo p").text("Invalid email. Email should have pattern: user@xxx.xxx");
				$("#popupInfo").popup("open",{positionTo: '#inviteEmail'});
				return false;
			}
		}
	});

	$("#refreshButton").click(function(){
		location.reload();
	});
	$("#GoChat").click(function(){
		var checkedGroups=new Array();
		$("input[name='groups_checkbox']:checked").each(function(){
			checkedGroups.push($(this).val());
		});
		if(checkedGroups.length==1)
			{
				
				var groupID=checkedGroups[0];
				//$.getJSON("goChat",{"groupID":groupID, "participant":participant}, function(info){
					//$.mobile.changePage("../Chat.html");
				//});
				console.log(groupID+" "+participant);
				window.location.assign(server+"Chat.jsp?"+"groupID="+groupID+"&participant="+participant);
			}
		else {
			$("#popupInfo p").text("You must choose one groups to go to chat with");
			$("#popupInfo").popup("open");
		}
	});
});








