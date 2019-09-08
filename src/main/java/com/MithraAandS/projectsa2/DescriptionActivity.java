package com.MithraAandS.projectsa2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;


public class DescriptionActivity extends AppCompatActivity implements PaymentResultListener, LocationListener {
    private Toolbar newPostToolbar;
    private Button buynowbtn,addtocartbtn;
    private TextView Productname,Productdesc,ProductPrice,Pricefieldfor500,Priceper1kg;
    private TextView variableWeigthtText,AboutProductText,Uses,OtherProductInfoText;
    private FirebaseFirestore firebaseFirestore;
    private String CatKey,total="1",categoryString;
    private PhotoView Desimageview;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String ProductName,Productdescription,ProductPriceString,Product5String,Product1String,UsesString,downloadUri,thumbUri;
    private EditText customername,contactno,houseno,housename,colonyname,streetname,landmark,cityname,pincode;
    private Spinner nicknameSpinner;
    private TextView nicknametext,locationText;
    private Toolbar desaddtoolbar;
    private Button Buybtn,locationbtn;
    private String namestring,contactstring,housenameString,housenumberString,colonystring,StreetnameString,OtherProductString;
    private String landstring,cityString,PincodeString,nicknameString,ProductId,VariableweightString,AbourtProductString;
    private String[] nicklist={"Home","office","other"},weightList={"1","2","3","5","6","7","8","9","10"};
    private String pay=null;
    private boolean flag;
    private Dialog addressDialog,WeightDialog,FaildDialogbox;
    private AlertDialog dialog;
    private String randomName, CusWeightString;
    private String currentUser,CartCount;
    private Map<String, Object> addressMap,cartMap,failMap;
    private double totalvalue,longitude,latitude;
    private String weightstring;
    LocationManager locationManager;
    private ProgressBar mapprpgress;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        newPostToolbar = findViewById(R.id.Description_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Product Description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Checkout.preload(getApplicationContext());


        final String Postkey=getIntent().getExtras().getString("BlogPostId");
        final String frkey=getIntent().getExtras().getString("key");



        buynowbtn=(Button)findViewById(R.id.buy_now);
        addtocartbtn=(Button)findViewById(R.id.Addtocart);
        Productname=(TextView)findViewById(R.id.decpProductName);
        Productdesc=(TextView)findViewById(R.id.Desp_Desp);
        ProductPrice=(TextView)findViewById(R.id.Description_Price);
        Pricefieldfor500=(TextView)findViewById(R.id.Privefieldfor500);
        Priceper1kg=(TextView)findViewById(R.id.Priceper1kg);
        variableWeigthtText=(TextView)findViewById(R.id.variableWeigthtText);
        AboutProductText=(TextView)findViewById(R.id.AboutProductText);
        Uses=(TextView)findViewById(R.id.Uses);
        OtherProductInfoText=(TextView)findViewById(R.id.OtherProductInfoText);
        Desimageview=(PhotoView)findViewById(R.id.Des_img);
        TextView deliverytimmingsText=findViewById(R.id.Product_delivery_timmings);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        if (frkey.equals("Home")){
            CatKey="organic";

        }else if (frkey.equals("Noti")){
            CatKey="In-organic";
        }else if(frkey.equals("Acc")){
            CatKey="Flowers";
        }else if(frkey.equals("Rice")){
            CatKey="Rice";

        }else if (frkey.equals("Fruits")){
            CatKey="Fruits";
        }

        if (CatKey=="Rice" || CatKey=="Fruits" ){
            deliverytimmingsText.setText(R.string.Fruitsandrice);
        }



        DocumentReference docRef = firebaseFirestore.collection("Posts").document(CatKey);

        docRef.collection("Product").document(Postkey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                   ProductName=doc.getString("product_name");
                    Productname.setText(ProductName);
                     Productdescription=doc.getString("desc");
                    Productdesc.setText(Productdescription);
                  ProductPriceString=doc.getString("price");
                    Priceper1kg.setText(ProductPriceString);

                    Pricefieldfor500.setText(String.valueOf(Integer.valueOf(ProductPriceString)*(5)));

                    ProductPrice.setText(String.valueOf(Integer.valueOf(ProductPriceString)*(0.5)));
                      VariableweightString=doc.getString("WeightString");
                    variableWeigthtText.setText(VariableweightString);
                    AbourtProductString=doc.getString("aboutString");
                    AboutProductText.setText(AbourtProductString);
                     UsesString=doc.getString("UsesString");
                    Uses.setText(UsesString);
                     OtherProductString=doc.getString("ProductInfo");
                    OtherProductInfoText.setText(OtherProductString);
                    ProductId=doc.getString("Orderid");
                     downloadUri=doc.getString("image_url");
                     thumbUri=doc.getString("image_thumb");
                     categoryString=doc.getString("category");
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.default_image);

                    Glide.with(getApplicationContext()).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                            Glide.with(getApplicationContext()).load(thumbUri)
                    ).into(Desimageview);




                }


            }
        });

       currentUser=firebaseAuth.getCurrentUser().getUid();
         dialog= new SpotsDialog.Builder().setContext(DescriptionActivity.this).build();
       randomName = UUID.randomUUID().toString();

        addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ProductName ==null && ProductPriceString==null && Productdescription==null && downloadUri==null &&
                        VariableweightString==null && AbourtProductString==null  && AbourtProductString==null && UsesString==null
                        && OtherProductString==null && ProductId==null && CusWeightString==null )
                {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DescriptionActivity.this);
                    builder1.setMessage("Please wait until the Data is loaded, if it takes too long please close and open app agin and also do check your internet connectivity");
                    builder1.setCancelable(true);
                    builder1.setIcon(R.drawable.appicon);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();




                }else {

                    WeightDialog=new Dialog(DescriptionActivity.this);
                    WeightDialog.setContentView(R.layout.weight_dialogbox);

                    final EditText weightEdittext=(EditText)WeightDialog.findViewById(R.id.WeightGetter);
                    Button OkBtn=(Button)WeightDialog.findViewById(R.id.WeightOkbtn);

                    WeightDialog.show();



                    OkBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CusWeightString = weightEdittext.getText().toString();

                            if (!CusWeightString.isEmpty()) {
                                WeightDialog.dismiss();

                                dialog.show();

                                cartMap = new HashMap<>();
                                cartMap.put("product_name", ProductName);
                                cartMap.put("cartprice", ProductPriceString);
                                cartMap.put("desc", Productdescription);
                                cartMap.put("image_url", downloadUri);
                                cartMap.put("image_thumb", thumbUri);
                                cartMap.put("timestamp", FieldValue.serverTimestamp());
                                cartMap.put("WeightString", VariableweightString);
                                cartMap.put("aboutString", AbourtProductString);
                                cartMap.put("UsesString", UsesString);
                                cartMap.put("ProductInfo", OtherProductString);
                                cartMap.put("orderid", ProductId);
                                cartMap.put("category", categoryString);
                                cartMap.put("cusweightstring", CusWeightString);



                                final String randomName = UUID.randomUUID().toString();

                                firebaseFirestore.collection("Cart").document("Product").collection(currentUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            CartCount = String.valueOf(task.getResult().size());


                                            if (CartCount.equals("5")) {
                                                dialog.dismiss();

                                                Toast.makeText(DescriptionActivity.this, "Your basket is Full", Toast.LENGTH_SHORT).show();


                                            } else {


                                                firebaseFirestore.collection("Cart").document("Product").collection(currentUser).document(Postkey + randomName).set(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        startActivity(new Intent(DescriptionActivity.this, CartActivity.class));

                                                    }
                                                });

                                            }

                                        }

                                    }
                                });


                            }


                        }


                    });



                }



            }
        });

        buynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();




                addressDialog=new Dialog(DescriptionActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                addressDialog.setContentView(R.layout.addressdialogbox);



                customername=(EditText)addressDialog.findViewById(R.id.CustomerName);
                contactno=(EditText)addressDialog.findViewById(R.id.CustomerContact);
                houseno=(EditText)addressDialog.findViewById(R.id.HouseNO);
                housename=(EditText)addressDialog.findViewById(R.id.Housename);
                colonyname=(EditText) addressDialog.findViewById(R.id.ColonyName);
                streetname=(EditText)addressDialog.findViewById(R.id.Streetname);
                landmark=(EditText)addressDialog.findViewById(R.id.Landmark);
                cityname=(EditText)addressDialog.findViewById(R.id.Cityname);
                pincode=(EditText)addressDialog.findViewById(R.id.PinCode);
                Buybtn=(Button)addressDialog.findViewById(R.id.buybtn);
                final EditText WeightStingText=(EditText) addressDialog.findViewById(R.id.Weightnumber);
                ImageButton right=addressDialog.findViewById(R.id.Rightbtn);
                final TextView Total=addressDialog.findViewById(R.id.TotalPrice);
                final Button codbtn=addressDialog.findViewById(R.id.Cod);
                locationbtn=addressDialog.findViewById(R.id.locationbtn);
                locationText=addressDialog.findViewById(R.id.locationtxt);
                mapprpgress=addressDialog.findViewById(R.id.addresprogress);

                firebaseFirestore.collection("Users").document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                String name = task.getResult().getString("name");
                                String image = task.getResult().getString("image");
                                String cont = task.getResult().getString("contactnumber");
                                String thouse = task.getResult().getString("housename");
                                String thouseno = task.getResult().getString("houseno");
                                String tcolony = task.getResult().getString("colonyname");
                                String tland = task.getResult().getString("landmark");
                                String tcity = task.getResult().getString("cityname");
                                String tpin = task.getResult().getString("pin");
                                String tstreet = task.getResult().getString("street");

                                try {

                                    customername.setText(name);
                                    contactno.setText(cont);
                                    housename.setText(thouse);
                                    houseno.setText(thouseno);
                                    colonyname.setText(tcolony);
                                    landmark.setText(tland);
                                    cityname.setText(tcity);
                                    pincode.setText(tpin);
                                    streetname.setText(tstreet);


                                }catch (Exception e){

                                }
                            }
                        }

                    }
                });


                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!TextUtils.isEmpty(WeightStingText.getText().toString())) {


                            weightstring = WeightStingText.getText().toString();


                            Float weightint = Float.valueOf(weightstring), productpriceint = Float.valueOf(ProductPriceString);


                            if (Integer.valueOf(weightstring) >= 5) {

                                double discountval = productpriceint * 0.05;
                                double totaldisval = productpriceint - discountval;

                                totalvalue = weightint * totaldisval;


                            } else {

                                totalvalue = weightint * productpriceint;


                            }

                            Total.setText(String.valueOf(totalvalue));


                        }else {
                            WeightStingText.setError("fill this");
                        }
                    }
                });

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(DescriptionActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);



                }
                locationbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mapprpgress.setVisibility(View.VISIBLE);
                        getLocation();
                        Toast.makeText(DescriptionActivity.this, "iam in", Toast.LENGTH_SHORT).show();
                    }
                });












                nicknameSpinner = (Spinner)addressDialog.findViewById(R.id.NicknameSpinner);
                addressDialog.show();


                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(DescriptionActivity.this,android.R.layout.simple_spinner_item,nicklist);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                nicknameSpinner.setAdapter(aa);

                nicknameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        nicknameString=adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                final String currentUser=firebaseAuth.getCurrentUser().getUid();


                codbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        namestring = customername.getText().toString();
                        contactstring = contactno.getText().toString();
                        housenameString = housename.getText().toString();
                        housenumberString = houseno.getText().toString();
                        colonystring = colonyname.getText().toString();
                        StreetnameString = streetname.getText().toString();
                        landstring = landmark.getText().toString();
                        cityString = cityname.getText().toString();
                        PincodeString = pincode.getText().toString();



                        if (!TextUtils.isEmpty(namestring) && !TextUtils.isEmpty(contactstring) &&!TextUtils.isEmpty(housenameString)
                                &&!TextUtils.isEmpty(housenumberString) &&!TextUtils.isEmpty(colonystring)
                                &&!TextUtils.isEmpty(colonystring) && !TextUtils.isEmpty(StreetnameString) &&!TextUtils.isEmpty(landstring) &&
                                !TextUtils.isEmpty(cityString) && !TextUtils.isEmpty(PincodeString)&& totalvalue!=0) {

                            dialog.show();


                            addressMap = new HashMap<>();
                            addressMap.put("type_of_order", "COD");
                            addressMap.put("customername", namestring);
                            addressMap.put("phone_number", contactstring);
                            addressMap.put("housename", housenameString);
                            addressMap.put("houseno", housenumberString);
                            addressMap.put("colonyname", colonystring);
                            addressMap.put("streetname", StreetnameString);
                            addressMap.put("landmark", landstring);
                            addressMap.put("cityname", cityString);
                            addressMap.put("pincode", PincodeString);
                            addressMap.put("nickname", nicknameString);
                            addressMap.put("orderid", ProductId);
                            addressMap.put("adresstimestamp", FieldValue.serverTimestamp());
                            addressMap.put("product_name",ProductName);
                            addressMap.put("price",ProductPriceString);
                            addressMap.put("desc",Productdescription);
                            addressMap.put("image_url",downloadUri);
                            addressMap.put("image_thumb",thumbUri);
                            addressMap.put("weight", weightstring);
                            addressMap.put("category", categoryString);
                            addressMap.put("totalpay",totalvalue);
                            addressMap.put("latitude",String.valueOf(latitude));
                            addressMap.put("longitude",String.valueOf(longitude));





                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection(currentUser).add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(DescriptionActivity.this, "Your Order is Placed Sucessfully", Toast.LENGTH_SHORT).show();

                                }
                            });

                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("IjostVW1oAfaMKgSHFGGwJniQdJ2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(DescriptionActivity.this, "Your Order is Placed to sathesh Successfully", Toast.LENGTH_SHORT).show();

                                 }
                            });

                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("1aEcTGqXcgWaeO8TLJyMhULhQ633").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(DescriptionActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

                                }
                            });





                        }





                    }
                });


                Buybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        namestring = customername.getText().toString();
                        contactstring = contactno.getText().toString();
                        housenameString = housename.getText().toString();
                        housenumberString = houseno.getText().toString();
                        colonystring = colonyname.getText().toString();
                        StreetnameString = streetname.getText().toString();
                        landstring = landmark.getText().toString();
                        cityString = cityname.getText().toString();
                        PincodeString = pincode.getText().toString();



                        if (!TextUtils.isEmpty(namestring) && !TextUtils.isEmpty(contactstring) &&!TextUtils.isEmpty(housenameString)
                                &&!TextUtils.isEmpty(housenumberString) &&!TextUtils.isEmpty(colonystring)
                                &&!TextUtils.isEmpty(colonystring) && !TextUtils.isEmpty(StreetnameString) &&!TextUtils.isEmpty(landstring) &&
                                !TextUtils.isEmpty(cityString) && !TextUtils.isEmpty(PincodeString) && !TextUtils.isEmpty(String.valueOf(longitude)) &&!TextUtils.isEmpty(String.valueOf(latitude))) {

                            dialog.show();


                         addressMap = new HashMap<>();
                        addressMap.put("customername", namestring);
                        addressMap.put("type_of_order", "Paid");
                        addressMap.put("phone_number", contactstring);
                        addressMap.put("housename", housenameString);
                        addressMap.put("houseno", housenumberString);
                        addressMap.put("colonyname", colonystring);
                        addressMap.put("streetname", StreetnameString);
                        addressMap.put("landmark", landstring);
                        addressMap.put("cityname", cityString);
                        addressMap.put("pincode", PincodeString);
                        addressMap.put("nickname", nicknameString);
                        addressMap.put("orderid", ProductId);
                        addressMap.put("adresstimestamp", FieldValue.serverTimestamp());
                        addressMap.put("product_name",ProductName);
                        addressMap.put("price",ProductPriceString);
                        addressMap.put("desc",Productdescription);
                        addressMap.put("image_url",downloadUri);
                        addressMap.put("image_thumb",thumbUri);
                        addressMap.put("weight", weightstring);
                        addressMap.put("totalpay",String.valueOf(totalvalue));
                        addressMap.put("latitude",String.valueOf(latitude));
                        addressMap.put("longitude",String.valueOf(longitude));


                      startPayment((float) totalvalue);
                            dialog.dismiss();




                       }





                    }
                });










            }
        });








    }

    public void startPayment(Float payAmount) {
        /**
         * Instantiate Checkout
         *
         *
         */

        int delivery=15;

        int payInt=Math.round(payAmount)+delivery;


        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.sa);
        checkout.setFullScreenDisable(true);

        /**
         * Reference to current activity
         */
        final Activity activity = DescriptionActivity.this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Sa");

            /**
             * Description can be anything
             * eg: Order #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "SRPid12");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", payInt*100);

            checkout.open(activity, options);
        } catch(Exception e) {
            Toast.makeText(activity, "Error in starting Razorpay Checkout"+e, Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onPaymentSuccess(String s) {
       Toast.makeText(this, "Payment Success"+s, Toast.LENGTH_SHORT).show();
       Upload();
        dialog.dismiss();



    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Error"+i+s, Toast.LENGTH_SHORT).show();
        Log.d("My",i+s);

        failMap = new HashMap<>();
        failMap.put("customername", namestring);
        failMap.put("type_of_order", "Transcation Failed");
        failMap.put("phone_number", contactstring);
        failMap.put("housename", housenameString);
        failMap.put("houseno", housenumberString);
        failMap.put("colonyname", colonystring);
        failMap.put("streetname", StreetnameString);
        failMap.put("landmark", landstring);
        failMap.put("cityname", cityString);
        failMap.put("pincode", PincodeString);
        failMap.put("nickname", nicknameString);
        failMap.put("orderid", ProductId);
        failMap.put("adresstimestamp", FieldValue.serverTimestamp());
        failMap.put("product_name",ProductName);
        failMap.put("price",ProductPriceString);
        failMap.put("desc",Productdescription);
        failMap.put("image_url",downloadUri);
        failMap.put("image_thumb",thumbUri);
        failMap.put("weight", weightstring);
        failMap.put("totalpay",String.valueOf(totalvalue));
        failMap.put("latitude",String.valueOf(latitude));
        failMap.put("longitude",String.valueOf(longitude));
        FaildDialogbox=new Dialog(DescriptionActivity.this);
        FaildDialogbox.setContentView(R.layout.faildlist_dialog);



        dialog.dismiss();

        firebaseFirestore.collection("SreeOrders").document("FailOrder").collection("FailTransList").add(failMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {


                addressDialog.dismiss();
                FaildDialogbox.show();


            }
        });

    }

    public void Upload(){


        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection(currentUser).add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                dialog.dismiss();
                addressDialog.dismiss();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("IjostVW1oAfaMKgSHFGGwJniQdJ2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(DescriptionActivity.this, "Your Order is Placed to sathesh Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("1aEcTGqXcgWaeO8TLJyMhULhQ633").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(DescriptionActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("gey5LO3faNWCl5SVqaNT6bQYhGs1").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(DescriptionActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("YJqdm0qcfCbEhc3spXxRGOyP0jy2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(DescriptionActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });






    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
         latitude=location.getLatitude();
         longitude=location.getLongitude();
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
            mapprpgress.setVisibility(View.GONE);

           /* double lat = location.getLatitude(), lon = location.getLongitude();
            String la=String.valueOf(lat),lo=String.valueOf(lon);
            Uri gmmIntentUri = Uri.parse("google.navigation:q="+la+","+lo);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);*/
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(DescriptionActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


}
