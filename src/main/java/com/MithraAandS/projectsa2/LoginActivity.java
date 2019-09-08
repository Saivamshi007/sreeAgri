package com.MithraAandS.projectsa2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
  private   EditText LoginText,Passwaord;
  private   Button loginbtn;
  private Button newbtn;
  private FirebaseAuth mAuth;
  private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkConnection();

        mAuth=FirebaseAuth.getInstance();


        LoginText=(EditText)findViewById(R.id.UserNamefield);
        Passwaord=(EditText)findViewById(R.id.PasswordField);
        loginbtn=(Button)findViewById(R.id.LogIn);
        newbtn=(Button)findViewById(R.id.regAcc);
        loginProgress=(ProgressBar)findViewById(R.id.LoginBar);

        newbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(reg_intent);

            }
        });

        findViewById(R.id.forgetText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,ResetPassword.class));

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserNameText=LoginText.getText().toString();
                String PasswordText=Passwaord .getText().toString();

                if (!TextUtils.isEmpty(UserNameText) && !TextUtils.isEmpty(PasswordText)){
                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(UserNameText,PasswordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                sendToMain();

                            }else {
                                String errormessage=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error"+errormessage, Toast.LENGTH_SHORT).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);

                        }

                    });

                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();

        FirebaseUser currentuser= mAuth.getCurrentUser();

        if (currentuser!=null){

            sendToMain();

        }

    }

    private void sendToMain() {
        Intent MainIntent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(MainIntent);
        finish();
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){
            Toast.makeText(LoginActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            final Dialog addressDialog=new Dialog(LoginActivity.this);
            addressDialog.setContentView(R.layout.nointernet);
            addressDialog.show();

            Button exit=(Button)addressDialog.findViewById(R.id.exitbtn);

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addressDialog.dismiss();
                }
            });

        }
    }
}
