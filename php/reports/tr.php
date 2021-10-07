	<?php
require_once "header.php";
 ?>
    <div class="content-page">
    
        <!-- Start content -->
        <div class="content">
            
            <div class="container-fluid">						

						<div class="row" style="background-color: #fff;">
							<div class="col-lg-12" style="padding: 10px;">
								<div class="float-left"><h4 class="main-title float-left">Estate Tenant Report</h4></div>
							</div>
						</div>
						<div class="row" style="background-color: #fff;padding-bottom: 10px;">
								<div class="col-lg-4">
									<hr>
									<?php
									/*REQUIRES ESTATE ID*/
									//$estate_id = $_GET["estate_id"];
								$estate_id = 1;	

									$row14192=mysqli_fetch_array(mysqli_query($conn, "SELECT `estate_name` FROM `estate` WHERE `estate_id` = '$estate_id'"));
									$estate_name = $row14192[0];

									echo '
									<div class="col-lg-12"> <div class="float-left">Estate Name</div><div class="float-right"><span class="selDebtor">'.$estate_name.".".'</span></div>:</div><hr>
									';
									
									?>
								</div>
							</div>
							<div class="row tableIn" style="background-color: #fff;margin-top: 20px;padding-top: 10px;">
							<div style="padding: 5px; ">

							<h5>Tenant List </h5>
							</div>
							<div class="col-lg-12">
										<div class=" table-responsive">
											<table id="sysTable" class="sysTable table table-bordered table-hover display">
												<thead>
													<tr>
														<th>Tenant Name </th>
														<th>Phone</th>
														<th>Room Number</th>
														<th>Date Processed</th>
														<th>Previous Balance (Kes)</th>
														<th>Actual Balance (kes)</th>
														<th>Remark</th>												
													</tr>
												</thead>
												<tbody class="myRows">
													<?php
													$ttlTenant = 0;
													$result = mysqli_query($conn, "SELECT `tenant_id`,`tenant_name`,`phone_number`,`room_id`,`date_added` FROM `tenants` WHERE (`estate_id` = '$estate_id' AND `status` = 1 ) ORDER BY `tenant_name`");
													while($row=mysqli_fetch_array($result)){
														$tenant_id = $row['tenant_id'];
													 	$tenant_name = $row['tenant_name'];
											            $phone_number = $row['phone_number'];
											            $room_id = $row['room_id'];
											            $date_added = $row['date_added'];

														$date_addedFormat = date('jS F Y',strtotime($date_added));

											            $row1492=mysqli_fetch_array(mysqli_query($conn, "SELECT `room_number`,`monthly_price` FROM `room` WHERE `room_id` = '$room_id'"));
														$room_number = $row1492[0];
														$monthly_price = $row1492[1];

														$ttlTenant = $ttlTenant + 1;

														//getting last payment for that particular trasaction
														$row2=mysqli_fetch_array(mysqli_query($conn, "SELECT `balance`,`month`,`year`,`date_processed` FROM `payment_transactions` WHERE `tenant_id` =  '$tenant_id' AND `type` = 0 AND `reverse_status` = 0 ORDER BY `payment_id` DESC LIMIT 1"));
														$balance = $row2[0];
														$last_month = $row2[1];
														$last_yr = $row2[2];
														$date_processed = $row2[3];

														$date_processedFormat = date('jS F Y',strtotime($date_processed));

														$current_month = date('m');
    													$current_year = date('Y');

    													if($last_month==""){

    													}else{
    													$curr_balance = (-(((($current_year - $last_yr)*12)+$current_month) - $last_month) * $monthly_price)+($balance);
    													$unpaidMonths = ((($current_year - $last_yr) * 12) + $current_month) - $last_month;


    													}
														//check room number using room_id
														$row1492=mysqli_fetch_array(mysqli_query($conn, "SELECT `monthly_price` FROM `room` WHERE (`room_id` = '$room_id' AND `estate_id` = '$estate_id')"));
														$monthly_price = $row1492[0];

														$monthsT = round(abs($balance/$monthly_price));

														$unpaidMonthsT = ($curr_balance/$monthly_price);
														$rbb =floor($unpaidMonthsT);
														$days = (abs($unpaidMonthsT) - floor(abs($unpaidMonthsT)))*30;
													   

														if($unpaidMonthsT < 0){
															$rm = "UNPAID - ".floor(abs($unpaidMonthsT)). "  Months " . floor($days)." Days";
														}else if($unpaidMonthsT == 0){
															$rm = "N/A";
														}else{
															$rm = "PREPAID - ".floor(abs($unpaidMonthsT)). " Months " . floor($days)." Days";
														}

														if($last_month=="1"){
															$monthStr = "JAN";
														}else if($last_month=="2"){
															$monthStr = "FEB";
														}else if($last_month=="3"){
															$monthStr = "MAR";
														}else if($last_month=="4"){
															$monthStr = "APR";
														}else if($last_month=="5"){
															$monthStr = "MAY";
														}else if($last_month=="6"){
															$monthStr = "JUN";
														}else if($last_month=="7"){
															$monthStr = "JUL";
														}else if($last_month=="8"){
															$monthStr = "AUG";
														}else if($last_month=="9"){
															$monthStr = "SEP";
														}else if($last_month=="10"){
															$monthStr = "OCT";
														}else if($last_month=="11"){
															$monthStr = "NOV";
														}else if($last_month=="12"){
															$monthStr = "DEC";
														}else{
															$monthStr = "Contact programmer";
														}



														if($balance>0){
															$remark = "Prepaid for the next ".$monthsT." Month from ".$monthStr." ".$last_yr;
														}else if($balance < 0 ){
															$remark = "Balance  for ".$monthsT." Month to ".$monthStr." ".$last_yr;
														}else{
															$remark = "N/A";
														}

														if($curr_balance>0){
															$CB = $curr_balance." (Exess)";
														}else if($curr_balance < 0 ){
															$CB = $curr_balance." (Less)";
														}else{
															$CB = $curr_balance;
														}
														
													$r = "kes ";
						                		echo "
													<tr>
														<td>$tenant_name</td>
														<td>$phone_number</td>
														<td>$room_number</td>
														<td>$date_processedFormat</td>
														<td>$balance</td>
														<td>$CB</td>
														<td>$rm</td>
													</tr>
													";
												$last_month = "";	
												$last_yr = "";
												$curr_balance=0;
												$unpaidMonths = 0;
												}


													?>
												</tbody>
											</table>
							</div>
						</div>
					<div class="col-lg-12">
                        <?php
                            echo '<div class="float-right"><b>Total Tenants:	'.$ttlTenant.'</b></div><br>';
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

