package com.es.core.util;

import java.util.List;

public class PaginationResult<T> {
    private int itemsNumber;
    private int itemsNumberPerPage;
    private int pagesNumber;
    private List<T> pageItems;

    public PaginationResult() {
    }

    public PaginationResult(int itemsNumber, int itemsNumberPerPage, List<T> pageItems) {
        this.itemsNumber = itemsNumber;
        this.itemsNumberPerPage = itemsNumberPerPage;
        this.pageItems = pageItems;

        this.pagesNumber = this.itemsNumber / this.itemsNumberPerPage;
        if (this.itemsNumber % this.itemsNumberPerPage != 0) {
            this.pagesNumber += 1;
        }
    }

    public int getItemsNumber() {
        return itemsNumber;
    }

    public void setItemsNumber(int itemsNumber) {
        this.itemsNumber = itemsNumber;
    }

    public int getItemsNumberPerPage() {
        return itemsNumberPerPage;
    }

    public void setItemsNumberPerPage(int itemsNumberPerPage) {
        this.itemsNumberPerPage = itemsNumberPerPage;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public List<T> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<T> pageItems) {
        this.pageItems = pageItems;
    }
}
