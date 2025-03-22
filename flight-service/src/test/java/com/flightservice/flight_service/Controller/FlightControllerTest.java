package com.flightservice.flight_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightservice.flight_service.Entity.Flight;
import com.flightservice.flight_service.Service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
        flight.setId(1);
        flight.setAirlineName("Air India");
        flight.setFlightNumber("AI123");
        flight.setSource("DEL");
        flight.setDestination("MUM");
        flight.setFlightDate(LocalDate.of(2025, 3, 25));
        flight.setDepartureTime(LocalTime.of(10, 30));
        flight.setArrivalTime(LocalTime.of(12, 30));
        flight.setPrice(new BigDecimal("3500"));
        flight.setSeatsAvailable(100);
    }

    @Test
    void testFindFlightsBySourceAndDestination() throws Exception {
        when(flightService.searchFlights("DEL", "MUM", flight.getFlightDate()))
                .thenReturn(Collections.singletonList(flight));

        mockMvc.perform(get("/flights/search")
                        .param("source", "DEL")
                        .param("destination", "MUM")
                        .param("flightDate", "2025-03-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("AI123"));
    }

    @Test
    void testFindFlightByFlightNumberAndDate() throws Exception {
        when(flightService.searchFlightByNumber("AI123", flight.getFlightDate()))
                .thenReturn(flight);

        mockMvc.perform(get("/flights/AI123")
                        .param("flightDate", "2025-03-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airlineName").value("Air India"));
    }

    @Test
    void testAddFlight() throws Exception {
        when(flightService.addFlight(any(Flight.class))).thenReturn(flight);

        mockMvc.perform(post("/flights/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AI123"));
    }

    @Test
    void testUpdateFlight() throws Exception {
        when(flightService.updateFlight(eq(1), any(Flight.class))).thenReturn(flight);

        mockMvc.perform(put("/flights/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AI123"));
    }

    @Test
    void testUpdateFlightFields() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("airlineName", "SpiceJet");
        updates.put("price", "5500");

        flight.setAirlineName("SpiceJet");
        flight.setPrice(new BigDecimal("5500"));

        when(flightService.updateFlightFields(eq(1), anyMap())).thenReturn(flight);

        mockMvc.perform(patch("/flights/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airlineName").value("SpiceJet"))
                .andExpect(jsonPath("$.price").value(5500));
    }

    @Test
    void testDeleteFlight() throws Exception {
        when(flightService.deleteFlight(1)).thenReturn(true);

        mockMvc.perform(delete("/flights/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight deleted successfully."));
    }
}

