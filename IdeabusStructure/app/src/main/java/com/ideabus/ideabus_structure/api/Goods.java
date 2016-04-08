package com.ideabus.ideabus_structure.api;

/**
 * Created by Ting on 16/4/1.
 */
public class Goods {

    public static final String IMAGE_URL = "image_url";
    public static final String PAGE = "page";
    public static final String PAGE_SIZE = "pagesize";
    public static final String ID = "goods_id";
    public static final String NAME = "name";
    public static final String ACCOUNT_NAME = "account_name";
    public static final String KEYWORD = "keyword";
    public static final String ADDRESS = "address";
    public static final String INTRODUCTION = "introduction";
    public static final String THUMB_IMAGE = "thum_image";
    public static final String HIT = "hit";
    public static final String NEED = "need";
    public static final String LIKE = "like";
    public static final String SHOW_TIMES = "showtimes";
    public static final String LIKES = "likes";

    private String imageUrl;
    private String page;
    private String pageSize;
    private String id;
    private String name;
    private String accountName;
    private String keyword;
    private String address;
    private String introduction;
    private String thumbImageUrl;
    private String hit;
    private String need;
    private String like;
    private String showTimes;
    private String likes;

    public void setImageUrl(String s){
        imageUrl = s;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setPage(String s){
        page = s;
    }

    public String getPage(){
        return page;
    }

    public void setPageSize(String s){
        pageSize = s;
    }

    public String getPageSize(){
        return pageSize;
    }

    public void setId(String s){
        id = s;
    }

    public String getId(){
        return id;
    }

    public void setName(String s){
        name = s;
    }

    public String getName(){
        return name;
    }

    public void setAccountName(String s){
        accountName = s;
    }

    public String getAccountName(){
        return accountName;
    }

    public void setKeyword(String s){
        keyword = s;
    }

    public String getKeyword(){
        return keyword;
    }

    public void setAddress(String s){
        address = s;
    }

    public String getAddress(){
        return address;
    }

    public void setIntroduction(String s){
        introduction = s;
    }

    public String getIntroduction(){
        return introduction;
    }

    public void setThumbImageUrl(String s){
        thumbImageUrl = s;
    }

    public String getThumbImageUrl(){
        return thumbImageUrl;
    }

    public void setHit(String s){
        hit = s;
    }

    public String getHit(){
        return hit;
    }

    public void setNeed(String s){
        need = s;
    }

    public String getNeed(){
        return need;
    }

    public void setLike(String s){
        like = s;
    }

    public String getLike(){
        return like;
    }

    public void setShowTimes(String s){
        showTimes = s;
    }

    public String getShowTimes(){
        return showTimes;
    }

    public void setLikes(String s){
        likes = s;
    }

    public String getLikes(){
        return likes;
    }
}
