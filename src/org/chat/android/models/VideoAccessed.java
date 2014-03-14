package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "videos_accessed")
public class VideoAccessed {
	@DatabaseField(generatedId = true)
	private int id;
    @DatabaseField
    private int video_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
	private Date date;


    /**
     * Default Constructor needed by ormlite
     */
    public VideoAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param video_id
     * @param visit_id
     * @param date
     * 
     */
    public VideoAccessed(int video_id, int visit_id, Date date) {
    	this.video_id = video_id;
    	this.visit_id = visit_id;
    	this.date = date;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public VideoAccessed(VideoAccessed existingVideosAccessedModel) {
        this.video_id = existingVideosAccessedModel.video_id;
        this.visit_id = existingVideosAccessedModel.visit_id;
        this.date = existingVideosAccessedModel.date;
    }
    
    public int getId() {
		return id;
	}

	public int getVideoId() {
		return video_id;
	}
	
	public void setVideoId(int video_id) {
		this.video_id = video_id;
	}
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
}