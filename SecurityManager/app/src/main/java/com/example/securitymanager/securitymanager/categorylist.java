package com.example.securitymanager.securitymanager;

public class categorylist {
    private String CatId;
    private String CategoryName;

    public categorylist(String catId, String categoryName) {
        CatId = catId;
        CategoryName = categoryName;
    }

    public String getCatId() {
        return CatId;
    }

    public void setCatId(String catId) {
        CatId = catId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
