package com.rbc.job.shadow.rest;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rbc.job.shadow.util.CloudantClientMgr;

@Controller
@Path("/cloudant")
public class RestJobShadowCloudantService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Response list(@QueryParam("interests") String interests)
			throws Exception {

		System.out.println("I have been called : ");
		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(
					javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}

		JsonObject resultObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		if (interests != null) {
			try {
				// get all the document present in database

				List<HashMap> allDocs = db.view("_all_docs").query(
						HashMap.class);

				System.out.println("Size" + allDocs.size());

				for (HashMap doc : allDocs) {

					HashMap<String, Object> obj = db.find(HashMap.class,
							doc.get("id") + "");
					JsonObject jsonObject = new JsonObject();
					if (interests.equalsIgnoreCase(obj.get("interests") + "")) {

						jsonObject.addProperty("summary", obj.get("summary")
								+ "");
						jsonObject.addProperty("education",
								obj.get("education") + "");
						jsonObject
								.addProperty("skills", obj.get("skills") + "");
						jsonObject.addProperty("interests",
								obj.get("interests") + "");
						jsonObject.addProperty("courses", obj.get("courses")
								+ "");
						jsonObject.addProperty("experience",
								obj.get("experience") + "");
						jsonObject.addProperty("contact", obj.get("contact")
								+ "");
						jsonObject.addProperty("patience", obj.get("patience")
								+ "");
						jsonObject.addProperty("passion", obj.get("passion")
								+ "");

						jsonArray.add(jsonObject);
					}
				}
				System.out.println("jsonArray Size" + jsonArray.size());

			} catch (Exception dnfe) {
				System.out.println("Exception thrown : " + dnfe.getMessage());
			}

			resultObject.addProperty("interests", interests);
			resultObject.add("body", jsonArray);

			return Response.ok(resultObject.toString()).build();

		}

		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND)
					.build();

	}

	@POST
	@Path("/attach")
	@Consumes("application/json")
	public Response create(String jsonRequest) {
		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(
					javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}

		try {
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(jsonRequest);
			db.save(jsonObject);
			System.out.println("Upload completed....");
			return Response.ok().build();

		} catch (Exception e) {

			System.out.println("Error saving..." + e);
			return Response.status(-1).build();

		}

	}

	private Database getDB() {
		return CloudantClientMgr.getDB();
	}

}