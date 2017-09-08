<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div>
<span ng-if="username && username !== null" ng-include="'./jsp/home.jsp'"></span>
<span ng-if="!username || username == null" ng-include="'./jsp/initial.jsp'"></span>
</div>
</body>
</html>