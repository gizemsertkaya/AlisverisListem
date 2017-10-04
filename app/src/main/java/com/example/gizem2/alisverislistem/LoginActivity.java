package com.example.gizem2.alisverislistem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gizem2.alisverislistem.Base.BaseActivity;
import com.example.gizem2.alisverislistem.Base.Kisi;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    private static final String TAG="LoginActivity";
    private CallbackManager callbackManager;
    private AccessToken facebookAccessToken;
    private LoginButton loginButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("user-friends");
//        getLoginDetails(loginButton);
        initializeFacebookLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            MainActivityegec();

        }

        kullaniciKontrol();




    }

    private void initializeFacebookLogin() {
        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions("email","public_profile","user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"facebook:onSuccess"+loginResult);
                facebookAccessToken=loginResult.getAccessToken();
                handleFacebookAccessToken(facebookAccessToken);
                kullaniciKontrol();

            }

            @Override
            public void onCancel() {
                Log.d(TAG,"facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"facebook:onError",error);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    public void handleFacebookAccessToken(AccessToken token){
        Log.d(TAG,"handleFacebookAccessToken: "+ token);
        AuthCredential credential=FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"signInWithCredential:success");
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    MainActivityegec();
                }else{
                    Log.w(TAG,"signInWithCredential:failure",task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void MainActivityegec(){
        Intent intent=new Intent(LoginActivity.this,MainListeActivity.class);
        startActivity(intent);
    }

    private void kullaniciKontrol() {
       // showProgressDialog("Oturum Açılıyor");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("uyeler");
            final Query query = myRef.child(user.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Kisi yeniUye = new Kisi();
                        yeniUye.setEmail(user.getEmail());
                        yeniUye.setId(user.getUid());
                        yeniUye.setAd(user.getDisplayName());
                        yeniUye.setFotograf(user.getPhotoUrl().toString());
                        myRef.child(user.getUid()).setValue(yeniUye);
                    }
                    query.removeEventListener(this);
                    Toast.makeText(LoginActivity.this, "Hoşgeldiniz", Toast.LENGTH_SHORT).show();
                    //hideProgressDialog();
                    //startActivity(new Intent(LoginActivity.this, ListActivity.class));
                   MainActivityegec();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}