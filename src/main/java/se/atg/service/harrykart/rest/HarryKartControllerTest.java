package se.atg.service.harrykart.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;
import se.atg.service.harrykart.exception.NumberOfLoopsAndPowerUpsMismatchException;
import se.atg.service.harrykart.model.HarryKart;
import se.atg.service.harrykart.model.Ranking;
import se.atg.service.harrykart.services.HarryKartPlayService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HarryKartControllerTest {

    XmlMapper mapper;
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Before
    public void setUp() throws Exception {
        mapper = new XmlMapper();
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
        String inputXML = readFileToString("input_0.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        // Calculate the race results
        List<Ranking> actualRanking = new HarryKartPlayService().play(hk).getBody().getRanking();
        // Expected race outcome
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

//        *************************************************************
//        String fourthPlace = actualRanking.get(3).getHorse();
//        assertEquals(actualRanking.get(3).getPosition(), actual);
//        assertTrue(fourthPlace.equals("CARGO DOOR") || fourthPlace.equals("TIMETOBELUCKY"));
    }

    //
//    /**
//     * All participants finish at the same time
//     * @throws HarryKartException
//     */
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

//    /**
//     * Less than 4 participants doesn't make a race (it throws an exception)
//     * @throws HarryKartException
//     */
//    @Test(expected=HarryKartException.class)
//    public void minimumParticipantTest() throws HarryKartException {
//        String inputXML = readFileToString("_MinimumNumberOfParticipants.xml");
//        HarryKart hk = hkSerializer.deserializeFromXML(inputXML);
//    }

    @Test
    public void invalidXmlFormatTest() throws NumberOfLoopsAndPowerUpsMismatchException, IOException {
        String inputXML = readFileToString("_InvalidHarryKartFormatTest.xml");
        HarryKart hk = mapper.readValue(inputXML, HarryKart.class);
        int resultJsonStatusCode = new HarryKartPlayService().play(hk).getStatusCode().value();
        int expectedStatusCode = 400;
        assertEquals(resultJsonStatusCode, expectedStatusCode);
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

}