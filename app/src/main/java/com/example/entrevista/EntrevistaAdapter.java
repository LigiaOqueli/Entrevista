package com.example.entrevista;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

public class EntrevistaAdapter extends RecyclerView.Adapter<EntrevistaAdapter.EntrevistaViewHolder> {

    private List<Entrevista> entrevistaList;
    private Context context;

    public EntrevistaAdapter(List<Entrevista> entrevistaList) {
        this.entrevistaList = entrevistaList;
    }

    @NonNull
    @Override
    public EntrevistaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_entrevista, parent, false);
        context = parent.getContext();
        return new EntrevistaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrevistaViewHolder holder, int position) {
        Entrevista entrevista = entrevistaList.get(position);
        holder.textViewDescripcion.setText(entrevista.getDescripcion());
        holder.textViewPeriodista.setText(entrevista.getPeriodista());

        Blob imagenBlob = entrevista.getImagenUrl();
        if (imagenBlob != null) {
            try {
                byte[] imagenBytes = imagenBlob.getBytes(1, (int) imagenBlob.length());
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                holder.imageViewEntrevistado.setImageBitmap(bitmap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return entrevistaList.size();
    }

    public class EntrevistaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewEntrevistado;
        TextView textViewDescripcion, textViewPeriodista;

        public EntrevistaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewEntrevistado = itemView.findViewById(R.id.imageViewEntrevistado);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewPeriodista = itemView.findViewById(R.id.textViewPeriodista);
        }
    }
}
