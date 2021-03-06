package com.ardhi.businessgame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ardhi.businessgame.controllers.BusinessGameController;
import com.ardhi.businessgame.models.TimePhase;

public class ThreadInvoker extends Thread {

	private boolean threadWork = false;
	
	public void setThreadWork(boolean val){
		threadWork = val;
	}
	
	@Override
	public void run() {
		try {
			ExecutorService executor = Executors.newFixedThreadPool(3);
			List<Future<Long>> futureList = new ArrayList<Future<Long>>();
			EngineCall engine = new EngineCall(EngineCall.SYNC);
			Thread.sleep(engine.call());
			engine = null;
			EngineCall.working = true;
			BusinessGameController.working = true;
			threadWork = true;
			while(threadWork){
//				BusinessGameController.working = false;
				futureList.add(executor.submit(new EngineCall(EngineCall.IDLE)));
				Thread.sleep(futureList.get(0).get());
				futureList.add(executor.submit(new EngineCall(EngineCall.CONTRACT)));
				futureList.add(executor.submit(new EngineCall(EngineCall.WORK)));
				Thread.sleep(futureList.get(2).get());
				futureList.add(executor.submit(new EngineCall(EngineCall.MARKET_SHARE)));
				futureList.add(executor.submit(new EngineCall(EngineCall.WORK)));
				Thread.sleep(futureList.get(4).get());
				futureList.add(executor.submit(new EngineCall(EngineCall.SECTOR_POWER_PLANT)));
				futureList.add(executor.submit(new EngineCall(EngineCall.WORK)));
				Thread.sleep(futureList.get(6).get());
				futureList.add(executor.submit(new EngineCall(EngineCall.BANK)));
				futureList.add(executor.submit(new EngineCall(EngineCall.SECTOR_NON_POWER_PLANT)));
				futureList.add(executor.submit(new EngineCall(EngineCall.WORK)));
				Thread.sleep(futureList.get(9).get());
//				futureList.add(executor.submit(new EngineCall(EngineCall.WORK)));
				
				futureList.clear();
				System.out.println("Cleaning before loop re-start..");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TimePhase getGameTime(){
		EngineCall engine = new EngineCall(EngineCall.REQ_TIME);
		return new TimePhase(engine.call(), EngineCall.TIME_WORK, EngineCall.TIME_TOTAL-EngineCall.TIME_WORK);
	}

}
