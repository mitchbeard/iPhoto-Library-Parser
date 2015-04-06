package com.beardsinc.iphoto

import com.beardsinc.iphoto.model.Album
import com.beardsinc.iphoto.model.Library
import com.beardsinc.iphoto.model.Photo

String baseDir = "C:/Users/mbeard/Desktop/iphoto album data"

File steveData = new File(baseDir, "AlbumData-steve-1.xml")

File macbookCurrentData = new File(baseDir, "macbook-current-AlbumData.xml")

File macbookPre2009Data = new File(baseDir, "pre-2009-AlbumData.xml")
File macbook2009Data = new File(baseDir, "2009-AlbumData.xml")
File macbook2010Data = new File(baseDir, "2010-AlbumData.xml")
File macbook2011Data = new File(baseDir, "2011-AlbumData.xml")
File macbook2012Data = new File(baseDir, "2012-AlbumData.xml")
File macbook2013Data = new File(baseDir, "2013-AlbumData.xml")
File macbook2014Data = new File(baseDir, "2014-AlbumData.xml")
File macbookOldActiveDataData = new File(baseDir, "active-AlbumData.xml")

Library steveLib = Library.parse(steveData, "Steve");

Library macbookCurrentLib = Library.parse(macbookCurrentData, "macbook-current");

Library macbookPre2009Lib = Library.parse(macbookPre2009Data, "macbook-pre-2009");
Library macbook2009Lib = Library.parse(macbook2009Data, "macbook-2009");
Library macbook2010Lib = Library.parse(macbook2010Data, "macbook-2010");
Library macbook2011Lib = Library.parse(macbook2011Data, "macbook-2011");
Library macbook2012Lib = Library.parse(macbook2012Data, "macbook-2012");
Library macbook2013Lib = Library.parse(macbook2013Data, "macbook-2013");
Library macbook2014Lib = Library.parse(macbook2014Data, "macbook-2014");
Library macbookOldActiveLib = Library.parse(macbookOldActiveDataData, "macbook-old-active");


LibraryComparator.compareLibraries(steveLib, macbookCurrentLib)

LibraryComparator.compareLibraries(macbookPre2009Lib, steveLib)
LibraryComparator.compareLibraries(macbook2009Lib, steveLib)
LibraryComparator.compareLibraries(macbook2010Lib, steveLib)
LibraryComparator.compareLibraries(macbook2011Lib, steveLib)
LibraryComparator.compareLibraries(macbook2012Lib, steveLib)

LibraryComparator.compareLibraries(macbook2012Lib, macbookCurrentLib)
LibraryComparator.compareLibraries(macbook2013Lib, macbookCurrentLib)
LibraryComparator.compareLibraries(macbook2014Lib, macbookCurrentLib)
LibraryComparator.compareLibraries(macbookOldActiveLib, macbookCurrentLib)
