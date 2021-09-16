package com.twfhclife.eservice.onlineChange.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QuestionVo implements Serializable {

    private String question;

    private String title;

    private String isMulti;

    private List<OptionVo> options = Lists.newArrayList();

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OptionVo> getOptions() {
        return options;
    }

    public void setOptions(List<OptionVo> options) {
        this.options = options;
    }

    public String getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(String isMulti) {
        this.isMulti = isMulti;
    }
}