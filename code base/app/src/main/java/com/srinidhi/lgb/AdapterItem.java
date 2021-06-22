package com.srinidhi.lgb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<dataUser> dataUserArrayList;
//    Locale id = new Locale("in","ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY");

    public AdapterItem(Context context, ArrayList<dataUser> dataUserArrayList) {
        this.context = context;
        this.dataUserArrayList = dataUserArrayList;
    }

    @NonNull
    @Override
    public AdapterItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemViewHolder holder, int position) {
        holder.viewBind(dataUserArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataUserArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,
                tv_cat,
                tv_text,
                tv_date,tv_end;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_nama);
            tv_cat= itemView.findViewById(R.id.tv_jk);
            tv_text = itemView.findViewById(R.id.tv_jurusan);
            tv_date = itemView.findViewById(R.id.tv_tanggal_pendaftaran);
            tv_end = itemView.findViewById(R.id.tv_enddate);
        }

        public void viewBind(dataUser dataUser) {
            tv_name.setText(dataUser.getName());
            tv_cat.setText(dataUser.getCat());
            tv_text.setText(dataUser.getReason());
            tv_date.setText(simpleDateFormat.format(dataUser.getStartDate()));
            tv_end.setText(simpleDateFormat.format(dataUser.getEndDate()));

        }
    }
}
