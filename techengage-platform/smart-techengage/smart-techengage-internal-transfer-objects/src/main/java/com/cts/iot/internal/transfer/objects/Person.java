package com.cts.iot.internal.transfer.objects;

public class Person {
    private String personIdentifier;
    private String personAlias;

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

    @Override
    public String toString() {
	return "Person [personIdentifier=" + personIdentifier + ", personAlias=" + personAlias + "]";
    }
    
    

}
