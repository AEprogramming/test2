package se.atg.service.harrykart.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.atg.service.harrykart.model.HarryKart;
import se.atg.service.harrykart.model.RankingResponse;
import se.atg.service.harrykart.services.HarryKartPlayInterface;
import se.atg.service.harrykart.services.HarryKartPlayService;

@RestController
@RequestMapping("/api")
public class HarryKartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HarryKartController.class);
    private final HarryKartPlayInterface harryKartPlayService;

    @Autowired
    public HarryKartController(HarryKartPlayService harryKartPlayService) {
        this.harryKartPlayService = harryKartPlayService;
    }


    @RequestMapping(method = RequestMethod.POST, path = "/play", consumes = "application/xml", produces = "application/json")
    public ResponseEntity<RankingResponse> playHarryKart(@RequestBody HarryKart harryKart) {
        LOGGER.info("Called Url: /api/play,  RequestBody: " + harryKart);
        return harryKartPlayService.play(harryKart);
    }
}
