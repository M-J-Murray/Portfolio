<?php 
	//sets up the database credentials for other pages to make sql queries
	$db = new PDO("mysql:dbname=murraym1_db; host=www.murraym1.eas-cs2410-1516.aston.ac.uk", "murraym1", "abri83eggs");
	//defines the error mode of the database
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
?>