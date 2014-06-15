<%@ page import="tai.dropbox.controller.FileController" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <span class="navbar-brand">TAI and Dropbox</span>
        </div>
        <shiro:authenticated>
            <div class="collapse navbar-collapse" id="navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Options<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="logout">Logout</a></li>
                            <%=FileController.prepareUploadButton() %>
                            <%=FileController.prepareAdminButton() %>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a>Logged as dropbox user : <c:choose>
                        <c:when test="${ not empty username }">
                            ${ username }
                        </c:when>
                        <c:otherwise>not connected</c:otherwise>
                    </c:choose>
                        (login : <shiro:principal/> , roles : <shiro:hasRole name="administrator">admin</shiro:hasRole>
                        <shiro:hasRole name="user">user</shiro:hasRole>) </a></li>
                </ul>
            </div>
        </shiro:authenticated>
    </div>
</div>