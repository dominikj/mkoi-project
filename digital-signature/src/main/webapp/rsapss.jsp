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
<link rel="stylesheet" type="text/css" href="css/style.css" />
<!-- modernizr enables HTML5 elements and feature detects -->
<script type="text/javascript" src="js/modernizr-1.5.min.js"></script>
<script type="text/javascript">

function enableGenerateKey(){
if( ($("#file").val() != null && $("#file").val() != "")&& ($("#key").val() != null && $("#key").val() != "")){
$("#submit").attr("disabled",false);
}
else{
$("#submit").attr("disabled",true);
}
}

function showLoadedFile(file){
var pieces = file.value.split('\\'); 
var filename = pieces[pieces.length-1]; 
$("#file_info b").html(filename); 
enableGenerateKey();
}

function showLoadedKey(file){
var pieces = file.value.split('\\'); 
var filename = pieces[pieces.length-1]; 
$("#key_info b").html(filename); 
 enableGenerateKey();
}

function openCity(evt, cityName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
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
						<li><a href="/dsa">DSA</a></li>
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

					<div class="tab">
						<button class="tablinks" onclick="openCity(event, 'Sign')">Podpis</button>
						<button class="tablinks" onclick="openCity(event, 'Keys')">Klucze</button>
						<button class="tablinks" onclick="openCity(event, 'Verify')">Weryfikacja</button>
					</div>

					<div id="Sign" class="tabcontent">
						<h3>Generacja podpisu pliku</h3>
						<form:form method="POST" enctype="multipart/form-data"
							action="/rsapss/sign-file">
							<table>
								<tr>
									<td><input type="file" name="file" id="file"
										onchange="showLoadedFile(this);" style="display: none;" /></td>
									<td><input type="file" name="key" id="key"
										onchange="showLoadedKey(this);" style="display: none;" /></td>
								</tr>
								<tr>
									<td><input type="button" value="Wybierz plik"
										onclick="$('#file').click(); " /></td>
									<td><input type="button" class="button_small"
										value="Wybierz klucz" onclick="$('#key').click(); " /></td>
									<td><input class="button_small" id="submit" type="submit"
										value="Generuj podpis" disabled="true" /></td>
								</tr>
							</table>
						</form:form>
						<p id="file_info">
							Wybrano plik:<b> nie wybrano pliku</b>
						</p>
						<p id="key_info">
							Wybrano klucz:<b> nie wybrano klucza</b>
						</p>
					</div>

					<div id="Keys" class="tabcontent">

						<h3>Generacja pary kluczy</h3>
						Wygeneruj nową parę kluczy
						<form:form method="GET" action="/rsapss/generate-keys">
							<p>
								<input type="submit" value="Generuj" />
							</p>
						</form:form>
					</div>

					<div id="Verify" class="tabcontent">
						<h3>Weryfikacja podpisu</h3>
						<p>------</p>
					</div>
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
