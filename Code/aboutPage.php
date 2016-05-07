<!DOCTYPE html>
<html lang="en">
	<head>
		<!--sets up title for page, character set, makes page match to all screen sizes and attaches style sheet-->
		<title>Aston Animal Sanctuary</title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" href="StyleSheets/mainStyleSheet.css">
	</head>
	<body>
		<!--attaches page header overlay-->
		<?php include "pageOverlay.php";?>
		<!--sets current page nav button as acive-->
		<script> document.getElementById('aboutNavButton').className = 'active';</script>
		<div id="mainContent">
			<h1 class="title roundContainer dark">About Aston Animal Sanctuary</h1>
			<div class="section">
				<div id="allAnimalsLink" class="animalGrid roundContainer dark">
					<a href="allAnimalsPage.php">
						<!--sets up grid of animal pictures-->
						<h2 class="animalGridTitle">Our Animals</h2>
						<ul>
							<li>
								<img src="images/cat1.jpg">
							</li>
							<li>
								<img src="images/Dog2.png">
							</li>
							<li>
								<img src="images/hamster.jpg">
							</li>
							<li>
								<img src="images/rabbit2.jpg">
							</li>
						</ul>
					</a>
				</div>
				<div class="descBox light" id="aboutDesc">
					<p>Welcome to the Aston Animal Sanctuary! We are a non-profit organisation who aims to find homes for all our animals. We have animals of all shapes and sizes that we cannot wait for you to see. They're all playful and cute and need to find a home. We hope you can find the right animal you are looking for!</p>
				</div>
			</div>
		</div>
	</body>
</html>