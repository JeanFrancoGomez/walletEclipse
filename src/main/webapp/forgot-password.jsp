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

	<div class="container">

		<!-- Outer Row -->
		<div class="row justify-content-center">

			<div class="col-xl-10 col-lg-12 col-md-9">

				<div class="card o-hidden border-0 shadow-lg my-5">
					<div class="card-body p-0">
						<!-- Nested Row within Card Body -->
						<div class="row">
							<div class="col-lg-6 d-none d-lg-block bg-password-image"></div>
							<div class="col-lg-6">
								<div class="p-5">
									<div class="text-center">
										<h1 class="h4 text-gray-900 mb-2">Forgot Your Password?</h1>
										<p class="mb-4">We get it, stuff happens. Just enter your
											email address below and we'll send you a link to reset your
											password!</p>
									</div>
									<form class="user" action="./forgotPassword" method="post">
										<div class="form-group">
											<input type="email" class="form-control form-control-user"
												name="email" id="email" aria-describedby="emailHelp"
												placeholder="Enter Email Address...">
										</div>
										<button class="btn btn-primary btn-user btn-block">Reset
											password</button>
									</form>
									<hr>
									<div class="text-center">
										<a class="small" href="register.jsp">Create an Account!</a>
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

		</div>

	</div>

</body>
</html>