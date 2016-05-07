<html lang="en">
	<head>
	</head>
	<body>
		<?php
			//starts session if it hasn't been already
			if (!isset($_SESSION)) 
			{
				session_start();
			}
			// define variables and set to empty values
			$username = $animalID = "";
			
			//sets session and post values to relevant variables
			$username = $_SESSION["username"];
			$animalID = $_POST['animalID'];
			
			try {				
				//attaches database credentials
				require 'linkDatabase.php';
				
				$approved = null;
				//creates adoptions request in database with relevant parameters and values
				$sth=$db->prepare("INSERT INTO `adoptionrequest` (`userID`, `animalID`, `approved`) VALUES (:puserid,:panimalid,:papproved);");
				$sth->bindParam(':puserid', $username, PDO::PARAM_STR);
				$sth->bindParam(':panimalid', $animalID, PDO::PARAM_INT);
				$sth->bindParam(':papproved', $approved, PDO::PARAM_BOOL);
				$sth->execute();
			} catch (PDOException $e) {
				echo 'Connection failed: ' . $e->getMessage();
				die();
			}
			//returns to animal adoption page to see pending requests
			header("Location: animalAdoptionsPage.php");
			die();
		?>
	</body>
</html>