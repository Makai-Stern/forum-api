package io.makai.payload.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PostSearchDto {

    @NotBlank
    @Size(max = 100)
    private String term;

    private int pageSize;

    private int pageNumber = 0;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
