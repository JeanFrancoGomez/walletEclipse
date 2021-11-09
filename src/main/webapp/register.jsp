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
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" />
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
								<h1 class="h4 text-gray-900 mb-4">Join to us today!</h1>
							</div>
							<form action="./register" method="post">
								<div class="form-group row">
									<div class="col-sm-6 mb-3 mb-sm-0">
										<input required type="text"
											class="form-control form-control-user" id="firstName"
											name="firstName" placeholder="First Name">
									</div>
									<div class="col-sm-6">
										<input required type="text"
											class="form-control form-control-user" id="lastName"
											name="lastName" placeholder="Last Name">
									</div>
								</div>
								<div class="form-group">
									<input required type="email"
										class="form-control form-control-user" id="inputEmail"
										name="inputEmail" placeholder="Email Address">
								</div>
								<div class="form-group row">
									<div class="col-sm-6 mb-3 mb-sm-0">
										<input required type="password"
											class="form-control form-control-user" id="inputPassword"
											name="inputPassword" placeholder="Password">
									</div>
									<div class="col-sm-6">
										<input required type="password"
											class="form-control form-control-user" id="repeatPassword"
											name="repeatPassword" placeholder="Repeat Password">
									</div>
								</div>
								<input type="submit" class="btn btn-primary btn-user btn-block"
									value="Create Account">
							</form>
							<hr>
							<div class="text-center">
								<a class="small" href="forgot-password.jsp">Forgot Password?</a>
							</div>
							<div class="text-center">
								<a class="small" href="index.jsp">Already have an account?
									Login!</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>
</html>