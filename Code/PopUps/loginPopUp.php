<html lang="en">
	<head>
		<!--sets up character set, makes popUp match to all screen sizes and attaches style sheet-->
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/PopUpStyleSheet.css">
	</head>
	<body>
		<script>
			//function to hide login pop up
			function hideLogin(){
				document.getElementById('loginBackWrap').className = 'backWrap undim';
				document.getElementById('loginPopUpWindow').className = 'popUpWindow hide';
			}
			//function to show login pop up
			function showLogin(){
				document.getElementById('loginBackWrap').className = 'backWrap dim';
				document.getElementById('loginPopUpWindow').className = 'popUpWindow show';
			}
		</script>
		<!-- creates the main conent of the popup -->
		<div class="popUpWindow hide" id="loginPopUpWindow">	
			<button type="button" class="closePopUpButton" onclick="hideLogin()">X</button>
			<h1 class="popUpTitle">Log In</h1>
			<!--creates a form for log in detail to be input-->
			<form role="form" name="loginForm" action="login.php" method="post">	
				<ul>
					<li><h4>Username</h4></li>
					<li><input type="text" class="form-control" name="username" placeholder="username" required></li>
					<li><h4>Password</h4></li>
					<li><input type="password" class="form-control" name="password" placeholder="Password" required></li>
					<input type="hidden" name="submitted" value="true" />
					<li><button class="submitButton"  type="submit" >Log In</button></li>
				</ul>
			</form>
		</div>
		<!-- creates a background which comes forward and dims the page when popup is shown, hides popup when it is clicked -->
		<div id="loginBackWrap" class="backWrap undim" onclick="hideLogin()">
		</div>
	</body>
</html>
