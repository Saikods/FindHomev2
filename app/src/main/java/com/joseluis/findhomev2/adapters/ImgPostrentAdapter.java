package com.joseluis.findhomev2.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluis.findhomev2.R;

import java.util.ArrayList;
import java.util.List;

public class ImgPostrentAdapter extends RecyclerView.Adapter<ImgPostrentAdapter.Image> {

    private Context context;
    private int resource;
    private List<Uri> imgUris;

    public ImgPostrentAdapter(Context context, int resource, List<Uri> imgUris) {
        this.context = context;
        this.resource = resource;
        this.imgUris = imgUris;
    }


    @NonNull
    @Override
    public Image onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View image = LayoutInflater.from(context).inflate(resource, null);
        Image element = new Image(image);
        return element;
    }

    @Override
    public void onBindViewHolder(@NonNull final Image holder, final int position) {

        holder.img.setImageURI(imgUris.get(position));

        //CODIGO ( A MEDIAS) PARA ELIMINAR UNA FOTO DEL RECYCLER
//        holder.btnLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imgUris.remove(position);
//                holder.container.removeView(v);
//                notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return imgUris.size();
    }

    public class Image extends RecyclerView.ViewHolder {
        private ImageView img;
//        private Button btnLeft;
//        private LinearLayout container;

        public Image(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgRentPostrents);
//            btnLeft = itemView.findViewById(R.id.btnLeftImagePostrents);
//            container = itemView.findViewById(R.id.containerImg);


        }
    }
}
