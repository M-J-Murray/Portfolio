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
		<!-- attaches header overlay to page-->
		<?php include "pageOverlay.php";?>
		<!--sets current page nav button as acive-->
		<script>document.getElementById('homeNavButton').className = 'active';</script>
		<div id="mainContent">
			<h1 class="title roundContainer dark"> Welcome To The Aston Animal Sanctuary Homepage</h1>
			<!-- creates section about the cats at the sanctuary -->
			<div class="section">
				<div id="catsLink" class="animalGrid roundContainer dark">
					<!--creates images in a table which works as a link to the all animals page where it will only display the cats in the sanctuary-->
					<a href="allAnimalsPage.php?type=Cat">
						<h2 class="animalGridTitle">Our Cats</h2>
						<ul>
							<li>
								<img src="images/cat1.jpg">
							</li>
							<li>
								<img src="images/cat2.jpg">
							</li>
							<li>
								<img src="images/cat3.jpg">
							</li>
							<li>
								<img src="images/cat4.jpg">
							</li>
						</ul>
					</a>
				</div>
				<!-- describes the cats at the sanctuary -->
				<div class="descBox light" id="catsDesc">
					<p>We have cats of all shapes, sizes, colours and breeds; you'd be suprised how many cats we have! From Abyssinian cats to Ukrainian Levkoy. We keep all our animals in the best condition they can be, so take a look!</p>
				</div>
			</div>
			<!-- creates section about the dogs at the sanctuary -->
			<div class="section">
				<div id="dogsLink" class="animalGrid roundContainer dark">
					<!--creates images in a table which works as a link to the all animals page where it will only display the dogs in the sanctuary-->
					<a href="allAnimalsPage.php?type=Dog">
						<h2 class="animalGridTitle">Our Dogs</h2>
						<ul>
							<li>
								<img src="images/dog1.jpg">
							</li>
							<li>
								<img src="images/Dog2.png">
							</li>
							<li>
								<img src="images/dog3.jpg">
							</li>
							<li>
								<img src="images/Dog4.png">
							</li>
						</ul>
					</a>
				</div>
				<!-- describes the dogs at the sanctuary -->
				<div class="descBox light" id="dogsDesc">
					<p>We have dogs of all shapes, sizes, colours and breeds; you'd be suprised how many dogs we have! From Affenpinscher cats to Yorkshire Terrier. We keep all our animals in the best condition they can be, so take a look!</p>
				</div>
			</div>
			<div class="section">
				<!-- creates section about animals other than cats or dogs at the sanctuary -->
				<div id="othersLink" class="animalGrid roundContainer dark">
					<!--creates images in a table which works as a link to the all animals page where it will only display animals other than cats and dogs in the sanctuary-->
					<a href="allAnimalsPage.php?type=Other">
						<h2 class="animalGridTitle">Our Other Animals</h2>
						<ul>
							<li>
								<img src="images/rabbit1.jpg">
							</li>
							<li>
								<img src="images/sugar.jpg">
							</li>
							<li>
								<img src="images/hamster.jpg">
							</li>
							<li>
								<img src="images/guinea.jpg">
							</li>
						</ul>
					</a>
				</div>
				<!-- describes the other animals at the sanctuary -->
				<div class="descBox light" id="othersDesc">
					<p>We have other animals that you may also want to take a look at; you'd be suprised how many others we have! From Hamsters to Sugar Gliders. We keep all our animals in the best condition they can be, so have a look!</p>
				</div>
			</div>
		</div>
	</body>
</html>