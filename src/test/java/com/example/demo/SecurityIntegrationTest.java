package com.example.demo;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.entity.*;
import com.example.demo.enums.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.util.PasswordEncoder;
import tools.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private BaseRepository baseRepository;

        @Autowired
        private EquipmentTypeRepository equipmentTypeRepository;

        @Autowired
        private InventoryRepository inventoryRepository;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private Base baseA;
        private Base baseB;
        private EquipmentType ammo;
        private EquipmentType vehicle;
        private User adminUser;
        private User commanderAUser;
        private User commanderBUser;
        private User logisticsUser;

        @BeforeEach
        void setUp() {
                baseA = baseRepository.save(new Base("Alpha Base", "Location A"));
                baseB = baseRepository.save(new Base("Bravo Base", "Location B"));

                ammo = equipmentTypeRepository.save(
                                new EquipmentType("5.56mm Ammunition", "Ammunition", SerializationType.QUANTITY));
                vehicle = equipmentTypeRepository.save(
                                new EquipmentType("Humvee", "Vehicle", SerializationType.SERIALIZED));

                adminUser = userRepository.save(
                                new User("admin", passwordEncoder.encode("password123"), Role.ADMIN, null));
                commanderAUser = userRepository.save(
                                new User("cmdrA", passwordEncoder.encode("password123"), Role.BASE_COMMANDER, baseA));
                commanderBUser = userRepository.save(
                                new User("cmdrB", passwordEncoder.encode("password123"), Role.BASE_COMMANDER, baseB));
                logisticsUser = userRepository.save(
                                new User("logistics", passwordEncoder.encode("password123"), Role.LOGISTICS_OFFICER,
                                                null));

                inventoryRepository.save(new Inventory(baseA, ammo, 1000));
                inventoryRepository.save(new Inventory(baseB, ammo, 500));
        }

        private String login(String username, String password) throws Exception {
                LoginRequest request = new LoginRequest(username, password);
                MvcResult result = mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andReturn();

                Cookie[] cookies = result.getResponse().getCookies();
                if (cookies != null) {
                        for (Cookie cookie : cookies) {
                                if ("auth_token".equals(cookie.getName())) {
                                        return cookie.getValue();
                                }
                        }
                }
                throw new RuntimeException("auth_token cookie not found in login response");
        }

        @Test
        void testValidLogin() throws Exception {
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new LoginRequest("admin", "password123"))))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value("admin"))
                                .andExpect(jsonPath("$.role").value("ADMIN"));
        }

        @Test
        void testInvalidLogin() throws Exception {
                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new LoginRequest("admin", "wrongpassword"))))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testMissingJwt() throws Exception {
                mockMvc.perform(get("/api/inventory"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testInvalidJwt() throws Exception {
                mockMvc.perform(get("/api/inventory")
                                .cookie(new Cookie("auth_token", "invalidtoken")))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testExpiredJwt() throws Exception {
                String expiredToken = Jwts.builder()
                                .setSubject(String.valueOf(adminUser.getId()))
                                .claim("username", "admin")
                                .claim("role", "ADMIN")
                                .setIssuedAt(Date.from(Instant.now().minusSeconds(7200)))
                                .setExpiration(Date.from(Instant.now().minusSeconds(3600)))
                                .signWith(
                                                Keys.hmacShaKeyFor(
                                                                "defaultSecretKeyForDevelopmentOnlyChangeInProduction1234567890"
                                                                                .getBytes(StandardCharsets.UTF_8)),
                                                SignatureAlgorithm.HS256)
                                .compact();

                mockMvc.perform(get("/api/inventory")
                                .cookie(new Cookie("auth_token", expiredToken)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testAdminAccess() throws Exception {
                String token = login("admin", "password123");
                mockMvc.perform(get("/api/inventory")
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isOk());
        }

        @Test
        void testBaseCommanderOwnAccess() throws Exception {
                String token = login("cmdrA", "password123");
                mockMvc.perform(get("/api/inventory")
                                .param("baseId", baseA.getId().toString())
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isOk());
        }

        @Test
        void testBaseCommanderCrossBaseAccess() throws Exception {
                String token = login("cmdrA", "password123");
                mockMvc.perform(get("/api/inventory")
                                .param("baseId", baseB.getId().toString())
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testLogisticsOfficerInventoryAccess() throws Exception {
                String token = login("logistics", "password123");
                mockMvc.perform(get("/api/inventory")
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isOk());
        }

        @Test
        void testLogisticsOfficerCannotAccessAssignments() throws Exception {
                String token = login("logistics", "password123");
                mockMvc.perform(get("/api/assignments")
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testNonAdminCannotAccessUsers() throws Exception {
                String token = login("cmdrA", "password123");
                mockMvc.perform(get("/api/users")
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void testAdminCanAccessUsers() throws Exception {
                String token = login("admin", "password123");
                mockMvc.perform(get("/api/users")
                                .cookie(new Cookie("auth_token", token)))
                                .andExpect(status().isOk());
        }
}
