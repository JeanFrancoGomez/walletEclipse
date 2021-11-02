<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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

	<div class="container">

		<div class="card o-hidden border-0 shadow-lg my-5">
			<div class="card-body p-0">
				<!-- Nested Row within Card Body -->
				<div class="row">
					<div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
					<div class="col-lg-7">
						<div class="p-5">
							<div class="text-center">
								<h1 class="h4 text-gray-900 mb-4">
									Thank you for join us
									<%= request.getAttribute("nameNewUser")%>!
								</h1>
								<h1 class="h4 text-gray-900 mb-4">Check your email for user
									confirmation</h1>
							</div>
							<div>
								<a href="./" class="btn btn-primary btn-user btn-block">Sign
									in</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>