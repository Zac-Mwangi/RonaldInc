<?php 
include 'config.php';
$estate_id = 1;
$sql="SELECT `room_number`,`monthly_price`,`room_type` FROM `room` WHERE (`estate_id` = '$estate_id' AND `status` = 0)";

$r = mysqli_query($con,$sql);

$result = array();
array_push($result,array('room_number'=>"Select Room")); //FIRST SELECTION TO NONE
while($row = mysqli_fetch_array($r)){
    $type = $row['room_type'];
    $row109=mysqli_fetch_array(mysqli_query($con, "SELECT `meta_value` FROM `sys_meta` WHERE (`meta_key` = 'room_type' AND `meta_value_int` = '$type') "));
    $rmType = $row109[0];
    array_push($result,array('room_number'=>"Rm - ".$row['room_number']." , (".$rmType." @ Ksh".$row['monthly_price'].")"));
}
echo json_encode(array('result'=>$result));
mysqli_close($con);
?>