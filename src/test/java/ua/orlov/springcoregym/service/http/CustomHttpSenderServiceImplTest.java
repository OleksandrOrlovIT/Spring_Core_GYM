package ua.orlov.springcoregym.service.http;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.model.HttpRequest;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomHttpSenderServiceImplTest {

    @Mock
    private CloseableHttpClient httpClient;

    @InjectMocks
    private CustomHttpSenderServiceImpl customHttpSenderService;

    @Test
    void executeRequestWithEntityShouldHandleStatusCode200() throws Exception {
        HttpRequest request = mock(HttpRequest.class);
        String entity = "{\"key\":\"value\"}";

        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = mock(StatusLine.class);
        HttpEntity mockHttpEntity = mock(HttpEntity.class);

        when(httpClient.execute(any(HttpRequest.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(200);
        when(mockHttpEntity.getContent()).thenReturn(new ByteArrayInputStream("Success".getBytes()));
        when(mockResponse.getEntity()).thenReturn(mockHttpEntity);

        String result = customHttpSenderService.executeRequestWithEntity(request, entity);

        assertEquals("Success", result);
        verify(httpClient).execute(any(HttpRequest.class));
    }

    @Test
    void executeRequestWithEntityShouldHandleStatusCode100() throws Exception {
        HttpRequest request = mock(HttpRequest.class);
        String entity = "{\"key\":\"value\"}";

        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        StatusLine mockStatusLine = mock(StatusLine.class);

        when(httpClient.execute(any(HttpRequest.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
        when(mockStatusLine.getStatusCode()).thenReturn(100);

        String result = customHttpSenderService.executeRequestWithEntity(request, entity);

        assertTrue(result.startsWith("Exception: Request failed: Mock for StatusLine"));
        verify(httpClient, times(1)).execute(any(HttpRequest.class));
    }
}