<?php
$tenant_name =ucwords($_POST['et_tenant_name']);
$id_number = $_POST['id_number'];
$phone_number = $_POST['phone_number'];
$deposit_amount = $_POST['deposit_amount'];
$added_by = $_POST['added_by'];
$estate_id = $_POST['estate_id'];
$room_number = $_POST['room_number'];


// $tenant_name =ucwords("alaa");
// $id_number = 123;
// $phone_number = "0790780464";
// $deposit_amount = 1;
// $added_by = 1;
// $estate_id = 1;
// $room_number = 1;

$output=array();

include ('config.php');

if($tenant_name!=""){
	$status = 0;
	$conn=$dbh->prepare('SELECT * FROM `tenants` WHERE (`tenant_name` = ? AND  `id_number` = ? AND `status` = ?)');
	$conn->bindParam(1,$tenant_name);
	$conn->bindParam(2,$id_number);
	$conn->bindParam(3,$status);
	$conn->execute();
	if($conn->rowCount() !==0){
		$output['error'] = true;
		$output['message'] = "Tenant Name ( ".$tenant_name." ) Already Exists please choose different name";
	}else{

		$conn=$dbh->prepare("SELECT * FROM `room` WHERE (`room_number` = ? AND `estate_id` = ?) LIMIT 1");
		$conn->bindParam(1,$room_number);
		$conn->bindParam(2,$estate_id);
		$conn->execute();
		if($conn->rowCount() !==0){
			$results=$conn->fetch(PDO::FETCH_OBJ);
			$room_id=$results->room_id;
		}else{
			$room_id="Err";
		}

		$conn=$dbh->prepare('INSERT INTO `tenants`(`tenant_name`, `id_number`, `phone_number`, `deposit_amount`, `estate_id`, `room_id`, `added_by`) VALUES (?,?,?,?,?,?,?)');
		$conn->bindParam(1,$tenant_name);
		$conn->bindParam(2,$id_number);
		$conn->bindParam(3,$phone_number);
		$conn->bindParam(4,$deposit_amount);
		$conn->bindParam(5,$estate_id);
		$conn->bindParam(6,$room_id);
		$conn->bindParam(7,$added_by);
		$conn->execute();
		if($conn->rowCount() == 0){
			$output['error'] = true;
			$output['message'] = "failed, Please try again";
		}elseif($conn->rowCount() !==0){

			$occupiedStatus = 1;
			$conn=$dbh->prepare("UPDATE `room` SET `status`=? WHERE `room_id` = ?");
			$conn->bindParam(1,$occupiedStatus);
			$conn->bindParam(2,$room_id);
			$conn->execute();
			
			if($conn->rowCount() == 0){
				$output['error'] = true;
				$output['message'] = "failed, Please try again";
			}elseif($conn->rowCount() !==0){
				$output['error'] = false;
				$output['message'] = "Tenant ( ".$tenant_name." ) Succefully Added";
			}
		}
	}
	echo json_encode($output);
}
else{
	$output['error'] = true;
	$output['message'] = "Insert the parameters";
}
?>