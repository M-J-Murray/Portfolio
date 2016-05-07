<?php	
	try {
		//gets database details
		require 'linkDatabase.php';
		//fetches all the animals from the database
		$sth=$db->prepare("SELECT * FROM `animal`");
		$sth->execute();
	} catch (PDOException $e) {
		echo 'Connection failed: ' . $e->getMessage();
		die();
	}
		
	//gets array size
	$rowcount=$sth->rowCount();
	//fetches data into an associative array
	$rows = $sth->fetchAll();
	//checks array is big enough to be looped through
	if($rowcount>=1){
		for ($x = 0; $x <= $rowcount-1; $x++) {
			//checks animal is available for adoption
			if ($rows[$x]["available"] == 1){
				//splits description into smaller parts such as gender, age, type and breed
				$description = explode(' - ', $rows[$x]["description"]);
				//checks that the current animal is not a dog or a cat
				if (($description[1] != 'Dog') && ($description[1] != 'Cat')){	
					$dob = $rows[$x]["dateofbirth"];
					//formats date of birth into age
					$age = round(((time() - strtotime($dob)) / 31556926), 1);
					//creates table rows to be displayed on the other page with onclick function to display all the information in a popup
					echo '<tr onclick="displayAnimalData(\'' . $rows[$x]["animalID"] . '\' , \'' . $rows[$x]["name"] . '\' , \'' . $age .  '\' , \'' . $rows[$x]["dateofbirth"] . '\' , \'' . $rows[$x]["description"] . '\' , \'' . base64_encode( $rows[$x]['photo'] ) .'\')">';
					echo '	<td><img src="data:image/jpg;base64,'.base64_encode( $rows[$x]['photo'] ).'"/></td>';
					echo '	<td>'.$rows[$x]["name"].'</td>';
					echo '	<td>'.$age .'</td>';
					echo '</tr>';
				}
			}
		}
	}

	$db = null;
	die();
?>