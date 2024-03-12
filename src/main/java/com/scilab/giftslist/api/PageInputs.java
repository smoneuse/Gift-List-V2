package com.scilab.giftslist.api;

import com.scilab.giftslist.infra.errors.BadRequestException;

public record PageInputs(int page, int pageSize) {
    public static final int MIN_PAGE=0;
    public static final int MAX_PER_PAGE=20;

    public PageInputs(int page, int pageSize){
        if(page < MIN_PAGE || pageSize > MAX_PER_PAGE){
            throw new BadRequestException("Paging data not supported page :"+page+" Per page :"+pageSize);
        }
        this.page=page;
        this.pageSize=pageSize;
    }
}
