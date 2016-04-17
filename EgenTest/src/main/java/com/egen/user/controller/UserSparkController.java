package com.egen.user.controller;

import spark.Spark;

import com.egen.user.service.UserService;
import com.egen.util.JacksonUtil;

public class UserSparkController {

	private static final int HTTP_BAD_REQUEST = 404;

	public static void main(String[] args) {
		
		// To create user
		Spark.post("/user", (request, response) -> {
			response.type("application/json");
			response.status(404);
			return new UserService().createUser(JacksonUtil
					.jsonToObject(request.body()));
		});

		// To get all users
		Spark.get("/user", (request, response) -> {
			response.type("application/json");
			return new UserService().getUsers();
		});
		
		// To update user
		Spark.put(
				"/user",
				(request, response) -> {

					String result = new UserService().updateUser(JacksonUtil
							.jsonToObject(request.body()));
					response.type("application/json");
					if (result.equalsIgnoreCase("success")) 
					{
						return "User updated successfully";
					} 
					else 
					{
						response.status(HTTP_BAD_REQUEST);
						return "Failed to update ,user not found.";
					}

				});
		
	}

}
