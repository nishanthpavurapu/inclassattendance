<?php 
session_start();
    
    if($_SERVER['REQUEST_METHOD']=="POST")
    {
		//Getting values 
		$username = $_POST['Username'];
		$password = $_POST['Password'];
		//Creating sql query for login using email
		$sql_email = "SELECT * FROM user_registration WHERE email='$username' AND password='$password'";
        //Creating sql query for login using studentID
		$sql_studentid = "SELECT * FROM user_registration WHERE studentid='$username' AND password='$password'";
		
		//importing dbConnect.php script 
		require_once('dbConnect.php');
		
		//executing query to check if the provided user details are valid assuming username is email
		$result_email = mysqli_query($con,$sql_email);
		$check_email_result = mysqli_fetch_array($result_email);
		
		if(isset($check_email_result)){
			//displaying success 
			$_SESSION['email'] = $check_email_result['email'];
            $_SESSION['id'] = $check_email_result['studentid'];
               if ($check_email_result['usertype'] == 'professor')
               {
                    header("Location: home.php");
               }
               else
               {
                    echo "<div class=\"alert alert-danger alert-dismissible\" \"role=alert\">";
                    echo "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>";
                    echo "<strong>You are not authorized for this login</strong>";
                    echo "</div>";
               }
		}else{
            //executing query to check if the provided user details are valid assuming username is studentID
            $result_studentid = mysqli_query($con,$sql_studentid);
            $check_studentid_result=mysqli_fetch_array($result_studentid);
            
            //check if the login is success
            if(isset($check_studentid_result))
            {
               $_SESSION['email'] = $check_studentid_result['email'];
               $_SESSION['id'] = $check_studentid_result['studentid'];
               if ($check_studentid_result['usertype'] == 'professor')
               {
                    header("Location: home.php");
               }
               else
               {
                    echo "<div class=\"alert alert-danger alert-dismissible\" \"role=alert\">";
                    echo "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>";
                    echo "<strong>You are not authorized for this login</strong>";
                    echo "</div>";
               }
            }
            else{
           echo "<div class=\"alert alert-danger alert-dismissible\" \"role=alert\">";
           echo "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>";
            echo "<strong>Login Failed!</strong>";
            echo "</div>";
            }
			
		}
		mysqli_close($con);
	}
?>

<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<title>InClassAttendance Login</title>
		<meta name="generator" content="Bootply" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!--[if lt IE 9]>
			<script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
		<link href="css/styles.css" rel="stylesheet">
	</head>
	<body>
<!--login modal-->
<div id="loginModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
  <div class="modal-content">
      <div class="modal-header">
          <h1 class="text-center">Login</h1>
      </div>
      <div class="modal-body">
          <form action="login.php" method="post" class="form col-md-12 center-block">
            <div class="form-group">
              <input type="text" class="form-control input-lg" placeholder="Email or Professor ID" name="Username" id="Username">
            </div>
            <div class="form-group">
              <input type="password" class="form-control input-lg" placeholder="Password" name="Password" id="Password">
            </div>
            <div class="form-group">
              <button type="submit" class="btn btn-primary btn-lg btn-block">Sign In</button>
            </div>
          </form>
      </div>
      <div class="modal-footer">
          <div class="col-md-12">
          
		  </div>	
      </div>
  </div>
  </div>
</div>
	<!-- script references -->
		<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</body>
</html>