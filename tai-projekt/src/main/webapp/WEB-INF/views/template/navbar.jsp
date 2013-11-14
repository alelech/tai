<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
          <div class="navbar-header">
            <span class="navbar-brand">TAI and Dropbox</span>
          </div>
          <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Options<b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="logout">Logout</a></li>
                  <shiro:hasRole name="administrator">
                  	<li><a>Add user</a></li>
                  </shiro:hasRole>
                </ul>
              </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
				<li><a>Logged as: ${username} (<shiro:principal/>)</a></li>            
            </ul>
          </div>
        </div>
      </div>