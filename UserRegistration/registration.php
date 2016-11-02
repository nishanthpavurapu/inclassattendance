<?php

	$name = $_GET['name'];
	$studentid = $_GET['studentid'];
        $mobile = $_GET['mobile'];
        $department = $_GET['department'];
        $deviceid=$_GET['deviceid'];
        $usertype=$_GET['usertype'];
		$password = $_GET['password'];
		$email = $_GET['email'];
		
		if($department == '' || $mobile == '' || $studentid == '' || $name == '' || $password == '' || $email == ''){
			echo 'please fill all values';
		}else{
			require_once('dbConnect.php');
			$sql = "SELECT * FROM user_registration WHERE studentid='$studentid' OR email='$email'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo 'Student ID or email already exist';
			}else{				
				$sql = "INSERT INTO user_registration (name,studentid,email,mobile,department,password,deviceid,usertype) VALUES('$name','$studentid','$email','$mobile','$department','$password','$deviceid','$usertype')";
				if(mysqli_query($con,$sql)){
					echo 'successfully registered';
				}else{
					echo 'oops! Please try again!';
				}
			}
			mysqli_close($con);
		}