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
		<!--attaches page header overlay and creates pop up to display animal data when animal row is clicked-->
		<?php 
			include "pageOverlay.php";
			include "PopUps/animalDetailsPopUp.php";
		?>
		<!--sets current page nav button as acive-->
		<script> document.getElementById('allAnimalsNavButton').className = 'active'; </script>
		<div class="allAnimalsContent" id="mainContent">
			<h1 class="title roundContainer dark">All Animals</h1>
			<div class="section">
				<!-- creates animal table to display relevant animals -->
				<div class="animalTable" class="roundContainer light">
					<table>
						<tr>
							<th>Picture</th>
							<th>Name</th>
							<th>Age</th>
						</tr>
						<?php 
							//checks if the page is being used to display specific animal types based on $_GET['type'] variable, then links to relevant php page to display the data
							if (isset($_GET['type'])){
								if ($_GET['type'] == 'Cat'){
									include "getCats.php";
								} else if ($_GET['type'] == 'Dog'){
									include "getDogs.php";
								} else if ($_GET['type'] == 'Other'){
									include "getOthers.php";
								}
							} else{
								include "getAnimals.php";
							}
						?>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>