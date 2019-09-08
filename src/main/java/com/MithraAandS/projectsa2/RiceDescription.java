package com.MithraAandS.projectsa2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class RiceDescription extends AppCompatActivity implements PaymentResultListener, LocationListener {
    private Toolbar RiceToolbar;
    private Spinner TypeSpinner,PolishSpinner;
    private String [] Polishlist={"Low","Medium","High"},Typelist={"Organic","Inorganic"};
    private String PolishString,TypeString,ProductName,Product1String,UsesString,Productdescription,downloadUri,thumbUri;
    private TextView riceProductname,riceUses,RicePricefield,locationText;
    private FirebaseFirestore firebaseFirestore;
    private ImageView RiceImg;
    private Button riceAddtoCart,nbuynow,locationbtn;
    private FirebaseAuth firebaseAuth;
    private String Productinfo,CartCount;
    private EditText customername,contactno,houseno,housename,colonyname,streetname,landmark,cityname,pincode;
    private Spinner nicknameSpinner;
    private Toolbar desaddtoolbar;
    private TextView nicknametext;
    private Button Buybtn;
    private String namestring,contactstring,housenameString,housenumberString,colonystring,StreetnameString;
    private String landstring,cityString,PincodeString,nicknameString,ProductId;
    private String[] nicklist={"Home","office","other"};
    private Map<String, Object> addressMap,cartMap,failMap;
    private float totalvalue;
    private  String currentUser,Ricecat,CusWeightString,categoryString;
    private  Dialog addressDialog,WeightDialog,FaildDialogbox;
    private  AlertDialog dialog;
    private ProgressBar mapprpgress;
    LocationManager locationManager;
    private double longitude,latitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rice_description);

        RiceToolbar=(Toolbar)findViewById(R.id.RiceMainbar);
        setSupportActionBar(RiceToolbar);
        getSupportActionBar().setTitle("Product Description");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        final String Postkey=getIntent().getExtras().getString("BlogPostId");
        final String frkey=getIntent().getExtras().getString("key");


        TypeSpinner = (Spinner) findViewById(R.id.Typespinner);
        PolishSpinner=(Spinner)findViewById(R.id.Polishspinner);
        riceProductname=(TextView) findViewById(R.id.RiceProductname);
        riceUses=(TextView)findViewById(R.id.riceproductUses);
        RicePricefield=(TextView)findViewById(R.id.RicePricefield);
        RiceImg=(ImageView)findViewById(R.id.RiceDescriptionImage);
        riceAddtoCart=(Button)findViewById(R.id.AddtoBasket);
        nbuynow=(Button)findViewById(R.id.RiceByNow);

        String loadsPosition = getIntent().getStringExtra("loadsPosition");






        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Typelist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        TypeSpinner.setAdapter(aa);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Polishlist);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        PolishSpinner.setAdapter(ab);



        TypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PolishString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        PolishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TypeString=adapterView.getSelectedView().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        DocumentReference docRef = firebaseFirestore.collection("Posts").document("Rice");
        docRef.collection("Product").document(Postkey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    try {


                        DocumentSnapshot doc = task.getResult();
                        ProductName = doc.getString("product_name");
                        riceProductname.setText(ProductName);
                        Product1String = doc.getString("price");
                        RicePricefield.setText(Product1String);
                        UsesString = doc.getString("UsesString");
                        riceUses.setText(UsesString);

                        Productdescription = doc.getString("Desc");

                        downloadUri = doc.getString("image_url");
                        thumbUri = doc.getString("image_thumb");
                        ProductId = doc.getString("Orderid");
                        categoryString=doc.getString("category");
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.default_image);

                        Glide.with(getApplicationContext()).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(Glide.with(getApplicationContext()).load(thumbUri)).into(RiceImg);


                    }catch (Exception e){

                    }
                }


            }
        });
       currentUser=firebaseAuth.getCurrentUser().getUid();
       dialog= new SpotsDialog.Builder().setContext(RiceDescription.this).build();
        final String randomName = UUID.randomUUID().toString();

        riceAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ProductName==null && Product1String==null && Productdescription==null && downloadUri==null && thumbUri==null
                && UsesString==null && PolishString==null && TypeString==null){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(RiceDescription.this);
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





                }else{

                    WeightDialog=new Dialog(RiceDescription.this);
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
                                cartMap.put("cartprice", Product1String);
                                cartMap.put("desc", Productdescription);
                                cartMap.put("image_url", downloadUri);
                                cartMap.put("image_thumb", thumbUri);
                                cartMap.put("timestamp", FieldValue.serverTimestamp());
                                cartMap.put("WeightString", "");
                                cartMap.put("aboutString", "");
                                cartMap.put("UsesString", UsesString);
                                cartMap.put("ProductInfo", "");
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

                                                Toast.makeText(RiceDescription.this, "Your basket is Full", Toast.LENGTH_SHORT).show();


                                            } else {


                                                firebaseFirestore.collection("Cart").document("Product").collection(currentUser).document(Postkey + randomName).set(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        startActivity(new Intent(RiceDescription.this, CartActivity.class));

                                                    }
                                                });

                                            }

                                        }

                                    }
                                });


                            }


                        }


                    });




                  /*  dialog.show();

                    Map<String, Object> cartMap = new HashMap<>();
                    cartMap.put("product_name",ProductName);
                    cartMap.put("cartprice",Product1String);
                    cartMap.put("desc",Productdescription);
                    cartMap.put("image_url",downloadUri);
                    cartMap.put("image_thumb",thumbUri);
                    cartMap.put("timestamp", FieldValue.serverTimestamp());
                    cartMap.put("quantity","");
                    cartMap.put("aboutString","");
                    cartMap.put("ProductInfo","");
                    cartMap.put("WeightString","");
                    cartMap.put("UsesString",UsesString);
                    cartMap.put("OrderId","");
                    cartMap.put("Polish",PolishString);
                    cartMap.put("Type",TypeString);

                    firebaseFirestore.collection("Cart").document("Product").collection(currentUser).document(Postkey).set(cartMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                            startActivity(new Intent(RiceDescription.this,CartActivity.class));

                        }
                    });


*/
                }



            }
        });

        nbuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addressDialog=new Dialog(RiceDescription.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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
                final EditText WeightStingText=addressDialog.findViewById(R.id.Weightnumber);
                ImageButton right=addressDialog.findViewById(R.id.Rightbtn);
                final TextView Total=addressDialog.findViewById(R.id.TotalPrice);
                final String[] weightstring = new String[1];
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
                        weightstring[0] =WeightStingText.getText().toString();
                        Float weightint=Float.valueOf(weightstring[0]),productpriceint=Float.valueOf(Product1String);

                        totalvalue = weightint * productpriceint;

                        Total.setText(String.valueOf(totalvalue));


                    }
                });



                nicknameSpinner = (Spinner)addressDialog.findViewById(R.id.NicknameSpinner);
                addressDialog.show();


                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(RiceDescription.this,android.R.layout.simple_spinner_item,nicklist);
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
                final AlertDialog dialog= new SpotsDialog.Builder().setContext(RiceDescription.this).build();

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(RiceDescription.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);



                }
                locationbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mapprpgress.setVisibility(View.VISIBLE);
                        getLocation();
                        Toast.makeText(RiceDescription.this, "iam in", Toast.LENGTH_SHORT).show();
                    }
                });



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
                                !TextUtils.isEmpty(cityString) && !TextUtils.isEmpty(PincodeString)) {

                            dialog.show();


                            addressMap = new HashMap<>();
                            addressMap.put("customername", namestring);
                            addressMap.put("type_of_order", "COD");
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
                            addressMap.put("price",Product1String);
                            addressMap.put("desc",Productdescription);
                            addressMap.put("image_url",downloadUri);
                            addressMap.put("image_thumb",thumbUri);





                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection(currentUser).add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(RiceDescription.this, "Your Order is Placed Sucessfully", Toast.LENGTH_SHORT).show();

                                }
                            });


                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("IjostVW1oAfaMKgSHFGGwJniQdJ2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RiceDescription.this, "Your Order is Placed to sathesh Successfully", Toast.LENGTH_SHORT).show();

                                }
                            });

                            firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("1aEcTGqXcgWaeO8TLJyMhULhQ633").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(RiceDescription.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

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



                        if (!TextUtils.isEmpty(namestring) && !TextUtils.isEmpty(contactstring) && !TextUtils.isEmpty(housenameString) && !TextUtils.isEmpty(housenumberString) && !TextUtils.isEmpty(colonystring) && !TextUtils.isEmpty(colonystring) && !TextUtils.isEmpty(StreetnameString) && !TextUtils.isEmpty(landstring) && !TextUtils.isEmpty(cityString) && !TextUtils.isEmpty(PincodeString)
                                && !TextUtils.isEmpty(String.valueOf(longitude)) &&!TextUtils.isEmpty(String.valueOf(latitude))) {
                            dialog.show();
                            addressMap = new HashMap<>();
                            addressMap.put("CustomerName", namestring);
                            addressMap.put("Contact", contactstring);
                            addressMap.put("Housename", housenameString);
                            addressMap.put("HouseNo", housenumberString);
                            addressMap.put("colonyname", colonystring);
                            addressMap.put("Streetname", StreetnameString);
                            addressMap.put("landmark", landstring);
                            addressMap.put("type_of_order", "Paid");
                            addressMap.put("cityname", cityString);
                            addressMap.put("pincode", PincodeString);
                            addressMap.put("nickname", nicknameString);
                            addressMap.put("Orderid", ProductId);
                            addressMap.put("adresstimestamp", FieldValue.serverTimestamp());
                            addressMap.put("product_name",ProductName);
                            addressMap.put("price",Product1String);
                            addressMap.put("desc",Productdescription);
                            addressMap.put("image_url",downloadUri);
                            addressMap.put("image_thumb",thumbUri);


                            startPayment(totalvalue);


                        }
                    }
                });










            }
        });









    }

    @Override
    public void onPaymentSuccess(String s) {

        Upload();

    }

    @Override
    public void onPaymentError(int i, String s) {
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
        failMap.put("price","");
        failMap.put("desc",Productdescription);
        failMap.put("image_url",downloadUri);
        failMap.put("image_thumb",thumbUri);
        failMap.put("weight", "");
        failMap.put("totalpay",String.valueOf(totalvalue));
        FaildDialogbox=new Dialog(RiceDescription.this);
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

    public void startPayment(Float payAmount) {
        /**
         * Instantiate Checkout
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
        final Activity activity = RiceDescription.this;

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
                Toast.makeText(RiceDescription.this, "Your Order is Placed to sathesh Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("1aEcTGqXcgWaeO8TLJyMhULhQ633").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RiceDescription.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("gey5LO3faNWCl5SVqaNT6bQYhGs1").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RiceDescription.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("YJqdm0qcfCbEhc3spXxRGOyP0jy2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RiceDescription.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(RiceDescription.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
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
