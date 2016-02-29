$(function(){


function getParameter(url)
	{
		var searchUrl=window.location.search.substring(1);
		var subString=searchUrl.split('&');
		for (var i=0;i<subString.length;i++)
			{
				var attribute=subString[i].split('=');
				if (attribute[0]==url)
				{
					return attribute[1];
				}
			}
		
	}

var clock = self.setInterval(function() {
	refresh()
}, 2000);




	var groupID=getParameter("groupID");
	var participant=getParameter("participant");
	console.log(groupID+participant);
	var info;
	
	

	$.getJSON('Chat1',{"status":"initialize", "groupID":groupID},function(groupInfo){
		console.log("success");
		info="<p>This is Chatbox for "+groupInfo.group_name+" and owner is "+groupInfo.owner+"</p>";
		$("#info").html(info);
		$("#info2").append(info);


	});

	function refresh() {
		var count = $('div.date1').length;
		
		$.getJSON('Chat1',
				{
					"groupID":groupID,
					"status":count
				},

		function(array) {
			
				$("#output").empty();
				$.each(array, function(index, i) {

					$("#output").append(
							'<p>' + i.chat + '</p>' + '<br></br>'
									+ '<div class="date1">' + i.time
									+ "     by  " + i.participant + '</div>');
					$("#output").scrollTop(100000);

				});
			
		});

	}
	
	function NewMessage() {
		
		if ($("#chat").val() != "") {
			$("#output").empty();
			$.getJSON('Chat1', {
				"participant" : participant,
				"chat" : $("#chat").val(),
				"status":"new",
				"groupID":groupID
				

			}, function(array) {

			
				});
			}
		}
	

	$("button").click(function() {

		NewMessage();

	});
	
	$(document).keypress(function(e) {
		if (e.which == 13) {
			NewMessage();
		}
	});
	
	
});