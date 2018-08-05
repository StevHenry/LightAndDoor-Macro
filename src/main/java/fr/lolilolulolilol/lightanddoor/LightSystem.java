package fr.lolilolulolilol.lightanddoor;

import java.io.IOException;
import java.net.URL;

public class LightSystem {
	
	private boolean isLightOn;
	
	public LightSystem(boolean currentLightState) {
		this.isLightOn = currentLightState;
	}
	
	/**
	 * @param newLightState
	 * 	Define which light state must be set
	 */
	public void setLightState(boolean newLightState) {
		send(newLightState ? "1" : "0", newLightState ? "ignition" : "extinction");
	}
	
	/**
	 * Allows changing light state without knowing the current one
	 */
	public void swapLightState() {
		send("swap", "swap");
	}
	
	/**
	 * @param state
	 * 	Define which state must be the new
	 * @param queryName
	 * 	Logger info query action
	 */
	private void send(String state, String queryName) {
		LightAndDoor.LOGGER.info("Light query: " + queryName + " !");
		new Thread(() -> {
			isLightOn = !isLightOn;
			try {
				new URL(LightAndDoor.URL_PATH + "light.php?state=" + state).openStream().close();
			} catch (IOException ex) {
				isLightOn = !isLightOn;
				LightAndDoor.LOGGER.error(ex.getMessage());
			}
		}).start();
	}
}
