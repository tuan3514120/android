package com.example.crud_api;


import static android.util.Log.println;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud_api.adapter.Api_Adapter;
import com.example.crud_api.api.ApiClient;
import com.example.crud_api.api.Api_Interface;
import com.example.crud_api.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
    private RecyclerView rcv;
    private RecyclerView.LayoutManager layoutManager;
    private Api_Adapter adapter;
    private  Menu action;
    private List<Student> studentsList;
    private Api_Interface apiInterface;
    FloatingActionButton add_button;
    Api_Adapter.RecyclerViewClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Gọi API interface
        apiInterface = ApiClient.getApiClient().create(Api_Interface.class);
        rcv = findViewById(R.id.rcv);
        layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        listener = new Api_Adapter.RecyclerViewClickListener(){
            // Bắt sự kiện khi click vào mỗi sinh viên
            @Override
            public void onClickRow(View view,final int position) {
                // Gửi dữ liệu sang activity- Edit_API
                Intent intent = new Intent(MainActivity.this, Edit_API.class);
                intent.putExtra("id", studentsList.get(position).getId());
                intent.putExtra("name", studentsList.get(position).getName());
                intent.putExtra("student_code", studentsList.get(position).getStudent_code());
                intent.putExtra("grade", studentsList.get(position).getGrade());
                intent.putExtra("phone", studentsList.get(position).getPhone());
                intent.putExtra("address", studentsList.get(position).getAddress());
                intent.putExtra("date", studentsList.get(position).getDate());
                intent.putExtra("image", studentsList.get(position).getImage());
                startActivity(intent);
            }
        };
        add_button = findViewById(R.id.floating_action_button);

        add_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, Edit_API.class));
           }
       });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        searchView.setQueryHint("Tìm Kiếm Sinh Viên...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                this.finish();
                return true;
//            case R.id.help:
//                showHelp();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getStudent(){
//        Resquest List danh sách các sinh viên từ server
        Call<List<Student>> call = apiInterface.getStudent();
        call.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                studentsList = response.body();
                Log.i(MainActivity.class.getSimpleName(), response.body().toString());
                // set List vào adapter
                adapter = new Api_Adapter(studentsList, MainActivity.this,listener);
                rcv.setAdapter(adapter);
                Log.e("List",String.valueOf(studentsList));
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getStudent();
    }
}