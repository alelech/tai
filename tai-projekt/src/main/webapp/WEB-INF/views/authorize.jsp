<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="res/css/bootstrap.min.css" rel="stylesheet" type="text/css">
		<link href="res/css/sticky-footer.css" rel="stylesheet" type="text/css">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- <link rel="stylesheet" href="res/css/springsource.css" type="text/css" /> -->
		<title>Authorization request</title>
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
            <span class="navbar-brand" href="#">TAI & Dropbox</span>
          </div>
          <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Options<b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="logout">Logout</a></li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="jumbotron">
      <div class="container">
        <h1>Connect to Dropbox !</h1>
        <p>Our app needs your permission to use Dropbox account</p>
        <p><a href="fetchToken" class="btn btn-primary btn-lg" role="button">Start authorization&raquo;</a></p>
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