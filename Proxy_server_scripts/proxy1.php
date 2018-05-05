<?php
require 'firebaseLib.php';
$deviceId = $_GET['deviceid'];
$status = -1;

$url = 'https://alertapp-9682a.firebaseio.com';
$token = 'iEk96fPuATPdoPZaPfPgKtvFfRxxaBrqIOynZMBH';
$path = '/accidents/'.$deviceId;

date_default_timezone_set('Asia/Kolkata');

$_devicestatus= array(
'status' => $status,
'vehicleID' => $deviceId
);

 

$firebase = new Firebase($url,$token);
$firebase->update($path, $_devicestatus); // updates data in Firebase

print('POST SUCCESSFUL');

