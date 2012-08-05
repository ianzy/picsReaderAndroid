package com.zy.android.xmlparser;

public abstract class FeedParserFactory {
//	static String feedUrl = "http://jandan.net/pic/feed";
	
	public static FeedParser getParser(String feedUrl){
		return getParser(ParserType.ANDROID_SAX, feedUrl);
	}
	
	public static FeedParser getParser(ParserType type, String feedUrl){
		switch (type){
			case ANDROID_SAX:
				return new AndroidSaxFeedParser(feedUrl);
			default: return null;
		}
	}
}
