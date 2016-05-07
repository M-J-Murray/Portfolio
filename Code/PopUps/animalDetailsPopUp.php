<html lang="en">
	<head>
		<!--sets up character set, makes popup match to all screen sizes and attaches style sheet-->
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/PopUpStyleSheet.css">
	</head>
	<body>
		<script>
			//attaches php associative array to javascript array to be processed later
			<?php 
				if (isset($_SESSION["username"])){
					echo 'userAdoptionRequests = [';
					$adoptionRequests = $_SESSION["userAdoptionAnimalIDs"];
					for ($x = 0; $x <= count($adoptionRequests)-1; $x++) {
						echo '['.$adoptionRequests[$x]["animalID"].', '.$adoptionRequests[$x]["approved"].']';
						if ($x != count($adoptionRequests)-1){
							echo ", ";
						}
					}
					echo '];';
				}
			?>
			//function to hide this popup
			function hideAnimalDetails(){
				document.getElementById('animalDetailsBackWrap').className = 'backWrap undim';
				document.getElementById('animalDetailsPopUpWindow').className = 'popUpWindow hide';
			}
			//function to display this popup
			function showAnimalDetails(){
				document.getElementById('animalDetailsBackWrap').className = 'backWrap dim';
				document.getElementById('animalDetailsPopUpWindow').className = 'popUpWindow show';
			}
			//function which attaches animal data to popup, displays relevant animal information when animal row is clicked
			function displayAnimalData(animalID, animalName, animalAge, animalDOB, animalDescription, animalPhoto){
				//splits up animal description into smaller details
				var animalDetails = animalDescription.split(" - ");
				//attaches relevant animal information to the correct elements
				document.getElementById('animalDetailsName').innerHTML = animalName;
				document.getElementById('animalDetailsGender').innerHTML = animalDetails[0];
				document.getElementById('animalDetailsAge').innerHTML = animalAge;
				document.getElementById('animalDetailsDOB').innerHTML = animalDOB;
				document.getElementById('animalDetailsType').innerHTML = animalDetails[1];
				document.getElementById('animalDetailsBreed').innerHTML = animalDetails[2];
				document.getElementById('animalDetailsDesc').innerHTML = animalDetails[3];
				document.getElementById('animalDetailsPhoto').src = 'data:image/jpg;base64,'+animalPhoto;
				//creates animal data object for later use
				window.animalData = {ID:animalID, name:animalName, gender:animalDetails[0], age:animalAge, dateOfBirth:animalDOB, type:animalDetails[1], breed:animalDetails[2], description:animalDetails[3], photo:animalPhoto};
				<?php 
					if (isset($_SESSION["username"])){
						//checks if staff login or normal login then runs functions to determine whether buttons should be displayed for the animals on the popup
						if ($_SESSION["staff"] == 0){ 
							echo 'determineAdoptionRequestButton();';
						} else {
							echo 'determineProcessAdoptionRequestButtons();';
						}
					}
				?>
				//displays animal popup once details attached
				showAnimalDetails();
			}
			//function runs when edit animal details is clicked, and creates a form with current animals details which is posted to addAnimalPage.php when animal details can be edited
			function editAnimalDetails(){
				//creates form
				var form = document.createElement('form');
				form.setAttribute('method', 'post');
				form.setAttribute('action', 'addAnimalPage.php');
				form.setAttribute('enctype', 'multipart/form-data');
				
				//creates animalID field and attaches it to form
				var IDField = document.createElement('input');
				IDField.setAttribute('type', 'hidden');
				IDField.setAttribute('name', 'animalID');
				IDField.setAttribute('value', animalData.ID);
				form.appendChild(IDField);
				
				//creates animalName field and attaches it to form
				var nameField = document.createElement('input');
				nameField.setAttribute('type', 'hidden');
				nameField.setAttribute('name', 'animalName');
				nameField.setAttribute('value', animalData.name);
				form.appendChild(nameField);
				
				//creates animalAge field and attaches it to form
				var ageField = document.createElement('input');
				ageField.setAttribute('type', 'hidden');
				ageField.setAttribute('name', 'animalAge');
				ageField.setAttribute('value', animalData.age);
				form.appendChild(ageField);
				
				//creates animalDateOfBirth field and attaches it to form
				var dateOfBirthField = document.createElement('input');
				dateOfBirthField.setAttribute('type', 'hidden');
				dateOfBirthField.setAttribute('name', 'animalDOB');
				dateOfBirthField.setAttribute('value', animalData.dateOfBirth);
				form.appendChild(dateOfBirthField);
				
				//creates animalType field and attaches it to form
				var typeField = document.createElement('input');
				typeField.setAttribute('type', 'hidden');
				typeField.setAttribute('name', 'animalType');
				typeField.setAttribute('value', animalData.type);
				form.appendChild(typeField);
				
				//creates animalBreed field and attaches it to form
				var breedField = document.createElement('input');
				breedField.setAttribute('type', 'hidden');
				breedField.setAttribute('name', 'animalBreed');
				breedField.setAttribute('value', animalData.breed);
				form.appendChild(breedField);
				
				//creates animalDescription field and attaches it to form
				var descriptionField = document.createElement('input');
				descriptionField.setAttribute('type', 'hidden');
				descriptionField.setAttribute('name', 'animalDesc');
				descriptionField.setAttribute('value', animalData.description);
				form.appendChild(descriptionField);
				
				//creates animalPhoto field which has raw picture data and attaches it to form
				var photoField = document.createElement('input');
				photoField.setAttribute('type', 'hidden');
				photoField.setAttribute('name', 'animalPhoto');
				photoField.setAttribute('value', animalData.photo);
				form.appendChild(photoField);
				
				//attaches form the popup and submits it
				document.body.appendChild(form);
				form.submit();
			}
			//function runs when normal user presses request adoption button 
			function requestAdoption(){
				//checks to confirm users request
				var request = confirm("Are you Sure About Adoption?");
				//if user confirms
				if (request == true) {
					//creates form to be sent to makeAdoptionRequest.php with animalID
					var form = document.createElement('form');
					form.setAttribute('method', 'post');
					form.setAttribute('action', 'makeAdoptionRequest.php');
					
					//creates animalID field and attaches it to form
					var animalIDField = document.createElement('input');
					animalIDField.setAttribute('type', 'hidden');
					animalIDField.setAttribute('name', 'animalID');
					animalIDField.setAttribute('value', animalData.ID);
					form.appendChild(animalIDField);
					
					//attaches form to page and submits it
					document.body.appendChild(form);
					form.submit();
				} 
			}
			//this function  is used to determine whether the adoption request button should be displayed based on wheter the user has already requested to adopt the animal
			function determineAdoptionRequestButton(){
				var showRequestButton = true;
				var i;
				for (i = 0; i < userAdoptionRequests.length; i++) { 
					//checks if animalID matches any of the animalIDs of the users requested adoption animals
					if (userAdoptionRequests[i][0]	== animalData.ID){
						showRequestButton = false;
						break;
					}
				}
				//if the user hasnt requested to adopt this animal then the button is shown
				if (showRequestButton == true){
					//button is created
					var requestButton = document.createElement('button');
					requestButton.type = 'button';
					requestButton.id = 'requestAdoptionButton'; 
					requestButton.onclick = function() { // Note this is a function
						requestAdoption();
					};
					requestButton.innerHTML = 'RequestAdoption';
					
					//button is attached to the popup
					var animalPopUp = document.getElementById('animalDetailsPopUpWindow');
					animalPopUp.appendChild(requestButton);
				} else {
					//if the user has already requested to adopt this animal then the button is removed if it already existed
					if (document.getElementById('requestAdoptionButton') != null){
						document.getElementById('animalDetailsPopUpWindow').removeChild(document.getElementById('requestAdoptionButton'));
					}
				}
			}
			//this function is used to determine whether or not the accept or deny adoption request buttons should be displayed based on whether the the request has already been accepted or denied before
			function determineProcessAdoptionRequestButtons(){
				var showProcessButtons = false;
				var i;
				for (i = 0; i < userAdoptionRequests.length; i++) { 
					//checks if the animal ID matches any of the other adoption request animals ids
					if (userAdoptionRequests[i][0]	== animalData.ID){
						//if it does, then it checks that the adoption request hasnt been accepted or denied
						if (userAdoptionRequests[i][1] == undefined){
							showProcessButtons = true;
							break;
						}
					}
				}
				//if the animals request hasnt been processed yet then the buttons are created
				if (showProcessButtons == true){
					//creates the approve adoption button
					var approveProcessButton = document.createElement('button');
					approveProcessButton.type = 'button';
					approveProcessButton.id = 'approveAnimalButton'; 
					approveProcessButton.onclick = function() {
						processAnimalRequest(1);
					};
					approveProcessButton.innerHTML = 'Approve';
					
					//creates the approve adoption button
					var denyProcessButton = document.createElement('button');
					denyProcessButton.type = 'button';
					denyProcessButton.id = 'denyAnimalButton'; 
					denyProcessButton.onclick = function() {
						processAnimalRequest(0);
					};
					denyProcessButton.innerHTML = 'Deny';

					//attaches the buttons to the popup
					var animalPopUp = document.getElementById('animalDetailsPopUpWindow');
					animalPopUp.appendChild(approveProcessButton);
					animalPopUp.appendChild(denyProcessButton);
				} else {
					//if the animals adoption request has been processed and the buttons have already been created then they are removed
					if (document.getElementById('approveAnimalButton') != null){
						document.getElementById('animalDetailsPopUpWindow').removeChild(document.getElementById('approveAnimalButton'));
					}
					if (document.getElementById('denyAnimalButton') != null){
						document.getElementById('animalDetailsPopUpWindow').removeChild(document.getElementById('denyAnimalButton'));
					}
				}
			}
			//this function runs once one of the accept or deny adoption request buttons have been pressed and creates a form of the animals ID and whether the adoption has been approved
			function processAnimalRequest(approved){
				//creates the form
				var form = document.createElement('form');
				form.setAttribute('method', 'post');
				form.setAttribute('action', 'processAnimalAdoptionRequest.php');
				
				//creates animal id field and attaches it to the form
				var animalIDField = document.createElement('input');
				animalIDField.setAttribute('type', 'hidden');
				animalIDField.setAttribute('name', 'animalID');
				animalIDField.setAttribute('value', animalData.ID);
				form.appendChild(animalIDField);
				
				//creates the approved field and attaches it to the form
				var approvedField = document.createElement('input');
				approvedField.setAttribute('type', 'hidden');
				approvedField.setAttribute('name', 'approved');
				approvedField.setAttribute('value', approved);
				form.appendChild(approvedField);
				
				//attaches form to the document and submits it
				document.body.appendChild(form);
				form.submit();
			}
		</script>
		<!-- creates the popups content-->
		<div class="popUpWindow hide" id="animalDetailsPopUpWindow">	
			<button type="button" class="closePopUpButton" onclick="hideAnimalDetails()">X</button>
			<h1 class="popUpTitle">Animal Details</h1>	
			<img src="" id="animalDetailsPhoto"></img>
			<!-- creates the list of animal details to be filled in later -->
			<ul>
				<li><h3>Name: </h3><h4 id="animalDetailsName"></h4></li>
				<li><h3>Gender: </h3><h4 id="animalDetailsGender"></h4></li>
				<li><h3>Age: </h3><h4 id="animalDetailsAge"></h4></li>
				<li><h3>Date Of Birth: </h3><h4 id="animalDetailsDOB"></h4></li>
				<li><h3>Type: </h3><h4 id="animalDetailsType"></h4></li>
				<li><h3>Breed: </h3><h4 id="animalDetailsBreed"></h4></li>
				<li><h3>Description: </h3><h4 id="animalDetailsDesc"></h4></li>
			</ul>
			<?php 
				//if user is logged in and is on a staff account then the edit animal button is available to be clicked
				if (isset($_SESSION['username'])){ 
					if ($_SESSION['staff'] == 1){ 
						echo '<button type="button" id="editAnimalButton" onclick="editAnimalDetails()">Edit Details</button>';
					}
				}
			?>
		</div>
		<!-- creates a background which comes forward and dims the page when popup is shown, hides popup when it is clicked -->
		<div id="animalDetailsBackWrap" class="backWrap undim" onclick="hideAnimalDetails()">
		</div>
	</body>
</html>
