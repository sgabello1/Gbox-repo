package com.example.gbox;

import java.util.ArrayList;

import com.example.accchart.AccelData;


public interface FragmentCommunicator {
	
	public void StartMonitoring();

	public void StopMonitoring();
	
	public ArrayList<Double> getListCircularBuffer();
}
