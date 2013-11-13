<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="<c:url value="res/css/springsource.css"/>"	type="text/css" />
<title>Files list</title>
</head>

<body>
	<div>
	<jsp:include page="template/header.jsp" />
	</div>
	<div id="main_wrapper">

		<center>
		<h1>Dropbox user: ${username}</h1>
		</center>

		<ul>
			<c:forEach items="${files}" var="f">

				<c:choose>
					<c:when test="${f.isFolder}">
						<li><img src=${f.img}> <a href="folder?path=${f.path}"> ${f.name}</a></li>
					</c:when>
					<c:otherwise>
						<li><img src=${f.img}> ${f.name}</li>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</ul>

	</div>
</body>

</html>