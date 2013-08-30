package com.example.gbox;

import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.accchart.AccelData;
 
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
 



/**
 * @author mwho
 *
 */
@SuppressLint("NewApi")
public class HomeActivity extends FragmentActivity implements ActionBar.TabListener,SensorEventListener, FragmentCommunicator {

	//tabs 
	
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    ViewPager mViewPager;

    //interface for the communication
    
    public ActivityCommunicator activityCommunicator;
    
    private static final String  CHECK_BOX = "checkbox";
    
    //accelerometers stuff
    
    private SensorManager sensorManager;
    private Button  btnStart,btnStop, btnBarChart;
    private Button  btnSee;
    private boolean started = false;
    
    private ArrayList<AccelData> sensorData;
    private ArrayList<AccelData> lastData;
    private ArrayList<Double> ListCB;
    
    AccelData data;
    AccelData ac = null ;
    
    private LinearLayout layout;
    private View mChart;
    private Handler handler = new Handler();
    private Handler handler2 = new Handler();
    private int timeDelay = 1000;
    private int timeDelay2 = 30;
    private int BlackBoxTimer = 10; //30s
    int count = 0;
    float MaxAcc = 30; //3g
    boolean maxAccFlag = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //array di oggetti tipo AccelData
        sensorData = new ArrayList<AccelData>();
        ListCB = new ArrayList<Double>();
        
        setContentView(R.layout.activity_home);

        // Get the between instance stored values
		
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
  
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
 
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0: //profile activity
                                    	
                    return new Profile();

                case 1: //statistics activity
                	
                	return new Statistics();
                	                	
                case 2: //fb activity
                	
                	return new Fb();
                default://error zone
                	
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	
        //set tab titles
        	switch(position){
        	case 0 :
        		
        		return "Profile";
        	case 1 :
        		
        		return "Statistics ";
        	case 2 :
        		return "FB";
        	default :
        		
        		return "Auch! Somethings went really wrong?!";
        }
    }

}

