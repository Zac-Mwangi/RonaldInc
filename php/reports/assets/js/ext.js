$(document).ready(function() {
    var in_Date;
    var in_Day;
    var out_Date;
    var out_Day;
    var duration = 0;
    var stay = 1;
    var room_date,room_rec;
    var room_disc,room_rate,room_total,room_paid,room_bal = 0;

    var cid,uid,rpid,bid;
    var expn_Date;
    

    $('.checkInDate').daterangepicker({
            singleDatePicker: true,
            autoUpdateInput: false,
            minDate: moment().subtract(1,'d').toDate(),
            maxDate: moment().toDate(),
            locale: {
            format: 'DD/MM/YYYY'
        },
            showDropdowns: true
        }, function(start, end, label) {
             in_Date =  moment(start).startOf('day');
             in_Day = parseInt(start.format('D'));
            //console.log('Check in date: ' + in_Date);
        });
    $('.checkInDate').on('apply.daterangepicker', function (ev, picker) {
                $(this).val(picker.startDate.format('DD/MM/YYYY'));
            });

    $('.checkOutDate').daterangepicker({
            singleDatePicker: true,
            autoUpdateInput: false,
            minDate: moment().toDate(),
            maxDate: moment().add(7,'d').toDate(),
            locale: {
            format: 'DD/MM/YYYY'
        },
            showDropdowns: true
        }, function(start, end, label) {
             out_Date = moment(start).startOf('day');
             out_Day = parseInt(start.format('D'));
            //console.log('Check out date: ' + out_Date);
        });
    $('.checkOutDate').on('apply.daterangepicker', function (ev, picker) {
                $(this).val(picker.startDate.format('DD/MM/YYYY'));
            });
//Calculate the stay duration
    $( '.booD' ) .blur(function () {
    var nigh = document.getElementById("nights");  
     duration = out_Date.diff(in_Date, 'days');
     if (duration===0) {
        stay = duration+1;
        nigh.innerHTML = '1 Day';
        $('.staydur').val('1 Day');
    }else{
        stay = duration;
        nigh.innerHTML = duration+' Night(s)';
        $('.staydur').val(duration+' Night(s)');
    }
    roomTotal();

    //console.log(duration);
    });  

var pageURL = $(location).attr("href");
    var myString = pageURL.substr(pageURL.indexOf("#") + 1);
    if (window.location.hash) {
      
        swal('Room '+myString, 'Check in this room.', 'success');
    }

//Auto fill room type and rate plan
    $('.myRoom') .change(function (e) {
        e.preventDefault();
        var cc1 = $(this).closest('.rType').val();
        var cc2 = $(this).closest('.rPlan').val();
        var roomId = $(this) .val();

       $.ajax({  
                type: "POST",  
                url: "room_proc.php",  
                data: { roomId : roomId },
                success: function(html) {
                    if (html=="501") {
                        $('.rType').val('');
                        $('.rPlan').val('');
                        swal('Oops!', 'The room is occupied!', 'error');
                    }
                    else if (html=="502") {
                        $('.rType').val('');
                        $('.rPlan').val('');
                        swal('Oops!', 'The room is booked!', 'error');
                    }
                    else if (html=="503") {
                        $('.rType').val('');
                        $('.rPlan').val('');
                        swal('Oops!', 'The room is out of service!', 'error');
                    }
                    else{
                        var data = html.split(",");
                        $('.rType').val(data[0]);
                        $('.rPlan').val(data[1]);
                        room_rate = data[1];
                        document.getElementById("summary-Rno").innerHTML = roomId;
                        roomTotal()
                     }
                }
            }); 
       //console.log(roomId);
    });


    $('.payMeth') .change(function () {
        var py = $('.payMeth').val();
        if (py=='Mpesa') {
            $('.transCode').prop('disabled', false);

            $('.transCode') .keyup(function () {
             $(this).val($(this).val().toUpperCase());
            });

        }
        else{$('.transCode').prop('disabled', true);}
        console.log(py);
    });

    //Summary section
    $('.discount') .change(function () {
        room_disc = parseInt($(this).val())|| 0;
        document.getElementById("summary-discount").innerHTML = room_disc;
        balance()
    });
    $('.payStatus') .change(function () {
    if ($('.payStatus').val()=="Confirmed") {
        room_paid = $('.amount').val();
            document.getElementById("summary-paid").innerHTML = room_paid;
            //console.log('Paid '+$('.amount').val());
            balance()
            received()
        }
    });

    $('.amount') .change(function () {
    if ($('.payStatus').val()=="Confirmed") {
        room_paid = $('.amount').val();
            document.getElementById("summary-paid").innerHTML = room_paid;
            //console.log('Paid '+$('.amount').val());
            balance()
            received()
        }
        console.log('Disc '+room_disc+' tot '+room_total+' paid '+room_paid);
    });

    //Check in button functions
    $('.check-in').click(function(e) {
        e.preventDefault();

        //Check if all required fields are filled
        if ($('.checkInDate').val()==""||$('.checkOutDate').val()==""||$('.myRoom').val()==""||$('.adults').val()==""||
            $('.children').val()==""||$('.fName').val()==""||$('.lName').val()==""||$('.idNO').val()==""||
            $('.payMeth').val()==""||$('.amount').val()==""||$('.payStatus').val()=="") {

            swal('Oops!', 'Fill all required fields!', 'error');
        }
        else{
                if ($('.payMeth').val()=="Mpesa"&&$('.transCode').val()=="") {

                        swal('Oops!', 'Fill all required fields!', 'error');
                }
                else{
                    if ($('.payStatus').val()=="Confirmed") {

                        if (room_bal==0) {
                            var stringData = $(".checkInDateForm,.checkOutDateForm,.guestInfo,.payDetails,.otherDetails,.roomEnter").serializeArray();
                            $.ajax({  
                                    type: "POST",  
                                    url: "room_proc.php",  
                                    data: stringData,
                                    beforeSend:function(){
                                        $('.check-in').unbind("click");
                                    },
                                    success: function(html) {
                                    }
                                });

                            swal('Thank you!', 'Room Checked in!', 'success')
                            .then(function(){
                                /*$(".guestInfo")[0].reset();
                                $(".payDetails")[0].reset();
                                $(".otherDetails")[0].reset();
                                $(".roomEnter")[0].reset();*/
                                        window.location.replace("room_occupation.php");
                                 });
                        }else{
                            swal('Balance!', 'There is a balance due.', 'error');
                        }

                }else{
                    swal('Oops!', 'The payment must be confirmed!', 'error');
                }
                }
            }
        
        });

     $('.search_text').keyup(function(){
              var search = $(this).val();
              if(search != '')
              {
               load_data(search);
              }
              else
              {
               $('.result').hide();
              }
             });

     

    function received(){
        if (typeof room_disc === "undefined") {room_disc=0;}
        if (typeof room_paid === "undefined") {room_paid=0;}
        room_rec = parseInt(room_paid);
        document.getElementById("summary-received").innerHTML = room_rec;
    }
    function balance(){
        if (typeof room_disc === "undefined") {room_disc=0;}
        if (typeof room_paid === "undefined") {room_paid=0;}
        room_bal = parseInt(room_total)-parseInt(room_paid)-parseInt(room_disc);
            document.getElementById("summary-balance").innerHTML = room_bal;
            $('.pbalance').val(room_bal);
    }
    function roomTotal(){
        room_total = room_rate*stay;
        document.getElementById("summary-rate").innerHTML = room_total;
    }

    function load_data(query)
             {
              $.ajax({
               url:"guest_search.php",
               method:"POST",
               data:{query:query},
               success:function(data)
                   {

                    $('.result').html(data).show();


                    $(".clickable-row").click(function() {
                        var currentRow=$(this).closest("tr"); 
                        var fiName=currentRow.find("td:eq(1)").text();
                        var laName=currentRow.find("td:eq(2)").text();
                        var idd=currentRow.find("td:eq(3)").text();
                        var phNo=currentRow.find("td:eq(4)").text();
                        var emailA=currentRow.find("td:eq(5)").text();
                        var carp=currentRow.find("td:eq(6)").text();
                        $('.fName').val(fiName);
                        $('.lName').val(laName);
                        $('.idNO').val(idd);
                        $('.pNumber').val(phNo);
                        $('.email').val(emailA);
                        $('.carPlates').val(carp);
                        $('.result').hide();
                    });
                }
            });
          }

//Login on Enter press
    $('.password').keypress(function(event){
    
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
        $(".loginBtn").click(); 
    }
    event.stopPropagation();
});

//Login 
    $(".loginBtn").click(function() {
        var $loginForm = $(".loginInfo");
        var loginFormData =  $loginForm.serializeArray();
        if ($('.username').val()!=""&&$('.password').val()!="") {
            $.ajax({  
                    type: "POST",  
                    url: "login_proc.php",  
                    data: loginFormData,
                    success: function(html) {
                        if (html=="102") {
                            swal('Oops!', 'No user found!', 'error');
                        }
                        else if (html=="106") {
                                swal('Oops!', 'User is inactive! Contact the administrator.', 'error');
                            }
                            else if (html=="103") {
                                swal('Oops!', 'Wrong password!', 'error');
                            }
                                else if (html=="100") {
                                    window.location.replace("index.php");
                                }
                    }
                });
        }
        else{
            $('.passMatch').html("<div class='alert alert-danger alert-dismissible fade show' role='alert'><strong>Fill all the required fields.</strong><button type='button' class='close ItCl' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
        }
    });

//Reset password
    $(".secBtn").click(function() {
        var $resetForm1 = $(".resetInfo1");
        var resetForm1Data =  $resetForm1.serializeArray();
        var resUname = $('.username').val();
        var d = new Date();d.setTime(d.getTime() + (2*60*1000));var expires = "expires="+ d.toUTCString();
        if ($('.username').val()!=""&&$('.securityAns').val()!="") {
            $.ajax({  
                    type: "POST",  
                    url: "reset_pass_proc.php",  
                    data: resetForm1Data,
                    success: function(html) {
                        if (html=="102") {
                            swal('Oops!', 'No user found!', 'error');
                        }
                            else if (html=="105") {
                                swal('Oops!', 'Wrong answer!', 'error');
                            }
                                else if (html=="100") {
                                    document.cookie =  "resetuserName=" + resUname + ";" + expires + ";path=/";
                                        $('.theform').html('<form class="resetInfo2" name="resetInfo2"><div class="form-group" style="padding-top: 10px;"><input type="password" class="form-control password" name="password" placeholder="Password" autocomplete="off"><span class="fa fa-eye field-icon toggle-passw" toggle=".password"></span></div><div class="form-group "><input type="password" class="form-control confPassword"  placeholder="Confirm Password" autocomplete="off"></div><div class="form-group" align="center"><span class="btn btn-primary resetBtn">Reset Password</span></div></form>');

                                        $(".resetBtn").click(function() {
                                        var $resetForm = $(".resetInfo2");
                                        var resetFormData =  $resetForm.serializeArray();
                                        var pass1 = $('.password').val();var pass2 = $('.confPassword').val();
                                        if ($('.password').val()!=""&&$('.confPassword').val()!="") {
                                            if (pass1==pass2) {
                                                $.ajax({  
                                                        type: "POST",  
                                                        url: "reset_pass_proc.php",  
                                                        data: resetFormData,
                                                        success: function(html) {
                                                            if (html=="100") {
                                                                window.location.replace("login.php");
                                                            }
                                                        }
                                                    });
                                            }else{
                                                $('.passMatch').html("<div class='alert alert-danger alert-dismissible fade show' role='alert'><strong>Passwords do not match.</strong><button type='button' class='close ItCl' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
                                            }
                                    }
                                    else{
                                            $('.passMatch').html("<div class='alert alert-danger alert-dismissible fade show' role='alert'><strong>Fill all the required fields!</strong><button type='button' class='close ItCl' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
                                        }
                                    });
                                        //See password function
                                        $(".toggle-passw").click(function() {

                                              $(this).toggleClass("fa-eye fa-eye-slash");
                                              var input1 = $(".password");
                                              var input2 = $(".confPassword");
                                              if (input1.attr("type") == "password"||input2.attr("type") == "password") {
                                                input1.attr("type", "text");
                                                input2.attr("type", "text");
                                              } else {
                                                input1.attr("type", "password");
                                                input2.attr("type", "password");
                                              }
                                            });
                                }
                    }
                });
        }else{
            $('.passMatch').html("<div class='alert alert-danger alert-dismissible fade show' role='alert'><strong>Fill all the required fields!</strong><button type='button' class='close ItCl' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
        }
    });


    $('.addStaff').click(function(){
        window.location.href = "register.php";
    });

    $('.addUser').click(function(){
        
    });

    //Register on Enter press
    $('.confPassword').keypress(function(event){
    
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
        $(".registerBtn").click(); 
    }
    event.stopPropagation();
});
    
//Register new User 
    $(".registerBtn").click(function() {
        var $registerForm = $(".registerInfo");
        var registerFormData =  $registerForm.serializeArray();
        var pass1 = $('.password').val();var pass2 = $('.confPassword').val();
        if ($('.fullName').val()!=""&&$('.username').val()!=""&&$('.phoneNo').val()!=""&&$('.securityAns').val()!=""
            &&$('.password').val()!=""&&$('.confPassword').val()!="") {
            if (pass1==pass2) {
                $.ajax({  
                        type: "POST",  
                        url: "register_proc.php",  
                        data: registerFormData,
                        success: function(html) {
                            if (html=="101") {
                                swal('Oops!', 'User Exists!', 'error');
                            }
                                else if (html=="104") {
                                    swal('Oops!', 'Cannot Add User!', 'error');
                                }
                                    else if (html=="100") {
                                        window.location.replace("staffDetails.php");
                                    }
                        }
                    });
            }else{
                $('.passMatch').html("<div class='alert alert-danger alert-dismissible fade show' role='alert'><strong>Passwords do not match.</strong><button type='button' class='close ItCl' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
            }
        }
        else{
            $('.passMatch').html("<div class='alert alert-danger alert-dismissible fade show' role='alert'><strong>Fill all the required fields!</strong><button type='button' class='close ItCl' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button></div>");
        }
    });

//Edit users
$('.uEdit').click(function(){
                var selRow = $(this).closest('tr');
                var selUName=selRow.find("td:eq(0)").text();var selFName=selRow.find("td:eq(1)").text();
                var selpNo=selRow.find("td:eq(2)").text();var selAccLev=selRow.find("td:eq(3)").text();
                if (selAccLev=="Not Assigned") {selAccLev="0";}else if (selAccLev=="All access") {selAccLev="1";}
                else if (selAccLev=="All read-only") {selAccLev="2";}else if (selAccLev=="Room") {selAccLev="3";}
                else if (selAccLev=="Bar") {selAccLev="4";}else if (selAccLev=="Restaurant") {selAccLev="5";}
                    
                var issue = "edit";
                $('.playField').html("<form class='form_edit_user'><div class='form-group'><label for='EuserName'>UserName</label><input type='text' name ='EuserName' class='EuserName form-control'></div><div class='form-group'><label for='EfullName'>Full Name</label><input type='text' placeholder='Full Name' name='EfullName' class='EfullName form-control'></div><div class='form-group'><label for='EphoneNo'>Phone Number</label><input type='text' placeholder='Phone Number' name='EphoneNo' class='EphoneNo form-control'></div><div class='form-group'><label for='EaccessLevel'>Access Level</label><select class='form-control EaccessLevel' name='EaccessLevel'><option value selected > -- select access level -- </option><option value='0'>No Access</option><option value='1'>All Access</option><option value='2'>All read-only</option><option value='3'>Room Access</option><option value='4'>Bar Access</option><option value='5'>Restaurant Access</option></select></div><input type='hidden' name='issue' value='editUser'><div class='form-group' align='center'><span class='btn btn-info editUserBtn'><i class='fa fa-edit'> Edit Info</i></span></div></form>");

                $('.EuserName').val(selUName);$('.EfullName').val(selFName);
                $('.EphoneNo').val(selpNo);$('.EaccessLevel').val(selAccLev);

                $('.editUserBtn').click(function(){
                    var $editForm = $(".form_edit_user");
                    var editFormData = $editForm.serializeArray();
                    $.ajax({  
                           type: "POST",  
                           url: "sysUsers_proc.php",  
                           data: editFormData,
                           success: function(html) {
                               if (html=="100") {
                                swal('Wow!', 'User info updated.', 'success').then(function(){
                                    window.location.replace("sysUsers.php");

                                 });
                               }
                           }
                       });
                });
            });

//Delete user
$('.uDelete').click(function(){
            var selRow = $(this).closest('tr');
            var selUName=selRow.find("td:eq(0)").text();
            console.log(selUName);
            $('.delUN').text(selUName);
            $('.delUser-modal').modal('show');

            $(".delUserBtn").click(function() {
                $.ajax({  
                        type: "POST",  
                        url: "sysUsers_proc.php",  
                        data: { userdel : selUName },
                        success: function(html) {
                            $('.delUser-modal').modal('hide');
                            if (html=="100") {
                                swal('Success!', 'User deleted.', 'success').then(function(){
                                    window.location.replace("sysUsers.php");

                                 });
                               }
                        }
                    });
            });
        });
//Deactivate staff
$('.sDeactivate').click(function(){
        var selRow = $(this).closest('tr');
        var selFName=selRow.find("td:eq(0)").text();
        $.ajax({  
               type: "POST",  
               url: "sysUsers_proc.php",  
               data: { sDeactivate : selFName },
               success: function(html) {
                   if (html=="100") {
                    swal('Wow!', 'User deactivated.', 'success').then(function(){
                        window.location.replace("staffDetails.php");

                     });
                   }
               }
            });
    });

//Activate staff
$('.sActivate').click(function(){
        var selRow = $(this).closest('tr');
        var selFName=selRow.find("td:eq(0)").text();
        $.ajax({  
               type: "POST",  
               url: "sysUsers_proc.php",  
               data: { sActivate : selFName },
               success: function(html) {
                   if (html=="100") {
                    swal('Wow!', 'User Activated.', 'success').then(function(){
                        window.location.replace("staffDetails.php");

                     });
                   }
               }
            });
    });

//Staff edit
$('.sEdit').click(function(){
                var selRow = $(this).closest('tr');
                var selFName=selRow.find("td:eq(0)").text();var selpNo=selRow.find("td:eq(1)").text();
                var selJobDes=selRow.find("td:eq(2)").text();var selAStatus=selRow.find("td:eq(3)").text();
                var selDocs=selRow.find("td:eq(4)").text();
                    
                var issue = "edit";
                $('.playField').html("<form class='form_edit_staff'><div class='form-group'><label for='EfullName'>Full Name</label><input type='text' placeholder='Full Name' name='EfullName' class='EfullName form-control'></div><div class='form-group'><label for='EphoneNo'>Phone Number</label><input type='text' placeholder='Phone Number' name='EphoneNo' class='EphoneNo form-control'></div><div class='form-group'><label for='EjobDesc'>Job Description</label><textarea name ='EjobDesc' class='EjobDesc form-control'></textarea></div><div class='form-group'><label for='EDocs'>Documents</label></div><input type='hidden' name='issue' value='editStaff'><div class='form-group' align='center'><span class='btn btn-info editStaffBtn'><i class='fa fa-edit'> Edit Info</i></span></div></form>");
                $('.EfullName').val(selFName);$('.EphoneNo').val(selpNo);
                $('.EjobDesc').val(selJobDes);$('.EDocs').val(selDocs);


                $('.editStaffBtn').click(function(){
                    var $editForm = $(".form_edit_staff");
                    var editFormData = $editForm.serializeArray();
                    $.ajax({  
                           type: "POST",  
                           url: "sysUsers_proc.php",  
                           data: editFormData,
                           success: function(html) {
                               if (html=="100") {
                                swal('Wow!', 'User info updated.', 'success').then(function(){
                                    window.location.replace("staffDetails.php");

                                 });
                               }
                           }
                       });
                });
            });
//Context Menus
    $(function() {
        var $sel = $(this);
        $.contextMenu({
            selector: '.context-menu-one', 
            callback: function(key, options) {
                var m = key;
                if (m=="checkout") {
                    var originalElement = $('.context-menu-active');
                    cid = $('.context-menu-active').text().replace(/\s/g,'').substr(4, 2);
                    $('.cheroom').text(cid);
                    $('.cout-modal').modal('show');
                }
            },
            items: {
                "checkout": {name: "Checkout", icon: "fa-sign-out"},
                "sep1": "---------",
                "cancel": {name: "Cancel", icon: function(){
                    return 'context-menu-icon context-menu-icon-quit';
                }}
            }
        });  
    });
    $(function() {
        var $sel = $(this);
        $.contextMenu({
            selector: '.context-menu-two', 
            callback: function(key, options) {
                var m = key;
                var originalElement = $('.context-menu-active');
                uid = $('.context-menu-active').text().replace(/\s/g,'').substr(4, 2);
                var uiduo = uid;
                if (m=="unbook") {
                    $('.unbroom').text(uid);
                    $('.unbook-modal').modal('show');
                }
                else if (m=="checkin") {
                    $.ajax({  
                        type: "POST",  
                        url: "room_proc.php",  
                        data: { uiduo : uiduo },
                        success: function(html) {
                            window.location.replace("room_booking.php#"+uid);
                        }
                    });
                    
                }
            },
            items: {
                "checkin": {name: "Check in", icon: "fa-check"},
                "unbook": {name: "Un-book", icon: "fa-calendar-times-o"},
                "sep1": "---------",
                "cancel": {name: "Cancel", icon: function(){
                    return 'context-menu-icon context-menu-icon-quit';
                }}
            }
        });  
    });
    $(function() {
        var $sel = $(this);
        $.contextMenu({
            selector: '.context-menu-three', 
            callback: function(key, options) {
                var m = key;
                var originalElement = $('.context-menu-active');
                rpid = $('.context-menu-active').text().replace(/\s/g,'').substr(4, 2);
                if (m=="repaired") {
                        $.ajax({  
                        type: "POST",  
                        url: "room_proc.php",  
                        data: { rpid : rpid },
                        success: function(html) {
                            if (html=="1") {
                                swal('Thank you!', 'Room repaired!', 'success').then(function(){
                                location.reload();
                             });
                            }else if (html=="0") {
                                swal('Sorry!', 'There was an error!', 'error');
                            }
                            else if (html=="00") {
                                swal('Oops!', 'You are not authorized!', 'error');
                            }

                        }
                    });
                }
            },
            items: {
                "repaired": {name: "Good to go", icon: "fa-check"},
                "sep1": "---------",
                "cancel": {name: "Cancel", icon: function(){
                    return 'context-menu-icon context-menu-icon-quit';
                }}
            }
        });  
    });
    $(function() {
        var $sel = $(this);
        $.contextMenu({
            selector: '.context-menu-four', 
            callback: function(key, options) {
                var m = key;
                var originalElement = $('.context-menu-active');
                bid = $('.context-menu-active').text().replace(/\s/g,'').substr(4, 2);
                if (m=="book") {
                    $.ajax({  
                        type: "POST",  
                        url: "room_proc.php",  
                        data: { bid : bid },
                        success: function(html) {
                            swal('Thank you!', html, 'success').then(function(){
                                location.reload();
                             });
                        }
                    });
                }else if (m=="outofservice") {
                        $.ajax({  
                        type: "POST",  
                        url: "room_proc.php",  
                        data: { oosid : bid },
                        success: function(html) {
                            if (html=="1") {
                                swal('Thank you!', 'Room Out of Service!', 'success').then(function(){
                                location.reload();
                             });
                            }else if (html=="0") {
                                swal('Sorry!', 'There was an error!', 'error');
                            }
                            else if (html=="00") {
                                swal('Oops!', 'You are not authorized!', 'error');
                            }
                        }
                    });
                }
            },
            items: {
                "book": {name: "Book Room", icon: "fa-calendar-plus-o"},
                "outofservice": {name: "Out of Service", icon: "fa-wrench"},
                "sep1": "---------",
                "cancel": {name: "Cancel", icon: function(){
                    return 'context-menu-icon context-menu-icon-quit';
                }}
            }
        });  
    });

//Room checkout
    $(".checkoutBtn").click(function() {
        $.ajax({  
                type: "POST",  
                url: "room_proc.php",  
                data: { cid : cid },
                success: function(html) {
                    $('.cout-modal').modal('hide');
                    swal('Thank you!', html, 'success').then(function(){
                        location.reload();

                     });

                }
            });
    });

//Room unbooking
$(".unbookBtn").click(function() {
        $.ajax({  
                type: "POST",  
                url: "room_proc.php",  
                data: { uid : uid },
                success: function(html) {
                    $('.unbook-modal').modal('hide');
                    swal('Thank you!', html, 'success').then(function(){
                        location.reload();

                     });

                }
            });
    });

//See password function
    $(".toggle-passw").click(function() {

          $(this).toggleClass("fa-eye fa-eye-slash");
          var input1 = $(".password");
          var input2 = $(".confPassword");
          if (input1.attr("type") == "password"||input2.attr("type") == "password") {
            input1.attr("type", "text");
            input2.attr("type", "text");
          } else {
            input1.attr("type", "password");
            input2.attr("type", "password");
          }
        });

//Expenses
    $('.expnDate').daterangepicker({
            singleDatePicker: true,
            autoUpdateInput: false,
            minDate: moment().subtract(7,'d').toDate(),
            maxDate: moment().toDate(),
            locale: {
            format: 'DD/MM/YYYY'
        },
            showDropdowns: true
        }, function(start, end, label) {
             expn_Date =  moment(start).startOf('day');
            console.log('Expense date: ' + expn_Date);
        });
    $('.expnDate').on('apply.daterangepicker', function (ev, picker) {
                $(this).val(picker.startDate.format('DD/MM/YYYY'));
            });
$('.addExpnField').fadeOut(1);
$('.addExpn').click(function(){
            $('.addExpnField').fadeIn('slow');
        });

$('.expnSubmit').click(function(){
                    var $editForm = $(".expnForm");
                    var editFormData = $editForm.serializeArray();
                    $('.expnDate').val(expn_Date);
                    if ($('.expnDate').val()==""||$('.expnMerchant').val()==""||$('.expnAmount').val()==""||$('.expnPayMeth').val()==""||
                    $('.expnCategory').val()==""||$('.expnDescription').val()=="") {
                        swal('Oops!', 'Some fields are empty.', 'error');
                    }else{
                        $.ajax({  
                               type: "POST",  
                               url: "expenses_proc.php",  
                               data: editFormData,
                               success: function(html) {
                                   if (html=="100") {
                                    swal('Thank you!', 'Expense added.', 'success').then(function(){
                                        $('.expnForm').trigger("reset");$('.expnDate').val("");
                                        window.location.replace("expenses.php");
                                       // $('.addExpnField').fadeOut();
                                     });
                                   }
                               }
                           });
                    }
                });

        $('.expnTranscCode').prop('disabled', true);

        $('.expnPayMeth').change(function () {
            console.log($('.expnPayMeth').val());
            if ($('.expnPayMeth').val()=='Mpesa') {
                $('.TChide').css('visibility', 'visible');
                $('.expnTranscCode').prop('disabled', false);
                $('.expnTranscCode') .keyup(function () {
                         $(this).val($(this).val().toUpperCase());
                        });
            }else{
                $('.TChide').css('visibility', 'hidden');
                $('.expnTranscCode').prop('disabled', true);
            }
        });

        $('.expnCancel').click(function(){
            $('.addExpnField').fadeOut();
        });


        $('.expnConfirm').click(function(){
        var selRow = $(this).closest('tr');
        var selId=selRow.find("td:eq(0)").text();
        $.ajax({  
                    type: "POST",  
                    url: "expenses_proc.php",  
                    data: {confirmExpnId : selId},
                    success: function(html) {
                        if (html=="100") {
                         swal('Thank you!', 'Expense approved.', 'success').then(function(){
                             window.location.replace("expenses.php");
                              });
                            }else{
                                swal('Oops!', html, 'error').then(function(){
                             //window.location.replace("expenses.php");
                              });
                            }
                        }
                    });
    });

    $('.expnDeny').click(function(){
        var selRow = $(this).closest('tr');
        var selId=selRow.find("td:eq(0)").text();
        $.ajax({  
                    type: "POST",  
                    url: "expenses_proc.php",  
                    data: {denyExpnId : selId},
                    success: function(html) {
                        if (html=="100") {
                         swal('Thank you!', 'Expense disapproved.', 'success').then(function(){
                             window.location.replace("expenses.php");
                              });
                            }else{
                                swal('Oops!', html, 'error').then(function(){
                             //window.location.replace("expenses.php");
                              });
                            }
                        }
                    });
    });

    //Inventory ops
    $('.addinvtDate,.reduceinvtDate').daterangepicker({
            singleDatePicker: true,
            autoUpdateInput: false,
            minDate: moment().subtract(7,'d').toDate(),
            maxDate: moment().toDate(),
            locale: {
            format: 'DD/MM/YYYY'
        },
            showDropdowns: true
        }, function(start, end, label) {
             expn_Date =  moment(start).startOf('day');
            console.log('Expense date: ' + expn_Date);
        });
    $('.addinvtDate,.reduceinvtDate').on('apply.daterangepicker', function (ev, picker) {
                $(this).val(picker.startDate.format('DD/MM/YYYY'));
            });

        $('.addInvtField').fadeOut(1);
        $('.addInvt').click(function(){
                    $('.addInvtField').fadeIn('slow');
                });

        $('.invtSubmit').click(function(){
                    var $editForm = $(".invtForm");
                    var editFormData = $editForm.serializeArray();
                    if ($('.invtProduct').val()==""||$('.invtCategory').val()==""||$('.invtItemPrice').val()==""||$('.invtMinLim').val()=="") {
                        swal('Oops!', 'Some fields are empty.', 'error');
                    }else{
                        $.ajax({  
                               type: "POST",  
                               url: "inventory_proc.php",  
                               data: editFormData,
                               success: function(html) {
                                   if (html=="100") {
                                    swal('Thank you!', 'Item added.', 'success').then(function(){
                                        $('.invtForm').trigger("reset");
                                        location.reload();
                                       // $('.addInvtField').fadeOut();
                                     });
                                   }
                               }
                           });
                    }
                });
        $('.invtCancel').click(function(){
                    $('.addInvtField').fadeOut();
                });

        $('.addStock').click(function(){
            var selRow = $(this).closest('tr');
            var selId=selRow.find("td:eq(0)").text();
            var selName=selRow.find("td:eq(1)").text();
            $('.addStock-modal').modal('show');
            $('.addStockId').val(selId);
            $('.prd').val(selName);

            $('.addStockBtn').click(function(){
                var $Form = $(".addStockForm");
                var FormData = $Form.serializeArray();
                
                if ($('.addinvtDate').val()==""||$('.addPrdNo').val()=="") {
                    swal('Oops!', 'Fill required fields.', 'error');
                }else{
                $.ajax({  
                            type: "POST",  
                            url: "inventory_proc.php",  
                            data: FormData,
                            success: function(html) {
                                if (html=="100") {
                                    $('.addStock-modal').modal('hide');
                                 swal('Thank you!', 'Stock added.', 'success').then(function(){
                                     location.reload();
                                      });
                                    }else{
                                        swal('Oops!', html, 'error').then(function(){
                                     //window.location.replace("expenses.php");
                                      });
                                    }
                                }
                            });
                    }
                });
            });

        $('.reduceStock').click(function(){
            var selRow = $(this).closest('tr');
            var selId=selRow.find("td:eq(0)").text();
            var selName=selRow.find("td:eq(1)").text();
            $('.reduceStock-modal').modal('show');
            $('.reduceStockId').val(selId);
            $('.prd').val(selName);

            $('.reduceStockBtn').click(function(){
                var $Form = $(".reduceStockForm");
                var FormData = $Form.serializeArray();
                
                if ($('.reduceinvtDate').val()==""||$('.reducePrdNo').val()=="") {
                    swal('Oops!', 'Fill required fields.', 'error');
                }else{
                $.ajax({  
                            type: "POST",  
                            url: "inventory_proc.php",  
                            data: FormData,
                            success: function(html) {
                                if (html=="100") {
                                    $('.reduceStock-modal').modal('hide');
                                 swal('Thank you!', 'Stock deducted.', 'success').then(function(){
                                     location.reload();
                                      });
                                    }else{
                                        swal('Oops!', html, 'error').then(function(){
                                     //window.location.replace("expenses.php");
                                      });
                                    }
                                }
                            });
                    }
                });
            });

});

