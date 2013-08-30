package com.example.gbox;

public class Story {
	
	//private variables
	int _id;
	String date;
	int dumps;
	int max_acc;
	int min_acc;
	
	
	// constructor
public Story(String date, int dumps, int max_acc, int min_acc) {
	// TODO Auto-generated constructor stub
	
	
	this.date = date;
	this.dumps = dumps;
	this.max_acc = max_acc;
	this.min_acc = min_acc;
	
}

	public Story() {
	// TODO Auto-generated constructor stub
}

	//getting ID
	public int getID(){
		return this._id;
	}
	
	// setting date
	public void setID(int id){
		this._id = id;
	}


	//getting date
	public String getDate(){
		return this.date;
	}
	
	// setting date
	public void setDate(String date){
		this.date = date;
	}


	//getting dumps
	public int getDumps(){
		return this.dumps;
	}
	
	// setting dumps
	public void setDumps(int dumps){
		this.dumps = dumps;
	}


	//getting max acc
	public int getMaxAcc(){
		return this.max_acc;
	}
	
	// setting max acc
	public void setMaxAcc(int max_acc){
		this.max_acc = max_acc;
	}
	

	//getting min acc
	public int getMinAcc(){
		return this.min_acc;
	}
	
	// setting max acc
	public void setMinAcc(int min_acc){
		this.min_acc = min_acc;
	}
		
}
