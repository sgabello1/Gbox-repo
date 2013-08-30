package com.example.gbox;





import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.example.gbox.accelerometer.monitoring.BarGraph;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class Statistics extends Fragment implements ActivityCommunicator {

		Context cntx = getActivity();
		
		//SQL Database objects
	 	
		DatabaseStoryboards db;
	
	 	ListView listview;
	 	ArrayList<String> list;
	 
	 	//story
		Story story = new Story();
	 
		//rootview for refreshing the screen
	 	View rootView2;
	 	
	 	//circular buffer coming from the activity
	 	ArrayList<Double> ListCB ;
	 	
	 	//bar graph for charts/wholer/ find out max acc min acc dumps and so on.. 
	 	BarGraph bar ;
	 	
	 	//interaface for communication between frag and activity
		FragmentCommunicator fragCommunicator;
		View view; 

		//some text fileds
		TextView accmin;
		TextView accmax;
		TextView dumps;
		
		//button save
		
		ImageButton saveButton;
		
		//to link to results arrays
		
		int dump;
		int maxacc;
		int minacc;
		
		@Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	        // This makes sure that the container activity has implemented
	        // the callback interface. If not, it throws an exception
	        try {
	           
	        	Context context = getActivity();
	        	fragCommunicator = (FragmentCommunicator) activity;
	        	((HomeActivity)context).activityCommunicator = this;
	        	
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnHeadlineSelectedListener");
	        }
	    }

	 
	
public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
		
		
		View rootView = inflater.inflate(R.layout.statistics, container, false);
		
		 rootView2 = (View) rootView;
		
		 accmin = (TextView) rootView.findViewById(R.id.minaccvalue);
		 accmax = (TextView) rootView.findViewById(R.id.maxaccvalue);
		 dumps  = (TextView) rootView.findViewById(R.id.dumpsvalue);
		 
		 accmin.setText("--");
		 accmax.setText("--");
		 dumps.setText("--");
		 
		 	//visualize rope internal DB
		 
		 	listview = (ListView) rootView.findViewById(R.id.listView1);
		 
		    list = new ArrayList<String>();	

		    // Adapter
		    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		            android.R.layout.simple_list_item_1, list);
		        listview.setAdapter(adapter);
		        
		     // Click on item -> display info of this item   
		        
		        OnItemClickListener clickListener = new OnItemClickListener() {

		        	@Override
					public void onItemClick(AdapterView<?> adapter, View view, int position,
							long id) {
						// TODO Auto-generated method stub
		        		
		        		DatabaseStoryboards db = new DatabaseStoryboards(getActivity());

		        		 Story cn = db.getStory(position + 1);
		        		 Toast.makeText(getActivity(), "Id " + cn.getID() + "Date: " + cn.getDate() + " ,Dumps: " + cn.getDumps() 
		        				 + " ,Max acc: " + cn.getMaxAcc() + " ,Min acc: " + cn.getMinAcc(), Toast.LENGTH_SHORT).show();
					}
		        };
		        listview.setOnItemClickListener(clickListener);
		 
		        //save button
		        
		        saveButton = (ImageButton) rootView.findViewById(R.id.savebutton);
		        saveButton.setOnClickListener(new OnClickListener() {
					
					@SuppressLint("SimpleDateFormat")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Calendar calend = Calendar.getInstance();
						
						SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
						String formattedDate = df.format(calend.getTime());
						
						Story newStory = new Story(formattedDate,dump, maxacc, minacc);
						db.addStory(newStory);
						
						db = new DatabaseStoryboards(getActivity());

							list.removeAll(list);
							
						// Reading all Ropes
							List<Story> story = db.getAllStory();       

						// Display on a listview and update
							for (Story cn : story) {
								
							String values =  cn.getDate() + "          "+ cn.getDumps();
							
							list.add(values);    
						
							listview.setAdapter(adapter);

					}
					}
		        	});
		
		        RelativeLayout lei =  (RelativeLayout) rootView.findViewById(R.id.fortext);
		        
		        lei.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					
						// it might mean that if there are y[] data to visualize you can build the graph-chart
						if(bar != null)
						{
							//method for build the chart
							Intent chart =bar.getIntentChart(getActivity());
							startActivity(chart);
						}
						
						
					}
				});
		        
		return rootView;

	    
}

@Override
public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
}

@Override
public void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
}

@Override
public void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	

}

@Override
public void onActivityCreated(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onActivityCreated(savedInstanceState);

	// db create when you've created the activty

	db = new DatabaseStoryboards(getActivity());

	// Reading all Ropes
		List<Story> story = db.getAllStory();       

	// Display on a listview
		for (Story cn : story) {
			
		String values =  cn.getDate() + "          "+ cn.getDumps();
		
		list.add(values);    
}

}

@Override
public  void updateView() {
	// TODO Auto-generated method stub

	ListCB = fragCommunicator.getListCircularBuffer();
	
	bar = new BarGraph(ListCB);
	
	int[] results = bar.getResults();
	
	dump = results[0];
	minacc = results[1];
	maxacc = results[2];
	//results
	accmin.setText("" +minacc);
	accmax.setText("" + maxacc);
	dumps.setText("" + dump);
	        
}

}