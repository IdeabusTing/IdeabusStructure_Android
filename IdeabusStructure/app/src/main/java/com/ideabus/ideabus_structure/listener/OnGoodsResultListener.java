package com.ideabus.ideabus_structure.listener;

import com.ideabus.ideabus_structure.api.Goods;
import com.ideabus.ideabuslibrary.BasePostAPI;

import java.util.ArrayList;

/**
 * Created by Ting on 16/4/7.
 */
public interface OnGoodsResultListener {
    /**
     * 商品資訊牆回傳
     * @param goodsArray 商品資訊 {@link Goods}
     */
    void onGoodsResult(ArrayList<Goods> goodsArray);
    /**
     * Post失敗
     * @param apiUrl {@link BasePostAPI#sendToServer}的apiUrl
     * @param errorCode {@link } -- API未定義
     * @param errorMessage Error Message
     */
    void onPostFailed(String apiUrl, int errorCode, String errorMessage);
}
