package com.abid.learning.serialization.serializer;

import java.io.IOException;

import com.abid.learning.serialization.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class UserJsonSerializer extends JsonSerializer<User> {

	private static ThreadLocal<Integer> depth = new ThreadLocal<Integer>() {

		@Override
		protected Integer initialValue() {
			return 0;
		}

	};

	@Override
	public void serialize(User value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		depth.set(depth.get() + 1);
		try {
			if (depth.get() > 2) {
				jgen.writeNull();
			} else {

				jgen.writeStartObject();
				jgen.writeStringField("firstName", value.getFirstName());
				jgen.writeStringField("lastName", value.getLastName());

				User createdByUser = value.getCreatedBy();
				if (null != createdByUser) {
					jgen.writeStringField(
							"createdBy",
							createdByUser.getFirstName() + " "
									+ createdByUser.getLastName());
				}

				User lastModifiedByUser = value.getLastModifiedBy();
				if (null != lastModifiedByUser) {
					jgen.writeStringField("lastModifiedBy",
							lastModifiedByUser.getFirstName() + " "
									+ lastModifiedByUser.getLastName());
				}

				jgen.writeEndObject();

				return;

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
