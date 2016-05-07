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
		<div class="animalAdoptionsContent" id="mainContent">
			<h1 class="title roundContainer dark">Review Animal Adoptions</h1>
			<div class="section">
				<!--creates table to display animal adoptions requests-->
				<div class="animalTable" class="roundContainer light">
					<table>
						<tr>
							<th>Picture</th>
							<th>Name</th>
							<th>Age</th>
							<th>Approved?</th>
						</tr>
						<!-- attaches php link to get all the information from the database-->
						<?php include "getAdoptionRequests.php" ?>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>