package com.twfhclife.eservice.api.jdzq.model;

import java.io.Serializable;

public class PolicyClaimSubDetailVo implements Serializable {

    private String noteDate;

    private String dueDate;

    private String contentMemo;

    private String itemContent;

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getContentMemo() {
        return contentMemo;
    }

    public void setContentMemo(String contentMemo) {
        this.contentMemo = contentMemo;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
}


