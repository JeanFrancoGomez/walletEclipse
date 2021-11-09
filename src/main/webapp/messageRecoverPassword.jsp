<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	session="false"
%>
<!DOCTYPE html>
<html>
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css"
	rel="stylesheet">
<title>Demo Servlet</title>
</head>
<body>
	<div class="card o-hidden border-0 shadow-lg my-5">
		<div class="card-body p-0">
			<!-- Nested Row within Card Body -->
			<div class="row">
				<div class="col-lg-6 d-none d-lg-block bg-login-image img-fluid"></div>
				<div class="col-lg-6">
					<div class="p-5">
						<div class="text-center">
							<h1 class="h4 text-gray-900 mb-4">
								We have send a message to <%=request.getAttribute("emailRep")%> to change the password.
							</h1>
						</div>
						<hr>
						<div class="text-center">
							<a class="small" href="index.jsp">Login</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>