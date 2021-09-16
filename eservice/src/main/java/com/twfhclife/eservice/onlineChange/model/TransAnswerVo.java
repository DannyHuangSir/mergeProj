package com.twfhclife.eservice.onlineChange.model;

import com.google.common.collect.Lists;

import java.util.List;

public class TransAnswerVo {

    private String questions;
    private List<String> answers = Lists.newArrayList();
    private Integer score;
    private String desc;
    private String level;

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
