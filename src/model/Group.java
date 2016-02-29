package model;

import java.util.ArrayList;

public class Group {
	private String groupID;
	private String groupName;
	private String owner;
	private ArrayList<String> members;
	
	public Group(String groupID, String groupName, String owner) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.owner = owner;
		this.members = new ArrayList<String>();
	}
	
	
	public String getGroupID() {
		return groupID;
	}


	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}


	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<String> members) {
		this.members = members;
	}
	
	
	
}
