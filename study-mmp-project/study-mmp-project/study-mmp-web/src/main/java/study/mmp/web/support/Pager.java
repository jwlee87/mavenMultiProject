package study.mmp.web.support;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Data;

@Data
public class Pager {
	
	public static final int DEFAULT_PER_PAGE = 10;
	public static final int DEFAULT_INDEX_SIZE = 10;
	
    private int perPage = DEFAULT_PER_PAGE; // 한페이지에 노출될 데이터 갯수
    private int indexSize = DEFAULT_INDEX_SIZE; //페이지 이동을 위한 색인 수
    
    private int page; //현재 페이지 번호
    private int totalCount = 0; //총 데이터 갯수
    
    /**
     * 첫 페이지 색인을 반환한다
     */
    public int getFirstPage() {
        return ((page - 1) / indexSize) * indexSize + 1;
    }

    /**
     * 마지막 페이지 색인을 반환한다
     */
    public int getLastPage() {
        return Math.min(getFirstPage() + indexSize - 1, getTotalPage());
    }

    /**
     * 이전 페이지 색인을 반환한다
     */
    public int getPrevPage() {
        return (page > 1 ? page - 1 : 0);
    }

    /**
     * 다음 페이지 색인을 반환한다
     */
    public int getNextPage() {
        return (page < getTotalPage() ? page + 1 : 0);
    }

    /**
     * 이전 페이지 리스트 마지막 색인
     */
    public int getPrevLastPage() {
        return (getFirstPage() - 1 > 0 ? getFirstPage() - 1 : 1);
    }

    /**
     * 다음 페이지 리스트 첫번째 색인
     */
    public int getNextFirstPage() {
        return (getLastPage() + 1 < getTotalPage() ? getLastPage() + 1 : getTotalPage());
    }


    public int getPage() {
        if (this.page < 1) this.page = 1;
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return (totalCount - 1) / perPage + 1;
    }
    
    public int getStartNum() {
    	return (page - 1) * perPage;
    }
    
    public boolean isEnoughListQuery() {
    	return getStartNum() < totalCount;
    }
    
    public Map<String, Object> getParamsMap() {
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("startRowNum",  getStartNum());
    	params.put("perPage", perPage);
    	return params;
    }
}
