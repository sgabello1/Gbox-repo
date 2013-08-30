package com.example.gbox.accelerometer.monitoring;

import java.io.Serializable;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;




public class BarChartActivity extends Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BarGraph bar;
	private View mBar;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_chart);
	    
	    Intent i = getIntent();
	    //ListCB =i.getSerializableExtra("ListCB");
	    bar = (BarGraph) i.getSerializableExtra("bar");
	
	    
	    
	    // TODO Auto-generated method stub
	    
	  //ad chart
        LinearLayout layout = (LinearLayout) findViewById(R.id.bar_chart1);
	    
        // Creating a Line Chart
        mBar = (View) bar.getView(getApplicationContext());
       

        // Adding the Line Chart to the LinearLayout
        layout.addView(mBar);
        
	}

}
