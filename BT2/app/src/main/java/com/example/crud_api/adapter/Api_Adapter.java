package com.example.crud_api.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.crud_api.R;
import com.example.crud_api.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Api_Adapter extends RecyclerView.Adapter<Api_Adapter.MyViewHolder> implements Filterable {
    List<Student> students,filterList;
    private Context context;
    public RecyclerViewClickListener mListener;
    // Khởi tạo Contrustor
public Api_Adapter(List<Student> students, Context context,RecyclerViewClickListener listener) {
    this.students = students;
    this.mListener = listener;
this.filterList = students;
    this.context = context;

}
// Khỏi tạo layout cho adapter
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view, mListener);
    }
    // Xây dựng dữ liệu
    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Resources res = context.getResources();
        holder.tv_name.setText(res.getString(R.string.name)+students.get(position).getName());
        holder.tv_student_code.setText(res.getString(R.string.student_code)+students.get(position).getStudent_code());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.images);
        requestOptions.error(R.drawable.images);
        Glide.with(context)
                .load(students.get(position).getImage())
                .apply(requestOptions)
                .into(holder.ImvStudent);

    }
    @Override
    public int getItemCount() {
        return students.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mListener;
        private ImageView ImvStudent;
        private TextView tv_name, tv_student_code;
    private RelativeLayout container;
        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ImvStudent = itemView.findViewById(R.id.imv_student);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_student_code = itemView.findViewById(R.id.tv_student_code);
            mListener = listener;
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(this);
        }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container:
                mListener.onClickRow(container, getAdapterPosition());
                break;
            default:
                break;
        }
    }
}
        public interface RecyclerViewClickListener {
            void onClickRow(View view, int position);
            }
    @Override
    // Tìm kiếm bằng serchView
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if(charSequence != null && charSequence.length() > 0)
                {
                    //CHANGE TO UPPER
                    charSequence=charSequence.toString().toUpperCase();
                    //STORE OUR FILTERED PLAYERS
                    ArrayList<Student> filteredPets=new ArrayList<>();
                    for (int i=0;i<filterList.size();i++)
                    {
                        //CHECK
                        if(filterList.get(i).getName().toUpperCase().contains(charSequence))
                        {
                            //ADD PLAYER TO FILTERED PLAYERS
                            filteredPets.add(filterList.get(i));
                        }
                    }
                    results.count=filteredPets.size();
                    results.values=filteredPets;
                }else
                {
                    results.count=filterList.size();
                    results.values=filterList;
                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                students= (ArrayList<Student>) filterResults.values;
                //REFRESH
               notifyDataSetChanged();
            }
        };
    }
        }



