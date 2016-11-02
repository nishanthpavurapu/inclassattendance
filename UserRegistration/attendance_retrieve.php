<?php
 
 $studentid  = $_GET['studentid'];
 $classid = $_GET['classid'];
 $method_name = $_GET['method'];
 
 require_once('dbConnect.php');
 
 if ($method_name == "classlist")
 {
    $query_string = "SELECT DISTINCT(classid) FROM attendance WHERE studentid=$studentid"; 
 } 
 elseif ($method_name == "classlistprofessor")
 {
    $query_string = "SELECT DISTINCT(classid) FROM class_db WHERE userid=$studentid"; 
 } 
 elseif ($method_name == "attendancelistprofessor")
 {
     $date_selected= $_GET['attendeddate'];
    $query_string = "SELECT studentid FROM attendance WHERE classid=$classid and attendeddate='$date_selected'"; 
    $proxy_query_string = "SELECT studentid FROM attendance where attendeddate='$date_selected' and deviceid in (select deviceid from attendance where attendeddate='$date_selected' group by deviceid having count(deviceid)>1)";
 }
 else
 {
     $query_string = "SELECT attendeddate FROM attendance WHERE studentid=$studentid and classid=$classid";
 }
 
 $resData = mysqli_query($con,$query_string); 
 if (isset($resData))
 {
     $result_string="";
     while($record= mysqli_fetch_array($resData))
     {
        if($method_name == "classlist")
        {
            $result_string = $result_string.$record[0].";";
            
        }
        if($method_name == "classlistprofessor")
        {
            $result_string = $result_string.$record[0].";";
            
        }
        
        else
        {
            $result_string=$result_string.$record[0].";";
            
        }         
     }
 }
else
{
   echo 'No Records Found;';
}
if ($method_name == "attendancelistprofessor")
{
    $proxy_res_data = mysqli_query($con,$proxy_query_string);
    if(isset($proxy_res_data))
    {
        $proxy_result_string="";
     while($record= mysqli_fetch_array($proxy_res_data))
     {
        $proxy_result_string=$proxy_result_string.$record[0].";"; 
     }
    }
}
if ($method_name == "attendancelistprofessor")
{
echo $result_string.",".$proxy_result_string;
}
else
{
    echo $result_string;
}
 mysqli_close($con);