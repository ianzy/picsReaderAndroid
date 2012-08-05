package com.zy.android.dal;

import java.util.List;

import com.zy.android.dos.Item;

public interface IItemsDAO {
	List<Item> getNewItems(String feedUrl) throws Exception;
}
