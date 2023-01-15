package com.darwishkolbwolf.infosec;

public class Actor {

    private String actorID = "";
    private String lastName = "";
    private String firstName = "";
    private String lastUpdate = "";

    public Actor(String actorID, String lastName, String firstName, String lastUpdate) {
        this.actorID = actorID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.lastUpdate = lastUpdate;
    }

    public Actor() {
    }
    

    public String getActorID() {
        return actorID;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Actor [actorID=" + actorID + ", lastName=" + lastName + ", firstName=" + firstName + ", lastUpdate="
                + lastUpdate + "]";
    }

    

}
