package com.twfhclife.eservice.api.jdzq.domain;

import com.google.common.collect.Lists;
import com.twfhclife.eservice.api.jdzq.model.NoteNotifyVo;

import java.io.Serializable;
import java.util.List;

public class NoteNotifyDataResponse implements Serializable {

    private List<NoteNotifyVo> noteNotifies = Lists.newArrayList();

    public List<NoteNotifyVo> getNoteNotifies() {
        return noteNotifies;
    }

    public void setNoteNotifies(List<NoteNotifyVo> noteNotifies) {
        this.noteNotifies = noteNotifies;
    }
}
