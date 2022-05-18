package com.csprs.cbw.service.page;


import java.util.List;

public interface PaginationInterface {
	
	List<?> getRightPage(int page);
	int getCurrentPage();
	int getMaxPage();
	List<?> getInputList();
	
}
