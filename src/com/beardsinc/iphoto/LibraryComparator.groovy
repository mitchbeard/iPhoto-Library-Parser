package com.beardsinc.iphoto;

import java.util.List;

import com.beardsinc.iphoto.model.Album;
import com.beardsinc.iphoto.model.Library;

public class LibraryComparator {

	private static interface AlbumMatcher {
		String match(Album a1, Album a2);
	}

	private static final List<AlbumMatcher> exactMatchers = []
	private static final List<AlbumMatcher> fuzzyMatchers = []
	
	static {
		exactMatchers << ({ Album a1, Album a2 ->
			
			return (
				a1.name == a2.name
				&& a1.rollDate == a2.rollDate
				&& a1.photoCount == a2.photoCount
				
				? "Name and Date and Size" : null)
		} as AlbumMatcher)

		exactMatchers << ({ Album a1, Album a2 ->
			
			return (
				a1.name == a2.name
				&& a1.photoCount == a2.photoCount
				
				? "Name and Size" : null)
		} as AlbumMatcher)

		exactMatchers << ({ Album a1, Album a2 ->
			
			return (
				a1.rollDate == a2.rollDate
				&& a1.photoCount == a2.photoCount
				
				? "Date and Size" : null)
		} as AlbumMatcher)
		
		
		fuzzyMatchers << ({ Album a1, Album a2 ->
			
			int shareCount = a1.photoHashCodes.intersect(a2.photoHashCodes).size()
			
			return shareCount > 0 ? "${shareCount} Shared Photos".toString() : null;
		} as AlbumMatcher)

		fuzzyMatchers << ({ Album a1, Album a2 ->
			
			return a1.name == a2.name ? "Name" : null
		} as AlbumMatcher)

		fuzzyMatchers << ({ Album a1, Album a2 ->
			
			return a1.photoCount == a2.photoCount ? "Size" : null
		} as AlbumMatcher)
	}
	
	public static void compareLibraries(Library fromLibrary, Library toLibrary) {

		println "Comparing ${fromLibrary.name} to ${toLibrary.name}"
		
		List<List> albumMatches = []
		List fuzzyMatches = []

		int exactMatchCount = 0
		int fuzzyMatchCount = 0
		
		fromLibrary.albums.each { fromAlbum ->

			boolean match = false
			
			for (Album toAlbum : toLibrary.albums) {
				
				if (match) break
				
				exactMatchers.each { AlbumMatcher matcher ->
					if (match) return
					
					String matchResult = matcher.match(fromAlbum, toAlbum)
					if (matchResult) {
						albumMatches << [fromAlbum, toAlbum, matchResult]
						exactMatchCount++
						match = true
					}
				}
			}
			
			if (!match) {
				albumMatches << [fromAlbum, null, "Not Matched"]
				
				Map<Album, List<String>> matchReasons = [:]

				for (Album toAlbum : toLibrary.albums) {
					fuzzyMatchers.each { AlbumMatcher matcher ->
						
						String matchResult = matcher.match(fromAlbum, toAlbum)
						
						if (matchResult) {
							if (!matchReasons[toAlbum])
								matchReasons[toAlbum] = []
							
							matchReasons[toAlbum] << matchResult
						}
					}
				}
				
				if (!matchReasons) {
					fuzzyMatches << [fromAlbum, null, "No fuzzy matches"]
				}
				else {
					fuzzyMatchCount++
					
					matchReasons.each { album, reasons ->
						fuzzyMatches << [fromAlbum, album, reasons.join("|")]
					}
				}
			}
		}
		
		println "Writing exact match file"
		
		new File("${fromLibrary.name}-${toLibrary.name}-exact.tsv").withWriter { writer ->
			writer.writeLine([
				"${fromLibrary.name} Album Name",
				"${fromLibrary.name} Album Size",
				"${toLibrary.name} Album Name",
				"${toLibrary.name} Album Size",
				"Match Type"
				].join("\t"))
			
			albumMatches.each {
				writer.writeLine([
					it[0].name,
					it[0].photoCount,
					it[1]?.name,
					it[1]?.photoCount,
					it[2]
					].join("\t"))
			}
		}
		
		println "Writing fuzzy match file"
		
		new File("${fromLibrary.name}-${toLibrary.name}-fuzzy.tsv").withWriter { writer ->
			writer.writeLine([
				"${fromLibrary.name} Album Name",
				"${fromLibrary.name} Album Size",
				"${toLibrary.name} Album Name",
				"${toLibrary.name} Album Size",
				"Fuzzy Match Types"
				].join("\t"))

			fuzzyMatches.each {
				writer.writeLine([
					it[0].name,
					it[0].photoCount,
					it[1]?.name,
					it[1]?.photoCount,
					it[2]
					].join("\t"))
			}
		}
		
		println "\n${fromLibrary.name}/${toLibrary.name} Comparison Stats:"
		println "${fromLibrary.name} total albums: ${fromLibrary.albums.size()}"
		println "${toLibrary.name} total albums: ${toLibrary.albums.size()}"
		println "${fromLibrary.name} albums exact matched: ${exactMatchCount}"
		println "${fromLibrary.name} albums fuzzy matched: ${fuzzyMatchCount}"
		println "${fromLibrary.name} albums unmatched: ${(fromLibrary.albums.size() - exactMatchCount - fuzzyMatchCount)}"
		
	}
}
