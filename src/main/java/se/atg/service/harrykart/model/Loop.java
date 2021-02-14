package se.atg.service.harrykart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representation of a loop within the race
 */
@JacksonXmlRootElement(localName = "loop")
public class Loop implements Serializable {
    @JacksonXmlProperty(isAttribute = true)
    int number;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "lane")
    ArrayList<Lane> lanes;

    public Loop() {}


    public Loop(int number, ArrayList<Lane> lanes) {
        this.number = number;
        this.lanes = lanes;
    }


    public int getNumber() {
        return number;
    }


    public ArrayList<Lane> getLanes() {
        return lanes;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
