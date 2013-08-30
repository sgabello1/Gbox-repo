
package com.example.gbox.accelerometer.monitoring;


import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;



public class BarGraph {

	
	final int dumpthreshold = 15; 
	
	/**
	 * 
	 */
	private ArrayList<Double> lcb ; 
	int size = 0;
	int[] y; // acceleration array for wholer and so on 
	int[] yy; //array son of y but just for visualization

	
public BarGraph(ArrayList<Double> listCB) {
	// TODO Auto-generated constructor stub
	
	this.lcb = listCB;
	this.size = lcb.size();
	this.y =new int[size];
	this.yy =new int[size];
}

public int[] getResults()
{

	//results[0] is dumps results[1] is accmin results[2] is accmax
	
	int[] results = {0,0,0};
	int dumps = 0;
	
	for(int i = 0; i < size; i++){
		
		double val = lcb.get(i);	
		 y[i]= (int) val; // convert array from double to int--> easier to compute accelerations
		
		 //dumps : if the acceleration is > than 15  is a dump!
		 
		if(y[i] > dumpthreshold) dumps++;
		
		//copy acc array into visualization array
		
		yy[i] = y[i];
		
		 System.out.printf("y[i]",+ y[i]);
		}
		
		Arrays.sort(y); //ascending order
		
		//first one is the min
		if(size > 2){
		results[1] = y[0];
	
		//the last is the max
		
		results[2] = y[size-1];
		
		//dumps
		
		results[0] = dumps;
		}
		
	return results;
	
		
	
}
	public View getView(Context context) {
		// TODO Auto-generated method stub
	
		for(int i = 0; i < size; i++){
			
		double val = lcb.get(i);	
		 y[i]= (int) val;
		
		 System.out.printf("y[i]",+ y[i]);
		}
		
		
		Arrays.sort(y); //ascending order
				
		//array per il diagramma di wholer
		int[] wholer = new int[20];
		
		for(int j = 0; j < 20; j++) wholer[j] = 0; //inizializzo il vettore wholer
		
		int c = 0;
		
		for(int i = 0; i <20;i++){ // per trovare quante volte hai la stessa velocità
			c = 0;
			while( c  != -1){
			
			 // se non trovi i c � -1 quindi esci	
			 //c = y.toString().indexOf(i);
				c = TrovaIndice(i);
			 if(c!=-1){
				 
			 wholer[i]++; //agg +1 quando lo trovi
			 y[c] = 21; // tolgo l elemento una volta trovato
		};
			 
		}
		}
		
		
		CategorySeries series = new CategorySeries("Demo Bar Graph 1");
		for (int i = 0; i < wholer.length; i++) {
			series.add("Bar " + (i+1), wholer[i]);
			 System.out.printf("y[i]",+ wholer[i]);
		}
		

		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series.toXYSeries());
//		dataset.addSeries(series2.toXYSeries());

		// This is how the "Graph" itself will look like
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.setChartTitle("Demo Graph Title");
		mRenderer.setXTitle("Time");
		mRenderer.setYTitle("Y VALUES");
		mRenderer.setAxesColor(Color.GREEN);
	    mRenderer.setLabelsColor(Color.RED);
	    // Customize bar 1
		XYSeriesRenderer renderer = new XYSeriesRenderer();
	    renderer.setDisplayChartValues(true);
	    renderer.setChartValuesSpacing((float) 0.5);
	    mRenderer.addSeriesRenderer(renderer);

		
		//Intent intent = ChartFactory.getBarChartIntent(context, dataset,mRenderer, Type.DEFAULT);
	    View view = ChartFactory.getBarChartView(context, dataset, mRenderer, Type.DEFAULT);
	
		//return intent;
	    return view;
	}





	private int TrovaIndice( int i) {
		// TODO Auto-generated method stub
		
		for(int c = 0; c < y.length ; c++)
			if(y[c] == i)
				return c;
		
				return -1;
		
	}
public Intent getIntentChart(Context context){
	
	
	CategorySeries series = new CategorySeries("Acceleration story");
	for (int i = 0; i < yy.length; i++) {
		series.add("Bar " + (i+1), yy[i]);
		 System.out.printf("y[i]",+ yy[i]);
	}

	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	dataset.addSeries(series.toXYSeries());

	// This is how the "Graph" itself will look like
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	mRenderer.setChartTitle("Acceleration story");
	mRenderer.setXTitle("Time");
	mRenderer.setYTitle("Y VALUES");
	mRenderer.setAxesColor(Color.GREEN);
    mRenderer.setLabelsColor(Color.RED);
    // Customize bar 1
	XYSeriesRenderer renderer = new XYSeriesRenderer();
    renderer.setDisplayChartValues(true);
    renderer.setChartValuesSpacing((float) 0.5);
    mRenderer.addSeriesRenderer(renderer);

	
	Intent intent = ChartFactory.getLineChartIntent(context, dataset,mRenderer);

	return intent;
	
}

//this is for wholer

/*	public Intent getIntent(Context context,
			ArrayList<Double> listCB) {
		// TODO Auto-generated method stub
		
		for(int i = 0; i < size; i++){
		AccelData ac = (AccelData) lcb.get(i);
		 y[i]= (int) Math.sqrt(ac.getX()*ac.getX() + ac.getY()*ac.getY() + ac.getZ()*ac.getZ());
		
		 System.out.printf("y[i]",+ y[i]);
		}
		Arrays.sort(y); //ascending order
		
		//Arrays.binarySearch(y,10);
		
		//array per il diagramma di wholer
		int[] wholer = new int[20];
		
		for(int j = 0; j < 20; j++) wholer[j] = 0; //inizializzo il vettore wholer
		
		int c = 0;
		
		for(int i = 0; i <20;i++){
			c = 0;
			while( c  != -1){
		
			 // se non trovi i c � -1 quindi esci	
			 //c = y.toString().indexOf(i);
				c = TrovaIndice(i);
			 if(c!=-1){
				 
			 wholer[i]++; //agg +1 quando lo trovi
			 y[c] = 21; // tolgo l elemento una volta trovato
		};
			 
		}
		}
		return null;
	    
	}*/



}
