<?php

$show_modal = false;
session_start();

function logout_user() {
    session_destroy();
     header("Location: login.php");
  }

  if (isset($_GET['logout'])) {
    logout_user();
  }
function query_class()
{
        echo "Professor email: ",$_SESSION['email'];
        echo "Professor id: ",$_SESSION['id'];
$professorid=$_SESSION['id'];


require_once('dbConnect.php');
$query_string = "select classid from class_db where userid='$professorid'";

    $myData = mysqli_query($con,$query_string);
    while($record = mysqli_fetch_array($myData))
    {
        echo "<option value=$record[classid]>$record[classid]</option>"; 
    }
    
    mysqli_close($con);
}

if(isset($_POST['buttongenerate']))
//function generate_qrcode()
{
    date_default_timezone_set('America/Chicago');
    $today_date = date("Y-m-d");
    $time_upto_valid = date('h:i:s', time()+60);
    $classid = $_POST['classlist'];
    
    $width = $height = 300;
    $url   = urlencode("http://inclassattendance.16mb.com/UserRegistration/attendance.php?dateselected=".$today_date."&validtimestamp=".$time_upto_valid."&classid=".$classid); 
    //$url   = urlencode("http://localhost:8080/project/attendance.php?dateselected=".$today_date."&validtimestamp=".$time_upto_valid."&classid=".$classid); 

      echo'<div class="modal fade" id="myModal" role="dialog">';
    echo'<div class="modal-dialog">';
      echo'<div class="modal-content">';
        echo'<div class="modal-header">';
          echo'<button type="button" class="close" data-dismiss="modal">&times;</button>';
          echo'<h4 class="modal-title">QR Code</h4>';
        echo'</div>';
        echo'<div class="modal-body" align="center">';
          echo "<img src=\"http://chart.googleapis.com/chart?chs={$width}x{$height}&cht=qr&chl=$url\" />";
        echo'</div>';
      echo'</div>';
    echo'</div>';
  echo'</div>';
 echo '<script type="text/javascript">';
 echo 'function myFunction() { $(\'#myModal\').modal(\'show\'); };';  
 echo 'myFunction()';
  echo '</script>';  
  $show_modal = true;
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
        <link rel="stylesheet" type="text/css" media="all" href="css/jsDatePick_ltr.min.css" />
        <script type="text/javascript" src="js/jsDatePick.min.1.3.js"></script>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    </head>
<body >
<div style="float:left;padding: 25px 0px 25px 25px;">
You have logged in with ID : <?php if (isset($_SESSION['id'])){echo $_SESSION['id'];}   else{header("Location: login.php");}?>
</div>
<div style="float:right;padding: 25px 25px 25px 0px;">
<a href='home.php?logout=true'>Logout User</a>
</div>
<br/><br/><br/>
<div align="center" >
<img style="height:100px;" src="logo.png">
</div><br/>

<form action="home.php" method="post">
    <div align="center">
    <span style="margin-left:-5px;">Class ID : </span>
    <select name="classlist" > <?php query_class(); ?> </select> 
    </div><br/>
    <div align="center">
    <button type=submit name=buttongenerate class="btn btn-primary" data-toggle="modal" data-target="#myModal">Generate QR Code</button>
    </div>
<?php if($show_modal):?>
  <script> $('#myModal').modal('show');</script>
<?php endif;?>
</form>
</body>
</html>