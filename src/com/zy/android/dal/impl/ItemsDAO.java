package com.zy.android.dal.impl;

import java.util.List;
import java.util.regex.Pattern;

import com.zy.android.dal.IItemsDAO;
import com.zy.android.dos.Item;
import com.zy.android.xmlparser.FeedParserFactory;

public class ItemsDAO implements IItemsDAO {
	
	private ItemsDAO() {
		
	}
	
	private static class SingletonHolder { 
        public static final ItemsDAO instance = new ItemsDAO();
	}
	
	public static ItemsDAO getInstance() {
	        return SingletonHolder.instance;
	}

	@Override
	public List<Item> getNewItems(String feedUrl) throws Exception {
		List<Item> items = FeedParserFactory.getParser(feedUrl).parse();
		for(Item item:items) {
        	Pattern p2 = Pattern.compile("(.*)<img src=\"(.+(jpg|png|gif))\"\\s?/>(.*)", Pattern.DOTALL);
        	//Jandan case
        	String content = item.getContent() == null ? item.getDescription() : item.getContent();
        	item.setImageUrl(p2.matcher(content).replaceAll("$2"));
        }
		return items;
	}

}
