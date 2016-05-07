<html lang="en">
	<head>
	</head>
	<body>
		<?php			
			//attaches posted data to relevant variables
			$animalID = $_POST["animalID"];
			$approved = $_POST["approved"];
			
			try {
				//links to database credentials
				require 'linkDatabase.php';
				//updates adoption request table to state whether adoption request has been accepted or denied
				$sth=$db->prepare("UPDATE `adoptionrequest` SET `approved`=:papproved WHERE `animalID`=:pid;");
				$sth->bindParam(':pid', $animalID, PDO::PARAM_INT);
				$sth->bindParam(':papproved', $approved, PDO::PARAM_BOOL);
				$sth->execute();
				
				$sth = null;
				//checks if adoption request has been successful
				if ($approved == true){		
					$available = 0;
					//updates animal in animal table to state that it is no longer available
					$sth=$db->prepare("UPDATE `animal` SET `available`=:pavailable WHERE `animalID`=:pid;");
					$sth->bindParam(':pavailable', $available, PDO::PARAM_INT);
					$sth->bindParam(':pid', $animalID, PDO::PARAM_INT);
					$sth->execute();
					//resets sth
					$sth = null;
					//gets the username from the database of the user who requested the adoption
					$sth=$db->prepare("SELECT `userID` FROM `adoptionrequest` WHERE `animalID`=:pid;");
					$sth->bindParam(':pid', $animalID, PDO::PARAM_INT);
					$sth->execute();
					
					$row = $sth->fetch();
					//resets sth
					$sth = null;
					//attaches username to relevant variables
					$userID = $row["userID"];
					//inserts username and animal into owns table to state that the user now owns that animal
					$sth=$db->prepare("INSERT INTO `owns` (`userID`, `animalID`) VALUES (:puserid, :panimalid);");
					$sth->bindParam(':puserid', $userID, PDO::PARAM_STR);
					$sth->bindParam(':panimalid', $animalID, PDO::PARAM_INT);
					$sth->execute();
				}
				
			} catch (PDOException $ex) {
				//states there has been a database error
				$_SESSION["error"] = 'Connection failed: ' . $ex->getMessage();
			}
			//returns user to animal adoptions page to process more adoption requests
			header("Location: animalAdoptionsPage.php");
			die();
		?>
	</body>
</html>