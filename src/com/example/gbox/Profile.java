package com.example.gbox;


import com.example.gbox.addrope.AddRope;
import com.example.gbox.addrope.OnNFCdiscovered;
import com.example.gbox.addrope.Rope;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class Profile extends Fragment {
	

	//manager button
	Button manButton;
	
	//Index for start stop and update
	int i;

	SharedPreferences settings;
	
	FragmentCommunicator fragCommunicator;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	fragCommunicator = (FragmentCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		 //lookup in the savedInstance to see if there is a previous state
 		if(savedInstanceState != null)
 		i = savedInstanceState.getInt("index");
 		else i = 0; //initialize
		
	}
	 
	 @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		
         View rootView = inflater.inflate(R.layout.profile, container, false);
       
 		
 		//Start/Stop/Update button
 		 manButton = (Button) rootView.findViewById(R.id.manbutton);
 		
 		//visualize the state of the button -- every time you build or re-build this fragment
 		 
 		//need to update rope parameters -- here so every time the fragment is built you don't need to update the rope params while you update it when
 		boolean needToUpdate = false;
 		
 			stateOfButton();
 		
 		 
 		//User's name
  		TextView userName;
  		userName = (TextView) rootView.findViewById(R.id.name);
  		userName.setText("Gabriele Ermacora");
 		
 		//get data from settings activity from the selected rope
  		//-------------------------------------------------- code snippet
		/* editor.putInt("id", cn.getID());
		 editor.putString("name", cn.getName());
		 editor.putString("lastupdate", cn.getLastUpdate());
		 editor.putInt("usage", cn.getUsage());
		 
		 System.out.print( "Usage added" + cn.getUsage());
		 
		 editor.putFloat("maxforce", cn.getMaxForce());
		 editor.putInt("mnd", cn.getMND());*/
  		//----------------------------------------------------------------
  		
  		settings = this.getActivity().getSharedPreferences("TAPPED_ROPE", Context.MODE_PRIVATE);
 		
         int usage = settings.getInt("usage", 0);
         System.out.print( "Usage retrieved" + usage);
        
        //rope name text
        TextView ropeName = (TextView) rootView.findViewById(R.id.addrope);
        ropeName.setText(settings.getString("name", "null"));
        
 		//progress bar
 		ProgressBar progressBar;

 		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
 		
 		progressBar.setProgress(usage);
 		progressBar.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				Intent add_rope= new Intent(getActivity(), AddRope.class);
 				add_rope.setFlags(add_rope.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
 				Profile.this.startActivity(add_rope);
 			}
 		});

 		//On click listener for manager button
 		
 		manButton.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 			
 				 				
 				//trick to visualize the state of the button
 				
 				if(i==2)i = 0; //circluar buffer
 				else i++; //every time you touch the button  changes status
 				stateOfButton(); //state machine
 				
 			}
 		});
 		
         return rootView;
         
         
     }

	 void stateOfButton(){
		// TODO Auto-generated method stub
		 
			switch(i){ // state machine
			case 0 :
			//Start accelerometer monitoring -- but you are actually in the update status! 
				
			manButton.setText("Start");
			manButton.setBackgroundColor(manButton.getContext().getResources().getColor(R.color.red));
			
			break;
			case 1 :
			//Stop - you re in the start status so you have to trigger the monitoring
				
			manButton.setText("Stop");
			manButton.setBackgroundColor(manButton.getContext().getResources().getColor(R.color.green));
			Toast.makeText(manButton.getContext(), "Start monitoring..", Toast.LENGTH_SHORT).show(); //show this when in the status
			fragCommunicator.StartMonitoring();
			
//			SartMonitoring();
			
			break;
			case 2 :
			//Update- you're in the stop status 
			manButton.setText("Update");
			manButton.setBackgroundColor(manButton.getContext().getResources().getColor(R.color.yellow));	
			Toast.makeText(manButton.getContext(), "Stop", Toast.LENGTH_SHORT).show();  //show this when in the status
			fragCommunicator.StopMonitoring();
			
			//update rope parameters on internet
			//create the object for the request
			
			//class implementing http
			OnNFCdiscovered onDisc;
			
			//Rope(int id, String name, String lastUpdate, int usage, float MaxForce, int mnd)
			Rope jrope = new Rope(settings.getInt("id", 0), settings.getString("usage", "no name"), settings.getString("lastupdate", "no update"),
					settings.getInt("usage", 0), settings.getFloat("maxforce", 0), settings.getInt("mnd", 0));
			
			int ropeId = settings.getInt("id", 0);
			String fakeSeparated[] = {Integer.toString(ropeId),""};
			
	    	onDisc =  (OnNFCdiscovered) new OnNFCdiscovered(jrope,fakeSeparated, getActivity());
	    	onDisc.wMethod = "put";
	    	onDisc.execute();
			
			break;
			}
		
		 
		 
	 }
	 
	 

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		//saving the state of the button
		outState.putInt("index", i);
		
	}
	 
}
