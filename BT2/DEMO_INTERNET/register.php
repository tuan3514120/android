
<?php
require_once 'config.php';

$key = $_POST['key'];
$user_name  = $_POST['user_name'];
$password   = $_POST['password'];
$name      = $_POST['name'];
if($key == "register"){
// echo $_SERVER["DOCUMENT_ROOT"];  // /home1/demonuts/public_html
//including the database connection file
   
      if($user_name == '' || $password == '' || $name == ''){
             echo json_encode(array( "status" => "false","result_code" => "Parameter missing!") );
      }else{         
                  $query = "SELECT * FROM account WHERE user_name = '$user_name' ";
                  $result = mysqli_query($conn,$query);
                  if(mysqli_num_rows($result)>0){
                  echo json_encode(array("status"=>'true',"result_code"=>"1"));
             }
            else{
                $query = "INSERT INTO account (user_name,password,name)
                VALUES ('$user_name', '$password', '$name')";
                      if(mysqli_query($conn,$query)){
                          echo json_encode(array( "status" => "true","result_code" => "0") );
                         
                       }else{
                        echo json_encode(array( "status" => "false",JSON_FORCE_OBJECT) );
                              
                      }
        
      }
}
  } else{
          
        mysqli_close($conn);
                }

?>