$(document).ready(function(){

	// SUBMIT FORM
    $("form").submit(function(event) {
		event.preventDefault();

		var inputs = $(this).find('input');

    	// prepare data from input-form
    	var data = {
    		name : $(inputs[0]).val(),
    		surname : $(inputs[1]).val(),
    		email : $(inputs[2]).val(),
    		positionName : $(inputs[3]).val()
    	}
		ajaxPost(data);

    	// reset input data
    	$(inputs[0]).val("");
    	$(inputs[1]).val("");
    	$(inputs[2]).val("");
        $(inputs[3]).val("");
	});

    function ajaxPost(data){

    	// DO POST
    	$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/api/add",
			data : JSON.stringify(data),
			dataType: 'text',
			success : function(result) {
			    alert("Dodano")
			},
			error : function(e) {
				alert("Error!")
			}
		});

    }

});