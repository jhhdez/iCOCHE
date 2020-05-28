package com.example.icoche;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsItemsAdapter extends RecyclerView.Adapter<NewsItemsAdapter.NewsItemViewHolder>{


    private List<Menu> newMenu;

    NewsItemsAdapter(List<Menu> newMenu) {
        this.newMenu = newMenu;
    }

    @NonNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.items_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsItemViewHolder holder, int position) {
             holder.funcionalidad.setText(newMenu.get(position).getFuncionalidad());
             holder.descripcion.setText(newMenu.get(position).getDescripcion());

             if (newMenu.get(position).getImagen()== 0){
                 holder.imagen.setImageResource(R.mipmap.ic_launcher_round);
             }else{
                 holder.imagen.setImageResource(newMenu.get(position).getImagen());
             }
    }

    @Override
    public int getItemCount() {
        return newMenu.size();
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder{

        private TextView funcionalidad, descripcion;
        private ImageView imagen;
        public NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            funcionalidad=itemView.findViewById(R.id.Funcionalidad);
            descripcion=itemView.findViewById(R.id.Descripcion);
            imagen=itemView.findViewById(R.id.img);



        }


    }
}
