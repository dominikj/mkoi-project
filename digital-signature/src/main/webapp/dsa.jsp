<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<title>Generator podpisów cyfrowych</title>
<meta name="description" content="website description" />
<meta name="keywords" content="website keywords, website keywords" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<!-- modernizr enables HTML5 elements and feature detects -->
<script type="text/javascript" src="/js/modernizr-1.5.min.js"></script>
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
						<li><a href="/">Home</a></li>
						<li><a href="/rsapss">RSA-PSS</a></li>
						<li class="current"><a href="/dsa">DSA</a></li>
					</ul>
				</div>
				<!--close menubar-->
			</nav>
		</header>

		<div id="slideshow_container">
			<div class="slideshow">
				<ul class="slideshow">
					<li class="show"><img width="940" height="250"
						src="/images/home_1.jpg"
						alt="Podpis cyfrowy jest ważną składową współczesnego cyberbezpieczeństwa" /></li>
					<li><img width="940" height="250" src="/images/home_2.jpg"
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
					<h1>Algorytm DSA</h1>
					<p>
						<b>Digital Signature Algorithm</b> to asymetryczny algorytm
						stworzony przez National Institute of Standards and Technology w
						1991 roku dla potrzeb Digital Signature Standard.
					</p>
					<p>Do wygenerowania kluczy oraz samego podpisu potrzebne są
						trzy parametry, które mogą być dzielone przez wszystkich
						użytkowników systemu. Są to: liczba pierwsza P, liczba pierwsza Q
						oraz generator G. Następnie każdy z użytkowników może wygenerować
						na podstawie tych trzech stałych swój indywidulany zestaw kluczy -
						klucz prywatny wykorzystywany do podpisywania dokumentu oraz klucz
						publiczny potrzebny przy weryfikacji podpisu.</p>
					<p>Podpis w algorytmie DSA składa się z dwóch elementów - S i
						R. Dodatkowo do weryfikacji podpisu niezbędne są parametry
						początkowe - P, Q i G oraz klucz publiczny podpisującego dokument.
					</p>
					<p>Przy podpisywaniu dokumentu każdorazowo losowana jest pewna
						wartość k. Jej unikatowość i pełna przypadkowość jest krytyczna
						dla bezpieczeństwa całego algorytmu, dlatego istotna jest jakość
						generatora liczb losowych.</p>
					<jsp:include page="tabsComponent.jsp" />
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
