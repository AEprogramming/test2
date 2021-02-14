package se.atg.service.harrykart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to represent the elements of a Harry Kart race: Participants, Loops, and power-ups
 */
@JacksonXmlRootElement(localName = "harryKart")
public class HarryKart implements Serializable {
    int numberOfLoops;
    ArrayList<Participant> startList;
    ArrayList<Loop> powerUps;

    public HarryKart() {}


    public HarryKart(int numberOfLoops, ArrayList<Participant> startList, ArrayList<Loop> powerUps) {
        this.numberOfLoops = numberOfLoops;
        this.startList = startList;
        this.powerUps = powerUps;
    }


    public int getNumberOfLoops() {
        return numberOfLoops;
    }


    public ArrayList<Participant> getStartList() {
        return startList;
    }


    public ArrayList<Loop> getPowerUps() {
        return powerUps;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }


}
