<?php	
	try {
		//links database credentials
		require 'linkDatabase.php';
		//gets all the animals information from databse
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
	//checks if array of data is large enough to be looped through			
	if($rowcount>=1){
		for ($x = 0; $x <= $rowcount-1; $x++) {
			//only shows animals which are available to be adopted
			if ($rows[$x]["available"] == 1){
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

	$db = null;
	die();
?>