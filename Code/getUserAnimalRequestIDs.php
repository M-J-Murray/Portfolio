<?php
	//starts session if it hasnt been already
	if (!isset($_SESSION)) 
	{
		session_start();
	}
	
	try {
		//links database credentials
		require 'linkDatabase.php';
		//checks if current user is a staff acount or normal user account, 0 = normal user, 1 = staff user
		if ($_SESSION['staff'] == 0){
			//returns animal ids and approved status of animals the user has requested to adopt
			$sth=$db->prepare("SELECT `animalID`, `approved` FROM `adoptionrequest` WHERE `userID`=:pusername");
			$sth->bindParam(':pusername', $_SESSION["username"], PDO::PARAM_STR);
		} else {
			//returns all adoption requests made by normal users
			$sth=$db->prepare("SELECT `animalID`, `approved` FROM `adoptionrequest`");
		}
		$sth->execute();
		//attaches associative array of adoption requests to $_SESSION["userAdoptionAnimalIDs"]
		$_SESSION["userAdoptionAnimalIDs"] = $sth->fetchAll();
	} catch (PDOException $e) {
		echo 'Connection failed: ' . $e->getMessage();
		die();
	}
?>