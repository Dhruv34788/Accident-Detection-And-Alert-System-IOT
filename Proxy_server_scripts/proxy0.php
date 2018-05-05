<?php
require 'firebaseLib.php';
$deviceId = $_GET['deviceid'];
$latitude = $_GET['lat'];
$longitude= $_GET['lon'];

$url = 'https://alertapp-9682a.firebaseio.com';
$token = 'iEk96fPuATPdoPZaPfPgKtvFfRxxaBrqIOynZMBH';
$path = '/vehicles/'.$deviceId;

date_default_timezone_set('Asia/Kolkata');

$_devicestatus= array(
'latitude' => $latitude + 0.0,
'longitude' => $longitude + 0.0	
);

 

$firebase = new Firebase($url,$token);
$firebase->update($path, $_devicestatus); // updates data in Firebase

print('POST SUCCESSFUL');

