package com.example.gbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	// text
	TextView  Yourname;
	TextView  Yourweight;
	TextView  Yourimage;
		
	//input text
	EditText  editName;
	EditText  editWeight;
	EditText  editYourImage;
	
	//button
	Button register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_login);
		
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
				LoginActivity.this.startActivity(homeActivity);
				
			}
		});
		
	
		
		Yourname = (TextView) findViewById(R.id.namesurname);
		Yourname.setTextColor(0xFFFFFFFF); //white
		
		Yourweight = (TextView) findViewById(R.id.Weight);
		Yourweight.setTextColor(0xFFFFFFFF); //white
		
		Yourimage = (TextView) findViewById(R.id.image);
		Yourimage.setTextColor(0xFFFFFFFF); //white

		editName = (EditText) findViewById(R.id.editName);
		editWeight = (EditText) findViewById(R.id.EditWeight);
		editYourImage = (EditText) findViewById(R.id.EditImage);
		
		editName.setTextColor(0xFFFFFFFF);
		editWeight.setTextColor(0xFFFFFFFF);
		editYourImage.setTextColor(0xFFFFFFFF);
		
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	
}
