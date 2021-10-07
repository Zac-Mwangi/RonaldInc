<?php 
include 'config.php';
$sql="SELECT meta_value FROM `sys_meta` WHERE meta_key='payment_method' ORDER BY 'meta_value_int'";

$r = mysqli_query($con,$sql);

$result = array();
array_push($result,array('meta_value'=>"Select Payment Method")); //FIRST SELECTION TO NONE
while($row = mysqli_fetch_array($r)){
    array_push($result,array('meta_value'=>$row['meta_value']));
}
echo json_encode(array('result'=>$result));
mysqli_close($con);
?>