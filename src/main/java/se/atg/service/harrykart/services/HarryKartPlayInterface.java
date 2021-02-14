package se.atg.service.harrykart.services;

import org.springframework.http.ResponseEntity;
import se.atg.service.harrykart.model.HarryKart;
import se.atg.service.harrykart.model.RankingResponse;

public interface HarryKartPlayInterface {
    public ResponseEntity<RankingResponse> play(HarryKart harryKart);
}
