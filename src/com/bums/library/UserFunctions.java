package com.bums.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
//    private static String loginURL = "http://10.0.2.2/small/";
//    private static String registerURL = "http://10.0.2.2/small/";
    
    //private static String loginURL = "http://192.168.1.141:80/small/";
    //private static String registerURL = "http://192.168.1.141:80/small/";
    
    //private static String loginURL = "http://192.168.1.113:80/small/";
    //private static String registerURL = "http://192.168.1.113:80/small/";
    
    private static String loginURL = "http://192.168.1.110:80/small/";
    private static String registerURL = "http://192.168.1.110:80/small/";
    private static String fashionURL = "https://api.instagram.com/v1/tags/incfashion/media/recent?client_id=a817372926af4107bb256a2036c6015d&count=50";


    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String store_office_tag = "store_office";
    private static String store_department_tag = "store_department";
    private static String delete_office_tag = "delete_office";
    private static String delete_department_tag = "delete_department";
    private static String office_tag = "get_offices";


    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/
    public JSONObject loginUser(String username, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }


     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String username, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
    
    public JSONObject storeDepartment(String id, String department, String organization){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", store_department_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("department", department));
        params.add(new BasicNameValuePair("organization", organization));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
    
    public JSONObject deleteDepartment(String id, String organization){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", delete_department_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("organization", organization));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
    
    public JSONObject storeOffice(String id, String officeType, String isLeader){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", store_office_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("officeType", officeType));
        params.add(new BasicNameValuePair("isLeader", isLeader));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
    
    public JSONObject deleteOffice(String id, String officeType){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", delete_office_tag));
        params.add(new BasicNameValuePair("id", id));
        params.add(new BasicNameValuePair("officeType", officeType));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        return json;
    }
    
    public JSONObject getOffices(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", office_tag));
        params.add(new BasicNameValuePair("id", id));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    /**
	 * Retrieves recent post data from the server.
	 */
	public JSONObject getFashionJSON() {
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(fashionURL);

		return json;
	}
	
	public JSONObject getFashionJSON(String url) {
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);

		return json;
	}
}

