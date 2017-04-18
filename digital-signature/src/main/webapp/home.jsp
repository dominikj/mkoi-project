<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<title>Generator podpisów cyfrowych</title>
<meta name="description" content="website description" />
<meta name="keywords" content="website keywords, website keywords" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<!-- modernizr enables HTML5 elements and feature detects -->
<script type="text/javascript" src="js/modernizr-1.5.min.js"></script>
</head>

<body>
	<div id="main">

		<header>
			<div id="strapline">
				<div id="welcome_slogan">
					<h3>
						Podpisy cyfrowe <span>Grupa AS_4</span>
					</h3>
				</div>
				<!--close welcome_slogan-->
			</div>
			<!--close strapline-->
			<nav>
				<div id="menubar">
					<ul id="nav">
						<li class="current"><a href="/">Home</a></li>
						<li><a href="/rsapss">RSA-PSS</a></li>
						<li><a href="/dsa">DSA</a></li>
						<li><a href="/ecdsa">ECDSA</a></li>
					</ul>
				</div>
				<!--close menubar-->
			</nav>
		</header>

		<div id="slideshow_container">
			<div class="slideshow">
				<ul class="slideshow">
					<li class="show"><img width="940" height="250"
						src="images/home_1.jpg"
						alt="Podpis cyfrowy jest ważną składową współczesnego cyberbezpieczeństwa" /></li>
					<li><img width="940" height="250" src="images/home_2.jpg"
						alt="Współczesny człowiek styka się  z cyberbezpieczeństwem w codziennym życiu" /></li>
				</ul>
			</div>
			<!--close slideshow-->
		</div>
		<!--close slideshow_container-->

		<div id="site_content">

			<div class="sidebar_container">
				<div class="sidebar">
					<div class="sidebar_item">
						<h2>Nowa strona</h2>
						<p>Strona zawiera przydatne narzędzie do generowania podpisów
							cyfrowych dokumentów trzema sposobami</p>
					</div>
					<!--close sidebar_item-->
				</div>
				<!--close sidebar-->
				<div class="sidebar">
					<div class="sidebar_item">
						<h2>Kontakt:</h2>
						<p>
							Email: <a href="mailto:dominik93j@gmail.com">Dominik
								Januszewicz</a>
						</p>
						<p>
							Email: <a href="mailto:herbalrp@gmail.com">Mateusz Zieliński</a>
						</p>
					</div>
					<!--close sidebar_item-->
				</div>
				<!--close sidebar-->
			</div>
			<!--close sidebar_container-->

			<div id="content">
				<div class="content_item">
					<h1>Co to jest podpis cyfrowy?</h1>
					<p>
						<b>Podpis cyfrowy</b> — matematyczny sposób sprawdzenia
						autentyczności dokumentów i wiadomości elektronicznych. Poprawny
						podpis oznacza, że wiadomość pochodzi od właściwego nadawcy, który
						nie może zaprzeczyć faktowi jej nadania oraz, że wiadomość nie
						została zmieniona podczas transmisji.
					</p>
					<p>Podpis cyfrowy służy zapewnieniu między innymi następujących
						funkcji bezpieczeństwa:
					<ul>
						<li>autentyczności pochodzenia, która daje pewność co do
							autorstwa dokumentu,</li>
						<li>niezaprzeczalności, która utrudnia wyparcie się autorstwa
							lub znajomości treści dokumentu,</li>
						<li>integralności, która pozwala wykryć nieautoryzowane
							modyfikacje dokumentu po jego podpisaniu.</li>
					</ul>
					</p>
					<div class="content_container">
						<p>
							<b>MKOI</b>
						</p>
						<p>Celem wykładu jest zaznajomienie słuchaczy ze współczesnymi
							algorytmami kryptograficznymi oraz metodami matematycznymi
							wykorzystywanymi w ich konstrukcji i analizie bezpieczeństwa.
							Wykład dostarcza informacji niezbędnych projektantom i
							administratorom systemów teleinformatycznych w zakresie
							stosowanych w nich zabezpieczeń kryptograficznych</p>
						<div class="button_small">
							<a
								href="http://studia.elka.pw.edu.pl/pl/17L/s/eres/eres/wwersje$.startup?Z_ID_PRZEDMIOTU=MKOI">Szczegóły</a>
						</div>
						<!--close button_small-->
					</div>
					<!--close content_container-->
					<div class="content_container">
						<p>
							<b>PKRY</b>
						</p>
						<p>Celem wykładu jest przedstawienie możliwości, jakich
							dostarczają metody kryptograficzne w zakresie zapewnienia
							bezpieczeństwa komunikacji w sieciach otwartych oraz bezpiecznego
							realizowania wszelkich usług w sieci.</p>
						<div class="button_small">
							<a
								href="http://studia.elka.pw.edu.pl/pl/16L/s/eres/eres/wwersje$.startup?Z_ID_PRZEDMIOTU=PKRY">Szczegóły</a>
						</div>
						<!--close button_small-->
					</div>
					<!--close content_container-->

				</div>
				<!--close content_item-->
			</div>
			<!--close content-->
		</div>
		<!--close site_content-->

	</div>
	<!--close main-->

	<footer>
		<div id="footer_content">
			website template by <a href="http://www.freehtml5templates.co.uk">Free
				HTML5 Templates</a>
		</div>
		<!--close footer_content-->
	</footer>

	<!-- javascript at the bottom for fast page loading -->
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/image_slide.js"></script>

</body>
</html>
