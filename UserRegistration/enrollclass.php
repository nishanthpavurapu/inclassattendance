<?php

	$classid = $_GET['classid'];
	$semester = $_GET['semester'];
        $year_selected = $_GET['year'];
        $userid = $_GET['userid'];
		
		if($classid == '' || $semester == '' || $year_selected == '' || $userid == ''){
			echo 'please fill all values';
		}else{
			require_once('dbConnect.php');
			$sql = "SELECT * FROM class_db WHERE classid='$classid' and semester='$semester' and year='$year_selected'";
			
			$check = mysqli_fetch_array(mysqli_query($con,$sql));
			
			if(isset($check)){
				echo 'Class ID is already in use';
			}
            else{				
                $sql = "INSERT INTO class_db (classid,userid,semester,year) VALUES('$classid','$userid','$semester','$year_selected')";
				if(mysqli_query($con,$sql)){
					echo 'Class Enrolled Successfully';
				}else{
					echo 'Oops! Please try again!';
				}
			}
			mysqli_close($con);
		}