package com.example.crud_api;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.crud_api.adapter.Api_Adapter;
import com.example.crud_api.api.ApiClient;
import com.example.crud_api.api.Api_Interface;
import com.example.crud_api.model.Account;
import com.example.crud_api.model.Student;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    CoordinatorLayout layout;
    private Api_Interface apiInterface;
    private EditText user_name,password,user;
    private String muser_name,mpassword,muser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo các View
        getSupportActionBar().setTitle("Đăng ký tài khoản " );
        setContentView(R.layout.activity_register);
        user_name = findViewById(R.id.edt_user_name);
        password = findViewById(R.id.edt_password);
        user = findViewById(R.id.edt_user);
        Button btn_register = findViewById(R.id.btn_register);
        Button btn_login = findViewById(R.id.btn_login_from_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(user_name.getText().toString()) ||
                        TextUtils.isEmpty(password.getText().toString()) ||
                        TextUtils.isEmpty(user.getText().toString())
                         ){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialog.setMessage("Xin hãy điền đầy đủ thông tin");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
                else {
                    muser_name = user_name.getText().toString().trim();
                    mpassword = password.getText().toString().trim();
                    muser = user.getText().toString().trim();
                    Register("register", muser_name, mpassword, muser);
                }
            }
            public void Register(String key,String user,String pass,String name){
// Gọi Api interface
                apiInterface = ApiClient.getApiClient().create(Api_Interface.class);
                // Gọi hàm call back
                Call<Account> call = apiInterface.register(key,user,pass,name);
call.enqueue(new Callback<Account>() {
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onResponse(Call<Account> call, Response<Account> response) {
        Log.i(RegisterActivity.class.getSimpleName(), response.toString());
        String status = response.body().getStatus();
        String result_code = response.body().getResult_code();
        if(status.equals("true") && result_code.equals("0")){
           layout = findViewById(R.id.layout_id);
            Snackbar snackbar = Snackbar
                    .make(layout, "Đăng ký thành công", Snackbar.LENGTH_LONG);
            snackbar.show();

            InputMethodManager imm = (InputMethodManager)RegisterActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
           // Xây dựng hàm đợi (delay)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);                        startActivity(i);
                }
            },1000);
        } else {
            Toast.makeText(RegisterActivity.this, "Tài khoản đã được sử dụng ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onFailure(Call<Account> call, Throwable t) {

    }
});
            }

        });

    }

}



