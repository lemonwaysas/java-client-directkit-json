package lw.api.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This example shows how to call the LemonWay DirectKit JSON API.
 * It is very simple: We have to send the post request in the right format to the right URL
 */
public class Program {

	public static String excutePost(String targetURL, String postData) throws IOException {
		HttpURLConnection connection = null;
		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(postData);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	/**
	 * Read the resource file which should be in the ClassPath.
	 * the simplest way is to put resources files in the source folder.
	 * @throws IOException 
	 */
	private static String readResource(String resourceName) throws IOException {
	    //read file as a string
	    InputStream is = Program.class.getClassLoader().getResourceAsStream(resourceName);
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader buf = new BufferedReader(isr);
	    try {
		    String s, s2 = new String();
		    while ((s = buf.readLine()) != null) {
		        s2 += s + "\n";
		    }
		    return s2;
	    }
	    finally {
	    	buf.close();
	    	isr.close();
	    	is.close();
	    }
	}

	public static void main(String[] args) {
		try {
			System.out.println("** START program **");
			
			//Prepare a request GetWalletDetails
			//See the request format in our API documentation 
			//http://documentation.lemonway.fr/api-en/directkit/manage-wallets/getwalletdetails-getting-detailed-wallet-data
			String jsonRequest = readResource("GetWalletDetails.001.json");
			
			//Send request (replace "hiep" by your company name)
			String response = excutePost("https://ws.lemonway.fr/mb/hiep/dev/directkitjson/service.asmx/GetWalletDetails", jsonRequest);
			
			//display response
			System.out.println(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			System.out.println("** END program **");
		}
	}
}
