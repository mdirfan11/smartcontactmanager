//alert("this it the alert");

const toggleSidebar = () => {
    if ($(".sidebar").is(":visible")){
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
}

const deleteConfirmation = (currentPage,cid) => {
	Swal.fire({
	  title: 'Are you sure?',
	  text: "You want to delete this contact!",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#CF0000',
	  cancelButtonColor: '#1CC5DC',
	  confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
	  if (result.isConfirmed) {
	    /*Swal.fire(
	      'Deleted!',
	      'Your file has been deleted.',
	      'success'
	    )*/
	    if (currentPage >= 0) {
			window.location = '/user/deletecontact/'+currentPage+'/'+cid;
		} else {
			window.location = '/user/deletecontact/'+cid;
		}
	    
	  } else {
		Swal.fire(
	      'Safe!',
	      'Your Contact is safe',
	      'success'
	    )
	}
	})
}

const showProfileImagePopup = (imageName) => {
	Swal.fire({
		  title: 'Sweet!',
		  text: ' ',
		  imageUrl: '/images/'+imageName,
		  imageWidth: 400,
		  imageHeight: 400,
		  imageAlt: 'Profile Image',
		})
}

const search = () => {
	let query = $("#searchInput").val();

	if (query.trim() != ""){
		let url = `http://localhost:8080/search/${query}`;
		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			console.log(data);
			let text = `<div class='list-group'>`;
			data.forEach((contact) => {
				text += `<a href='/user/${contact.id}/updatecontact' class='list-group-item list-group-item-action'> ${contact.firstName} ${contact.lastName}</a>`;
			});
			text += `</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
		});
	} else {
		$(".search-result").hide();
	}
}