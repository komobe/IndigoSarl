jQuery(document).ready(function($){

	var address; // le lien de l'item a supprimer
	var ligne; // la ligne du tableau contenant l'article ou l'emission en quetion

	$('.btn-delete').on('click',function(event){

		event.preventDefault(); // On annule l'action par defaut du boutton

		address=$(this).attr('onclick');

		ligne=$(this).parent().parent() ; 

		$("#confirmModalYes").attr('onclick',address);
		
		$("#confirmModal").modal("show");
	});
	
	
});
