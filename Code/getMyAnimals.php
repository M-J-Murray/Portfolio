<?php	
	//starts session if it hasnt already
	if (!isset($_SESSION)) 
	{
		session_start();
	}

	try {
		//attaches database credentials
		require 'linkDatabase.php';
		//selects animalsIDs from database for the current user
		$sth=$db->prepare("SELECT `animalID` FROM `owns` WHERE `userID`=:pusername");
		$sth->bindParam(':pusername', $_SESSION["username"], PDO::PARAM_STR);
		$sth->execute();
		
		//checks array size
		$rowcount=$sth->rowCount();
		//returns all data as an associative array
		$animalIDS = $sth->fetchAll();
		//resets sth
		$sth=null;
		//checks array is big enough to be looped through
		if($rowcount>=1){
			for ($x = 0; $x <= $rowcount-1; $x++) {
				//selects relevant animals information from the database
				$sth=$db->prepare("SELECT * FROM `animal` WHERE `animalID` = ".$animalIDS[$x]["animalID"].";");
				$sth->execute();
				//returns data to array
				$row = $sth->fetch();
				$dob = $row["dateofbirth"];
				//formats date of birth to age
				$age = round(((time() - strtotime($dob)) / 31556926), 1);
				//creates table rows to be displayed on the other page with onclick function to display all the information in a popup
				echo '<tr onclick="displayAnimalData(\'' . $row["animalID"] . '\' , \'' . $row["name"] . '\' , \'' . $age .  '\' , \'' . $row["dateofbirth"] . '\' , \'' . $row["description"] . '\' , \'' . base64_encode( $row['photo'] ) .'\')">';
				echo '	<td><img src="data:image/jpg;base64,'.base64_encode( $row['photo'] ).'"/></td>';
				echo '	<td>'.$row["name"].'</td>';
				echo '	<td>'.$age .'</td>';
				echo '</tr>';
			}
		} 
	} catch (PDOException $e) {
		echo 'Connection failed: ' . $e->getMessage();
		die();
	}
	die();
?>