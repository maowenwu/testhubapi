<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul side-navigation class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="profile-element" uib-dropdown>
                    <img alt="image" class="img-circle" src="img/profile_small.jpg"/>
                    <a uib-dropdown-toggle href>
                            <span class="clear">
                                <span class="block m-t-xs">
                                    <strong class="font-bold" ng-bind="principal.realName"></strong>
                             </span>
                                <!-- <span class="text-muted text-xs block">Art Director</span> -->
                            </span>
                    </a>
                    <%--<ul uib-dropdown-menu class="animated fadeInRight m-t-xs">--%>
                        <%--<li><a ui-sref="app.profile">Profile</a></li>--%>
                        <%--<li><a ui-sref="app.contacts">Contacts</a></li>--%>
                        <%--<li><a ui-sref="mailbox.inbox">Mailbox</a></li>--%>
                        <%--<li class="divider"></li>--%>
                        <%--<li><a ng-click="logout()">Logout</a></li>--%>
                    <%--</ul>--%>
                </div>
                <div class="logo-element">
                    YFB
                </div>
            </li>   
         
            <c:forEach items="${menus}" var="menu">
            <li ng-class="{active: $state.includes('${menu.menuCode }')}">
            	<c:choose>
            	<c:when test="${not empty menu.children && fn:length(menu.children)> 0 }">
            	<a href=""><i class="fa fa-th-large ${menu.icons }"></i> <span class="nav-label">${menu.menuName } </span><span class="fa arrow"></span></a>
            	<ul class="nav nav-second-level collapse" ng-class="{in: $state.includes('${menu.menuCode }')}">
            		<c:forEach items="${menu.children }" var="menu1">
            		<li ui-sref-active="active"><a ui-sref="${menu1.menuCode }">${menu1.menuName }</a></li>
            		</c:forEach>
            	</ul>
            	</c:when>
            	<c:otherwise>
            	<a ui-sref="${menu.menuCode }"><i class="fa fa-th-large ${menu.icons }"></i> <span class="nav-label">${menu.menuName }</span> </a>
            	</c:otherwise>
            	</c:choose>
            </li>
            </c:forEach>
            
        </ul>
    </div>
</nav>