package com.egen.user.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.egen.model.Address;
import com.egen.model.Company;
import com.egen.model.User;
import com.egen.util.JacksonUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class UserMongoDB {

	private static DB connectToMongo() {
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient("localhost", 27017);

			// Now connect to your databases
			DB db = mongoClient.getDB("test");
			return db;
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		}

		return null;

	}

	public static String insertUser(User user) {
		DB db = connectToMongo();
		//Get user collection
		DBCollection coll = db.getCollection("user");
		System.out.println("Collection mycol selected successfully");
       
		//Creating document for insert into user collection
		BasicDBObject doc = new BasicDBObject("firstName", user.getFirstName())
				.append("lastName", user.getLastName())
				.append("email", user.getEmail())
				.append("address",
						new BasicDBObject("street", user.getAddress()
								.getStreet())
								.append("city", user.getAddress().getCity())
								.append("zip", user.getAddress().getZip())
								.append("state", user.getAddress().getState())
								.append("country",
										user.getAddress().getCountry()))
				.append("dateCreated", user.getDateCreated())
				.append("company",
						new BasicDBObject("name", user.getCompany().getName())
								.append("website", user.getCompany()
										.getWebsite()))
				.append("profilePic", user.getProfilePic());

		coll.insert(doc);

		return JacksonUtil.objectToJson(dbObjectToUser(coll.findOne()));
	}

	public static List<String> getAllUsers() {
		DB db = connectToMongo();
		//Get user collection
		DBCollection coll = db.getCollection("user");
		//Get all document from user collections
		DBCursor cursor = coll.find();

		List<String> resultList = new ArrayList<String>();

		while (cursor.hasNext()) {
			DBObject updateDocument = cursor.next();
			String userString = JacksonUtil
					.objectToJson(dbObjectToUser(updateDocument));
			resultList.add(userString);

		}
		return resultList;
	}

	public static String updateUser(User user) {

		try {

			DB db = connectToMongo();
			//Get user collection
			DBCollection coll = db.getCollection("user");		
			ObjectId objId = new ObjectId(user.getId());
			//Find document using id
			DBObject dbObject = coll.findOne(new BasicDBObject().append("_id",
					objId));

			//create a document for update 
			BasicDBObject newDocument = new BasicDBObject("firstName",
					user.getFirstName())
					.append("lastName", user.getLastName())
					.append("email", user.getEmail())
					.append("address",
							new BasicDBObject("street", user.getAddress()
									.getStreet())
									.append("city", user.getAddress().getCity())
									.append("zip", user.getAddress().getZip())
									.append("state",
											user.getAddress().getState())
									.append("country",
											user.getAddress().getCountry()))
					.append("dateCreated", user.getDateCreated())
					.append("company",
							new BasicDBObject("name", user.getCompany()
									.getName()).append("website", user
									.getCompany().getWebsite()))
					.append("profilePic", user.getProfilePic());

			coll.update(dbObject, newDocument);
			return "success";
		} catch (IllegalArgumentException e) {
			return "fail";
		} catch (NullPointerException e) {
			return "fail";
		}
	}
	
	public static User dbObjectToUser(DBObject updateDocument) {
		ObjectId id = (ObjectId) updateDocument.get("_id");
		String firstName = (String) updateDocument.get("firstName");
		String lastName = (String) updateDocument.get("lastName");
		String email = (String) updateDocument.get("lastName");
		String dateCreated = (String) updateDocument.get("dateCreated");
		String profilePic = (String) updateDocument.get("profilePic");

		DBObject addressDocument = (DBObject) updateDocument.get("address");
		String street = (String) addressDocument.get("street");
		String city = (String) addressDocument.get("city");
		String zip = (String) addressDocument.get("zip");
		String state = (String) addressDocument.get("state");
		String country = (String) addressDocument.get("country");

		Address address = new Address();
		address.setCity(city);
		address.setCountry(country);
		address.setState(state);
		address.setStreet(street);
		address.setZip(zip);

		DBObject companyDocument = (DBObject) updateDocument.get("company");
		String name = (String) companyDocument.get("name");
		String website = (String) companyDocument.get("website");

		Company company = new Company();
		company.setName(name);
		company.setWebsite(website);

		User user = new User();
		user.setId(id.toString());
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setAddress(address);
		user.setCompany(company);
		user.setDateCreated(dateCreated);
		user.setProfilePic(profilePic);

		return user;
	}


	
}
