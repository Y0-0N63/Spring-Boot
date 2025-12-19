package edu.kh.project.board.model.dto;

/**
 * lombok을 사용하지 않는 이유 : 모든 변수에 대하여 getter, setter를 생성하고 + 매개변수 생성자에서 호출을 해야 하기 때문
 * Pagination : 목록을 일정 페이지로 분할해서 원하는 페이지를 볼 수 있게 하는 것 (페이징 처리)
 * Pagination 객체 : 
 */
public class Pagination {
	private int currentPage;		// 현재 페이지 번호
	private int listCount;			// 전체 게시글 수
	
	private int limit = 10;			// 한 페이지 목록에 보여지는 게시글 수 (세로 목록)
	private int pageSize = 10;		// 보여질 페이지 번호 개수 (가로 버튼)
	
	// 가장 첫페이지는 당연히 1페이지 -> 그래서 minPage는 따로없음
	private int maxPage;			// 마지막 페이지 번호
	private int startPage;			// 보여지는 맨 앞 페이지 번호
	private int endPage;			// 보여지는 맨 뒤 페이지 번호
	
	private int prevPage;			// 이전 페이지 모음의 마지막 번호
	private int nextPage;			// 다음 페이지 모음의 시작 번호
	
	// 기본 생성자를 만들지 않은 이유 > 필요가 없음!
	// 매개변수에 들어오는 내용이 없음 > 값을 계산할 때 기준으로 삼을 수 없음
	
	// 기본값(Default)를 쓸 때
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

	// 값의 변화에 대응하기 위해 > setter가 필요 >> calculate()에 사용하는 변수들
	// 사용자가 1페이지를 보다 > 다른 페이지를 클릭하면 > 값이 변화해야 > 재계산이 필요 (직접 입력하는 값이 아닌 경우)
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		calculate();
	}

	// 목록 개수를 변경할 때 사용(10개씩 보기, 20개씩 보기 등...)
	public void setListCount(int listCount) {
		this.listCount = listCount;
		calculate();
	}

	// 하단 버튼 개수를 동적으로 조절해야 할 때 사용됨
	public void setLimit(int limit) {
		this.limit = limit;
		calculate();
	}

	// 검색 결과가 0일 때 등 강제로 값을 고정해야 할 때
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
		// 현재 페이지에서 1을 빼서 > 0부터 시작하게 만듦 > page Size로 나눠 몇 번째 묶음인지 알아내 > +1로 묶음의 시작 번호 보정
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