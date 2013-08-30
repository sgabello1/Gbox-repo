package com.example.gbox.addrope;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.gbox.HomeActivity;
import com.example.gbox.Profile;
import com.example.gbox.R;
import com.example.gbox.R.id;
import com.example.gbox.R.layout;


import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddRope extends Activity {

		//SQL Database objects
		DatabaseHandler db = new DatabaseHandler(this);
		ListView listview;
		ArrayList<String> list;

		public static final String MIME_TEXT_PLAIN = "text/plain";
		public static final String TAG = "NfcDemo";

		//data rope
		Rope jrope = new Rope();
		
		private TextView tv;
		private NfcAdapter mNfcAdapter;
		
		//string to pass to http request
		private String whichMethod = null; 
		public String[] separated;
		
		//class implementing http
		OnNFCdiscovered onDisc;
		
		//buttons
		public Button addButton;
		public Button nfcButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_add_rope);
		
		 //visualize rope internal DB
		 listview = (ListView) findViewById(R.id.listView1);
		
		 //NFC adapterf
		 mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		 
		 //add rope button
		 addButton = (Button) findViewById(R.id.add);
		 addButton.setEnabled(false);
		 
		/* //update rope's usage button
		 updateButton = (Button) findViewById(R.id.update);
		 updateButton.setEnabled(false);*/
		 
		 
		 
		 //cosa hai cliccato?
		 View.OnClickListener gestore = new View.OnClickListener() {
			  public void onClick(View view) { 
			    
			    switch(view.getId()){
			            	
			      case R.id.add:
			  
			    	  
						    	//this starts a thread for a get request
					    		  
						    	  //create the object for the request
						    	  onDisc =  (OnNFCdiscovered) new OnNFCdiscovered(jrope, separated, getApplicationContext());
						    	  onDisc.wMethod = "get";
						    	  onDisc.execute();
						    	  
					//Waits if necessary for the computation to complete, and then retrieves its result.
					try {
						if(!onDisc.get().equals(null)){ // quindi se la risposta dal server non è nulla va avanti
							 
						//SQL db: adding rope to internal database
						  
						  DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						   
						  //adding rope to internal db retrieved from http response
						  
						  db.addRope(new Rope(onDisc.jrope.getID(), onDisc.jrope.getName(),onDisc.jrope.getLastUpdate() , 
								   onDisc.jrope.getUsage(), onDisc.jrope.getMaxForce(), onDisc.jrope.getMND()));
						  
						    visualize();

						 }
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
			    	  
			    	  
			    	  
			        break;
			            	    	
			/*      case R.id.update:
			    	  
			    	 
			    	  //create the object for the request
			    	  onDisc =  (OnNFCdiscovered) new OnNFCdiscovered(jrope, separated, getApplicationContext());
			    	  onDisc.wMethod = "put";
			    	  onDisc.execute();
			    	   
			        break;*/
			     
			    }	
			  }
			};
			
		 addButton.setOnClickListener(gestore);
		 
		if (mNfcAdapter == null) {
			// Stop here, we definitely need NFC
			Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
			finish();
			return;

		}
	
		
		
       visualize();

	}
	
	void visualize(){
	    
	    list = new ArrayList<String>();	
	    
	    // Reading all Ropes
	    List<Rope> ropes = db.getAllRopes();       

	    // Display on a listview
	    for (Rope cn : ropes) {
	        String values =  cn.getName() + "          "+ cn.getUsage();
	        list.add(values);    
	    }
	    
	    // Adapter
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, list);
	        listview.setAdapter(adapter);
	        
	     // Click on item -> display info of this item   
	        OnItemClickListener clickListener = new OnItemClickListener() {

	        	@Override
				public void onItemClick(AdapterView<?> adapter, View view, int position,
						long id) {
					// TODO Auto-generated method stub
	        		
	        		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

	        		 Rope cn = db.getRope(position + 1);
	        		 Toast.makeText(getApplicationContext(), "Id " + cn.getID() + "Name: " + cn.getName() + " ,Last update: " + cn.getLastUpdate() 
	        				 + " ,Usage: " + cn.getUsage() + " ,Max force: " + cn.getMaxForce() + " ,MND: " + cn.getMND(), Toast.LENGTH_SHORT).show();
	        		 
	        		 //shared the object Rope that you tapped with every activity
	        		 
	        		 SharedPreferences tappedRope = getSharedPreferences("TAPPED_ROPE", Context.MODE_PRIVATE);
	        		 SharedPreferences.Editor editor = tappedRope.edit();
	        		 editor.putInt("id", cn.getID());
	        		 editor.putString("name", cn.getName());
	        		 editor.putString("lastupdate", cn.getLastUpdate());
	        		 editor.putInt("usage", cn.getUsage());
	        		 
	        		 System.out.print( "Usage added" + cn.getUsage());
	        		 
	        		 editor.putFloat("maxforce", cn.getMaxForce());
	        		 editor.putInt("mnd", cn.getMND());
	        		 editor.commit();
	        		 
	        		 Intent back_to_profile= new Intent(AddRope.this, HomeActivity.class);
	 				 AddRope.this.startActivity(back_to_profile);
				}
	        };
	        listview.setOnItemClickListener(clickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		/*
		 * It's important, that the activity is in the foreground (resumed). Otherwise
		 * an IllegalStateException is thrown. 
		 */
		setupForegroundDispatch(this, mNfcAdapter);
	}
	
	@Override
	protected void onPause() {
		/*
		 * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
		 */
		stopForegroundDispatch(this, mNfcAdapter);
		
		super.onPause();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		/*
		 * This method gets called, when a new Intent gets associated with the current activity instance.
		 * Instead of creating a new activity, onNewIntent will be called. For more information have a look
		 * at the documentation.
		 * 
		 * In our case this method gets called, when the user attaches a Tag to the device.
		 */
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		//che intent è? ACTION_NDEF_DISCOVERED?
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			
			String type = intent.getType();
			//di che tipo è? deve essere MIME_TEXT_PLAIN
			if (MIME_TEXT_PLAIN.equals(type)) {

				//prendi da Intent un oggetto parcelable(NfcAdapter.EXTRA_TAG) che è la dicitura standard per identificare l'oggetto tag nfc
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				//ha fatto una classe che implementa un thread asincrono, quindi per eseguirlo fa <nome_classe>.execute(tag) che è per leggere
				new NdefReaderTask().execute(tag);
				
			} else {
				Log.d(TAG, "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			
			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();
			
			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					new NdefReaderTask().execute(tag);
					break;
				}
			}
		}
	}
	
	/**
	 * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		
		//crei un nuovo Intent uguale a questo dell'activity
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		
		//If set, the activity will not be launched if it is already running at the top of the history stack.
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		//se dai questo pendingIntent a un altra app lei può svolgere il compito assegnato come fosse questa
		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		//filtro per gli intents
		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][]{};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}
		
		adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
		
	}

	/**
	 * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
	 * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}
	
	/**
	 * Background task for reading the data. Do not block the UI thread while reading. 
	 * 
	 * @author Ralf Wondratschek
	 *
	 */
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		@Override
		protected String doInBackground(Tag... params) {
			Tag tag = params[0];

			//Ndef è un oggetto formato di dati del nfc
			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag. 
				return null;
			}

			//oggetto di dati messaggio ndef
			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			//NdefRecord contiene i records
			NdefRecord[] records = ndefMessage.getRecords();
			
			//controlla che nel record il tnf contenga tnf_well_known e rtd_text
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
					try {
						//finalmente leggi il testo!
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, "Unsupported Encoding", e);
					}
				}
			}

			return null;
		}
		
		private String readText(NdefRecord record) throws UnsupportedEncodingException {
			/*
			 * See NFC forum specification for "Text Record Type Definition" at 3.2.1 
			 * 
			 * http://www.nfc-forum.org/specs/
			 * 
			 * bit_7 defines encoding
			 * bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */

			byte[] payload = record.getPayload();

			// Get the Text Encoding
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

			// Get the Language Code
			int languageCodeLength = payload[0] & 0063;
			
			// String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
			// e.g. "en"
			
			// Get the Text
			return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				
				
				
				separated = result.split("#");
				
				//now I ve got the link, start the Http connection
				 addButton.setEnabled(true);
				
				
			}
		}
	}

	
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_rope, menu);
		return true;
	}*/

}
