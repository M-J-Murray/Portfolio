<?php
	//starts session it hasnt been already
	if (!isset($_SESSION)) 
	{
		session_start();
	}
	
	try {
		//links database credentials
		require 'linkDatabase.php';
		
		//attaches adoptionrequest details to variable
		$animalIDS = $_SESSION["userAdoptionAnimalIDs"];
		
		//counts array size
		$rowcount = count($animalIDS);
		
		//checks if array size is big enough to loop
		if($rowcount>=1){
			for ($x = 0; $x <= $rowcount-1; $x++) {
				//gets all the information relating the current animal in array to be displayed in table
				$sth=$db->prepare("SELECT * FROM `animal` WHERE `animalID` = ".$animalIDS[$x]["animalID"].";");
				$sth->execute();
				$row = $sth->fetch();
				$dob = $row["dateofbirth"];
				//formats date of birth to get an age
				$age = round(((time() - strtotime($dob)) / 31556926), 1);
				//creates table row with relevant animal information, creates onclick function to send all the animal information to animal row on other page
				echo '<tr onclick="displayAnimalData(\'' . $row["animalID"] . '\' , \'' . $row["name"] . '\' , \'' . $age .  '\' , \'' . $row["dateofbirth"] . '\' , \'' . $row["description"] . '\' , \'' . base64_encode( $row['photo'] ) .'\')">';
				echo '	<td><img src="data:image/jpg;base64,'.base64_encode( $row['photo'] ).'"/></td>';
				echo '	<td>'.$row["name"].'</td>';
				echo '	<td>'.$age .'</td>';
				//sets up approved collumn based on the values of approved
				if ($animalIDS[$x]["approved"] == NULL){
					echo '	<td>Pending Response</td>';
				} else if ($animalIDS[$x]["approved"] == 0){
					echo '	<td>Denied</td>';
				} else {
					echo '	<td>Approved</td>';
				}
				echo '</tr>';
			}
		} 
	} catch (PDOException $e) {
		echo 'Connection failed: ' . $e->getMessage();
		die();
	}
	die();
?>