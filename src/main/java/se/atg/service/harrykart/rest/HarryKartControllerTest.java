package se.atg.service.harrykart.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import se.atg.service.harrykart.exception.InvalidSpeed;
import se.atg.service.harrykart.exception.NumberOfLoopsAndPowerUpsMismatchException;
import se.atg.service.harrykart.model.HarryKart;
import se.atg.service.harrykart.model.Participant;
import se.atg.service.harrykart.model.Ranking;
import se.atg.service.harrykart.model.RankingResponse;
import se.atg.service.harrykart.services.HarryKartPlayService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HarryKartControllerTest {
    XmlMapper mapper;
    ObjectWriter ow;
    HarryKartPlayService harryKartPlay;


    @Before
    public void setUp() {
        mapper = new XmlMapper();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        harryKartPlay = new HarryKartPlayService();
    }


    private String readFileToString(String filename) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        Objects.requireNonNull(in);
        String xmlString = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            xmlString = br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
            return xmlString;
        }
        return xmlString;
    }


    @Test
    public void lanesFinishInOrderTest() throws IOException {
        // given
        String inputXML = readFileToString("input_0.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        List<Ranking> actualRanking = new HarryKartPlayService().play(hk).getBody().getRanking();
        ArrayList<Ranking> expectedRanking = new ArrayList<>();
        expectedRanking.add(new Ranking(1, "TIMETOBELUCKY"));
        expectedRanking.add(new Ranking(2, "HERCULES BOKO"));
        expectedRanking.add(new Ranking(3, "CARGO DOOR"));
        // Compare expected and actual JSON results
        String resultJson = ow.writeValueAsString(actualRanking);
        String expectedJson = ow.writeValueAsString(expectedRanking);
        assertEquals(resultJson, expectedJson);
    }


    @Test
    public void twoWayTieTest() throws IOException {
        String inputXML = readFileToString("_TwoWayTieTest.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        Integer actual = 1;
        ;
        // Calculate the race results
        List<Ranking> actualRanking = new HarryKartPlayService().play(hk).getBody().getRanking();
        // Verify the finishing position of each participant
        assertEquals(actualRanking.get(0).getPosition(), actual);
        assertEquals(actualRanking.get(0).getHorse(), "WAIKIKI SILVIO");
        actual = 2;
        assertEquals(actualRanking.get(1).getPosition(), actual);
        assertEquals(actualRanking.get(1).getHorse(), "HERCULES BOKO");
        String thirdPlace = actualRanking.get(2).getHorse();
        actual = 3;
        assertEquals(actualRanking.get(2).getPosition(), actual);
        assertEquals(actualRanking.get(3).getPosition(), actual);
        assertTrue(thirdPlace.equals("CARGO DOOR") || thirdPlace.equals("TIMETOBELUCKY"));

    }


    /**
     * All participants finish at the same time
     */
    @Test
    public void allWayTieTest() throws IOException {
        String inputXML = readFileToString("_AllWayTieTest.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        // Calculate the race results
        List<Ranking> actualRanking = new HarryKartPlayService().play(hk).getBody().getRanking();
        // Verify that the finishing position of each participant is #1
        Integer actual = 1;
        actualRanking.forEach(ranking -> assertEquals(ranking.getPosition(), actual));
    }


    @Test
    public void invalidXmlFormatTest() throws NumberOfLoopsAndPowerUpsMismatchException, IOException {
        String inputXML = readFileToString("_InvalidHarryKartFormatTest.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        ResponseEntity<RankingResponse> actualResult = new HarryKartPlayService().play(hk);
        int resultJsonStatusCode = actualResult.getStatusCode().value();
        int expectedStatusCode = 400;
        assertEquals(resultJsonStatusCode, expectedStatusCode);

        String actualErrorMessage = actualResult.getBody().getErrorMessage();
        String expectedErrorMessage = "Invalid number of powerUps for loops. 7 loops must have 6 powerUps.";
        assertEquals(actualErrorMessage, expectedErrorMessage);
    }


    @Test
    public void zeroAndNegativePowerTest() throws IOException {
        String inputXML = readFileToString("_ZeroAndNegativePowerTest.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        List<Ranking> actualRanking = new HarryKartPlayService().play(hk).getBody().getRanking();
        // Expected race outcome
        ArrayList<Ranking> expectedRanking = new ArrayList<>();
        expectedRanking.add(new Ranking(1, "WAIKIKI SILVIO"));
        expectedRanking.add(new Ranking(2, "HERCULES BOKO"));
        // Compare expected and actual JSON results
        String resultJson = ow.writeValueAsString(actualRanking);
        String expectedJson = ow.writeValueAsString(expectedRanking);
        assertEquals(resultJson, expectedJson);
    }

    @Test
    public void calculateTimeForParticipantTest() throws IOException {

        String inputXML = readFileToString("input_1.xml");
        HarryKart harryKart = mapper.readValue(inputXML, HarryKart.class);
        BigDecimal resultTime = harryKartPlay.calculateTimeForParticipant(new Participant(1, "TIMETOBELUCKY", 10), harryKart);
        BigDecimal expectedTime = BigDecimal.valueOf(250.00000).setScale(5, RoundingMode.DOWN);
        assertEquals(resultTime, expectedTime);
    }


    @Test(expected = InvalidSpeed.class)
    public void calculateTimeForParticipantZeroSpeedExceptionTest() throws IOException {
        String inputXML = readFileToString("input_1.xml");
        HarryKart harryKart = mapper.readValue(inputXML, HarryKart.class);
//        participant with 0 baseSpeed
        Participant participant = new Participant(1, "TIMETOBELUCKY", 0);
        harryKartPlay.calculateTimeForParticipant(participant, harryKart);

    }


    @Test(expected = InvalidSpeed.class)
    public void calculateTimeForParticipantNegativeSpeedExceptionTest() throws IOException {
        String inputXML = readFileToString("input_1.xml");
        HarryKart harryKart = mapper.readValue(inputXML, HarryKart.class);
//        participant with 0 baseSpeed
        Participant participant = new Participant(2, "CARGO DOOR", -1);
        harryKartPlay.calculateTimeForParticipant(participant, harryKart);

    }


    @Test
    public void calculateTimeForLaneTest() {
        ArrayList<Integer> powerUps = new ArrayList<>();
        Collections.addAll(powerUps, 5, 10);
        BigDecimal resultTime = harryKartPlay.calculateTimeForLane(3, 10, powerUps);
        BigDecimal expectedTime = BigDecimal.valueOf(206.66666).setScale(5, RoundingMode.DOWN);
        assertEquals(resultTime, expectedTime);
    }


    @Test
    public void calculateTimeForLaneZeroPowerUpsLaneTest() {
        ArrayList<Integer> powerUps = new ArrayList<>();
        Collections.addAll(powerUps, 0, 0);
        BigDecimal resultTime = harryKartPlay.calculateTimeForLane(3, 10, powerUps);
        BigDecimal expectedTime = BigDecimal.valueOf(300.00000).setScale(5, RoundingMode.DOWN);
        assertEquals(resultTime, expectedTime);
    }


    @Test(expected = NumberOfLoopsAndPowerUpsMismatchException.class)
    public void calculateTimeForLaneInvalidPowerUpsNumberLaneTest() {
        ArrayList<Integer> powerUps = new ArrayList<>();
//        3 powerUps for 3 loop is invalid
        Collections.addAll(powerUps, 1, 2, 3);
        harryKartPlay.calculateTimeForLane(3, 10, powerUps);
    }

    @Test(expected = InvalidSpeed.class)
    public void calculateTimeForLaneZeroSpeedInOnOfLoopsTest() {
        ArrayList<Integer> powerUps = new ArrayList<>();
        Collections.addAll(powerUps, 5, -15);
        harryKartPlay.calculateTimeForLane(3, 10, powerUps);

    }

    @Test(expected = InvalidSpeed.class)
    public void calculateTimeForLaneNegativeSpeedTest() {
        ArrayList<Integer> powerUps = new ArrayList<>();
        Collections.addAll(powerUps, 5, -30);
        harryKartPlay.calculateTimeForLane(3, 10, powerUps);

    }


}