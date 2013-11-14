<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html> 

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%-- <link rel="stylesheet" href="<c:url value="res/css/springsource.css"/>"	type="text/css" /> --%>
	<link href="res/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="res/css/sticky-footer.css" rel="stylesheet" type="text/css">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Files list</title>
	<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
</head>

<body>
	
	<div id="wrap">
		<jsp:include page="template/navbar.jsp"></jsp:include>
		<div class="container">
			<div class="row">
			<div class="col-md-8 col-md-offset-2 text-center">
				<%-- <h1>Dropbox user: ${username}</h1> --%>
			</div>
			</div>
			<div class="row">
			<div class="col-md-8 col-md-offset-2">
			<ul>
				<c:forEach items="${files}" var="f">
	
					<c:choose>
						<c:when test="${f.isFolder}">
							<li><img src="${f.img}"/> <a href="folder?path=${f.path}"> ${f.name}</a></li>
						</c:when>
						<c:otherwise>
							<li><img src="${f.img}"/><%-- <a href="download?path=${f.path}"> --%>${f.name}<!-- </a> --></li>
						</c:otherwise>
					</c:choose>
	
				</c:forEach>
			</ul>
			</div>
			</div>
		</div>

	</div>
	
	<div id="footer">
		<div class="container">
			<p class="text-muted credit">2013</p>
		</div>
    </div>
	
	<script src="res/js/jquery.js" type="text/javascript"></script>
	<script src="res/js/bootstrap.min.js" type="text/javascript"></script>
</body>

</html>