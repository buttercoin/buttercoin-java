package com.buttercoin.api;

import java.net.URL;

public interface HasPagination {
    void setNextPage(URL nextPage);

    void setPageSize(int pageSize);
}
