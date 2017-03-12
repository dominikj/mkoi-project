<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
prefix="form"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c"%>
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
<script type="text/javascript">
function showLoadedFile(file){
var pieces = file.value.split('\\'); 
var filename = pieces[pieces.length-1]; 
$("#file_info b").html(filename); 
$("#submit").attr("disabled",false); 
}
</script>
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
						<li class="current"><a href="/rsapss">RSA-PSS</a></li>
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
					<h1>Algorytm RSA</h1>
					<p>
						<b>RSA</b> – jeden z pierwszych i obecnie najpopularniejszych
						asymetrycznych algorytmów kryptograficznych z kluczem publicznym,
						zaprojektowany w 1977 przez Rona Rivesta, Adi Szamira oraz
						Leonarda Adlemana. Pierwszy algorytm, który może być stosowany
						zarówno do szyfrowania jak i do podpisów cyfrowych.
					</p>
					<p>Bezpieczeństwo szyfrowania opiera się na trudności
						faktoryzacji dużych liczb złożonych. Jego nazwa pochodzi od
						pierwszych liter nazwisk jego twórców</p>

					<p>W przypadku użycia RSA do przeprowadzenia operacji podpisu,
						szyfruje się zazwyczaj skrót wiadomości za pomocą klucza
						prywatnego i propaguje taki szyfrogram wraz z oryginalną
						wiadomością. Odbiorca posiadający klucz publiczny deszyfruje
						otrzymaną z wiadomością, zaszyfrowaną wartość funkcji skrótu,
						następnie oblicza wartość tejże funkcji z otrzymanej wiadomości.
						Jeśli obie wartości się zgadzają, to przyjmuje się, że wiadomość
						została podpisana poprawnie.</p>

					<div class="content_container">
						<p>
							<h3>Wybierz plik do podpisania:</h3>
							<form:form method="POST" enctype="multipart/form-data"
								action="/sign-file">
								<table>
									<tr>
										<td><input type="file" name="file" id="file"
											onchange="showLoadedFile(this);" style="display: none;" /></td>
									</tr>
									<tr>
										<td><input type="button" class="button_small"
											value="Wybierz plik" onclick="$('#file').click(); " /></td>
									</tr>
									<tr>
										<td><input class="button_small" id="submit" type="submit"
											value="Generuj podpis" disabled="true" /></td>
									</tr>
								</table>
							</form:form>
						<p id="file_info">Wybrano plik:<b> nie wybrano pliku</b></p>
						</p>
					</div>
					<!--close content_container-->
					<div class="content_container">
						<p><b>Wygenerowany podpis:</b></p>
					    <div style="border:1px solid black;">
					    ${fileSignature}
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
