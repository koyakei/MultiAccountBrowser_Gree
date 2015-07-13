package com.koyakei.MUB;

import java.io.Serializable;

class Tasks implements Serializable {
	  private static final long serialVersionUID = 8023254505558453097L;
	  String name;
	  String url;
	  String cookie;
	  int is_finished;

	  Tasks(String name, String url, String cookie){
	    this.name = name;
	    this.url = url;
	    this.cookie = cookie;
	    //this.is_finished = is_finished;
	  }

	  public String getName(){
	    return this.name;
	  }
	  public void setName(String name){
		    this.name = name;
	  }

	  public String getURL(){
	    return this.url;
	  }
	  public void setURL(String url){
	    this.url = url;
	  }

	  public String getCookie(){
	    return this.cookie;
	  }
	  public void setCookie(String cookie){
	    this.cookie = cookie;
	  }

	  public int getIsFinished(){
	    return this.is_finished;
	  }
	  public void setIsFinished(int flg){
	    this.is_finished = flg;
	  }
	}