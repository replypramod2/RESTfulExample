package com.rbc.job.shadow.util;

import java.util.Map.Entry;
import java.util.Set;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class CloudantClientMgr {
	
	private static CloudantClient cloudant = null;
	private static Database db = null;
	private static String databaseName = "jobshadowing";
	private static String user = null;
	private static String password = null;
	private static String host = null;

	
	 
	private static void initClient() {
		
		if ( cloudant == null ) {
			synchronized (CloudantClientMgr.class) {
				if ( cloudant != null ) {
					return;
				}				
				cloudant = createClient();			
				
			}// end synchronized
		}
	}
    
	private static CloudantClient createClient() {

		// VCAP_SERVICES is a system environment variable
		// Parse it to obtain the  NoSQL DB connection info
	//	String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		String VCAP_SERVICES = null;
		String serviceName = null;

    	if (VCAP_SERVICES != null) {
    		System.out.println("I am if");
			// parse the VCAP JSON structure
			JsonObject obj =  (JsonObject) new JsonParser().parse(VCAP_SERVICES);
			Entry<String, JsonElement> dbEntry = null;
			Set<Entry<String, JsonElement>> entries = obj.entrySet();
			// Look for the VCAP key that holds the cloudant no sql db information
			for (Entry<String, JsonElement> eachEntry : entries) {				
				if (eachEntry.getKey().equals("Programmer")) {
					dbEntry = eachEntry;
					break;
				}
			}
			if (dbEntry == null) {			
				throw new RuntimeException("Could not find cloudantNoSQLDB key in VCAP_SERVICES env variable");    					
			}

			obj =(JsonObject) ((JsonArray)dbEntry.getValue()).get(0);		
			serviceName = (String)dbEntry.getKey();
			System.out.println("Service Name - "+serviceName);

			obj = (JsonObject) obj.get("credentials");

			user = obj.get("username").getAsString();
			password = obj.get("password").getAsString();
			host = obj.get("host").getAsString();
		
		}
		else {
			System.out.println("I am else");
			user= "4dc8d458-b099-4dfb-bee6-0b1b766eb512-bluemix";
	        password= "83beb271368907f12c4b7bbe8f8aeb490d4cf9e9e761d596c1d81cd25fefb54e";
	       host= "4dc8d458-b099-4dfb-bee6-0b1b766eb512-bluemix.cloudant.com";
			//throw new RuntimeException("VCAP_SERVICES not found");
		}

		try {
			return new CloudantClient(user, user, password);
		}
		catch(org.lightcouch.CouchDbException e) {
			throw new RuntimeException("Unable to connect to repository", e);
		}
	}

	
	public static Database getDB() {
		
		if ( cloudant == null ) {
			initClient();
		}
		
		if(db == null)
		{
			try {
				
				db = cloudant.database(databaseName, true);
			}
			catch(Exception e) {

				throw new RuntimeException("DB Not found", e);
			}
		}
		return db;
	}
	
	public static String getUser()
	{
		return user;
	}
	
	public static String getPassword()
	{
		return password;
	}
	
	public static String getHost()
	{
		return host;
	}
	
	public static String getDatabaseName()
	{
		return databaseName;
	}
	
	private CloudantClientMgr() {
	    	
	 }
}