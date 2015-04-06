package com.beardsinc.iphoto.model

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includeNames=true)
@EqualsAndHashCode(includes="fileName,createdDate")
class Photo {

	String key
	String caption
	String type
	String path
	
	long createdDate
	
	public String getFileName() {
		return path.substring(path.lastIndexOf("/") + 1)
	}
}
