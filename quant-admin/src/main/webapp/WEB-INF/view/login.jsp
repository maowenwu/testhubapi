<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

 <c:if test="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal != null}">
    <c:redirect url="welcome.do"/>
</c:if>   
    
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>登录</title>

    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="${ctx}/css/animate.css" rel="stylesheet">
    <link href="${ctx}/css/style.css" rel="stylesheet">
</head>

<body class="loginbg">
    <div class="row login-bg">
    <h2 class="font-bold" style="color:#fff; width:1200px; margin:0 auto; padding:65px 0 0 100px; font-weight:normal">欢迎登录征流动性管理系统</h2>
    <div class="loginColumns animated fadeInDown">
        <div class="row">

            <div class="col-md-8 ">


                <!-- <p>
                    Perfectly designed and precisely prepared admin theme with over 50 pages with extra new web app views.
                </p>

                <p>
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.
                </p>

                <p>
                    When an unknown printer took a galley of type and scrambled it to make a type specimen book.
                </p>

                <p>
                    <small>It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.</small>
                </p> -->

            </div>
            <div class="col-md-4">
                <div class="ibox-content" style="border-radius:5px; width:320px;">
                    <c:url value="/perform_login.do" var="loginUrl" />
                    <form action="${loginUrl}" method="post">
                        
                        <c:if test="${param.logout != null || param.logout == null}">
                        <p>欢迎登录</p>
                        </c:if>
                        <c:if test="${param.error != null}">
	                        <c:choose>
		                        <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'blocked'}">
					            	<p>登录错误太多,30分钟后重试.</p>
					       		 </c:when>
					       		 <c:otherwise>
		                            <p>用户名或密码错误.</p>
							    </c:otherwise>
					       	</c:choose>
				       	</c:if>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入用户名" required=""  name="username">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" placeholder="请输入密码" required="" name="password">
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-new block full-width m-b">登录</button>

                    </form>
                    <!-- <p class="m-t">
                        <small>Inspinia we app framework base on Bootstrap 3 &copy; 2016</small>
                    </p> -->
                </div>
            </div>
        </div>


    </div>
    </div>
    <div style="futurePosition:fixed; bottom:10px; left:50%; width:1200px; margin-left:-600px; ">
    <div class="col-md-6" style="color:#fff">
    
    </div>
    <div class="col-md-6 text-right" style="color:#fff">
    <small> © 2018-2020</small>
    </div>
    </div>
</body>

</html>

