package com.poscodx.jblog.vo;

public class BlogVo {
	private String id;
	private String title;
	private String logo;
	
	public BlogVo(String userId, String title, String logo) {
		this.id = userId;
		this.title = title;
		this.logo = logo;
	}
	
	public String getUserId() {
		return id;
	}
	public void setUserId(String userId) {
		this.id = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@Override
	public String toString() {
		return "BlogVo [userId=" + id + ", title=" + title + ", logo=" + logo + "]";
	}
}
