package com.abid.learning.serialization.entity;

import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
