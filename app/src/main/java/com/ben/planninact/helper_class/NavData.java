package com.ben.planninact.helper_class;


import com.ben.planninact.room_persitence.entity.PartDetailsContent;
import com.ben.planninact.room_persitence.entity.PartDetailsTitle;

import java.util.ArrayList;
import java.util.List;

public class NavData {

    List<PartDetailsTitle> partDetailsTitleList = new ArrayList<>();
    List<PartDetailsContent> partDetailsContentList = new ArrayList<>();

    public NavData() {
    }

    public NavData(List<PartDetailsTitle> partDetailsTitleList, List<PartDetailsContent> partDetailsContentList) {
        this.partDetailsTitleList = partDetailsTitleList;
        this.partDetailsContentList = partDetailsContentList;
    }

    public List<PartDetailsTitle> getPlupaInfoList() {
        return partDetailsTitleList;
    }

    public void setPlupaInfoList(List<PartDetailsTitle> partDetailsTitleList) {
        this.partDetailsTitleList = partDetailsTitleList;
    }

    public List<PartDetailsContent> getPlupaDataInfoList() {
        return partDetailsContentList;
    }

    public void setPlupaDataInfoList(List<PartDetailsContent> partDetailsContentList) {
        this.partDetailsContentList = partDetailsContentList;
    }
}
