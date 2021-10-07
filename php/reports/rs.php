	<?php
require_once "header.php";
 ?>
    <div class="content-page">
    
        <!-- Start content -->
        <div class="content">
            
            <div class="container-fluid">						

						<div class="row" style="background-color: #fff;">
							<div class="col-lg-12" style="padding: 10px;">
								<div class="float-left"><h4 class="main-title float-left">Estate Room Report</h4></div>
							</div>
						</div>
						<div class="row" style="background-color: #fff;padding-bottom: 10px;">
								<div class="col-lg-4">
									<hr>
									<?php
									/*Requires only the Estate ID */
								$estate_id = 1;	

									$row14192=mysqli_fetch_array(mysqli_query($conn, "SELECT `estate_name`,`commission` FROM `estate` WHERE `estate_id` = '$estate_id'"));
									$estate_name = $row14192[0];
									$commission = $row14192[1];

									echo '
									<div class="col-lg-12"> <div class="float-left">Estate Name</div><div class="float-right"><span class="selDebtor">'.$estate_name.".".'</span></div>:</div><hr>
									';
								
									
									?>
								</div>
							</div>
							<div class="row tableIn" style="background-color: #fff;margin-top: 20px;padding-top: 10px;">
							<div style="padding: 5px; ">

							<h5>Rooms List </h5>
							</div>
							<div class="col-lg-12">
										<div class=" table-responsive">
											<table id="sysTable" class="sysTable table table-bordered table-hover display">
												<thead>
													<tr>
														<th>Room No </th>
														<th>Description</th>
														<th>Status</th>
														<th>Monthly Price</th>
														<th>Deposit Price</th>
														

												
													</tr>
												</thead>
												<tbody class="myRows">
													<?php
													$ttlRm = 0;
													$ttlRmEmpty = 0;
													$ttlRmOccupied = 0;
													$ttlRmInactive = 0;

													$ttlRentToBeCollected = 0;

													$result = mysqli_query($conn, "SELECT `room_number`,`deposit_price`,`monthly_price`,`status`,`room_type` FROM `room` WHERE (`estate_id`='$estate_id') ORDER BY `room_number`");
													while($row=mysqli_fetch_array($result)){
													 	$room_number = $row['room_number'];
											            $deposit_price = $row['deposit_price'];
											            $monthly_price = $row['monthly_price'];
											            $status = $row['status'];
											            $room_type = $row['room_type'];

											            //get roomdescriptions
													$row1492=mysqli_fetch_array(mysqli_query($conn, "SELECT `meta_value` FROM `sys_meta` WHERE (`meta_value_int` = '$room_type' AND `meta_key` = 'room_type')"));
													$room_typeStr = $row1492[0];
											            //get room status 
											        $row14923=mysqli_fetch_array(mysqli_query($conn, "SELECT `meta_value` FROM `sys_meta` WHERE (`meta_value_int` = '$status' AND `meta_key` = 'room_status')"));
													$room_statusStr = $row14923[0];

													if($status == 0){
														$ttlRmEmpty = $ttlRmEmpty + 1;
													}else if($status == 1 ){
														$ttlRmOccupied = $ttlRmOccupied + 1;
														$ttlRentToBeCollected = $ttlRentToBeCollected + $monthly_price;
													}else if($status == 2){
														$ttlRmInactive = $ttlRmInactive + 1;
													}else{

													}
													$ttlRm = $ttlRm + 1;


	
						                 echo "
													<tr>
														<td>$room_number</td>
														<td>$room_typeStr</td>
														<td>$room_statusStr</td>
														<td>$monthly_price</td>
														<td>$deposit_price</td>
														
													</tr>
													";
												}
													?>
												</tbody>
											</table>
							</div>
						</div>
						<div class="col-lg-12">
                        <?php
                            echo '<div class="float-right"><b>Total Rooms:	'.$ttlRm.'</b></div><br>';
                            echo '<div class="float-right"><b>Occupied Rooms:	'.$ttlRmOccupied.'</b></div><br>';
                            echo '<div class="float-right"><b>Empty Rooms:	'.$ttlRmEmpty.'</b></div><br>';
                           // echo '<div class="float-right"><b>Inactive Rooms:	'.$ttlRmInactive.'</b></div><br>';
                            //echo '<div class="float-right"><b>Rent To Be Collected:	'.$ttlRentToBeCollected.'</b></div><br>';
                            //echo '<div class="float-right"><b>'.$commission .'% Commission:	'.$comm.'</b></div><br>';
                        ?>
                    </div>
				</div>

					<footer class="footer">
		<div style="text-align : center;">

			<?php 
			//getting co. name for footer
				$row1349=mysqli_fetch_array(mysqli_query($conn, "SELECT `meta_value` FROM `sys_meta` WHERE `meta_key` = 'company name'"));
				$coName = $row1349[0];
			?>
			
			<span class="text-right">Copyright &copy;<script>document.write(new Date().getFullYear());</script> <?php echo $coName?>  </span>
		</div>
	</footer>

</div>
	<script src="assets/js/jquery.dataTables.min.js"></script>
	<script src="assets/js/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
						$('.sysTable').DataTable({
        "order": [[ 0, "asc" ]]
    });
					} );
</script>

