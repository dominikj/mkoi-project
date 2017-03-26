<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">

function enableGenerateKey(){
if( ($("#file").val() != null && $("#file").val() != "")&& ($("#key").val() != null && $("#key").val() != "")){
$("#submit").attr("disabled",false);
}
else{
$("#submit").attr("disabled",true);
}
}

function enableVerify(){
if( ($("#file_s").val() != null && $("#file_s").val() != "")&& ($("#key_s").val() != null && $("#key_s").val() != "") && ($("#sign_s").val() != null && $("#sign_s").val() != "")){
$("#submit_s").attr("disabled",false);
}
else{
$("#submit_s").attr("disabled",true);
}
}

function showLoaded(file, id){
var pieces = file.value.split('\\'); 
var filename = pieces[pieces.length-1]; 
$(id +" b").html(filename); 
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
<div class="tab">
	<button class="tablinks" onclick="openCity(event, 'Sign')">Podpis</button>
	<button class="tablinks" onclick="openCity(event, 'Keys')">Klucze</button>
	<button class="tablinks" onclick="openCity(event, 'Verify')">Weryfikacja</button>
</div>

<div id="Sign" class="tabcontent">
	<h3>Generacja podpisu pliku</h3>
	Utwórz podpis pliku
	<form:form method="POST" enctype="multipart/form-data"
		action="${baseUrl}/sign-file">
		<table>
			<tr>
				<td><input type="file" name="file" id="file"
					onchange="showLoaded(this, '#file_info'); enableGenerateKey()"
					style="display: none;" /></td>
				<td><input type="file" name="key" id="key"
					onchange="showLoaded(this, '#key_info'); enableGenerateKey()"
					style="display: none;" /></td>
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
	<form:form method="GET" action="${baseUrl}/generate-keys">
		<p>
			<input type="submit" value="Generuj" />
		</p>
	</form:form>
</div>
<c:choose>
	<c:when test="${signVerified ne null}">
		<div id="Verify" class="tabcontent" style="display: block;">
	</c:when>
	<c:otherwise>
		<div id="Verify" class="tabcontent">
	</c:otherwise>
</c:choose>
<h3>Weryfikacja podpisu</h3>
Zweryfikuj poprawność podpisu
<form:form method="POST" enctype="multipart/form-data"
	action="${baseUrl}/verify-sign">
	<table>
		<tr>
			<td><input type="file" name="file" id="file_s"
				onchange="showLoaded(this, '#file_info_s'); enableVerify()"
				style="display: none;" /></td>
			<td><input type="file" name="key" id="key_s"
				onchange="showLoaded(this, '#key_info_s'); enableVerify()"
				style="display: none;" /></td>
			<td><input type="file" name="sign" id="sign_s"
				onchange="showLoaded(this, '#sign_info_s'); enableVerify()"
				style="display: none;" /></td>
		</tr>
		<tr>
			<td><input type="button" value="Wybierz plik"
				onclick="$('#file_s').click(); " /></td>
			<td><input type="button" class="button_small"
				value="Wybierz klucz" onclick="$('#key_s').click(); " /></td>
			<td><input type="button" class="button_small"
				value="Wybierz podpis" onclick="$('#sign_s').click(); " /></td>
			<td><input class="button_small" id="submit_s" type="submit"
				value="Sprawdź podpis" disabled="true" /></td>

		</tr>
	</table>
</form:form>
<p id="file_info_s">
	Wybrano plik:<b> nie wybrano pliku</b>
</p>
<p id="key_info_s">
	Wybrano klucz:<b> nie wybrano klucza</b>
</p>
<p id="sign_info_s">
	Wybrano podpis:<b> nie wybrano podpisu</b>
</p>
<c:if test="${signVerified ne null}">
	<c:choose>
		<c:when test="${signVerified}">
			<p>
			<h3 style="color: red;">Podpis prawidłowy</h3>
			</p>
		</c:when>
		<c:otherwise>
			<p>
			<h3 style="color: red;">Podpis nieprawidłowy</h3>
			</p>
		</c:otherwise>
	</c:choose>
</c:if>
</div>

