depth-wise-json-serializer
==========================

In this blog I will show you, how you can serialize a entity  depth wise.

Why we need it
==============
Very often we land in situation with cyclic reference if target entity has self references. There are multiple solution available  over internet, but none of these will solve your problem. The only option is left to write own depth wise serializer.


This example is based on spring-data-jpa. Hence forth I will use entity which is used in a spring-data-jpa project. 
Our target entity is User which  extends AbstractAuditableEntity. Here we have two properties createdBy and lastModifiedBy  referencing to User entity.

Entity 
=======

```java

@SuppressWarnings("serial")
@Entity
@Table(name = "user")
public class User extends AbstractAuditableEntity {

	private String firstName;
	
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}


@SuppressWarnings("serial")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditableEntity extends AbstractPersistable<Long> implements Auditable<User, Long> {

    @Version
    protected Long version;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    protected DateTime createdDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    protected DateTime lastModifiedDate;

    protected User createdBy;

    protected User lastModifiedBy;

    @Enumerated(EnumType.STRING)
    protected StatusType status;

    public Long getVersion() {
	    return version;
    }

    public void setVersion(Long version) {
	    this.version = version;
    }
  
    public DateTime getCreatedDate() {
	    return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
	    this.createdDate = createdDate;
    }

    public DateTime getLastModifiedDate() {
	    return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
	    this.lastModifiedDate = lastModifiedDate;
    }

    public User getCreatedBy() {
	    return createdBy;
    }

    public void setCreatedBy(User createdBy) {
	    this.createdBy = createdBy;
    }

    public User getLastModifiedBy() {
	    return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
	    this.lastModifiedBy = lastModifiedBy;
    }

    public StatusType getStatus() {
	    return status;
    }

    public void setStatus(StatusType status) {
	    this.status = status;
    }

}
```

Serializer
==========

I will use thread local variable to determine depth of recursion with initial value 0.

```java
private static ThreadLocal<Integer> depth = new ThreadLocal<Integer>() {

		@Override
		protected Integer initialValue() {
			return 0;
		}

	};
```


In this serializer if depth of recursion is more than 2, recursion will return null.
```java
depth.set(depth.get() + 1);
if (depth.get() > 2) {
				jgen.writeNull();
} else {
  ...
}
```

Complete serializer
```java
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
				
				...
			  
			  User createdByUser = value.getCreatedBy();
				if (null != createdByUser) {
	        jgen.writeStringField("createdBy", createdByUser.getFirstName() + " " + createdByUser.getLastName());
				}

				User lastModifiedByUser = value.getLastModifiedBy();
				if (null != lastModifiedByUser) {
					jgen.writeStringField("lastModifiedBy", lastModifiedByUser.getFirstName() + " " + lastModifiedByUser.getLastName());
				}
        ....
				jgen.writeEndObject();

				depth.set(0);
				return;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
```







