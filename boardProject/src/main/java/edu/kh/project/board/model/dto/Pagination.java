package edu.kh.project.board.model.dto;

/**
 * lombok을 사용하지 않는 이유 : 모든 변수에 대하여 getter, setter를 생성하고 + 매개변수 생성자에서 호출을 해야 하기 때문
 * Pagination : 목록을 일정 페이지로 분할해서 원하는 페이지를 볼 수 있게 하는 것 (페이징 처리)
 * Pagination 객체 : 
 */
public class Pagination {
	private int currentPage;		// 현재 페이지 번호
	private int listCount;			// 전체 게시글 수
	
	private int limit = 10;			// 한 페이지 목록에 보여지는 게시글 수
	private int pageSize = 10;		// 보여질 페이지 번호 개수
	
	// 가장 첫페이지는 당연히 1페이지 -> 그래서 minPage는 따로없음
	private int maxPage;			// 마지막 페이지 번호
	private int startPage;			// 보여지는 맨 앞 페이지 번호
	private int endPage;			// 보여지는 맨 뒤 페이지 번호
	
	private int prevPage;			// 이전 페이지 모음의 마지막 번호
	private int nextPage;			// 다음 페이지 모음의 시작 번호
	
	// 기본 생성자를 만들지 않은 이유 > 필요가 없음!
	// 매개변수에 들어오는 내용이 없음 > 값을 계산할 때 기준으로 삼을 수 없음
	
	public Pagination(int currentPage, int listCount) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		
		// Pagination 객체를 만들 때, 값에 따라 계속 변화하는 cp, 전체 게시글 수 계산해주기
		calculate();
	}

	public Pagination(int currentPage, int listCount, int limit, int pageSize) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		this.limit = limit;
		this.pageSize = pageSize;
		
		// Pagination 객체를 만들 때, 값에 따라 계속 변화하는 cp, 전체 게시글 수 계산해주기
		calculate();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getListCount() {
		return listCount;
	}

	public int getLimit() {
		return limit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		calculate();
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
		calculate();
	}

	public void setLimit(int limit) {
		this.limit = limit;
		calculate();
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		calculate();
	}

	@Override
	public String toString() {
		return "Pagination [currentPage=" + currentPage + ", listCount=" + listCount + ", limit=" + limit
				+ ", pageSize=" + pageSize + ", maxPage=" + maxPage + ", startPage=" + startPage + ", endPage="
				+ endPage + ", prevPage=" + prevPage + ", nextPage=" + nextPage + "]";
	}
	
	/**
	 * 페이징 처리에 필요한 값 계산 > 필드에 대입 (startPage, endPage, maxPage, prevPage, nextPage)
	 */
	private void calculate() {
		// maxPage : 최대 페이지 = 마지막 페이지 = 총 페이지 수
		// 한 페이지에 게시글이 10개(limit)씩 보여질 경우 > 게시글 수 95개 : 10page, 게시글 수 100개 : 10page...
		maxPage = (int)Math.ceil((double)listCount/limit);
		
		// startPage : 페이지 번호 목록의 시작 번호
		// 페이지 번호 목록이 10(pageSize)개씩 보여지는 경우 > 현재 페이지가 1~10page 사이 : 1page, 11~20page 사이 : 11page
		startPage = (currentPage - 1) / pageSize * pageSize + 1;
		
		// endPage : 페이지 번호 목록의 끝 번호 > 현재 페이지가 1~10page 사이 : 10page, 11~20page 사이 : 20page
		endPage = pageSize - 1 + startPage;
		
		// 페이지 끝번호가 최대 페이지 수를 초과한 경우
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		// prevPage : "<" 클릭 시 이동할 페이지 번호 (이전 레벨 페이지 번호 목록 중 마지막 번호 11에서 이전 > 10으로)
		// 더이상 이전 페이지가 없을 경우(맨 앞 페이지(1page)일 경우)
		if(currentPage <= pageSize) {
			prevPage = 1;
		} else {
			prevPage = startPage - 1;
		}
		
		// nextPage : ">" 클릭 시 이동할 페이지 번호 (다음 레벨 페이지 번호 목록 중 시작 번호 56>61)
		// 더이상 넘어갈 페이지가 없을 경우 > 맨 마지막 페이지로
		if(endPage == maxPage) {
			nextPage = maxPage;
		} else {
			nextPage = endPage + 1;
		}
	}
}