jQuery(document).ready(function($){

	
	$("form#ajouterArticle").on('submit',function(e){ // On sélectionne le formulaire par son identifiant
	    e.preventDefault(); // Le navigateur ne peut pas envoyer le formulaire
	    var formulaire = $(this); 
	    var tbody= $("#datatableArticle").find("tbody");
	    var donnees = $(this).serialize();
	    var address = $(this).attr("action");
	    var nouvelLigne = $("#article" + $("form #code").val());
	   
	    //var formulaire = $(this).parents().find("form");
	    //var donnees = formulaire.serialize(); // On créer une variable content le formulaire sérialisé
	   
	     
	    $.ajax({ url: address, type : 'POST', data : donnees})
	    
		    .done(	function(data,texTstatus,jqXHR){					
				if (texTstatus==='success') {
					if(data==='error'){
						alert(data);
					}else{
						formulaire[0].reset();
						nouvelLigne.remove();
						tbody.prepend(data);
						$(".alert-success").slideDown(500);
					}
				}else{
					$(".alert-warning").fadeIn();
				}
			})
		    
	    	.fail(
				function(texTstatus,jqXHR){
					alert('Une erreur est survenu lors de l\'operation, veuillez reéssayer une nouvelle fois\n ' +
							'Excusez nous du désagrement causé par cet incident');
	     	})
					
	 		.always(
	 			function (){
	 				// On le cache 8 secondes plus tard
	 				setTimeout(function() { $(".alert").slideUp(500)}, 4000);
	 			}
	 		);

	});


	$("form#validerCommande").on('submit',function(e){ // On sélectionne le formulaire par son identifiant
	    e.preventDefault(); // Le navigateur ne peut pas envoyer le formulaire
	    var formulaire = $(this); 
	    var tbody= $("#datatableArticle").find("tbody");
	    var donnees = formulaire.serialize();
	    var address = formulaire.attr("action");
	   
	    //var formulaire = $(this).parents().find("form");
	    //var donnees = formulaire.serialize(); // On créer une variable content le formulaire sérialisé
	   
	     
	    $.ajax({ url: address, type : 'POST', data : donnees})
	    
		    .done(	function(data,texTstatus,jqXHR){					
				if (texTstatus==='success') {
					if(data==='error'){
						alert(data);
					}else{
						formulaire[0].reset();
						tbody.find("tr").remove();
						alert(data);
					}
				}else{
					alert(data);
				}
			})
		    
	    	.fail(
				function(texTstatus,jqXHR){
					alert('Une erreur est survenu lors de l\'operation, veuillez reéssayer une nouvelle fois\n ' +
							'Excusez nous du désagrement causé par cet incident');
	     	})
					
	 		.always(
	 			function (){
	 			//
	 			}
	 		);

	});


});
