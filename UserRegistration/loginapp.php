<?php 
	
	if($_SERVER['REQUEST_METHOD']=='POST'){
		//Getting values 
		$username = $_POST['username'];
		$password = $_POST['password'];
		
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
			echo "success,".$check_email_result['email'].",".$check_email_result['studentid'].",".$check_email_result['usertype'].",".$check_email_result['name'];
		}else{
            //executing query to check if the provided user details are valid assuming username is studentID
            $result_studentid = mysqli_query($con,$sql_studentid);
            $check_studentid_result=mysqli_fetch_array($result_studentid);
            
            //check if the login is success
            if(isset($check_studentid_result))
            {
                echo "success,".$check_studentid_result['email'].",".$check_studentid_result['studentid'].",".$check_studentid_result['usertype'].",".$check_studentid_result['name'];
            }
            else{
                echo "Login Failed!xxxx";
            }
			
		}
		mysqli_close($con);
}