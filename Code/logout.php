<?php 
	//destroys session and all session variables with it to log out and returns home
	session_start();
	session_destroy();
	header("location: index.php");
?>