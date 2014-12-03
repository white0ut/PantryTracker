package com.whiteout.pantrytracker.barcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;



import android.util.Log;

public class OutpanFetcher {
	private final String URL_START = "http://www.outpan.com/api/get_product.php?barcode=";
	
	
	
	public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    
    public String fetchItems(String barcode){
    	try{
			String url = URL_START + barcode;
    		Log.d("OutFetcher", "URL:" + url);
			String jsonString = getUrl(url);
			Log.d("OutFetcher", jsonString);
			

			OutpanJSONParser parser = new OutpanJSONParser();
			if(parser.isValidData(jsonString)){
				return parser.getProductNameFromString(jsonString);
			}
			
			
			return jsonString;
		} catch (Exception e) {
			Log.e("WUFetcher FetchItems","Error fetching items");
			e.printStackTrace();
		}
		return null;
    }

}
