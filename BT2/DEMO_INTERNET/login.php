<?php 

require_once 'config.php';

$key = $_POST['key'];
$user_name  = $_POST['user_name'];
$password   = $_POST['password'];
if ( $key == "login" ){  
    if( $user_name =="" || $password==""){
        echo json_encode(array( "status" => "false","result_code" => "Parameter missing!") );
    }
    else{
        // echo json_encode(array( "status" => "true","result_code" => "Đã Nhận!","user_name"=>$user_name,"password"=>$password
        // ) );
$sql = "SELECT * FROM account WHERE user_name ='$user_name' AND password='$password'";
$result = mysqli_query($conn,$sql);
        if (mysqli_num_rows($result)>0)
        {
                echo json_encode(array("status" => "true", 'result_code' =>'Co tai khoan'));
        } else 
        {
              
                echo json_encode(array("status" => "false", 'result_code' =>'Khong co tai khoan '));
         }
}
    }

        else 
        {
            mysqli_close($conn);
        }

        
?>