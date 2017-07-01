<!doctype html>
<html>
<head>
    <title>Login</title>
    <asset:stylesheet src="application.css" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <asset:link rel="apple-touch-icon-precomposed" sizes="144x144" href="favicon_apple_touch_144x144.png" />
    <asset:link rel="apple-touch-icon-precomposed" sizes="152x152" href="favicon_apple_touch_152x152.png" />
    <style type="text/css">
    .form-signin {
        width: 400px;
        margin: auto;
    }
    </style>
</head>
<body>
<div class="row">
    <div class="col-lg-7 col-centered">
        <form action="/login" method="POST" id="loginForm" class="form-signin" autocomplete="off">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <g:if test="${params.error != null}">
                <g:if test="${session.SPRING_SECURITY_LAST_EXCEPTION?.message?.contains('disabled')}">
                    <div class="alert alert-warning" role="alert">Inloggen is tijdelijk uitgeschakeld</div>
                </g:if>
                <g:else>
                    <div class="alert alert-warning" role="alert"><g:message code="test.login.error" default="Error"/></div>
                </g:else>
            </g:if>
            <div class="form-group">
                <label for="username" class="sr-only"><g:message code="test.login.username.label" default="Username"/></label>
                <input type="text" name="username" class="form-control" placeholder="${message(code: "test.login.username.placeholder", default: "Username")}" required autofocus>
            </div>
            <div class="form-group">
                <label for="password" class="sr-only"><g:message code="test.login.password.label" default="Password"/></label>
                <input type="password" name="password" class="form-control" placeholder="${message(code: "test.login.password.placeholder", default: "Password")}" required>
            </div>
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="remember-me" checked> <g:message code="test.login.rememberMe.label" default="Remember me"/>
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit"><g:message code="test.login.button" default="Login"/></button>
        </form>
    </div>
</div>
</body>
</html>
