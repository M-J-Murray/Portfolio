<html lang="en">
	<head>
	</head>
	<body>
		<?php
			//starts the session if it hasnt been already
			if (!isset($_SESSION)) 
			{
				session_start();
			}
			
			//function to validate data against hackers
			function test_input($data){
			  $data = trim($data);
			  $data = stripslashes($data);
			  $data = htmlspecialchars($data);
			  return $data;
			}	
			
			//checks if the submitted value has been sent from the login popup
			if (isset($_POST['submitted'])){
				// define variables and set to empty values
				$username = $password = "";
				
				//tests the username and password inputs from the other page and sets them to relevant variables
				$username = test_input($_POST["username"]);
				$password = test_input($_POST["password"]);
				
				//resets the session error value
				unset($_SESSION["error"]);
				
				try {
					//links to database credentials
					require 'linkDatabase.php';
					
					//encrypts password to md5 for security
					$encrypted_mypassword=md5($password);
					
					//selects data from databse relevant to the users input
					$sth=$db->prepare("SELECT * FROM `user` WHERE `userID`=:pid AND `password`=:ppass;");
					$sth->bindParam(':pid', $username, PDO::PARAM_STR);
					$sth->bindParam(':ppass', $encrypted_mypassword, PDO::PARAM_STR, 32);
					$sth->execute();
					$rows = $sth;
				} catch (PDOException $e) {
					echo 'Connection failed: ' . $e->getMessage();
					die();
				}
				
				//checks array size
				$rowcount=$rows->rowCount();
				
				// If result matched $myusername and $mypassword, table row must be 1 row
				if($rowcount==1){
					$row = $rows->fetch();
					// Register $myusername, $mypassword to session variables
					$_SESSION["username"] = $row["userID"];
					$_SESSION["staff"] = $row["staff"];
					//sets up session adoption requests for user
					include 'getUserAnimalRequestIDs.php';
					//unsets error as login is successful and returns to index
					unset($_SESSION["error"]);
					header("Location: index.php");
				}
				else {
					//setslogin error to session variable and returns to index
					$_SESSION["error"] = "The Username Or Password You Have Entered Is Incorrect!";
					header("Location: index.php");
				}
				die();
			}
		?>
	</body>
</html>
