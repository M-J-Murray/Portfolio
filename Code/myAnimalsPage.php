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
		<!-- attaches header overlay to page and pop up to display details of selected animals-->
		<?php 
			include "pageOverlay.php";
			include "PopUps/animalDetailsPopUp.php";
		?>
		<!-- displays animals of accepted adoption requests-->
		<div class="allAnimalsContent" id="mainContent">
			<h1 class="title roundContainer dark">My Animals</h1>
			<div class="section">
				<!-- tables shows animals which adoption requests have been successful for this user-->
				<div class="animalTable" class="roundContainer light">
					<table>
						<tr>
							<th>Picture</th>
							<th>Name</th>
							<th>Age</th>
						</tr>
						<!-- attaches file to return all animals which now belong the user-->
						<?php include "getMyAnimals.php" ?>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>