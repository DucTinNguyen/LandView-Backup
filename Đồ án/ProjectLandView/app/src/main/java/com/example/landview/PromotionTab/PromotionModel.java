package com.example.landview.PromotionTab;

import java.io.Serializable;

public class PromotionModel implements Serializable {
    private int mImgSale;
    private String mTextsale,mTextSaledes,mExp;

    public PromotionModel() {
    }

    public PromotionModel(int mImgSale, String mTextsale, String mTextSaledes, String mExp) {
        this.mImgSale = mImgSale;
        this.mTextsale = mTextsale;
        this.mTextSaledes = mTextSaledes;
        this.mExp = mExp;
    }

    public int getmImgSale() {
        return mImgSale;
    }

    public void setmImgSale(int mImgSale) {
        this.mImgSale = mImgSale;
    }

    public String getmTextsale() {
        return mTextsale;
    }

    public void setmTextsale(String mTextsale) {
        this.mTextsale = mTextsale;
    }

    public String getmTextSaledes() {
        return mTextSaledes;
    }

    public void setmTextSaledes(String mTextSaledes) {
        this.mTextSaledes = mTextSaledes;
    }

    public String getmExp() {
        return mExp;
    }

    public void setmExp(String mExp) {
        this.mExp = mExp;
    }
}
