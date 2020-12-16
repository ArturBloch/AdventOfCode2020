package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Advent13 implements Runnable {

	File file = new File("src/files/advent13.txt");
	ArrayList<Bus> buses = new ArrayList<>();
	Bus firstBus = null;

	class Bus{
		public long busId;
		public long busDelay;
		public long busTime;

		public Bus(int busId, int busDelay) {
			this.busId = busId;
			this.busTime = busId;
			this.busDelay = busDelay;
		}

		@Override public String toString() {
			return "Bus{" + "busId=" + busId + ", busDelay=" + busDelay + ", busTime=" + busTime + '}';
		}
	}


	@Override public void run() {
//		System.out.println(fastestBusOneStar());
		fastestBusTwoStar();
	}

	public void fastestBusTwoStar() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				line = br.readLine(); // skip first line
				String[] busTimes = line.split(",");
				int busDelay = 0;
				for (String busId : busTimes) {
					if(busId.equals("x")) {
						busDelay++;
						continue;
					}
					buses.add(new Bus(Integer.parseInt(busId), busDelay));

					busDelay++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		findViableBusDepartureTime();
	}

	private void findViableBusDepartureTime() {
		long time = 0;
		long stepSize = buses.get(0).busId;
		for (Bus bus : buses) {
			System.err.println(bus);
		}
		for (int i = 1; i < buses.size(); i++) {
			Bus bus = buses.get(i);
			while((time + bus.busDelay) % bus.busId != 0){
				time += stepSize;
			}

			stepSize *= buses.get(i).busId;
		}

		System.out.println(time);
	}

	public int fastestBusOneStar() {
		BufferedReader br;
		int busBestId = -1;
		int fastestTime = Integer.MAX_VALUE;
		int departTime = -1;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if(departTime == -1) {
					departTime = Integer.parseInt(line);
					continue;
				}
				String[] busTimes = line.split(",");
				for (String busTimeStamp : busTimes) {
					if(busTimeStamp.equals("x")) continue;
					int busDelay = Integer.parseInt(busTimeStamp);
					int busTime = busDelay;
					while (busTime < departTime){
						busTime += busDelay;
					}
					if(busTime - departTime < fastestTime - departTime){
						busBestId = Integer.parseInt(busTimeStamp);
						fastestTime = busTime;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return busBestId * (fastestTime - departTime);
	}

}
