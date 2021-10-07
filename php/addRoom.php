<?php
$selectedRoomType = $_POST['selectedRoomType'];
$room_number = $_POST['et_room_number'];
$monthly_price = $_POST['monthly_price'];
$deposit_amount = $_POST['deposit_amount'];
$added_by = $_POST['added_by'];
$estate_id = $_POST['estate_id'];



$output=array();

include ('config.php');

if($room_number!=""){
	$conn=$dbh->prepare('SELECT * FROM `room` WHERE (`room_number` = ? AND `estate_id` = ?)');
	$conn->bindParam(1,$room_number);
	$conn->bindParam(2,$estate_id);
	$conn->execute();
	if($conn->rowCount() !==0){
		$output['error'] = true;
		$output['message'] = "Room ( ".$room_number." ) Already Exists in the Estate";
	}else{
		$conn=$dbh->prepare("SELECT * FROM sys_meta WHERE meta_value=?");
		$conn->bindParam(1,$selectedRoomType);
		$conn->execute();
		if($conn->rowCount() !==0){
			$results=$conn->fetch(PDO::FETCH_OBJ);
			$room_type=$results->meta_value_int;
		}else{
			$room_type="Err";
		}

		$conn=$dbh->prepare('INSERT INTO `room`(`room_number`, `estate_id`, `monthly_price`, `deposit_price`, `room_type`, `added_by`) VALUES (?,?,?,?,?,?)');
		$conn->bindParam(1,$room_number);
		$conn->bindParam(2,$estate_id);
		$conn->bindParam(3,$monthly_price);
		$conn->bindParam(4,$deposit_amount);
		$conn->bindParam(5,$room_type);
		$conn->bindParam(6,$added_by);
		$conn->execute();
		if($conn->rowCount() == 0){
			$output['error'] = true;
			$output['message'] = "failed, Please try again";
		}elseif($conn->rowCount() !==0){
			$output['error'] = false;
			$output['message'] = "Room ( ".$room_number." ) Succefully Added";
		}
	}
	echo json_encode($output);
}
else{
	$output['error'] = true;
	$output['message'] = "Insert the parameters";
}
?>