//accelerometers!!!!!
	
	public void StartMonitoring() {
		// TODO Auto-generated method stub
         //set up accelerometer
        
		//set up accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accel = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel,
                SensorManager.SENSOR_DELAY_FASTEST);
		
		//you monitor the sensors
        runnable.run();
	}
	
	 //one runnable for refreshing datas
    private Runnable runnable = new Runnable() 
    {
    	
        public void run() 
        {
        
        	//se non hai dati da visualizzare non parti!
        	if ((sensorData != null || sensorData.size() > 0) ){
        		
	             long ts = 0;
	             double amx = 0;
	             double amy = 0;
	             double amz = 0;
	             
	             int size = sensorData.size();
	             double meanAcc = 0; // media identificativa del vettore sensor data
	             double[] mod = new double[size]; //modulo delle accelerazioni
	             
	             //per semplicità prendiamo il valore MEDIO in MODULO  di accelerazione del vettore sensor data 
	             for(int k = 0; k < size; k++){
	            
	            	ac = (AccelData) sensorData.get(k);
	            	
	            	mod[k]= (double) Math.sqrt(ac.getX()*ac.getX() + ac.getY()*ac.getY() + ac.getZ()*ac.getZ()); //modulo di ogni accelerazione
	            	meanAcc += mod[k];  //sommatoria delle accelerazioni
	             }
	             
	             meanAcc = meanAcc/size; //valore medio dei moduli delle accelerazioni
	             
//	             //time
//	             ts = (Long) ac.getTimestamp();
	             
	             //CircularBuffer è un elemento acceldata con tempo e accelerazioni MEDIE che serve quando vai a fare il bar chart
	             
	             System.out.printf("cosa ci butti nel bar chart?"+ meanAcc);
	             
	             //questo si riempe e col metodo getListCircularBuffer() prendi la lista di oggetti dal fragment statistics
	             
	             if(Math.sqrt(meanAcc*meanAcc) > SensorManager.GRAVITY_EARTH || Math.sqrt(meanAcc*meanAcc) < SensorManager.GRAVITY_EARTH/5) // salvi solo se l'acc è maggiore di un g
	             ListCB.add(meanAcc); // barchart data, continui ad aggiungere come se non ci fosse un domani
	             
	             
	             
//	             if( meanAcc > MaxAcc )
//	             {
//	            	 //ouch! you r over max acc so trigger 1 min of record and then an alert dialog
//	            	 Toast.makeText(getApplicationContext(), "You are over the Maximum Acceleration! Take care!", Toast.LENGTH_LONG).show();
//	            	 //flag of max acc
//	            	 maxAccFlag = true;
//	            	 //reset counter
//	            	 count = 0;
//	            	 btnSee.setEnabled(false);
//	             }
//	             
//	             //counter up to 60 for the wider buffer---> see last data
//	             
//	             if(count == BlackBoxTimer ){
//	            	 count = 0;
//	            	 //se hai oltrepassato max acc ha resettato il counter per registrare ancora l'ultimo buffer del black box
//	            	 //ora dovrebbe aver finito e parte con l'alert dialog
//	            	 if(maxAccFlag)
//	            	 {
//	            		 alertInfo();
//	            	 }
//	            	 btnSee.setEnabled(true);
//	             }
//	             else {
//	            	 //aggiungo i dati di accelerazione ogni volta per farti vedere cosa è successo in un certo intervallo di tempo
//	            	 lastData.add(ac);
//	            	 if(lastData.size() > BlackBoxTimer && maxAccFlag != true)lastData.remove(0);
//	            	 count++;
//	             }
	             
        		 sensorData.removeAll(sensorData);
        	}
             handler.postDelayed(this, timeDelay);
        }
    };
	
    protected void alertInfo() {
		 
	     started = false;
	   	 AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
		 miaAlert.setTitle("Abbiamo registrato un elevata accelerazione..");
		 miaAlert.setMessage("Sei ancora vivo?");
		 miaAlert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int id) {
				  DisplayLastGraph();

			  }
			});
		 AlertDialog alert = miaAlert.create();
		 alert.show();
	    	
		}
    
    public ArrayList<Double> getListCircularBuffer() {
    	
    	ArrayList<Double> ListCircularBuffer = ListCB;
    	
    	return ListCircularBuffer;
    }
    
	public void StopMonitoring() {
		
		//stop accelerometers
        sensorManager.unregisterListener(this);

        //call from here the fragment method for update the layout
        
        activityCommunicator.updateView();
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		 float x = event.values[0];
         float y = event.values[1];
         float z = event.values[2];
       
         long timestamp = System.currentTimeMillis();
         //ogni volta che l accelerometro si muove memorizza i valori x y z e tempo
         
         data = new AccelData(timestamp, x, y, z);
        
         //aggiungi l oggetto con tutti i valori alla lista
         
         sensorData.add(data);
	}
	
	private void DisplayLastGraph() {
		// TODO Auto-generated method stub
		
    	 if (lastData != null || lastData.size() > 0) {
        	 int size = lastData.size();
        	 System.out.print(size+"\n");
        	 //prendi dalla lista sensordata l'elemto 0 cio� il primo e invochi gettimestamp per prendere il tempo inziale cio� da dove parte
             long t = ((AccelData) lastData.get(0)).getTimestamp();
             
             //Oggetto per x y z
             XYMultipleSeriesDataset dataset2 = new XYMultipleSeriesDataset();
  
             //questo serve a visualizzare i nomi delle variabili(?)
             XYSeries xSeries = new XYSeries("X");
          
             
             //mi prendo tutti gli oggetti AccelData che ho salvato nella lista sensorData
             for (int i = 0; i< lastData.size() ; i++) {
            	 //prendo l oggetto e lo metto nell oggetto data
            	 AccelData data =  (AccelData) lastData.get(i);
            	  
            	 double value = (double) Math.sqrt(data.getX()*data.getX() + data.getY()*data.getY() + data.getZ()*data.getZ());
            	 
            	 //per ogni variabile da visualizzare ci metto nelle x il tempo dell'oggetto che sto aggiungendo(punto) - tempo assoluto 
                 xSeries.add(data.getTimestamp() - t, value);
                 
             }
  
             //
             dataset2.addSeries(xSeries);
             
  
             //renderizzi il grafico
             XYSeriesRenderer xRenderer = new XYSeriesRenderer();
             xRenderer.setColor(Color.RED);
             xRenderer.setPointStyle(PointStyle.CIRCLE);
             xRenderer.setFillPoints(true);
             xRenderer.setLineWidth(1);
             xRenderer.setDisplayChartValues(false);
  
             
  
             XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
             multiRenderer.setXLabels(0);
             multiRenderer.setLabelsColor(Color.RED);
             
             multiRenderer.setLabelsTextSize(20);
        	
             
             //aggiunge le scritte sulle x
             for (int i = 0; i < lastData.size(); i++) {
  
            	 //multiRenderer.addXTextLabel(i, "" +1 );
//                 multiRenderer.addXTextLabel(i + 1, "		"
//                         + (((AccelData) lastData.get(i)).getTimestamp()));
//                 multiRenderer.addXTextLabel(x, "x")
             }
             
             
             multiRenderer.addSeriesRenderer(xRenderer);
 
  
             // Getting a reference to LinearLayout of the MainActivity Layout
  
             // Creating a Line Chart
             
             Intent lineIntent2 = ChartFactory.getLineChartIntent(getApplicationContext(), dataset2, multiRenderer);
             startActivity(lineIntent2);
  
         }
    	 
	}
}