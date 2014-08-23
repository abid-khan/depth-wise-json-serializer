package com.abid.learning.serialization.entity;

import org.junit.Test;

import com.abid.learning.serialization.serializer.UserJsonSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class UserTest {

	@Test
	public void serialize() throws JsonProcessingException {

		User user = new User();
		user.setFirstName("First Name");
		user.setLastName("Last Name");
		user.setCreatedBy(user);
		user.setLastModifiedBy(user);

		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule userModule = new SimpleModule();
		userModule.addSerializer(User.class, new UserJsonSerializer());
		objectMapper.registerModule(userModule);

		String userString = objectMapper.writeValueAsString(user);
		System.out.println("JSON String :: " + userString);
	}
}
