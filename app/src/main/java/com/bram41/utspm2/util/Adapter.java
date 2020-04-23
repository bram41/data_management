package com.bram41.utspm2.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bram41.utspm2.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Dosen> dosen;
    private Context context;
    private RecyclerViewClickListener mListener;

    // membuat method adapter
    public Adapter(List<Dosen> dosen, Context context, RecyclerViewClickListener listener) {
        this.dosen = dosen;
        this.context = context;
        this.mListener = listener;
    }

    // membuat method my view holder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.icon_dosen);
        requestOptions.error(R.drawable.icon_dosen);

        Glide.with(context)
                .load(dosen.get(position).getGambar())
                .apply(requestOptions)
                .into(holder.mGambar);

        if (dosen.get(position).getStatus().equals("1")){
            holder.mStatus.setText("Pegawai Tetap");
            holder.mStatus.setBackgroundResource(R.drawable.bg2);
        } else {
            holder.mStatus.setText("Pegawai Tidak Tetap");
            holder.mStatus.setBackgroundResource(R.drawable.bg1);
        }
        holder.mNama_dosen.setText(dosen.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return dosen.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener mListener;
        private CircleImageView mGambar;
        private TextView mNama_dosen, mStatus;
        private RelativeLayout mRowContainer;

        MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mGambar = itemView.findViewById(R.id.gambar);
            mNama_dosen = itemView.findViewById(R.id.nama_dosen);
            mStatus = itemView.findViewById(R.id.status);
            mRowContainer = itemView.findViewById(R.id.row_container);
            mListener = listener;
            mRowContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.row_container) {
                mListener.onRowClick(mRowContainer, getAdapterPosition());
            }
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }
}

