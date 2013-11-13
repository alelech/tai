<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="res/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<title>Auhtorize with dropbox</title>
</head>
<body>
<div>
	<jsp:include page="template/header.jsp" />
	</div>
	<div>
<a href="fetchToken" class="btn btn-primary btn-lg" role="button">
  		Authorize with dropbox
</a>
</div>
<script src="res/js/jquery.js" type="text/javascript"></script>
<script src="res/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>