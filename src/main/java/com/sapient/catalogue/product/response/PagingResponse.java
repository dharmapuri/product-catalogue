package com.sapient.catalogue.product.response;

import lombok.Data;

@Data
public class PagingResponse {

    private boolean hashNextPage;
    private boolean hashPreviousPage;
    private long totalNumberOfRecords;
    private long totalNoOfPages;
    private long pageNumber;
    private long pageSize;
}
