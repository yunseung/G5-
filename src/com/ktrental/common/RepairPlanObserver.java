package com.ktrental.common;

import java.util.Observable;
import java.util.Observer;

public class RepairPlanObserver extends Observable {
	@Override
	public void addObserver(Observer observer) {
		// TODO Auto-generated method stub
		super.addObserver(observer);
	}

	public void testA() {
		setChanged();
		notifyObservers();
	}
}
