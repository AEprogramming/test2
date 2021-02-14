package se.atg.service.harrykart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


public class Lane implements Serializable {
    @JacksonXmlProperty(isAttribute = true)
    int number;

    @JacksonXmlText
    int powerValue;

    public Lane() {}


    public Lane(int number, int powerValue) {
        this.number = number;
        this.powerValue = powerValue;
    }

    public int getNumber() {
        return number;
    }

    public int getPowerValue() {
        return powerValue;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
