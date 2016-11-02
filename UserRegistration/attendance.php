<?php
    
    date_default_timezone_set('America/Chicago');

	$dateselected = $_GET['dateselected'];
	$studentid = $_GET['studentid'];
    $validtimestamp = $_GET['validtimestamp'];
    $classid = $_GET['classid'];
    $deviceid=$_GET['deviceid'];
    
    require_once('dbConnect.php');
    $sql_command = "SELECT * FROM attendance where studentid='$studentid' and attendeddate='$dateselected'";
    
    $sql_duplicate_check = mysqli_fetch_array(mysqli_query($con,$sql_command));
    
    if(isset($sql_duplicate_check))
    {
        echo "This is a duplicate entry!";
    }
    else
    {
        $sql_command = "INSERT INTO attendance (classid,studentid,attendeddate,deviceid) values ($classid,$studentid,'$dateselected','$deviceid')";
        if(mysqli_query($con,$sql_command))
        {
            echo "success";
        }
        else
        {
            echo "failed";
        }
    }		
?>