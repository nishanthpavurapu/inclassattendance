<?php
 define('HOST','mysql.hostinger.in');
 define('USER','u125413163_root');
 define('PASS','nishanth');
 define('DB','u125413163_appdb');
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');