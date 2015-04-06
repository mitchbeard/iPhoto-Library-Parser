package com.beardsinc.iphoto.model

import groovy.transform.ToString

@ToString(includeNames=true, excludes="photos")
class Album {

	String id
	String uuid
	long rollDate;
	
	String name
	int photoCount
	
	final List<Photo> photos = []
	final Set<Integer> photoHashCodes = new HashSet()
}
