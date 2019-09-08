package com.MithraAandS.projectsa2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import com.mikhaellopez.circularimageview.CircularImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;



import dmax.dialog.SpotsDialog;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>  {

    public List<BlogPost> blog_list,fraglist;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String CatKey,collectionKey;




    public BlogRecyclerAdapter(List<BlogPost> blog_list,List<BlogPost> fraglist){

        this.blog_list = blog_list;
        this.fraglist=fraglist;

    }




    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, final int position) {

        final String blogPostId = blog_list.get(position).BlogPostId;
        final String fragk = fraglist.get(position).fragkey;



        final String Admin = firebaseAuth.getCurrentUser().getUid();


        if (Admin.equals("o9O0P429OPPrVM5f8GzcYQ0nfNn1") || Admin.equals("1aEcTGqXcgWaeO8TLJyMhULhQ633") || Admin.equals("YJqdm0qcfCbEhc3spXxRGOyP0jy2")|| Admin.equals("7Olx1zllvFSbltT9ibKlNuU4TU72") ){

            holder.DeleteImage.setVisibility(View.VISIBLE);

        }




        final String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = blog_list.get(position).getImage_url();
        String thumbUri = blog_list.get(position).getImage_thumb();
        holder.setBlogImage(image_url, thumbUri);

        String ProductStringName=blog_list.get(position).getProduct_name();
        holder.setPost_nameString(ProductStringName);

        String QuantityString=blog_list.get(position).getQuantity();


        String ProductPriceString=blog_list.get(position).getPrice();
        holder.setPrice_String(ProductPriceString);











        if (fragk.equals("Home")){
            CatKey="organic";

        }else if (fragk.equals("Noti")){
            CatKey="In-organic";
        }else if(fragk.equals("Acc")){
            CatKey="Flowers";
        }else if (fragk.equals("Rice")){
            CatKey="Rice";
        }else if (fragk.equals("Cart")){
            CatKey="Cart";
        }else {
            CatKey="Fruits";
        }



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CatKey.equals("Rice")){
                    Intent DespIntent=new Intent(context,RiceDescription.class);
                    DespIntent.putExtra("BlogPostId",blogPostId);
                    DespIntent.putExtra("key",fragk);
                    context.startActivity(DespIntent);


                }else if (CatKey.equals("Cart")){

                    Intent DespIntent=new Intent(context,CartDescription.class);
                    DespIntent.putExtra("BlogPostId",blogPostId);
                    DespIntent.putExtra("key",fragk);
                    context.startActivity(DespIntent);
                }
                else{
                    Intent DespIntent=new Intent(context,DescriptionActivity.class);
                    DespIntent.putExtra("BlogPostId",blogPostId);
                    DespIntent.putExtra("key",fragk);
                    context.startActivity(DespIntent);


                }



            }

        });



       final AlertDialog dialog= new SpotsDialog.Builder().setContext(context).build();


        holder.DeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                if (CatKey.equals("Cart")){
                    firebaseFirestore.collection("Cart").document("Product").collection(firebaseAuth.getCurrentUser().getUid()).document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            try {
                                blog_list.remove(position);
                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                                dialog.dismiss();

                            }catch (Exception e){
                                Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                notifyItemRemoved(position);

                            }




                        }
                    });
                }

                firebaseFirestore.collection("Posts").document(CatKey).collection("Product").document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       try {
                           blog_list.remove(position);
                           notifyDataSetChanged();
                           notifyItemRemoved(position);
                           dialog.dismiss();

                       }catch (Exception e){
                           Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                           notifyDataSetChanged();
                           notifyItemRemoved(position);

                       }




                    }
                });
            }
        });




    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private ImageView blogUserImage;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;
        private TextView product_name;
        private TextView Price,totaltext;
        private TextView Quantity;
        private ImageView DeleteImage;
        private Button test;


        private ImageView blogCommentBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;


            descView=mView.findViewById(R.id.Main_Product_Description);
            DeleteImage=mView.findViewById(R.id.DeletePost);



        }


        public void setDescText(String desctext){

           // descView=mView.findViewById(R.id.Main_Product_Description);
            descView.setText(desctext);
        }


        public void setBlogImage(String downloadUri, String thumbUri){

            blogImageView = mView.findViewById(R.id.Main_Productimg);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.default_image);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);

        }

        public void setPost_nameString(String Productname){

            product_name=mView.findViewById(R.id.Main_Product_name);

            product_name.setText(Productname);


        }
        public void setPrice_String(String productPriceString){

            Price=mView.findViewById(R.id.Main_Product_Price);
            Price.setText(productPriceString);
        }

    }

}
