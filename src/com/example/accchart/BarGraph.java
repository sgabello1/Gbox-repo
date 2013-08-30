package com.example.accchart;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.FloatMath;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;



public class BarGraph {

	/**
	 * 
	 */
	private ArrayList<AccelData> lcb = null; 
	int size = 0;
	int[] y;
	
	
public BarGraph(ArrayList<AccelData> listCB) {
	// TODO Auto-generated constructor stub
	
	this.lcb = listCB;
	this.size = lcb.size();
	this.y =new int[size];
}


	public View getView(Context context) {
		// TODO Auto-generated method stub
//		lcb = listCB;
//		size = lcb.size();
//		y =new int[size];
//		
		
		
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




	public Intent getIntent(Context context,
			ArrayList<AccelData> listCB) {
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

		
		Intent intent = ChartFactory.getBarChartIntent(context, dataset,mRenderer, Type.DEFAULT);
	    //View view = ChartFactory.getBarChartView(context, dataset, mRenderer, Type.DEFAULT);
	
		return intent;
	    //return view;
		
		
		
	}

//
//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		 return hashCode();
//	}
//
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		// TODO Auto-generated method stub
//		
//	}

}
