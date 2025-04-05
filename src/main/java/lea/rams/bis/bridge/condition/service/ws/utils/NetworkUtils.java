/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author Nobol
 *
 */
public class NetworkUtils {
	
	public static String getMacAddress() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
				if (networkInterface.getHardwareAddress() != null) {
					byte[] mac = networkInterface.getHardwareAddress();
					StringBuilder macAddress = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						macAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
					}
					return macAddress.toString();
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException("Error retrieving MAC address", e);
		}
		return "Unknown";
	}
}
