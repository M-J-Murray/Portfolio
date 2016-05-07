<!-- Overlay is set as the header on every page and contains functionality for navigating throughout the website-->
<!DOCTYPE html>
<html lang="en">
	<head>
		<!--sets up character set, makes page match to all screen sizes and attaches style sheet-->
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/pageOverlayStyleSheet.css">
	</head>
	<!-- sets function to be executed when page is loaded or resized which keeps formatting the same depending on window size-->
	<body onresize="adjustNavbar()" onload="adjustNavbar()">
		<?php 
			//starts session if it hasnt been already
			if (!isset($_SESSION)) 
			{
				session_start();
			}
			//checks if user is logged in and get adoption requests relevant to the user
			if (isset($_SESSION['username'])){
				include 'getUserAnimalRequestIDs.php';
			}
		?>
		<script>
			//function is used to adjust page formating to work for all window sizes
			function adjustNavbar() {
				var width = 960;
				//if page is smaller than 960px then the page stops changing size, but if it's larger page adjusts to new sizing
				if (window.innerWidth < width){
					document.getElementById('navbar').style.width='950px';
					document.getElementById('navbar-right').style.left='920px';
					document.getElementById('navbar-center').style.left='450px';
					document.getElementById('mainContent').className = 'fixed';
					document.getElementById('loginPopUpWindow').style.left='475px';
					document.getElementById('signUpPopUpWindow').style.left='475px';
				} else {
					document.getElementById('navbar').style.width='100%';
					document.getElementById('navbar-right').style.left='100%';
					document.getElementById('navbar-center').style.left='50%';
					document.getElementById('mainContent').className = 'dynamic';
					document.getElementById('loginPopUpWindow').style.left='50%';
					document.getElementById('signUpPopUpWindow').style.left='50%';
				}
			}
		</script>
		<!--creates the navigation bar to hold the navigational links-->
		<div id="navbar">
			<ul>
				<!--splits navebar into three segments which move around depending on the screen size-->
				<div id="navbar-left">
					<?php
						//checks if user is logged in and attaches a 'my profile' button which is used to trigger a popup with user links
						if (isset($_SESSION['username'])) {
							echo '<li><a href="#" id="myProfileNavButton" onclick="moveMyProfile()"> MY PROFILE <img id="downArrowIcon" src="images/arrow_down.png" height="21" width="21"></a></li>';
						}
					?>
				</div>
				<div  id="navbar-center">
					<!--links to get to the main pages of the website-->
					<li><a href="allAnimalsPage.php" id="allAnimalsNavButton"> ALL ANIMALS</a></li>
					<li><a href="index.php" id="homeNavButton"> ASTON ANIMAL SANCTUARY</a></li>
					<li><a href="aboutPage.php" id="aboutNavButton"> ABOUT</a></li>
				</div>
				<div  id="navbar-right">
					<?php
						//if user is not logged in, then it creates buttons to login or signup
						if (!isset($_SESSION['username'])) {
							echo '<li><a href="#" id="logInNavButton" onclick="showLogin()"> LOG IN</a></li>';
							echo '<li><a href="#" id="signUpNavButton" onclick="showSignUp()"> SIGN UP</a></li>';
						}
					?>		
				</div>
			</ul>
		</div>
		<!--creates an error bar below the navigation bar which displays error information about failing to login, add animal picture, or database failures-->
		<div id="errorBar">
			<?php 
				//displays error information if there is any
				if (isset($_SESSION['error'])){
					echo $_SESSION['error'];
				}
			?>
		</div>
		<?php 
			//checks if user is logged in or not
			if (isset($_SESSION['username'])){
				//attaches pop up which holds links to other pages which only logged in users may access
				include "PopUps/myProfilePopUp.php";
			} else {
				//attaches pop ups to allow users to create an account or log in if they dont have an account or are not logged int
				include "PopUps/loginPopUp.php";
				include "PopUps/signUpPopUp.php";
			}
		?>
	</body>
</html>