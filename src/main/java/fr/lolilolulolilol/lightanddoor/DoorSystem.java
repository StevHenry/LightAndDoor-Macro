package fr.lolilolulolilol.lightanddoor;

import java.io.IOException;
import java.net.URL;

public class DoorSystem {
	
	private static DoorSystem currentOne;
	
	public static DoorSystem get() {
		return currentOne == null ? (currentOne = new DoorSystem()) : currentOne;
	}

	private int pCount = 0;

	public DoorSystem() {
		new Thread(() -> {
			try {
				Thread.sleep(200);
				sendDoorAction(pCount >= 2 ? 1 : 0);
				currentOne = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * This method is used increasing typed "P" letter count
	 */
	public void increasePKeyPressCount() {
		pCount++;
	}

	/**
	 * This method is used to send door movement query to the web page.
	 * 
	 * @param actionID
	 * 	This parameter define the movement sens.
	 */
	private void sendDoorAction(int actionID) {
		LightAndDoor.LOGGER.info("Door query: " + (actionID > 0 ? "opening" : "closing") + " !");
		new Thread(() -> {
			try {
				new URL(LightAndDoor.URL_PATH + "door.php?action=" + actionID).openStream().close();
			} catch (IOException e) {
				LightAndDoor.LOGGER.error(e.getMessage());
			}
		}).start();
	}
}