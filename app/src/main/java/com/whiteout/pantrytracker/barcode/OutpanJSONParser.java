package com.whiteout.pantrytracker.barcode;

import org.json.JSONException;
import org.json.JSONObject;

public class OutpanJSONParser {
	
	public OutpanJSONParser() {
		
	}
	
	/**
	 * Checks if a string contains product data or if the 
	 * data retrieved was empty.
	 * 
	 * Outpan returns one of three things:
	 * <ul>
	 * <li> The barcode was not found at all. This returns a JSON item with a title of "error" </li>
	 * <li> The barcode was found but there is no data associated with it. This has a name of null. </li>
	 * <li> The barcode was found and returns good data. </li>
	 * </ul>
	 * 
	 * @param jsonString String containing entire JSON document
	 * @return True if data is valid. False otherwise.
	 * @throws org.json.JSONException
	 */
	public boolean isValidData(String jsonString) throws JSONException{
		JSONObject json = new JSONObject(jsonString);
		
		if(json.has("error")){
			return false;
		}
		else if(json.getString("name").equals("null")){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Parses and returns the name of the product
	 * @param jsonString String containing entire JSON document
	 * @return Name of item as a string
	 * @throws org.json.JSONException
	 */
	public String getProductNameFromString(String jsonString) throws JSONException{
		if(isValidData(jsonString)){
            JSONObject json = new JSONObject(jsonString);
            return json.getString("name");
        }
        else{
            return null;
        }
	}
}
