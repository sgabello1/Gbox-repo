package com.example.gbox;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class GboxJoinActivity extends Activity {


	private static final String  CHECK_BOX = "checkbox";
	Button join;
	CheckBox keepLogged;
	
	boolean isClicked;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 // Get the between instance stored values
		  SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_gbox_join);
		
		keepLogged = (CheckBox) findViewById(R.id.logged);
		
		// Restore value of members from saved state
		
		if(preferences.contains(CHECK_BOX)){
		isClicked = preferences.getBoolean(CHECK_BOX, false);
		keepLogged.setChecked(isClicked);
		}
		else
			isClicked = false; //or initialize
		
		keepLogged.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isClicked = !isClicked;
				
			}
		});
		
		
		join = (Button) findViewById(R.id.join);
	
		//check button
		join.setOnClickListener(new OnClickListener() {
		
			//checkbox is clicked?
			@Override
			public void onClick(View arg0) {
				if(isClicked){
				Intent login = new Intent(GboxJoinActivity.this, HomeActivity.class); //go straitght to home activity
				GboxJoinActivity.this.startActivity(login);
				}else{
					
					Intent login = new Intent(GboxJoinActivity.this, LoginActivity.class); //go to registartion form
					login.setFlags(login.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
					GboxJoinActivity.this.startActivity(login);
				}
			}
		});
		}	
			
		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();
			
			editor.putBoolean(CHECK_BOX, isClicked); // value to store    
			  // Commit to storage
			editor.commit(); 
		}

		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gbox_main, menu);
		return true;
	}

}
