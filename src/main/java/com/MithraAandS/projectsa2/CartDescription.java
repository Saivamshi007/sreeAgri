package com.MithraAandS.projectsa2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class CartDescription extends AppCompatActivity {

    private Toolbar newPostToolbar;
    private Button buynowbtn,addtocartbtn;
    private TextView Productname,Productdesc,ProductPrice,Pricefieldfor500,Priceper1kg;
    private TextView variableWeigthtText,AboutProductText,Uses,OtherProductInfoText,about,uses,other,variable,five,one;
    private FirebaseFirestore firebaseFirestore;
    private String CatKey,categorystring;
    private ImageView Desimageview;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String ProductName,Productdescription,ProductPriceString,Product5String,Product1String,UsesString,downloadUri,thumbUri;
    private EditText customername,contactno,houseno,housename,colonyname,streetname,landmark,cityname,pincode;
    private Spinner nicknameSpinner;
    private TextView nicknametext;
    private Toolbar desaddtoolbar;
    private Button Buybtn;
    private String namestring,contactstring,housenameString,housenumberString,colonystring,StreetnameString,OtherProductString;
    private String landstring,cityString,PincodeString,nicknameString,ProductId,VariableweightString,AbourtProductString;
    private String[] nicklist={"Home","office","other"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_description);

        newPostToolbar = findViewById(R.id.Description_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Cart Description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String Postkey=getIntent().getExtras().getString("BlogPostId");
        final String frkey=getIntent().getExtras().getString("key");
        final String over=getIntent().getExtras().getString("TotalPrice");





        buynowbtn=(Button)findViewById(R.id.buy_now);
        Productname=(TextView)findViewById(R.id.decpProductName);
        Productdesc=(TextView)findViewById(R.id.Desp_Desp);
        ProductPrice=(TextView)findViewById(R.id.Description_Price);
        Pricefieldfor500=(TextView)findViewById(R.id.Privefieldfor500);
        Priceper1kg=(TextView)findViewById(R.id.Priceper1kg);
        variableWeigthtText=(TextView)findViewById(R.id.variableWeigthtText);
        AboutProductText=(TextView)findViewById(R.id.AboutProductText);
        Uses=(TextView)findViewById(R.id.Uses);
        OtherProductInfoText=(TextView)findViewById(R.id.OtherProductInfoText);
        Desimageview=(ImageView)findViewById(R.id.Des_img);
        about=(TextView)findViewById(R.id.AbHead);
        uses=(TextView)findViewById(R.id.UseHead);
        other=(TextView)findViewById(R.id.OtherHead);
        variable=(TextView)findViewById(R.id.VariableHead);
        five=(TextView)findViewById(R.id.textView4);
        one=(TextView)findViewById(R.id.textView5);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String Current=firebaseAuth.getCurrentUser().getUid();








        DocumentReference docRef = firebaseFirestore.collection("Cart").document("Product");

        docRef.collection(Current).document(Postkey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    ProductName=doc.getString("product_name");
                    Productname.setText(ProductName);
                    Productdescription=doc.getString("desc");
                    Productdesc.setText(Productdescription);
                    ProductPriceString=doc.getString("cartprice");
                    ProductPrice.setText(ProductPriceString);

                    int PriceInt= Integer.parseInt(ProductPriceString);

                    int pricefor5kg=PriceInt*5;



                    Pricefieldfor500.setText(String.valueOf(pricefor5kg));

                    Priceper1kg.setText(String.valueOf((int) (PriceInt*(0.5))));
                    VariableweightString=doc.getString("WeightString");
                    variableWeigthtText.setText(VariableweightString);
                    AbourtProductString=doc.getString("aboutString");
                    AboutProductText.setText(AbourtProductString);
                    UsesString=doc.getString("UsesString");
                    Uses.setText(UsesString);
                    OtherProductString=doc.getString("ProductInfo");
                    OtherProductInfoText.setText(OtherProductString);
                    ProductId=doc.getString("OrderId");


                    categorystring=doc.getString("category");



                    downloadUri=doc.getString("image_url");
                    thumbUri=doc.getString("image_thumb");
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.default_image);

                    Glide.with(getApplicationContext()).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                            Glide.with(getApplicationContext()).load(thumbUri)
                    ).into(Desimageview);

                    OtherProductString=doc.getString("ProductInfo");

                    if (OtherProductString.equals("")){
                        about.setVisibility(View.GONE);
                        other.setVisibility(View.GONE);
                        variable.setVisibility(View.GONE);
                        five.setVisibility(View.GONE);
                        one.setVisibility(View.GONE);
                    }







                }


            }
        });

        final String currentUser=firebaseAuth.getCurrentUser().getUid();
        final AlertDialog dialog= new SpotsDialog.Builder().setContext(CartDescription.this).build();
        final String randomName = UUID.randomUUID().toString();


        buynowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog addressDialog=new Dialog(CartDescription.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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

                nicknameSpinner = (Spinner)addressDialog.findViewById(R.id.NicknameSpinner);
                addressDialog.show();


                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(CartDescription.this,android.R.layout.simple_spinner_item,nicklist);
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
                final AlertDialog dialog= new SpotsDialog.Builder().setContext(CartDescription.this).build();

                Buybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                        namestring=customername.getText().toString();
                        contactstring=contactno.getText().toString();
                        housenameString=housename.getText().toString();
                        housenumberString=houseno.getText().toString();
                        colonystring=colonyname.getText().toString();
                        StreetnameString=streetname.getText().toString();
                        landstring=landmark.getText().toString();
                        cityString=cityname.getText().toString();
                        PincodeString=pincode.getText().toString();

                        Map<String, Object> addressMap = new HashMap<>();
                        addressMap.put("CustomerName",namestring);
                        addressMap.put("Contact",contactstring);
                        addressMap.put("Housename",housenameString);
                        addressMap.put("HouseNo",housenumberString);
                        addressMap.put("colonyname",colonystring);
                        addressMap.put("Streetname",StreetnameString);
                        addressMap.put("landmark",landstring);
                        addressMap.put("cityname",cityString);
                        addressMap.put("pincode",PincodeString);
                        addressMap.put("nickname",nicknameString);
                        addressMap.put("orderid",ProductId);
                        addressMap.put("category",categorystring);
                        addressMap.put("adresstimestamp", FieldValue.serverTimestamp());


                        firebaseFirestore.collection("Orders").document("CustomerOrder").collection(currentUser).add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                dialog.dismiss();
                                addressDialog.dismiss();


                            }
                        });
                   }
                });

          }
        });


    }

}

