<!DOCTYPE html>
<html lang="en">
	<head>
		<!--sets up title for page, character set, makes page match to all screen sizes and attaches style sheet-->
		<title>Aston Animal Sanctuary</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/mainStyleSheet.css">
	</head>
	<body>
		<!--This page doubles up as a page for adding a new animal or editing one, if isset($_POST["animalID"]) is true, then page is being used for editing animal -->
		<!--attaches page header overlay-->
		<?php include "pageOverlay.php";?>
		<div id="mainContent">
			<!--changes title to match current use of page-->
			<h1 class="title roundContainer dark"><?php if (!isset($_POST["animalID"])){ echo 'Add Animal';} else { echo 'Edit Animal';}?></h1>
			<div id="addAnimalForm" class="section roundContainer light">
				<!--changes post location depending on current use of page-->
				<!--sets up form for adding a new animal to the database-->
				<form role="form" name="addAnimalForm" action="<?php if (!isset($_POST["animalID"])){ echo 'addAnimal.php';} else { echo 'editAnimal.php';}?>" enctype="multipart/form-data" method="post">	
					<ul>
						<li><h4>Animals Name</h4></li>
						<li><input type="text" maxlength="20" class="form-control" id="animalNameInput" name="animalName" placeholder="Animal Name" required></li>
						<li><h4>Animals Gender</h4></li>
						<li><input type="radio" class="form-control" id="animalGenderMaleInput" name="animalGender" value="Male" required> Male <input type="radio" class="form-control" id="animalGenderFemaleInput" name="animalGender" value="Female" required> Female</li>
						<li><h4>Animals Date Of Birth</h4></li>
						<li><input type="date" class="form-control" id="animalDOBInput" name="animalDOB" required></li>
						<li><h4>Animals Type(e.g. Cat, Dog, Rabbit...)</h4></li>
						<li><input type="text" maxlength="20" class="form-control" id="animalTypeInput" name="animalType" placeholder="Animal Type" required></li>
						<li><h4>Animals Breed(e.g. for dogs: Bulldog, German Shepherd...)</h4></li>
						<li><input type="text" maxlength="20" class="form-control" id="animalBreedInput" name="animalBreed" placeholder="Animal Breed" required></li>
						<li><h4>Animals Description(e.g. animals behaviour, notable features..)</h4></li>
						<li><textarea rows="4" cols="50"  maxlength="200" class="form-control" id="animalDescriptionInput" name="animalDesc" placeholder="Animal Description..."></textarea></li>
						<!-- sets up original animal picture and lets user pick whether to keep or change it-->
						<?php 
							if (isset($_POST["animalID"])){ 
								echo '<li><h4>Current Animal Picture</h4></li>';
								echo '<li><img id="currentAnimalImage" src="'.'data:image/jpg;base64,'.$_POST["animalPhoto"].'"></img></li>';
								echo '<li><input type="radio" class="form-control" name="changePhoto" value="False" required> Keep Photo <input type="radio" class="form-control" name="changePhoto" value="True" required> Change Photo</li>';
								echo '<li><h4>New Animal Picture</h4></li>';
							} else {
								echo '<li><h4>Animal Picture</h4></li>';
							}
						?>
						<li><input type="file" id="animalPhotoInput" name="animalPicture"></li>
						<input type="hidden" name="submitted" value="true" />
						<!-- changes what button says based on page function -->
						<li><button class="submitButton" type="submit" ><?php if (!isset($_POST["animalID"])){ echo 'Add Animal';} else { echo 'Edit Animal';}?></button></li>
					</ul>
				</form>
			</div>
		</div>
		<script>
			<?php 
				//automatically fills in all the forms inputs if the page is being used for editing
				if (isset($_POST["animalID"])){
					$_SESSION["animalID"] = $_POST["animalID"];
					echo "document.getElementById('animalNameInput').value = '".$_POST["animalName"]."';"; 
					if ($_POST["animalGender"] = "Male"){
						echo "document.getElementById('animalGenderMaleInput').checked = true;";
					} else {
						echo "document.getElementById('animalGenderFemaleInput').checked = true;";
					}
					echo "document.getElementById('animalDOBInput').value = '".$_POST["animalDOB"]."';"; 
					echo "document.getElementById('animalTypeInput').value = '".$_POST["animalType"]."';"; 
					echo "document.getElementById('animalBreedInput').value = '".$_POST["animalBreed"]."';"; 
					echo "document.getElementById('animalDescriptionInput').value = '".$_POST["animalDesc"]."';"; 
					echo "document.getElementById('animalPhotoInput').value = '".$_POST["animalPhoto"]."';"; 
				}
			?>
		</script>
	</body>
</html>