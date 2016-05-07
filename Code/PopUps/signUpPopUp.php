<html lang="en">
	<head>
		<!--sets up character set, makes popUp match to all screen sizes and attaches style sheet-->
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/PopUpStyleSheet.css">
	</head>
	<body>
		<script>
			//function is run when signup form is submitted and validates the details
			function validateSignUpForm() {
				//gets data from form
				var username = document.forms['signUpForm']['username'].value;
				var password = document.forms['signUpForm']['password'].value;
				var reconfirmPassword = document.forms['signUpForm']['reconfirmPassword'].value;
				var error = '0';
				var patt = /^[a-zA-Z0-9_-]{6,20}$/;
				//checks username is made of only chars, ints, - or _
				if (patt.test(username) == false) {
					document.getElementById('signUpErrorBox').innerHTML = 'Invalid Username\n';
					var error = '1';
				}
				//checks password is made of only chars, ints, - or _
				if (patt.test(password) == false) {
					document.getElementById('signUpErrorBox').innerHTML = 'Invalid Password\n';
					var error = '1';
				}
				//checks that both passwords match eachother
				if (reconfirmPassword != password) {
					document.getElementById('signUpErrorBox').innerHTML = 'Passwords Do Not Match! \n';
					var error = '1';
				}
				//if there was an error then error box in popup is displayed with details to the error
				if (error == '1'){
					document.getElementById('signUpErrorBox').style.marginTop = '20px';
					document.getElementById('signUpPopUpWindow').style.height = 300+document.getElementById('signUpErrorBox').offsetHeight+'px';
					return false;
				} else {
					return true;
				}
			}
			//function hides the popup
			function hideSignUp(){
				document.getElementById('signUpBackWrap').className = 'backWrap undim';
				document.getElementById('signUpPopUpWindow').className = 'popUpWindow hide';
				document.getElementById('signUpPopUpWindow').style.height = '300px';
				document.getElementById('signUpErrorBox').innerHTML = '';
				document.getElementById('signUpErrorBox').style.marginTop = '0px';
			}
			//functions shows the popup
			function showSignUp(){
				document.getElementById('signUpBackWrap').className = 'backWrap dim';
				document.getElementById('signUpPopUpWindow').className = 'popUpWindow show';
			}
		</script>
		<!--main content of the popup-->
		<div class="popUpWindow hide" id="signUpPopUpWindow">	
			<button type="button" class="closePopUpButton" onclick="hideSignUp()">X</button>
			<h1 class="popUpTitle">Sign Up</h1>
			<!--form to input all signup details-->
			<form role="form" name="signUpForm" onsubmit="return validateSignUpForm()" action="signUp.php" method="post">	
				<ul>
					<li><h4>Please Enter A Username</h4></li>
					<li><input type="text" class="form-control" name="username" placeholder="username" required></li>
					<li><h4>Please Enter A Password</h4></li>
					<li><input type="password" class="form-control" name="password" placeholder="Password" required></li>
					<li><h4>Please Reconfirm Your Password</h4></li>
					<li><input type="password" class="form-control" name="reconfirmPassword" placeholder="Reconfirm Password" required></li>
					<li><div class="errorBox" id="signUpErrorBox"></div></li>
					<input type="hidden" name="submitted" value="true" />
					<li><button class="submitButton"  type="submit" >Sign Up</button></li>
				</ul>
			</form>
		</div>
		<!-- creates a background which comes forward and dims the page when popup is shown, hides popup when it is clicked -->
		<div id="signUpBackWrap" class="backWrap undim" onclick="hideSignUp()">
		</div>
	</body>
</html>
