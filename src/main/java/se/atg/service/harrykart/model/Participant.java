package se.atg.service.harrykart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Class representation of the racers by lane number, horse name, and initial speed
 */
@JacksonXmlRootElement(localName = "participant")
public class Participant implements Serializable {
    @JacksonXmlProperty
    int lane;

    @JacksonXmlProperty
    String name;

    @JacksonXmlProperty
    int baseSpeed;

    public Participant() {}


    public Participant(int lane, String name, int baseSpeed) {
        this.lane = lane;
        this.name = name;
        this.baseSpeed = baseSpeed;
    }


    public int getLane() {
        return lane;
    }


    public String getName() {
        return name;
    }


    public int getBaseSpeed() {
        return baseSpeed;
    }


    public void setBaseSpeed(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
