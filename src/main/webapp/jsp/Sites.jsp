<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sites</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"
	integrity="sha384-rwoIResjU2yc3z8GV/NPeZWAv56rSmLldC3R/AZzGRnGxQQKnKkoFVhFQhNUwEyJ"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"
	integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"
	integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb"
	crossorigin="anonymous"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.6/js/bootstrap.min.js"
	integrity="sha384-vBWWzlZJ8ea9aCX4pEW3rVHjgjt7zpkNpZk+02D9phzyeVkE+jo0ieGizqPLForn"
	crossorigin="anonymous"></script>
</head>
<body>
	<div class="container">

		<div class="jumbotron">
			<h1>${message}</h1>
			<p>Sharepoint data</p>
		</div>
		<c:set var="siteIndex" value="${loop.siteIndex}" />


		<%-- <table class="table">
			<thead>
				<tr>
					<th>#</th>
					<th>Site Name</th>
					<th>SIte URL</th>
					<th>Include site in search</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${siteList}" var="site">
					<c:set var="siteIndex" value="${siteIndex + 1}" />
					<tr>
						<td scope="row"><c:out value="${siteIndex}" /></td>
						<td>${site.siteName}</td>
						<td>${site.siteURL}</td>
						<td><input type="checkbox" /></td>
					</tr>
					<tr>
						<td colspan="4">
						<c:set var="folderIndex"
								value="${loop.folderIndex}" />
							<table class="table" style="margin-left: 15%">
								<thead>
									<tr>
										<th>#</th>
										<th>Server Relative URL</th>
										<th>Item Count</th>
										<th>Include Folder in search</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${siteFolderMap[site.siteName]}"
										var="siteFolder">

										<tr>
											<c:set var="folderIndex" value="${folderIndex + 1}" />
											<td scope="row"><c:out value="${folderIndex}" /></td>
											<td>${siteFolder.serverRelativeURL}</td>
											<td>${siteFolder.itemCount}</td>
											<td><input type="checkbox" /></td>
										</tr>
									</c:forEach>
								</tbody>
							</table></td>
					</tr>
				</c:forEach>
			</tbody>
		</table> --%>
		<c:forEach items="${siteList}" var="site">
			<c:set var="siteIndex" value="${siteIndex + 1}" />
			<div id="accordion" role="tablist" aria-multiselectable="true">
				<div class="card">
					<div class="card-header" role="tab" id="headingOne">
						<h5 >
							<a data-toggle="collapse" data-parent="#accordion"
								href="#${site.siteName}" aria-expanded="true"
								aria-controls="collapseOne">#${siteIndex} ${site.siteName} </a>
								<span style="font-size:14px" class="text-center">${site.siteURL} </span>
							<div style="float: right">

								<input type="checkbox" />
							</div>
						</h5>
					
					</div>

				<div id="${site.siteName}" class="collapse " role="tabpanel"
					aria-labelledby="headingOne">
					<div class="card-block">
					<c:set var="folderIndex"
								value="${loop.folderIndex}" />
					<table class="table" style="margin-left: 15%;width:85%">
							<thead>
								<tr>
									<th>#</th>
									<th>Server Relative URL</th>
									<th>Item Count</th>
									<th>Include Folder in search</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${siteFolderMap[site.siteName]}"
									var="siteFolder">

									<tr>
										<c:set var="folderIndex" value="${folderIndex + 1}" />
										<td scope="row"><c:out value="${folderIndex}" /></td>
										<td>${siteFolder.serverRelativeURL}</td>
										<td>${siteFolder.itemCount}</td>
										<td><input type="checkbox" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
				</div>
			</div>


			</div>
		</c:forEach>
	</div>
</body>
</html>