<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<!-- <link href="res/css/bootstrap.min.css" rel="stylesheet" type="text/css"> -->
	<link rel="stylesheet" href="res/css/springsource.css" type="text/css" />
	<title>Insert title here</title>
</head>
<body>
	<h1>LOGIN FORM</h1>
	<form id="loginform" name="loginform" method="post" action="">
	  <label for="username">Username</label>: <input name="username" id="username" type="text" /><br/><br/>
	  <label for="password">Password</label>: <input name="password" id="password" type="password" /><br/><hr/>
	  <input type="submit" name="Login" value="Login" />
	</form>
	<script src="res/js/jquery.js" type="text/javascript"></script>
	<script src="res/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>