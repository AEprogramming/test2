package se.atg.service.harrykart.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class LoopInfo {
    private int loopNumber;
   private List<LaneInfo> laneInfoList = new ArrayList<>();

    public LoopInfo() {
    }

    public LoopInfo(int loopNumber, List<LaneInfo> laneInfoList) {
        this.loopNumber = loopNumber;
        this.laneInfoList = laneInfoList;
    }

    public int getLoopNumber() {
        return loopNumber;
    }

    public void setLoopNumber(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public List<LaneInfo> getLaneInfoList() {
        return laneInfoList;
    }

    public void setLaneInfoList(List<LaneInfo> laneInfoList) {
        this.laneInfoList = laneInfoList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
