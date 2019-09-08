package com.MithraAandS.projectsa2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DescriptionAdderss extends AppCompatActivity {

    private EditText customername,contactno,houseno,housename,colonyname,streetname,landmark,cityname,pincode;
    private Spinner nicknameSpinner;
    private TextView nicknametext;
    private Toolbar desaddtoolbar;
    private Button Buybtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String namestring,contactstring,housenameString,housenumberString,colonystring,StreetnameString;
    private String landstring,cityString,PincodeString,nicknameString;
    private String[] nicklist={"Home","office","other"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_adderss);
        desaddtoolbar=(Toolbar)findViewById(R.id.Descriptionaddress_toolbar);
        setSupportActionBar(desaddtoolbar);
        getSupportActionBar().setTitle("Product Description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        customername=(EditText)findViewById(R.id.CustomerName);
        contactno=(EditText)findViewById(R.id.CustomerContact);
        houseno=(EditText)findViewById(R.id.HouseNO);
        housename=(EditText)findViewById(R.id.Housename);
        colonyname=(EditText)findViewById(R.id.ColonyName);
        streetname=(EditText)findViewById(R.id.Streetname);
        landmark=(EditText)findViewById(R.id.Landmark);
        cityname=(EditText)findViewById(R.id.Cityname);
        pincode=(EditText)findViewById(R.id.PinCode);
        Buybtn=(Button)findViewById(R.id.buybtn);

       nicknameSpinner = (Spinner) findViewById(R.id.NicknameSpinner);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,nicklist);
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
        final AlertDialog dialog= new SpotsDialog.Builder().setContext(DescriptionAdderss.this).build();

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
                addressMap.put("colonyname",colonyname);
                addressMap.put("Streetname",StreetnameString);
                addressMap.put("landmark",landstring);
                addressMap.put("cityname",cityString);
                addressMap.put("pincode",PincodeString);
                addressMap.put("nickname",nicknameString);

                firebaseFirestore.collection("Orders").document("CustomerOrder").collection(currentUser).add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });





            }
        });







    }
}
