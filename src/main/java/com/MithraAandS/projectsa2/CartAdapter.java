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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;



import dmax.dialog.SpotsDialog;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>  {

    public List<BlogPost> blog_list,fraglist;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String CatKey,collectionKey;
    public static int counter1;
    public String[] cartorderids=new String[100];

    public void setListener(TotalListener listener) {
        this.listener = listener;
    }
    private TotalListener listener;
    public void setOrderIdWeight(OrderIdWeight orderIdWeight){
        this.orderIdWeight=orderIdWeight;
    }
    private OrderIdWeight orderIdWeight;




    public CartAdapter(List<BlogPost> blog_list,List<BlogPost> fraglist){

        this.blog_list = blog_list;
        this.fraglist=fraglist;

    }







    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_single_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, final int position) {

        final String blogPostId = blog_list.get(position).BlogPostId;
        final String fragk = fraglist.get(position).fragkey;




        final String Admin = firebaseAuth.getCurrentUser().getUid();



        final String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);



        String ProductStringName=blog_list.get(position).getProduct_name();
        holder.setPost_nameString(ProductStringName);

        String QuantityString=blog_list.get(position).getQuantity();

        String CusWeight=blog_list.get(position).getCusweightstring();
        holder.setTotal(CusWeight);






        String ProductPriceString=blog_list.get(position).getCartprice();
        holder.setPrice_String(ProductPriceString);
        counter1=0;


        int cusweightint=Integer.valueOf(CusWeight);
        double discount=0.05;


            try {

                    for (int i = 0; i < blog_list.size(); i++) {
                        if (Integer.valueOf(blog_list.get(i).getCusweightstring()) >= 5 && !blog_list.get(i).getCategory().equals("Rice")) {

                        //    Toast.makeText(context, blog_list.get(i).getCategory(), Toast.LENGTH_SHORT).show();
                            double gdis =(Float.valueOf(blog_list.get(i).getCartprice()) * discount);
                            double totalpriced = Integer.valueOf(blog_list.get(i).getCartprice()) - gdis;
                            counter1 = (int) (counter1 + (totalpriced * Float.valueOf(blog_list.get(i).getCusweightstring())));
                            cartorderids[i] = ("OrderId :" + blog_list.get(i).getOrderid() + "Weight : " + blog_list.get(i).getCusweightstring()
                                    +"Price : "+((Float.valueOf(blog_list.get(i).getCartprice())*Float.valueOf(blog_list.get(i).cusweightstring))));
                        }else {
                           // Toast.makeText(context, "raleee", Toast.LENGTH_SHORT).show();

                            counter1 = counter1 + (Integer.valueOf(blog_list.get(i).getCartprice()) * Integer.valueOf(blog_list.get(i).getCusweightstring()));
                            cartorderids[i] = ("OrderId :" + blog_list.get(i).getOrderid() + "Weight : " + blog_list.get(i).getCusweightstring());
                        }
                    }

                listener.onTotalChanged(counter1);
                orderIdWeight.onOrderIdWeight(cartorderids);


            }catch (Exception e){

                Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();

            }




          /*  try {

                for (int i = 0; i < blog_list.size(); i++) {
                    counter1 = counter1 + (Integer.valueOf(blog_list.get(i).getCartprice()) * Integer.valueOf(blog_list.get(i).getCusweightstring()));
                    cartorderids[i] = ("OrderId :" + blog_list.get(i).getOrderid() + "Weight : " + blog_list.get(i).getCusweightstring());
                }
                listener.onTotalChanged(counter1);
                orderIdWeight.onOrderIdWeight(cartorderids);


            } catch (Exception e) {

                Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();

            }
*/
















        final AlertDialog dialog= new SpotsDialog.Builder().setContext(context).build();


        holder.DeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

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

        private TextView product_name;
        private TextView Price,totaltext;
        private TextView Quantity;
        private ImageView DeleteImage;
        private Button test;


        private ImageView blogCommentBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;


            descView=mView.findViewById(R.id.Cart_Product_Description);
            DeleteImage=mView.findViewById(R.id.DeletePost);



        }
        public void setTotal(String total){

            totaltext=mView.findViewById(R.id.Total);
            totaltext.setText(total);


        }

        public void setDescText(String desctext){

            // descView=mView.findViewById(R.id.Main_Product_Description);
            descView.setText(desctext);
        }




        public void setPost_nameString(String Productname){

            product_name=mView.findViewById(R.id.Cart_Product_name);

            product_name.setText(Productname);


        }
        public void setPrice_String(String productPriceString){

            Price=mView.findViewById(R.id.Cart_Product_Price);
            Price.setText(productPriceString);
        }

    }

}
