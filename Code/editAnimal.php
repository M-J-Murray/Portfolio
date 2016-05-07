<html lang="en">
	<head>
	</head>
	<body>
		<?php
			//starts session if it hasn't already been
			if (!isset($_SESSION)) 
			{
				session_start();
			}
			
			//function to validate inputs to stop hacking
			function test_input($data){
			  $data = trim($data);
			  $data = stripslashes($data);
			  $data = htmlspecialchars($data);
			  return $data;
			}
			
			//checks that form has correctly been submitted with hidden submitted input
			if (isset($_POST['submitted'])){	
				
				// define variables and set to empty values
				$animalName = $animalGender = $animalDOB = $animalType = $animalBreed = $animalDesc = "";
				
				//attaches all posted variables to variables, test_input used for text inputs
				$animalID = $_SESSION["animalID"];
				$animalName = test_input($_POST["animalName"]);
				$animalGender = $_POST["animalGender"];
				$animalDOB = $_POST["animalDOB"];
				$animalType = test_input($_POST["animalType"]);
				$animalBreed = test_input($_POST["animalBreed"]);
				$animalDesc = test_input($_POST["animalDesc"]);
				$animalPhotoFile=null;
				$fp = null;
				$changePhoto = $_POST["changePhoto"];
				
				//attaches animal gender, type, breed and description to all be stored as one variable in the database
				$animalDesc = $animalGender . " - " . $animalType . " - " . $animalBreed . " - " . $animalDesc;
				
				//resets session error value and animalID value
				unset($_SESSION["error"]);
				unset($_SESSION["animalID"]);
			
				//checks if user wanted to change the animals photo
				if ($changePhoto == "True"){
					//checks file doesnt have an error
					if ($_FILES["animalPicture"]["error"] > 0){
						//updates session error to display relevant relevant information
						$_SESSION["error"] = "There was an error with the photo";
						//sets blank image if it cannot use image given
						$fp = fopen('images/NoImage.png', 'rb');
					}   else   {
						$type = $_FILES["animalPicture"]["type"];

						//checks if the type is one of the three image types
						if (($type == "image/jpeg")||($type == "image/png")||($type == "image/gif")){
							$fp = fopen($_FILES['animalPicture']['tmp_name'], 'rb');
						} else {
							//sets blank image if file supplies was wrong type
							$fp = fopen('Images/NoImage.png', 'rb');
							//updates session error to display relevant relevant information
							$_SESSION["error"] = "There was an error with the file type";
						}
				   }
				}
				
				try {
					//attaches the database credentials
					require 'linkDatabase.php';
					
					//checks if animal photo is to be updated or not then uses relevant sql queries and parameters
					if ($changePhoto == "True"){
						$sth=$db->prepare("UPDATE `animal` SET `name`=:pname, `dateofbirth`=:pdob, `description`=:pdesc, `photo`=:pphoto WHERE `animal`.`animalID`=:pid;");
						$sth->bindParam(':pphoto', $fp, PDO::PARAM_LOB);
					} else {
						$sth=$db->prepare("UPDATE `animal` SET `name`=:pname, `dateofbirth`=:pdob, `description`=:pdesc  WHERE `animal`.`animalID`=:pid;");
					}
					$sth->bindParam(':pid', $animalID, PDO::PARAM_INT);
					$sth->bindParam(':pname', $animalName, PDO::PARAM_STR);
					$sth->bindParam(':pdob', $animalDOB, PDO::PARAM_STR);
					$sth->bindParam(':pdesc', $animalDesc, PDO::PARAM_STR, 250);
					
					$sth->execute();
					
				} catch (PDOException $ex) {
					//updates session error to display relevant relevant information
					$_SESSION["error"] = 'Connection failed: ' . $ex->getMessage();
				}
				//returns user to all animals page to show edited animal
				header("Location: allAnimalsPage.php");
				die();
			}
		?>
	</body>
</html>