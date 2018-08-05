package fr.lolilolulolilol.lightanddoor;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LightAndDoor {
	
	public final static Logger LOGGER = LoggerFactory.getLogger(LightAndDoor.class);
	public final static String URL_PATH = "http://localhost/lad/";
	
	/**
	 * Method tu register the native hook and enable macro system.
	 */
	public static void main(String[] args) {	
		HashMap<String, Byte> infos = getInformations();
		KeyboardListener listener = new KeyboardListener(infos.containsKey("light") ? (infos.get("light") == 1 ? true : false) : false);
		try {
			LOGGER.info("Registering native hook.");
			java.util.logging.Logger gs = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
			gs.setLevel(Level.OFF);
			GlobalScreen.registerNativeHook();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException e) {
					LOGGER.error(e.getMessage());
				}
			}));
			LOGGER.info("Shutdown hook added");
		} catch (NativeHookException ex) {
			LOGGER.error("It seems that there was a problem adding the native hook.");
			LOGGER.error(ex.getMessage());
			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(listener);
		LOGGER.info("Keyboard listener added");
	}
	
	/**
	 * This method allow us to know current informations (light and door states) delivered by the arduino
	 * 
	 * Light possible values:
	 * 0 => Off
	 * 1 => On
	 * 
	 * Door possible values:
	 * 0 => Door totaly close
	 * 1 => Door totaly open
	 * 2 => Opening
	 * 3 => Closing
	 * 4 => Door between close and open
	 */
	private static HashMap<String, Byte> getInformations() {
		HashMap<String, Byte> informations = new HashMap<String, Byte>();
		try {
			InputStream infosXML = new URL("http://192.254.182.124:550").openStream();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(infosXML);
			infosXML.close();
			doc.getDocumentElement().normalize();

			NodeList infos = (NodeList) doc.getDocumentElement();
			for (int nodeIndex = 0; nodeIndex < infos.getLength(); nodeIndex++) {
				Node n = infos.item(nodeIndex);
				if (n.getNodeType() == Node.ELEMENT_NODE)
					informations.put(n.getNodeName(), Byte.parseByte(n.getTextContent()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return informations;
	}
}