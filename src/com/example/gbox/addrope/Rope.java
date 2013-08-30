package com.example.gbox.addrope;

public class Rope {
	

	//private variables
	int _id;
	String name;
	String LastUpdate;
	int usage;
	float MaxForce;
	int MND;

	// constructor
	public Rope(int id, String name, String lastUpdate, int usage, float MaxForce, int mnd){
		this._id = id;
		this.name = name;
		this.LastUpdate = lastUpdate;
		this.usage = usage;
		this.MaxForce = MaxForce;
		this.MND = mnd;
	}
	


	// empty constructor
	public Rope() {
		// TODO Auto-generated constructor stub
	}




	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getName(){
		return this.name;
	}
	
	// setting name
	public void setName(String name){
		this.name = name;
	}
	
	// getting LastUpdate
	public String getLastUpdate(){
		return this.LastUpdate;
	}
	
	// setting LastUpdate
	public void setLastUpdate(String lastUpdate){
		this.LastUpdate = lastUpdate;
	}
	
	// getting usage
		public int getUsage(){
			return this.usage;
	}
		
	// setting usage
	public void setUsage(int usage){
			this.usage = usage;
	}
	
	// getting MaxForce
		public float getMaxForce(){
			return this.MaxForce;
	}
			
	// setting MaxForce
	public void setMaxForce(float maxForce){
			this.MaxForce = maxForce;
	}
	
	// getting Max Number of Dumps
	public int getMND(){
		
		return this.MND;
	}
		
	// setting Max Number of Dumps
	public void setMND(int mnd){
		this.MND = mnd;
	}
	
	}
