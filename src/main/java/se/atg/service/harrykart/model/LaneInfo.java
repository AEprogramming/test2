package se.atg.service.harrykart.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class LaneInfo {
    private int laneNumber;
    List<Integer> powerUps;
    ParticipantInfo participantInfo;

    public  LaneInfo() {
    }

    public LaneInfo(int laneNumber, List<Integer> powerUps, ParticipantInfo participantInfo) {
        this.laneNumber = laneNumber;
        this.powerUps = powerUps;
        this.participantInfo = participantInfo;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public List<Integer> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(List<Integer> powerUps) {
        this.powerUps = powerUps;
    }

    public ParticipantInfo getParticipantInfo() {
        return participantInfo;
    }

    public void setParticipantInfo(ParticipantInfo participantInfo) {
        this.participantInfo = participantInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
