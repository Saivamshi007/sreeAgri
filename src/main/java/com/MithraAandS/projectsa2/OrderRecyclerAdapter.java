package com.MithraAandS.projectsa2;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    private List<OrdersSetter> Order_List;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Context context;
    private String CurrentUser;
    private String onlydateString,onlytime;

    public OrderRecyclerAdapter(List<OrdersSetter> Order_List){
        this.Order_List=Order_List;

    }

    @NonNull
    @Override
    public OrderRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_single_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderRecyclerAdapter.ViewHolder holder, final int position) {

        CurrentUser=firebaseAuth.getCurrentUser().getUid();
        final String blogPostId = Order_List.get(position).BlogPostId;


        final String desc_data = Order_List.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = Order_List.get(position).getImage_url();
        String thumbUri = Order_List.get(position).getImage_thumb();
       // holder.setBlogImage(image_url, thumbUri);


        String ProductStringName=Order_List.get(position).getProduct_name();
        holder.setPost_nameString(ProductStringName);

        String ProductPriceString=Order_List.get(position).getPrice();
        holder.setPrice_String(ProductPriceString);

        final String AdressString=Order_List.get(position).getCityname();
        final String housename=Order_List.get(position).getHousename();
        final String houseNo=Order_List.get(position).getHouseno();
        final String phonenumber=Order_List.get(position).getPhone_number();
        final String customer=Order_List.get(position).getCustomername();
        final String colony=Order_List.get(position).getCityname();
        final String street=Order_List.get(position).getStreetname();
        final String land=Order_List.get(position).getLandmark();
        final String pin=Order_List.get(position).getPincode();
        final String nick=Order_List.get(position).getNickname();
        Date orderDate=Order_List.get(position).getAdresstimestamp();
        final String orderIdstring=Order_List.get(position).getOrderid();
        final String TypeofOrder=Order_List.get(position).getType_of_order();
        final String orderId=Order_List.get(position).getOrderid();
        String weightString=Order_List.get(position).getWeight();
        String TotalPayString=Order_List.get(position).getTotalpay();


        Date date=new Date();
        final String Onlycureentdate= (String) DateFormat.format("MM/dd/yyyy kk:mm:ss",date.getTime());
        final String Onlycureenttimeanddate= (String) DateFormat.format("MM/dd/yyyy",date.getTime());
        




    /*   if (onlydateString.compareTo(onlydateString)>0){
           Toast.makeText(context, "you can't", Toast.LENGTH_SHORT).show();
       }else{
           if (onlytime.compareTo(Onlycureentdate)>0){
               Toast.makeText(context, "your in timmer", Toast.LENGTH_SHORT).show();
           }
       }*/

        try {
            long millisecond=Order_List.get(position).getAdresstimestamp().getTime();
            onlydateString = DateFormat.format("MM/dd/yyyy kk:mm:ss", new Date(millisecond)).toString();
            onlytime = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();



            String TotalString=orderId+"\n"+customer+"\n"+phonenumber+"\n"+housename+"\n"+houseNo+"\n"+
                    colony+"\n"+street+"\n"+AdressString+"\n"+land+"\n"+pin+"\n"+nick+onlydateString+"\n"+"Type Of Order"+TypeofOrder+"\n"+"Weight : "+weightString+"\n"+"Total amount Paid :"+TotalPayString;
            holder.setAddress(TotalString);


        }catch (Exception e){

        }

        







        final AlertDialog dialog= new SpotsDialog.Builder().setContext(context).build();


        holder.Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                try {
                    Date date1 = simpleDateFormat.parse(onlydateString);
                    Date date2 = simpleDateFormat.parse(Onlycureentdate);
                    long different = date2.getTime() - date1.getTime();

                    if (onlytime.compareTo(Onlycureenttimeanddate)>0){
                        Toast.makeText(context, "you can't", Toast.LENGTH_SHORT).show();
                    }else {

                        if (!((int) (long)(different/(1000*60))>=30)){

                            dialog.show();

                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection(firebaseAuth.getCurrentUser().getUid()).document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    try {
                                        Order_List.remove(position);
                                        notifyDataSetChanged();
                                        notifyItemRemoved(position);
                                        dialog.dismiss();

                                    }catch (Exception e){
                                        Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                        notifyItemRemoved(position);

                                    }

                                    Map<String, Object> refundmap = new HashMap<>();
                                    refundmap.put("customername",customer);
                                    refundmap.put("orderid",orderIdstring);
                             firebaseFirestore.collection("SreeRefunds").document("refunds").collection(firebaseAuth.getCurrentUser().getUid()).add(refundmap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                 @Override
                                 public void onComplete(@NonNull Task<DocumentReference> task) {

                                     Toast.makeText(context, "Refund as been notified", Toast.LENGTH_SHORT).show();

                                 }
                             });





                                }
                            });


                        }
                    }

                  // Toast.makeText(context,String.valueOf( different/(1000*60)), Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return Order_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView ProductName,ProductPrice,ProductDescription,AddressText;
        private ImageView ProductImage,Deletebtn;
        private ImageView blogImageView;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            Deletebtn=mView.findViewById(R.id.Order_DeletePost);


        }

        public void setDescText(String desctext){

            ProductDescription=mView.findViewById(R.id.order_OrderLayoutName);
            ProductDescription.setText(desctext);
        }




        public void setPost_nameString(String Productname){

            ProductName=mView.findViewById(R.id.order_OrderLayoutName);

            ProductName.setText(Productname);


        }

        public void setPrice_String(String productPriceString){

            ProductPrice=mView.findViewById(R.id.OrderProductPrice);
            ProductPrice.setText(productPriceString);
        }
        public void setAddress(String address){
            AddressText=mView.findViewById(R.id.Address);
            AddressText.setText(address);
        }
        public void setBlogImage(String downloadUri, String thumbUri){

            blogImageView = mView.findViewById(R.id.Main_Productimg);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.default_image);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);

        }






    }


}
