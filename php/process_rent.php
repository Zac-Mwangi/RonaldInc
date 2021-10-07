<?php
$tenant_id = $_POST['tenant_id'];
//$tenant_id = 7;
$paymentMethod = $_POST['paymentMethod'];
$rent_amount = $_POST['rent_amount'];
$transaction_code = $_POST['transaction_code'];
$processed_by = $_POST['processed_by'];


include ('config.php');

if($tenant_id!=""){
	//code to process rent here

	//get room and estate from tenant table	
	$row10=mysqli_fetch_array(mysqli_query($con, "SELECT `estate_id`,`room_id` FROM `tenants` WHERE `tenant_id` = '$tenant_id' "));
    $estate_id = $row10[0];
    $room_id = $row10[1];

    //get room monthly_price
    $row111=mysqli_fetch_array(mysqli_query($con, "SELECT `monthly_price` FROM `room` WHERE (`estate_id` = '$estate_id' AND `room_id` = '$room_id') "));
    $monthly_price = $row111[0];

    $current_month = date('m');
    $current_year = date('Y');

    //get payment method int
    $row11=mysqli_fetch_array(mysqli_query($con, "SELECT `meta_value_int` FROM `sys_meta` WHERE `meta_value` = '$paymentMethod' "));
    $paymentMethodInt = $row11[0];
    

    //check if there is existing trasactions
	$conn=$dbh->prepare("SELECT * FROM `payment_transactions` WHERE `tenant_id` = ? ORDER BY `payment_id` DESC LIMIT 1");
	$conn->bindParam(1,$tenant_id);
	$conn->execute();
	if($conn->rowCount() !== 0){
		$results=$conn->fetch(PDO::FETCH_OBJ);
		$balance=$results->balance;
		$last_paid_month=$results->month;
		$last_paid_year = $results->year;
		

		$unpaid_months = ($current_month+(($current_year-$last_paid_year)*12))-$last_paid_month;

		$rent_to_be_paid = $unpaid_months * $monthly_price;

		$S = $rent_amount - $rent_to_be_paid;

		//$new_balance = $S - $balance;

		$new_balance =  $rent_amount - ($rent_to_be_paid - $balance);


		/*$output['error'] = true;
		$output['message'] = $new_balance;*/

		//now insert
		$conn=$dbh->prepare("INSERT INTO `payment_transactions`(`tenant_id`, `Amount`, `balance`, `month`, `payment_method`, `reference`, `processed_by`,`year`) VALUES (?,?,?,?,?,?,?,?)");
		$conn->bindParam(1,$tenant_id);
		$conn->bindParam(2,$rent_amount);
		$conn->bindParam(3,$new_balance);
		$conn->bindParam(4,$current_month);
		$conn->bindParam(5,$paymentMethodInt);
		$conn->bindParam(6,$transaction_code);
		$conn->bindParam(7,$processed_by);
		$conn->bindParam(8,$current_year);
		$conn->execute();
		if($conn->rowCount() == 0){
			$output['error'] = true;
			$output['message'] = "Err In payment";
		}else{
			$output['error'] = false;
			$output['message'] = "Entry done owk";
		}

	}else{
		//first record get the balance
		$balance = $rent_amount - $monthly_price;
		//insert first record

		$conn=$dbh->prepare("INSERT INTO `payment_transactions`(`tenant_id`, `Amount`, `balance`, `month`, `payment_method`, `reference`, `processed_by`,`year`) VALUES (?,?,?,?,?,?,?,?)");
		$conn->bindParam(1,$tenant_id);
		$conn->bindParam(2,$rent_amount);
		$conn->bindParam(3,$balance);
		$conn->bindParam(4,$current_month);
		$conn->bindParam(5,$paymentMethodInt);
		$conn->bindParam(6,$transaction_code);
		$conn->bindParam(7,$processed_by);
		$conn->bindParam(8,$current_year);

		$conn->execute();
		if($conn->rowCount() == 0){
			$output['error'] = true;
			$output['message'] = "Err In payment";
		}else{
			$output['error'] = false;
			$output['message'] = "First Entry done owk";
		}
	}

}else
{
	$output['error'] = true;
	$output['message'] = "Insert the parameters";
}

	echo json_encode($output);

?>