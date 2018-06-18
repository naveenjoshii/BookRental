package com.example.hp.bookrental;

/**
 * Created by HP on 16-06-2018.
 */

public class BooksDetails {
    private String mBookId;
    private String mUserId;
    private String mImageurl;
    private String mBookName;
    private String mAuthorName;
    private String mEditionNo;
    private String mStatus;
    private String mPrice;
    private String mDate;

    public BooksDetails() {
    }

    public BooksDetails(String mBookId,String mUserId,String mImageurl, String mBookName, String mAuthorName, String mEditionNo,String mStatus ,String mPrice,String mDate) {
        if(mAuthorName.trim().equals("")){
            mAuthorName="No Name";
        }
        this.mBookId = mBookId;
        this.mUserId = mUserId;
        this.mImageurl = mImageurl;
        this.mBookName = mBookName;
        this.mAuthorName = mAuthorName;
        this.mEditionNo = mEditionNo;
        this.mStatus = mStatus;
        this.mPrice = mPrice;
        this.mDate = mDate;
    }

    public String getmBookId() {
        return mBookId;
    }

    public void setmBookId(String mBookId) {
        this.mBookId = mBookId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmImageurl() {
        return mImageurl;
    }

    public void setmImageurl(String mImageurl) {
        this.mImageurl = mImageurl;
    }

    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public void setmAuthorName(String mAuthorName) {
        this.mAuthorName = mAuthorName;
    }

    public String getmEditionNo() {
        return mEditionNo;
    }

    public void setmEditionNo(String mEditionNo) {
        this.mEditionNo = mEditionNo;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
