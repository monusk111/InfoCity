package com.example.infocity.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infocity.EntityDetails;
import com.example.infocity.R;
import com.example.infocity.models.ShopReferenceModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ShopReferenceAdapter extends RecyclerView.Adapter<ShopReferenceAdapter.ViewHolder> {
    private Context context;
    private List<ShopReferenceModel> rList;

    public ShopReferenceAdapter(Context context, List<ShopReferenceModel> rList) {
        this.context = context;
        this.rList = rList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_entity, parent, false);
        return new ShopReferenceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String seller_id = rList.get(position).getUser_id();
        String shop_id = rList.get(position).getShop_id();
        SharedPreferences sharedPreferences = context.getSharedPreferences("category",MODE_PRIVATE);
        String catetgory = sharedPreferences.getString("category","null");
        String collection_address = "users/"+seller_id + "/" +catetgory;

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(collection_address).document(shop_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        holder.setIv_image(documentSnapshot.getString("img_uri"));
                        holder.setDescription(documentSnapshot.getString("other"));
                        holder.setShop_name(documentSnapshot.getString("name"));

                        holder.card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, EntityDetails.class);
                                intent.putExtra("address",documentSnapshot.getString("address"));
                                intent.putExtra("latitude",documentSnapshot.getDouble("latitude"));
                                intent.putExtra("longitude",documentSnapshot.getDouble("longitude"));
                                intent.putExtra("name",documentSnapshot.getString("name"));
                                intent.putExtra("other",documentSnapshot.getString("other"));
                                intent.putExtra("phone",documentSnapshot.getString("phone"));
                                intent.putExtra("img_uri",documentSnapshot.getString("img_uri"));
                                context.startActivity(intent);
                            }
                        });

                    }
                });




    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_image;
        ImageButton ibt_call,ibt_message;
        TextView shop_name,description;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_image_single_entity);
            ibt_call = itemView.findViewById(R.id.ibt_call_single_entity);
            ibt_message = itemView.findViewById(R.id.ibt_text_single_entity);
            shop_name = itemView.findViewById(R.id.et_title_single_entity);
            description = itemView.findViewById(R.id.tv_info_single_entity);
            card = itemView.findViewById(R.id.card_single_entity);


        }

        public void setIv_image(String uri) {
            Picasso.get().load(uri).into(iv_image);
        }

        public void setShop_name(String local_shop_name) {
            shop_name.setText(local_shop_name);
        }

        public void setDescription(String local_description) {
            description.setText(local_description);
        }
    }
}
