<html lang="en">
	<head>
	</head>
	<body>
		<?php		
			//starts session if it hasn't already been started
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
				$animalName = test_input($_POST["animalName"]);
				$animalGender = $_POST["animalGender"];
				$animalDOB = $_POST["animalDOB"];
				$animalType = test_input($_POST["animalType"]);
				$animalBreed = test_input($_POST["animalBreed"]);
				$animalDesc = test_input($_POST["animalDesc"]);
				$animalPhotoFile=null;
				
				//attaches animal gender, type, breed and description to all be stored as one variable in the database
				$animalDesc = $animalGender . " - " . $animalType . " - " . $animalBreed . " - " . $animalDesc;
				
				//resets session error value if it was already in use
				unset($_SESSION["error"]);
			
				//checks file doesnt have an error
				if ($_FILES["animalPicture"]["error"] > 0)
				{
					//sets blank image if it cannot use image given
					$fp = fopen('Images/NoImage.png', 'rb');
					//states which error has occurred for user to see
					$_SESSION["error"] = "There was an error with the Image";

				}   else   {
					$type = $_FILES["animalPicture"]["type"];

					//checks if the type is one of the three image types
					if (($type == "image/jpeg")||($type == "image/png")||($type == "image/gif")){
						$fp = fopen($_FILES['animalPicture']['tmp_name'], 'rb');
					} else {
						//attaches blank image if it is not one of the types
						$fp = fopen('Images/NoImage.png', 'rb');
						$_SESSION["error"] = "There was an error with the file type";
					}
			   }
				
				try {
					//attaches the database credentials
					require 'linkDatabase.php';
					
					//sets the animal availability
					$available = 1;
						
					//sets up PDO with relevant query and binds parameters
					$sth=$db->prepare("INSERT INTO `animal` (`name`, `dateofbirth`, `description`, `photo`, `available`) VALUES (:pname,:pdob,:pdesc, :pphoto, :pavailable);");
					$sth->bindParam(':pname', $animalName, PDO::PARAM_STR);
					$sth->bindParam(':pdob', $animalDOB, PDO::PARAM_STR);
					$sth->bindParam(':pdesc', $animalDesc, PDO::PARAM_STR, 250);
					$sth->bindParam(':pphoto', $fp, PDO::PARAM_LOB);
					$sth->bindParam(':pavailable', $available, PDO::PARAM_BOOL);
					
					$sth->execute();
					
				} catch (PDOException $ex) {
					//updates session error to give user information about database error
					$_SESSION["error"] = 'Connection failed: ' . $ex->getMessage();
				}
				//returns to all animals page to show newly added animal then dies
				header("Location: allAnimalsPage.php");
				die();
			}
		?>
	</body>
</html>