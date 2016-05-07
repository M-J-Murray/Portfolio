<html lang="en">
	<head>
		<!--sets up character set, makes popUp match to all screen sizes and attaches style sheet-->
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/PopUpStyleSheet.css">
	</head>
	<body>
		<script>
			//function controls whether the popup should slide down or slide up based on the current classname of myProfilePopUpWindow
			function moveMyProfile(){
				if (document.getElementById('myProfilePopUpWindow').className == 'popUpWindow slide'){
					document.getElementById('myProfilePopUpWindow').className = 'popUpWindow unslide';
					document.getElementById('downArrowIcon').src = 'images/arrow_down.png';
				} else {
					document.getElementById('myProfilePopUpWindow').className = 'popUpWindow slide';
					document.getElementById('downArrowIcon').src = 'images/arrow_up.png';
				}	
			}
		</script>
		<div class="popUpWindow unslide" id="myProfilePopUpWindow">
			<ul>
				<div id="userInfoTitle">
				<!--displays users username-->
				<li><h2><?php echo $_SESSION['username'];?></h2></li>
				<!--displays users account type depending whether it is a staff login or normal user-->
				<li><h2>
					<?php 
							if ($_SESSION['staff'] == 0){
								echo "USER LOGIN";
							} else {
								echo "STAFF LOGIN";
							}
					?>
				</h2></li>	
				</div>
				<!--displays the add animals link button or my animals link button based on if it is a staff user or normal user respectively-->
				<?php 
					if ($_SESSION['staff'] == 1){
						echo '<li><a href="addAnimalPage.php" id="addAnimalButton">ADD ANIMAL</a></li>';
					} else {
						echo '<li><a href="myAnimalsPage.php" id="myAnimalsButton">MY ANIMALS</a></li>';
					}
				?>	
				<!--creates logged in user button-->
				<li><a href="animalAdoptionsPage.php" id="adoptionRequestsButton">ADOPTION REQUESTS</a></li>
				<li><a href="logout.php" id="logOutButton">LOG OUT</a></li>					
			</ul>
		</div>
	</body>
</html>
