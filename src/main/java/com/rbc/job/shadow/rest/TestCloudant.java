package com.rbc.job.shadow.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rbc.job.shadow.util.CloudantClientMgr;

public class TestCloudant {

	public static void main(String[] args) {
		Database db = CloudantClientMgr.getDB();
		
		System.out.println(db.getDBUri());

		
		JsonObject resultObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();		
			
				
			try
			{
				//get all the document present in database			
				
				List<HashMap> allDocs = db.view("_all_docs").query(HashMap.class); 
				
				System.out.println("Size" + allDocs.size());
				
				
				
				
				for(HashMap doc : allDocs)
				{
					
					HashMap<String, Object> obj = db.find(HashMap.class, doc.get("id")+"");
					JsonObject head = new JsonObject();	
					head.addProperty("name", obj.get("fullname")+"");
					
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("key", obj.get("_id")+"");
					jsonObject.addProperty("fullname", obj.get("fullname")+"");
					jsonObject.addProperty("summary", obj.get("summary")+"");
					jsonObject.addProperty("education", obj.get("education")+"");
					jsonObject.addProperty("skills", obj.get("skills")+"");
					jsonObject.addProperty("interests", obj.get("interests")+"");
					jsonObject.addProperty("courses", obj.get("courses")+"");
					jsonObject.addProperty("positionlevel", obj.get("positionlevel")+"");
					
					if(obj.get("experience")!=null)
					jsonObject.addProperty("experience", Integer.parseInt(obj.get("experience")+""));
					else
						jsonObject.addProperty("experience", Integer.parseInt("0"));
					jsonObject.addProperty("contact", obj.get("contact")+"");
					jsonObject.addProperty("patience", obj.get("patience")+"");
					jsonObject.addProperty("passion", obj.get("passion")+"");
					head.add("values", jsonObject);
					jsonArray.add(head);
				}
				System.out.println("jsonArray Size" + jsonArray.size());
				
				printJason(jsonArray);
			}
			catch(Exception dnfe)
			{
				dnfe.printStackTrace();
			}
	
			}

	public static void printJason(JsonArray jsonArray) {
		Iterator<JsonElement> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().toString());
		}
	}
}
