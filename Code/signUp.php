<html lang="en">
	<head>
	</head>
	<body>

		<?php
			//starts session if it hasnt been already
			if (!isset($_SESSION)) 
			{
				session_start();
			}
			//function to validate inputs against hackers
			function test_input($data) {
			  $data = trim($data);
			  $data = stripslashes($data);
			  $data = htmlspecialchars($data);
			  return $data;
			} 
			//checks that this page has been accessed correctly using hidden submitted field
			if (isset($_POST['submitted'])){
				// define variables and set to empty values
				$username = $password = "";
				//sets posted data to relevant variables and validates them with function
				$username = test_input($_POST["username"]);
				$password = test_input($_POST["password"]);
				//resets error value
				unset($_SESSION["error"]);
				
				try {
					//links database credentials
					require 'linkDatabase.php';
					//encrypts passoword
					$encrypted_mypassword=md5($password);
					//automatically sets staff number to 0
					$staff = 0;
					//inserts new user account into the user table of the database
					$sth=$db->prepare("INSERT INTO `user` VALUES(:pid,:pstaff,:ppass);");
					$sth->bindParam(':pid', $username, PDO::PARAM_STR);
					$sth->bindParam(':pstaff', $staff, PDO::PARAM_INT);
					$sth->bindParam(':ppass', $encrypted_mypassword, PDO::PARAM_STR, 32);
					$sth->execute();
					
				} catch (PDOException $ex) {
					//returns error that username is in user if conflict occurs
					if ($ex->getCode() == 23000){
						$_SESSION["error"] = "Username Is Already In Use!";
					} else {
						echo 'Connection failed: ' . $ex->getMessage();
					}
				}
				//returns to home and dies
				header("Location: index.php");
				die();
			}
		?>
	</body>
</html>