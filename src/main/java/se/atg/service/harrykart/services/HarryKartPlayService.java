package se.atg.service.harrykart.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.atg.service.harrykart.exception.InvalidSpeed;
import se.atg.service.harrykart.exception.NumberOfLoopsAndPowerUpsMismatchException;
import se.atg.service.harrykart.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Class to handle Harry Kart race result calculation and return
 */
@Service
public class HarryKartPlayService implements HarryKartPlayInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(HarryKartPlayService.class);
    private static final int LOOP_LENGTH = 1000;
    private static final int LOOP_TIME_ROUNDING_SCALE = 5;

    @Override
    public ResponseEntity<RankingResponse> play(HarryKart harryKart) {
        LOGGER.info("play(), start, input: " + harryKart.toString());
        Map<String, BigDecimal> map = new HashMap<>();
        for (Participant participant : harryKart.getStartList()) {
            try {
                try {
                    map.put(participant.getName(), calculateTimeForParticipant(participant, harryKart));
                } catch (InvalidSpeed e) {
                    LOGGER.info(participant.getName() + " ignored");
                }
            } catch (NumberOfLoopsAndPowerUpsMismatchException e) {

                return new ResponseEntity<>(new RankingResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
            }

        }

        AtomicInteger rank = new AtomicInteger(1);
        AtomicReference<BigDecimal> time = new AtomicReference<>(BigDecimal.ZERO);
        final List<Ranking> sortedByCount = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(stringBigDecimalEntry -> {
                    if (time.get().compareTo(BigDecimal.ZERO) == 0) {
                        time.set(stringBigDecimalEntry.getValue());
                        return new Ranking(rank.get(), stringBigDecimalEntry.getKey());
                    } else if (time.get().compareTo(stringBigDecimalEntry.getValue()) != 0) {
                        time.set(stringBigDecimalEntry.getValue());
                        return new Ranking(rank.addAndGet(1), stringBigDecimalEntry.getKey());
                    } else {
                        return new Ranking(rank.get(), stringBigDecimalEntry.getKey());
                    }
                }).filter(ranking -> rank.get() < 4).collect(Collectors.toList());

        RankingResponse rankingResponse = new RankingResponse(sortedByCount);
        LOGGER.info("play(), finished, output: " + rankingResponse.toString());

        return new ResponseEntity<>(rankingResponse, HttpStatus.OK);
    }


    public BigDecimal calculateTimeForParticipant(Participant participant, HarryKart harryKart) throws InvalidSpeed {
        LOGGER.info("calculateTimeForParticipant(), start, input: " + participant.toString() + " " + harryKart.toString());
        List<Integer> powerUpsInt = new ArrayList<>();
        for (Loop loop : harryKart.getPowerUps()) {
            powerUpsInt.add(loop.getLanes().stream().filter(lane -> lane.getNumber() == participant.getLane()).findFirst().get().getPowerValue());
        }
        BigDecimal calculatedTime = calculateTimeForLane(harryKart.getNumberOfLoops(), participant.getBaseSpeed(), powerUpsInt);
        LOGGER.info("calculateTimeForParticipant(), start, output: " + calculatedTime);
        return calculatedTime;
    }


    public BigDecimal calculateTimeForLane(int numberOfLoops, int baseSpeed, List<Integer> powerUps) throws InvalidSpeed {
        LOGGER.info("calculateTimeForLane(), input: numberOfLoops = " + numberOfLoops + ", " + "baseSpeed = " + baseSpeed + ", powerUps = " + powerUps);
        if (numberOfLoops != powerUps.size() + 1) {
            throw new NumberOfLoopsAndPowerUpsMismatchException("Invalid number of powerUps for loops. " + numberOfLoops + " loops must have " + (numberOfLoops - 1) + " powerUps.");
        }
        BigDecimal elapsedTime = calculateTimeForLoop(baseSpeed, LOOP_LENGTH);

        for (int powerUp : powerUps) {
            elapsedTime = elapsedTime.add(calculateTimeForLoop(baseSpeed += powerUp, LOOP_LENGTH));
        }

        LOGGER.info("calculateTimeForLane(), output: " + elapsedTime);
        return elapsedTime;
    }

    private BigDecimal calculateTimeForLoop(int speed, int loopLength) throws InvalidSpeed {
        LOGGER.info("calculateTimeForLoop(), start, input: speed = " + speed + ", loopLength = " + loopLength);

        BigDecimal timeForLoop;
        if (speed <= 0) {
            throw new InvalidSpeed("Invalid speed. speed should be more than 0.");
        }
        timeForLoop = BigDecimal.valueOf(loopLength).divide(BigDecimal.valueOf(speed), LOOP_TIME_ROUNDING_SCALE, RoundingMode.DOWN);
        LOGGER.info("calculateTimeForLoop(), finished, output: " + timeForLoop);
        return timeForLoop;
    }

}
