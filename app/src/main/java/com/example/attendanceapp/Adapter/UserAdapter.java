package com.example.attendanceapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.attendanceapp.R;
import com.example.attendanceapp.model.UsersList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyView>
{
    private List<UsersList> list;

    public UserAdapter(List<UsersList> list)
    {
        //Log.e("errorAdapter", String.valueOf(list));
        this.list=list;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list,parent,false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        UsersList usersList=list.get(position);
        holder.name.setText(usersList.getName());
        holder.email.setText(usersList.getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyView extends RecyclerView.ViewHolder
    {
        EditText name,email;
        public MyView(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.users_name);
            email=itemView.findViewById(R.id.users_email);
        }
    }
}
