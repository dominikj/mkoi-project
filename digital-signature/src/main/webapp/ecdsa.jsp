<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<title>Generator podpisów cyfrowych</title>
<meta name="description" content="website description" />
<meta name="keywords" content="website keywords, website keywords" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href=<spring:url value="/css/style.css" /> />
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
						<li><a href="/dsa">DSA</a></li>
						<li class="current"><a href="/ecdsa">ECDSA</a></li>
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
					<h1>Algorytm ECDSA</h1>
					<p>
						<b>Elliptic Curve Cryptography (ECC)</b> – grupa technik
						kryptografii asymetrycznej, wykorzystująca jako podstawową
						technikę matematyczną krzywe eliptyczne. Użycie krzywych
						eliptycznych w celach kryptograficznych zostało zasugerowane
						niezależnie przez dwójkę badaczy, Neala Koblitza oraz Victora S.
						Millera w roku 1985.
					</p>
					<p>Bezpieczeństwo ECC jest oparte na złożoności obliczeniowej
						dyskretnych logarytmów na krzywych eliptycznych – Elliptic Curve
						Discrete Logarithm Problem (ECDLP). Algorytm podpisu cyfrowego
						przy użyciu ECC to <i>ECDSA</i>. ECC oferuje bezpieczeństwo porównywalne
						do RSA przy znacznie krótszych kluczach</p>
					<p>
						ECC wykorzystuje działania matematyczne krzywych eliptycznych w
						ciałach, na liczbach całkowitych i w oparciu o duże liczby
						pierwsze. W rezultacie ich reprezentacja graficzna nie ma wiele
						wspólnego ani z krzywymi, ani z elipsami — są to przypominające
						chmurę zbiory punktów.
						</p>

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
	<script type="text/javascript" src="/js/jquery.min.js"></script>
	<script type="text/javascript" src="/js/image_slide.js"></script>

</body>
</html>
