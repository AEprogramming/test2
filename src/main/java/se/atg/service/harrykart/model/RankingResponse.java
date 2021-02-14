package se.atg.service.harrykart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankingResponse {

    @JsonProperty("ranking")
    private List<Ranking> ranking;

    private String errorMessage;


    public RankingResponse() {
    }

    public RankingResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RankingResponse(List<Ranking> ranking) {
        this.ranking = ranking;
    }

    public List<Ranking> getRanking() {
        return ranking;
    }

    public void setRanking(List<Ranking> ranking) {
        this.ranking = ranking;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
