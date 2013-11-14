<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		<div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
          <div class="navbar-header">
           <!--  <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
              <span class="sr-only">Toggle navigation</span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button> -->
            <span class="navbar-brand" href="#">TAI & Dropbox</span>
          </div>
         <!--  <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="#">Action</a></li>
                  <li><a href="#">Another action</a></li>
                  <li><a href="#">Something else here</a></li>
                  <li class="divider"></li>
                  <li class="dropdown-header">Nav header</li>
                  <li><a href="#">Separated link</a></li>
                  <li><a href="#">One more separated link</a></li>
                </ul>
              </li>
            </ul>
          </div>/.nav-collapse -->
        </div>
      </div>
		<div class="container">
			<div class="row">
				<div class="col-md-2">
					<img alt="Dropbox logo" src="res/img/dropbox-logo-black.png" class="img-rounded center-block">
				</div>
				<div class="col-md-4">
					<form id="loginform" name="loginform" method="post" action="" role="form">
						<h2>Please login</h2>
						<div class="form-group">
							<label for="username">Username</label>
							<input name="username" id="username" type="text" class="form-control"/>
						</div>
						<div class="form-group">
							<label for="password">Password</label>
							<input name="password" id="password" type="password" class="form-control"/>
						</div>
						<button type="submit" name="Login" class="btn btn-success">Login</button>
					</form>
				</div>
			</div>
		</div>
		<div></div>
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