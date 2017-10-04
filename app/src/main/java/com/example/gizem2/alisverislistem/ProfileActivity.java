package com.example.gizem2.alisverislistem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gizem2.alisverislistem.Base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
     //   displayUSerInfo(object);
    }
    public void displayUSerInfo(JSONObject object){
        String first_name,last_name,email,id;
        first_name="";
        last_name="";
        email="";
        id="";
        try {
            first_name=object.getString("first_name");
            last_name=object.getString("last_name");
            email=object.getString("email");
            id=object.getString("id");


        }catch (JSONException e){
            e.printStackTrace();
        }
        TextView TV_name,TV_mail,Tv_id;
        TV_name= (TextView) findViewById(R.id.TV_name);
        TV_mail= (TextView) findViewById(R.id.TV_email);
        Tv_id= (TextView) findViewById(R.id.TV_id);

        TV_name.setText(first_name+" "+last_name);
        TV_mail.setText("Email: "+email);
        Tv_id.setText("ID: "+id);
    }
}
