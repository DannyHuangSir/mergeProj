package com.twfhclife.adm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.twfhclife.generic.model.Pagination;

import java.io.Serializable;
import java.util.Date;

public class QuestionaireVo extends Pagination {

    private String attributeName;
    private String answerMethod;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date effectiveStart;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date effectiveEnd;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date stopStart;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date stopEnd;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAnswerMethod() {
        return answerMethod;
    }

    public void setAnswerMethod(String answerMethod) {
        this.answerMethod = answerMethod;
    }

    public Date getEffectiveStart() {
        return effectiveStart;
    }

    public void setEffectiveStart(Date effectiveStart) {
        this.effectiveStart = effectiveStart;
    }

    public Date getEffectiveEnd() {
        return effectiveEnd;
    }

    public void setEffectiveEnd(Date effectiveEnd) {
        this.effectiveEnd = effectiveEnd;
    }

    public Date getStopStart() {
        return stopStart;
    }

    public void setStopStart(Date stopStart) {
        this.stopStart = stopStart;
    }

    public Date getStopEnd() {
        return stopEnd;
    }

    public void setStopEnd(Date stopEnd) {
        this.stopEnd = stopEnd;
    }
}