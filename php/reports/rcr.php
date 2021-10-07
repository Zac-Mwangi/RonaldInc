	<?php
require_once "header.php";
 ?>
    <div class="content-page">
    
        <!-- Start content -->
        <div class="content">
            
            <div class="container-fluid">						

						<div class="row" style="background-color: #fff;">
							<div class="col-lg-12" style="padding: 10px;">
								<div class="float-left"><h4 class="main-title float-left">Estate Rent Report</h4></div>
							</div>
						</div>
						<div class="row" style="background-color: #fff;padding-bottom: 10px;">
								<div class="col-lg-4">
									<hr>
									<?php
									/*DATE FROM TO AND ESTATE ID*/

									//$estate_id = $_GET["estate_id"];
								$estate_id = 1;	

									$row14192=mysqli_fetch_array(mysqli_query($conn, "SELECT `estate_name` FROM `estate` WHERE `estate_id` = '$estate_id'"));
									$estate_name = $row14192[0];

									//DATES FROM AND TO
									$date1 = "18 Sep 2020";
									$date2 = "19 Sep 2022";

									$lowerDate = date('Y-m-d',strtotime($date1));
									$upperDate = date('Y-m-d',strtotime($date2));

									echo '
									<div class="col-lg-12"> <div class="float-left">Estate Name</div><div class="float-right"><span class="selDebtor">'.$estate_name.".".'</span></div>:</div><hr>
									';
								
									?>
								</div>
							</div>
							<div class="row tableIn" style="background-color: #fff;margin-top: 20px;padding-top: 10px;">
							<div style="padding: 5px; ">

							<h5>Transactions </h5>
							</div>
							<div class="col-lg-12">
										<div class=" table-responsive">
											<table id="sysTable" class="sysTable table table-bordered table-hover display">
												<thead>
													<tr>
														<th>Receipt Number</th>
														<th>Tenant Name </th>
														<th>Room</th>
														<th>Rent Paid</th>
														<th>Date</th>
													</tr>
												</thead>
												<tbody class="myRows">
													<?php
													$ttCollected = 0;
													$result = mysqli_query($conn, "SELECT `payment_id`, `tenant_id`, `Amount`, `balance`, `year`, `month`, `payment_method`, `reference`, `date_processed`, `processed_by` FROM `payment_transactions` WHERE (`type` = 0 AND `reverse_status` = 0 AND (DATE(`date_processed`)>='$lowerDate' AND DATE(`date_processed`)<='$upperDate'))");
													while($row=mysqli_fetch_array($result)){
														$payment_id = $row['payment_id'];
													 	$tenant_id = $row['tenant_id'];
													 	$Amount = $row['Amount'];
													 	$balance = $row['balance'];
													 	$year = $row['year'];
													 	$month = $row['month'];
													 	$payment_method = $row['payment_method'];
													 	$reference = $row['reference'];
													 	$date_processed = $row['date_processed'];
													 	$processed_by = $row['processed_by'];
	


														$row1492=mysqli_fetch_array(mysqli_query($conn, "SELECT `tenant_name`, `phone_number`, `estate_id`, `room_id` FROM `tenants` WHERE `tenant_id` = '$tenant_id'"));
														$tenant_name = $row1492[0];
														$phone_number = $row1492[1];
														$estate_idT = $row1492[2];
														$room_id = $row1492[3];

														

														if($estate_id == $estate_idT){

														$row14922=mysqli_fetch_array(mysqli_query($conn, "SELECT `room_number` FROM `room` WHERE (`room_id` = '$room_id' AND `estate_id` = '$estate_id')"));
														$room_number = $row14922[0];
														

														$ttCollected = $ttCollected + $Amount;

														//$ttlNetPay = $ttCollected - $TTLCommission;

								                		echo "
															<tr>
																<td>$payment_id</td>
																<td>$tenant_name</td>
																<td>$room_number</td>
																<td>$Amount</td>
																<td>$date_processed</td>
															</tr>
															";
														}
												}
													?>
												</tbody>
											</table>
							</div>
						</div>
					<div class="col-lg-12">
                        <?php
                            echo '<div class="float-right"><b>Total Tenants Ksh.'.$ttCollected.'</b></div><br>';
                            //echo '<div class="float-right"><b>'.$commission.'% Commission Ksh.'.$TTLCommission.'</b></div><br>';
                            //echo '<div class="float-right"><b>Total Net Pay Ksh.'.$ttlNetPay.'</b></div><br>';
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

