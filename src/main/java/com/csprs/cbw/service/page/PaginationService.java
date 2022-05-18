package com.csprs.cbw.service.page;


import java.util.ArrayList;
import java.util.List;



public class PaginationService implements PaginationInterface{
	private List<?> originList = new ArrayList<>();
	private List<List<?>> resultList = new ArrayList<>();
	private int CurrentPage = 1;
			
	public PaginationService(List<?> lists, int perPageMany) {
		this.originList = lists;
		createPagination(perPageMany);
	}
	
	/** perPageMany = 5; // 每頁幾筆
 	 * */
	private void createPagination(int perPageMany) {
		int pageCount = 1; // 需要幾頁
		
		// 計算每頁會有多少筆資料
		if(this.originList.size() % perPageMany != 0) { // 有餘
			pageCount = (this.originList.size() / perPageMany) + 1;
		}else {
			pageCount = (this.originList.size() / perPageMany);
		}
		//System.out.println("總共需要頁數: " + pageCount);
		
		for(int currentPage=1; currentPage<pageCount+1; currentPage++) {
			int start = (currentPage-1) * perPageMany;
			int end = currentPage * perPageMany;
			if(end > this.originList.size()) {
				resultList.add(this.originList.subList(start, this.originList.size()));
			}else {
				resultList.add(this.originList.subList(start, end));
			}
		}
	}
	
	public List<?> getRightPage(int page){
		if(page <= resultList.size()) {
			CurrentPage = page;
			return resultList.get(page-1);
		}else {
			CurrentPage = 1;
			return resultList.get(0);
		}
	}
	
	public int getCurrentPage() {
		return CurrentPage;
	}
	
	public int getMaxPage() {
		return resultList.size();
	}
	
	public List<?> getInputList(){
		return originList;
	}
}
