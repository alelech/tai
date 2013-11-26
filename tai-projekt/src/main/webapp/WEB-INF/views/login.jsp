<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="res/css/bootstrap.min.css" rel="stylesheet" type="text/css">
		<link href="res/css/sticky-footer.css" rel="stylesheet" type="text/css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- <link rel="stylesheet" href="res/css/springsource.css" type="text/css" /> -->
		<title>Login</title>
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
				<div class="col-md-2">
					<img alt="Dropbox logo" src="res/img/dropbox-logo-black.png" class="img-rounded center-block">
				</div>
				<div class="col-md-4">
					<form id="loginform" name="loginform" method="post" action="" role="form" class="<c:if test="${not empty shiroLoginFailure}">has-error</c:if>">
						<h2>Please login</h2>
						<c:if test="${not empty shiroLoginFailure}">
							<div class="alert alert-danger">
  							<span>Authentication error</span>
							</div>
							<!-- <h3 class="help-block">Authentication error</h3> -->
						</c:if>
						<h3></h3>
						<div class="form-group">
							<label for="username" class="control-label">Username</label>
							<input name="username" id="username" placeholder="Username" type="text" class="form-control"/>
						</div>
						<div class="form-group">
							<label for="password" class="control-label">Password</label>
							<input name="password" id="password" placeholder="Password" type="password" class="form-control"/>
						</div>
						<button type="submit" name="Login" class="btn btn-success">Login</button>
					</form>
				</div>
			</div>
		</div>
		<div></div>
	</div>
	<div id="footer">
		<jsp:include page="template/footer.jsp"></jsp:include>
    </div>
		<script src="res/js/jquery.js" type="text/javascript"></script>
		<script src="res/js/bootstrap.min.js" type="text/javascript"></script>
	</body>
</html>