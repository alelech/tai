<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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

		<div id="upload" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title">Upload a file</h4>
					</div>
					<div class="modal-body">
						<form role="form" enctype="multipart/form-data" method="post" name="fileUpload" action="upload">
							<div class="form-group">
								<label for="inputDir">Directory</label> <input
									type="text" class="form-control" id="inputDir"
									placeholder="Directory" value="${ path }" name="dir">
							</div>
							<div class="form-group">
								<label for="inputFile">File</label> <input
									type="file" id="inputFile" name="file">
							</div>
							<button type="submit" class="btn btn-default" >Upload</button>
						</form>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->


		<div class="container">

			
			<div class="row" id="dl-progress" style="display: none;">
				<div class="col-md-8 col-md-offset-2">
					<div class="alert alert-info">
						<div class="progress progress-striped active">
							<div class="progress-bar" role="progressbar" aria-valuenow="100"
								aria-valuemin="0" aria-valuemax="100" style="width: 100%">
							</div>
						</div>
						<span>Processing file : </span><span id="dl-filename"></span>
					</div>
				</div>
			</div>
			<div class="row" id="dl-err" style="display: none;">
				<div class="col-md-8 col-md-offset-2">
					<div class="alert alert-danger">
						<span>Error while downloading file !</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-8 col-md-offset-2 text-center">
					<%-- <h1>Dropbox user: ${username}</h1> --%>
					<h1>${ path }</h1>
				</div>
			</div>
			<div class="row">
				<div class="col-md-8 col-md-offset-2">
					<ul>
						<c:forEach items="${files}" var="f">

							<c:choose>
								<c:when test="${f.isFolder}">
									<li><img src="${f.img}" /> <a
										href="folder?path=${f.path}">${f.name}</a></li>
								</c:when>
								<c:otherwise>
									<li><img src="${f.img}" /> <a class="flist-link"
										href="download?path=${f.path}"> <span class="flist-name"
											data-file-name="${f.name}">${f.name}</span>
									</a></li>
								</c:otherwise>
							</c:choose>

						</c:forEach>
					</ul>
				</div>
			</div>
		</div>

	</div>

	<div id="footer">
		<jsp:include page="template/footer.jsp"></jsp:include>
	</div>



	<!-- <iframe style="display: none;" id="dl-target"></iframe> -->

	<script src="res/js/jquery.js" type="text/javascript"></script>
	<script src="res/js/bootstrap.min.js" type="text/javascript"></script>
	<!-- 	<script type="text/javascript">
		$( document ).ready(function(){
			$('a.flist-link').click(function(evt){
				evt.preventDefault();
				evt.stopPropagation();
				var el = $(this).children('span.flist-name');
				var fileName = el.text();
				var fileUrl = $(this).attr('href');
				$('#dl-filename').text(fileName);
				var progress = $('#dl-progress');
				var err = $('#dl-err');
				var target = $('#dl-target');
				progress.show('slow');
				target.ready(function(){
					progress.hide('slow');
				});
				target.attr('src',fileUrl);
			});
		});
	</script> -->
</body>

</html>