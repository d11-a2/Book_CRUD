package com.bookscrud;

import java.util.List;

public class SearchCriteria {

    private String type;
    private String attribute = "";

    private List<String> criteriaArray;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public List<String> getCriteriaArray() {
        return criteriaArray;
    }

    public void setCriteriaArray(List<String> criteriaArray) {
        this.criteriaArray = criteriaArray;
    }
}
