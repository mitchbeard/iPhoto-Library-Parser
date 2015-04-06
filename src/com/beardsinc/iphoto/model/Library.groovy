package com.beardsinc.iphoto.model

import groovy.transform.ToString
import xmlwise.Plist

@ToString(includeNames=true, excludes="albums, photos")
class Library {

	String name
	String location
	
	final List<Album> albums = []
	final Map<String, Photo> photos = [:]
	
	
	public static Library parse(File libraryData, String name) {
		
		/** Parse file and create Library*/
		
		print "Parsing file ${libraryData}... "
		long startMillis = System.currentTimeMillis()
		Map<String, Object> libraryProps = Plist.load(libraryData)
		
		long totalSeconds = (System.currentTimeMillis() - startMillis) / 1000
		println "parse completed in ${totalSeconds} seconds!"
		
		Library library = new Library(
			name: name,
			location: libraryProps["Archive Path"])
		
		
		/** Parse and create all individual Photos */
		
		libraryProps["Master Image List"].each { photoKey, photoProps ->
			
			Photo photo = new Photo(
				key: photoKey,
				caption: photoProps["Caption"],
				type: photoProps["MediaType"],
				path: photoProps["ImagePath"],
				createdDate: photoProps["DateAsTimerInterval"])
			
			library.photos[photo.key] = photo
		}
		
		println "Added ${library.photos.size()} total photos"
		
		
		/** Parse and create all Albums */
		
		libraryProps["List of Rolls"].each { Map<String, Object> albumData ->
			
			Album album = new Album(
				id: albumData["RollID"],
				uuid: albumData["ProjectUuid"],
				name: albumData["RollName"],
				rollDate: albumData["RollDateAsTimerInterval"],
				photoCount: albumData["PhotoCount"])
			
			albumData["KeyList"].each {String photoId ->
				if(!library.photos[photoId])
					System.err.println "Failed to find photo ${photoId}"
				else {
					album.photos << library.photos[photoId]
					album.photoHashCodes << library.photos[photoId].hashCode()
				}
			}
			
			if (album.photoCount.toInteger() != album.photos.size())
				System.err.println "Album photo count mismatch for album ${album.name}"
			
			library.albums << album
		}

		println "Added ${library.albums.size()} total albums"
				
		return library
	}
}
