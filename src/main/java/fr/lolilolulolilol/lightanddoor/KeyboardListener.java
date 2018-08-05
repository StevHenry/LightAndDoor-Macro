package fr.lolilolulolilol.lightanddoor;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyboardListener implements NativeKeyListener {

	public boolean alt, ctrl;
	private final LightSystem light;

	/**
	 * @param isLightOn
	 * 	Current light state in the room
	 */
	public KeyboardListener(boolean isLightOn) {
		light = new LightSystem(isLightOn);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_ALT)
			alt = true;
		else if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL)
			ctrl = true;
	} 

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		LightAndDoor.LOGGER.debug("Key realeased: " + e.getKeyChar() + " (" + e.getKeyCode() + ") from " + e.getSource().toString());
		switch (e.getKeyCode()) {
		case NativeKeyEvent.VC_ALT:
			alt = false;
			break;
		case NativeKeyEvent.VC_CONTROL:
			ctrl = false;
			break;
		case NativeKeyEvent.VC_P:
			if (alt && ctrl)
				DoorSystem.get().increasePKeyPressCount();
			break;
		case NativeKeyEvent.VC_L:
			if (alt && ctrl)
				light.swapLightState();
			break;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
	}
}