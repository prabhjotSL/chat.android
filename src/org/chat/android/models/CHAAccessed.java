package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "cha_accessed")
public class CHAAccessed {
    @DatabaseField
    private int client_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
    private Date start_time;
    @DatabaseField
    private Date end_time;

    /**
     * Default Constructor needed by ormlite
     */
    public CHAAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param client_id
     * @param visit_id
     * @param start_time
     * 
     */
    public CHAAccessed(int client_id, int visit_id, Date start_time) {
    	this.client_id = client_id;
    	this.visit_id = visit_id;
    	this.start_time = start_time;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public CHAAccessed(CHAAccessed existingServicesAccessedModel) {
        this.client_id = existingServicesAccessedModel.client_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
        this.start_time = existingServicesAccessedModel.start_time;
        this.end_time = existingServicesAccessedModel.end_time;
    }

	public int getClientId() {
		return client_id;
	}
	
	public void setClientId(int client_id) {
		this.client_id = client_id;
	}	
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public Date setStartTime() {
		return start_time;
	}

	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	
	public Date setEndTime() {
		return end_time;
	}

	public void setEndTime(Date end_time) {
		this.end_time = end_time;
	}
	
}