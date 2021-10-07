<?php
include 'config.php';
	if(isset($_GET['key'])){
		$key = $_GET['key'];
		$query = "SELECT `tenant_id`,`tenant_name`,`estate_id`,`room_id` FROM `tenants` WHERE (`tenant_id` LIKE '%$key%' OR `tenant_name` LIKE '%$key%' OR `phone_number` LIKE '%$key%' OR `id_number` LIKE '%$key%' OR `alt_name_1` LIKE '%$key%' OR `alt_name_2` LIKE '%$key%') ORDER BY `tenant_name`";
		$result = mysqli_query($con,$query);
		$response = array();
		while($row = mysqli_fetch_assoc($result)){

			$estate_id = $row['estate_id'] ;
			$room_id = $row['room_id'] ;
			$tenant_id = $row['tenant_id'] ;

			//getting previous balance and last payment date
			$conn=$dbh->prepare("SELECT * FROM `payment_transactions` WHERE `tenant_id` = ? ORDER BY `payment_id` DESC LIMIT 1");
		    $conn->bindParam(1,$tenant_id);
		    $conn->execute();
		    if($conn->rowCount() !==0){
		        $results=$conn->fetch(PDO::FETCH_OBJ);
		        $balance=$results->balance;
		        $date_processed=$results->date_processed;
		        $last_yr=$results->year;
		        $last_month=$results->month;
		    }else{
		        $balance="";
		        $date_processed="";
		         $last_month = "";
    			 $last_yr = "";
	  	  	}


			//getting room number and monthly payment
			$conn=$dbh->prepare("SELECT * FROM `room` WHERE (`room_id` = ? AND `estate_id` =?)");
		    $conn->bindParam(1,$room_id);
		    $conn->bindParam(2,$estate_id);
		    $conn->execute();
		    if($conn->rowCount() !==0){
		        $results=$conn->fetch(PDO::FETCH_OBJ);
		        $monthly_price=$results->monthly_price;
		        $room_number=$results->room_number;
		    }else{
		        $room_number="Please Contact Zack";
		        $monthly_price="Please Contact Zack";
	  	  	}

			//getting estate name
			$conn=$dbh->prepare("SELECT * FROM `estate` WHERE `estate_id` = ?");
		    $conn->bindParam(1,$estate_id);
		    $conn->execute();
		    if($conn->rowCount() !==0){
		        $results=$conn->fetch(PDO::FETCH_OBJ);
		        $estate_name=$results->estate_name;
		    }else{
		        $estate_name="Please Contact Zack";
	  	  	}
			$current_month = date('m');
    		$current_year = date('Y');
			//$curr_balance = (-(((($current_year - $last_yr)*12)+$current_month) - $last_month) * $monthly_price)+($balance);
			
			if($last_month != "")
			{
				$curr_balance = (-(((($current_year - $last_yr)*12)+$current_month) - $last_month) * $monthly_price)+($balance);
			}
			else{
				$curr_balance = "N/A";
			}

			array_push($response,
				array(
					'tenant_id'=>$row['tenant_id'],
					'tenant_name'=>$row['tenant_name'],
					'balance'=>$curr_balance,
					'date_processed'=>$date_processed,
					'room_number'=>$room_number,
					'monthly_price'=>$monthly_price,
					'estate_name'=>$estate_name
				)
			);
		}
		echo json_encode($response);
	}
mysqli_close($con)
?>