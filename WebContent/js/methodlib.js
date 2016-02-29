

$(function() {
	
	var clock = self.setInterval(function() {
		refresh()
	}, 1000);
	$("#url").val("http://swen90002-28.cis.unimelb.edu.au:8080/5th_ea/");
	
		$("#send").button({
			icons : {
				primary : "ui-icon-comment"
			},
			text : false
		});
	
		$("#share").button().click(function(){
			$( "#dialog-form" ).dialog( "open" );
		});
	
		 var email = $( "#email" ),	 tips = $( ".validateTips" ), allFields = $( [] ).add( email );
		 function updateTips( t ) {
			 tips.text( t ).addClass( "ui-state-highlight" );
			 setTimeout(function() {
			 tips.removeClass( "ui-state-highlight", 1500 );
			 }, 500 )
		 }
			 
	
	$( "#dialog-form" ).dialog({
		autoOpen: false,
		height: 300,
		width: 350,
		modal: true,
		buttons: {
			"Send invitation": function() {
				var bValid = true;
				allFields.removeClass( "ui-state-error" );
				bValid = bValid && checkLength( email, "email", 6, 80 );
				// From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
				bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com" );
				
				if(bValid){
					$.getJSON('Chat',{'email':$("#email").val(),'url':$("#url").val()},function(array){});
					$( this ).dialog( "close" );
					$("#loading").dialog("open");
					
					
				}
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			allFields.val( "" ).removeClass( "ui-state-error" );
		}
	});
	
	 $( "#loading" ).dialog({
		 autoOpen :false,
		 height: 140,
		 modal: true,
		 close: function() {
			location.reload();
		 }
	});
 
	 var progressbar = $( "#progressbar" ),
	 progressLabel = $( ".progress-label" );
	 progressbar.progressbar({
		 value: false,
		 change: function() {
			 progressLabel.text( progressbar.progressbar( "value" ) + "%" );
		 },
		 complete: function() {
			 progressLabel.text( "Email sent!" );
		 }
	 });
	 
	 function progress() {
		 var val = progressbar.progressbar( "value" ) || 0;
		 progressbar.progressbar( "value", val + 1 );
		 if ( val < 99 ) {
			 setTimeout( progress, 100 );
		 }
	 }
	 setTimeout( progress, 1000 );
	 
	function checkLength( o, n, min, max ) {
		 if ( o.val().length > max || o.val().length < min ) {
			 o.addClass( "ui-state-error" );
			 updateTips( "Length of " + n + " must be between " +
			 min + " and " + max + "." );
			 return false;
		 } else {
			 return true;
		 }
	 }
	 
	 function checkRegexp( o, regexp, n ) {
		 if ( !( regexp.test( o.val() ) ) ) {
			 o.addClass( "ui-state-error" );
			 updateTips( n );
			 return false;
		 } else {
			 return true;
		 }
	 }
	 
	function NewMessage() {

		if ($("#chat").val() != "") {
			$("#output").empty();
			$.getJSON('Chat', {
				username1 : $("#username").val(),
				chat1 : $("#chat").val(),

			}, function(array) {

			});
		}
	}

	
	function refresh() {
		var count = $('div.date1').length;

		$.getJSON('Chat',

		function(array) {
			if (count < array.length) {
				$("#output").empty();
				$.each(array, function(index, i) {

					$("#output").append(
							'<p>' + i.chat2 + '</p>' + '<br></br>'
									+ '<div class="date1">' + i.time2
									+ "     by  " + i.username2 + '</div>');
					$("#output").scrollTop(10000);

				});
			}
		});

	}
	

	
	$(document).ready(function() {

		$("#send").click(function() {

			NewMessage();
			$("#chat").val("");

		});
	});
	$(document).keypress(function(e) {
		if (e.which == 13) {
			NewMessage();
			$("#chat").val("");
		}
	});
	
});