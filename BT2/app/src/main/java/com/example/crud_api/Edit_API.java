package com.example.crud_api;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crud_api.api.ApiClient;
import com.example.crud_api.api.Api_Interface;
import com.example.crud_api.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_API extends AppCompatActivity {
    private EditText edt_name, edt_student_code, edt_grade, edt_address,edt_phone, edt_date;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;
    Calendar myCalendar = Calendar.getInstance();
    private Bitmap bitmap;
    private String name, student_code, grade, address,phone, birth,picture;
    private int id;
    private Menu action;
    private Api_Interface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_api);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Khởi tạo các view
        edt_name = findViewById(R.id.ed_std_name);
        edt_student_code = findViewById(R.id.ed_std_code);
        edt_grade = findViewById(R.id.ed_std_grade);
        edt_address = findViewById(R.id.ed_std_address);
        edt_phone = findViewById(R.id.ed_std_phone);
        edt_date = findViewById(R.id.ed_std_date);
        mPicture = findViewById(R.id.picture);
        mFabChoosePic = findViewById(R.id.fabChoosePic);
        edt_date.setFocusableInTouchMode(false);
        edt_date.setFocusable(false);
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Edit_API.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        // Nhận dữ liệu thông qua intent từ MainAcitivy
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        student_code = intent.getStringExtra("student_code");
        grade = intent.getStringExtra("grade");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        birth = intent.getStringExtra("date");
        picture = intent.getStringExtra("image");
        setDataFromIntentExtra();
    }private void setDataFromIntentExtra() {
        if (id != 0) {
            // Nếu id khác 0 set các giá trị từ intent vào textView
            readMode();
            getSupportActionBar().setTitle("Edit " + name.toString());
            edt_name.setText(name);
            edt_student_code.setText(student_code);
            edt_grade.setText(grade);
            edt_address.setText(address);
            edt_phone.setText(phone);
            edt_date.setText(birth);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.images);
            requestOptions.error(R.drawable.images);
            Glide.with(Edit_API.this)
                    .load(picture)
                    .apply(requestOptions)
                    .into(mPicture);

        }
    }
    // Khởi tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        if (id == 0){
            action.findItem(R.id.menu_edit).setVisible(false);
            action.findItem(R.id.menu_delete).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_edit:
                //Edit
                editMode();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_name, InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_delete).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);
                return true;
            case R.id.menu_save:
                //Save
                if (id == 0) {
                    // Nếu như không nhập gì cả , hiển thị cho người dùng dòng thông báo
                    if (TextUtils.isEmpty(edt_name.getText().toString()) ||
                            TextUtils.isEmpty(edt_grade.getText().toString()) ||
                            TextUtils.isEmpty(edt_student_code.getText().toString()) ||
                            TextUtils.isEmpty(edt_phone.getText().toString()) ||
                            TextUtils.isEmpty(edt_address.getText().toString()) ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Please complete the field!");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        // Post dữ liệu vào host
                        postData("insert");
                        action.findItem(R.id.menu_edit).setVisible(true);
                        action.findItem(R.id.menu_save).setVisible(false);
                        action.findItem(R.id.menu_delete).setVisible(true);
                        readMode();

                    }
                } else {
                    // Cập Nhật dữ liệu
                    updateData("update", id);
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                    action.findItem(R.id.menu_delete).setVisible(true);
                    readMode();
                }

                return true;
            case R.id.menu_delete:
                // Xóa
                AlertDialog.Builder dialog = new AlertDialog.Builder(Edit_API.this);
                dialog.setMessage("Delete this pet?");
                dialog.setPositiveButton("Yes" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData("delete", id, picture);
                    }
                });
                dialog.setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setBirth();
        }
    };

    private void setBirth() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edt_date.setText(sdf.format(myCalendar.getTime()));
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        else {
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                mPicture.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
    );
// Chế độ đọc
    @SuppressLint("RestrictedApi")
    void readMode(){
        edt_name.setFocusableInTouchMode(false);
        edt_student_code.setFocusableInTouchMode(false);
        edt_grade.setFocusableInTouchMode(false);
        edt_address.setFocusable(false);
        edt_phone.setFocusable(false);
        edt_date.setEnabled(false);
        mFabChoosePic.setVisibility(View.INVISIBLE);

    }
    // Thêm dữ liệu
    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();
        readMode();
        String name = edt_name.getText().toString().trim();
        String student_code = edt_student_code.getText().toString().trim();
        String grade = edt_grade.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String phone = edt_phone.getText().toString().trim();
        String birth = edt_date.getText().toString().trim();
        String image = null;
        Log.i("bitmap",String.valueOf(bitmap));
        if (bitmap == null) {
            image = "";
        } else {
            image = getStringImage(bitmap);

        }
       // Gọi API_interface
        apiInterface = ApiClient.getApiClient().create(Api_Interface.class);
        // Gọi hàm call back đẩy dữ liệu lên host thông qua API_interface
        Call<Student> call = apiInterface.insertStudent( key, name, student_code, grade,address,phone, birth, image);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                progressDialog.dismiss();
                Log.i(Edit_API.class.getSimpleName(), response.toString());
                String value = response.body().getValue();
                String message = response.body().getMassage();
                if (value.equals("1")){
                    finish();
                } else {
                    Toast.makeText(Edit_API.this, message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Edit_API.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Cập Nhật Dữ Liệu
    private void updateData(final String key, final int id) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        readMode();
        String name = edt_name.getText().toString().trim();
        String student_code = edt_student_code.getText().toString().trim();
        String grade = edt_grade.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        String phone = edt_phone.getText().toString().trim();
        String birth = edt_date.getText().toString().trim();
        String image = null;
        Log.i("bitmap",String.valueOf(bitmap));
        if (bitmap == null) {
            image = "";
        } else {
            image = getStringImage(bitmap);
        }
        // Gọi API interface
        apiInterface = ApiClient.getApiClient().create(Api_Interface.class);
// Gọi Hàm Call Back đẩy dữ liệu lên host thông qua API interface
        Call<Student> call = apiInterface.update_student( key,id, name, student_code, grade,address, phone, birth, image);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                progressDialog.dismiss();
                Log.i(Edit_API.class.getSimpleName(), response.toString());
                String value = response.body().getValue();
                String message = response.body().getMassage();
                if (value.equals("1")){
                    finish();
                } else {
                    Toast.makeText(Edit_API.this, message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Edit_API.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
// Xóa dữ liệu
    private void deleteData(final String key, final int id, final String pic) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();
        readMode();
// Gọi API interface
        apiInterface = ApiClient.getApiClient().create(Api_Interface.class);
// Gọi hàm Call back đẩy dữ liệu lên host thông qua Api interface
        Call<Student> call = apiInterface.delete_Student(key, id, pic);
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                progressDialog.dismiss();
                Log.i(Edit_API.class.getSimpleName(), response.toString());
                String value = response.body().getValue();
                String message = response.body().getMassage();
                if (value.equals("1")){
                    Toast.makeText(Edit_API.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Edit_API.this, message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Edit_API.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("RestrictedApi")
    // Chế độ chỉnh sửa
    private void editMode(){
        edt_name.setFocusableInTouchMode(true);
        edt_grade.setFocusableInTouchMode(true);
        edt_address.setFocusableInTouchMode(true);
        edt_phone.setFocusableInTouchMode(true);
        edt_student_code.setFocusableInTouchMode(true);
        edt_date.setEnabled(true);
        mFabChoosePic.setVisibility(View.VISIBLE);
    }
}