<?php	
	try {
		//links database credentials
		require 'linkDatabase.php';
		//gets all the data about animals from the database
		$sth=$db->prepare("SELECT * FROM `animal`");
		$sth->execute();
	} catch (PDOException $e) {
		echo 'Connection failed: ' . $e->getMessage();
		die();
	}
		
	//counts array size
	$rowcount=$sth->rowCount();
	//fetches all the data into an associative array
	$rows = $sth->fetchAll();
	//checks array is large enought to be looped through	
	if($rowcount>=1){
		for ($x = 0; $x <= $rowcount-1; $x++) {
			//checks animal is available to be adopted
			if ($rows[$x]["available"] == 1){
				//splits description into smaller parts such as gender, age, type and breed
				$description = explode(' - ', $rows[$x]["description"]);
				//checks that the current animal is a cat then displays it in a table
				if ($description[1] == 'Cat'){	
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