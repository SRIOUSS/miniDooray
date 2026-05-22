package com.nhnacademy.minidoorayfe.api;

import com.nhnacademy.minidoorayfe.dto.auth.AccountResponseDto;
import com.nhnacademy.minidoorayfe.dto.auth.SignFormDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(AccountApiClient.class)
class AccountApiClientTest {

    @TestConfiguration
    static class TestConfig {

        @Bean("gatewayRestClient")
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8000")
                    .build();
        }
    }

    @Autowired
    private AccountApiClient accountApiClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void findByUserId() {
        String userId = "testUser1234";
        String url = "http://localhost:8000/account-api/v1/accounts/login?userId=%s".formatted(userId);
        mockServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": 1,
                          "userId": "testUserId1234"
                        }
                        """, MediaType.APPLICATION_JSON));

        AccountResponseDto result = accountApiClient.findByUserId(userId);
        assertThat(result.getUserId()).isEqualTo("testUserId1234");

        mockServer.verify();
    }

    @Test
    void register() {
        String url = "http://localhost:8000/account-api/v1/accounts/register";
        mockServer.expect(requestTo(url))
                .andRespond(withSuccess());
        SignFormDto signFormDto = new SignFormDto();
        signFormDto.setUserId("testUser");
        signFormDto.setUserName("testName");
        signFormDto.setUserPassword("testPassword");
        signFormDto.setUserEmail("test@example.com");
        accountApiClient.register(signFormDto);
        mockServer.verify();
    }
}