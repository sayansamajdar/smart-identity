/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cts.iot.internal.transfer.objects;

/**
 *
 * @author 307132
 */
public class PersonResult {
    private String createdAt;
    private String objectId;
    private String updatedAt;
    private String personIdentifier;
    private String personAlias;

    public String getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
    }

    public String getObjectId() {
	return objectId;
    }

    public void setObjectId(String objectId) {
	this.objectId = objectId;
    }

    public String getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
	this.updatedAt = updatedAt;
    }

    public String getPersonIdentifier() {
	return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
	this.personIdentifier = personIdentifier;
    }

    public String getPersonAlias() {
	return personAlias;
    }

    public void setPersonAlias(String personAlias) {
	this.personAlias = personAlias;
    }

}
