package org.xiaotuitui.framework.domain.model;

public class PageObject {
	
	private Long totalRecord;
	
	private Long totalPage;

	private Long currentPage;
	
	private static Integer pageSize = 6;
	
	private static Integer pageBarSize = 6;
	
	public PageObject() {
		totalRecord = Long.valueOf(0);
		totalPage = Long.valueOf(0);
		currentPage = Long.valueOf(1);
	}
	
	private static Long calculateTotalPage(Long totalRecord){
		if(totalRecord==null||totalRecord<=0){
			return Long.valueOf(0);
		}
		if(totalRecord % pageSize==0){
			return totalRecord / pageSize;
		}else{
			return totalRecord / pageSize + 1;
		}
	}

	public Long getTotalRecord() {
		if(totalRecord==null||totalRecord<0){
			totalRecord = Long.valueOf(0);
		}
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
		this.totalPage = calculateTotalPage(totalRecord);
	}

	public Long getTotalPage() {
		if(totalPage==null||totalPage<0){
			totalPage = Long.valueOf(0);
		}
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

	public Long getCurrentPage() {
		if(currentPage==null||currentPage<=0){
			currentPage = Long.valueOf(1);
		}
		return currentPage;
	}

	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}

	public static Integer getPageSize() {
		return pageSize;
	}

	public static Integer getPageBarSize() {
		return pageBarSize;
	}

	public String toString() {
		return "PageObject [totalRecord=" + totalRecord + ", totalPage="
				+ totalPage + ", currentPage=" + currentPage + "]";
	}

}