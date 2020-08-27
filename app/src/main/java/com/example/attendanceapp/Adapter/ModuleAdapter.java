package com.example.attendanceapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.R;
import com.example.attendanceapp.model.Module;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.MyView>{
    private ItemClickEvent itemClickEvent;
    private List<Module> list;
    public ModuleAdapter(List<Module> list)
    {
        this.list=list;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item,parent,false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Module module=list.get(position);
        holder.im.setImageResource(module.getImage());
        holder.tname.setText(module.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyView extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView im;
        TextView tname;
        public MyView(@NonNull View itemView) {
            super(itemView);
            im=itemView.findViewById(R.id.myImage);
            tname=itemView.findViewById(R.id.myTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickEvent.clickOnItem(view,getAdapterPosition());
        }
    }

    public void setMyCustomListener(ItemClickEvent itemClickEvent)
    {
        this.itemClickEvent=itemClickEvent;
    }

    public interface ItemClickEvent
    {
        void clickOnItem(View view,int position);
    }
}
