package study.mmp.common.component.http;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.ParseException;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import study.mmp.common.component.http.ApiRequestContext.LogLevel;
 


/**
 * RestTemplate 대신 ApiHelper, ApiRequestContext를 사용하는 경우,
 * 
 * 1. requests별로, domain별  request config설정을 다르게 가져가고 싶을 때
 * 2. Euc-kr json 통신시  restTemplate의 mapper동작 시, 인코딩 오류 - 버젼확인
 * 3. 요청, 응답에 대한 자세한 로그 추적
 * 4. timeout에 대한 추적
 * 5. request별 timeout 설정 불가
 * 
 * 아니라면 restTemplate() 호출하여 RestTemplate객체 사용하시면 됩니다.
 * - thread safe
 */
@ThreadSafe
@Slf4j
public class ApiHelper {

    /**
     * Connection 설정
     */
    public final static int MAX_CONNECTION = 200;                                 /* http-client의 최대 Connection 갯 수*/
    public final static int MAX_CONNECTIONS_PER_ROUTE = 200;                /* host당 최대 Connection 갯 수  MAX_CONNECTION > MAX_CONNECTIONS_PER_ROUTE*/

    
    /**
     * Default Request Config - Time out 설정
     */
    public final static int REQUEST_READ_TIME_OUT = 7000;                         /* 응답값 읽는 동안 packet이 없을 경우  3->5초로 늘림*/
    public final static int REQUEST_CONNECTION_TIME_OUT = 5000;               /* 연결 수립까지의 time out 설정*/
    public final static int REQUEST_CONNECTION_REQUEST_TIME_OUT = 5000;  /* ???  3->5초로 늘림*/

    private CloseableHttpClient httpClient;
    private RestTemplate restTemplate;

    final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.ZZZ");
    
    /**
     * 생성자 - httpClient 생성 - defalut client-config, defalut request-config
     */
    public ApiHelper() {
        httpClient  = createHttpClient(createDefaultRequestConfig());
        
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    /**
     * Custom RequestConfig
     * @param ApiRequestContext
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws ParseException
     * @throws HttpResponseException
     */
    public String execute(ApiRequestContext context)  {
        
    	try {
            HttpUriRequest request = context.getRequest();
            LogLevel logLevel = context.getLogLevel();
            
            CloseableHttpResponse response = null;
            long startTime = System.currentTimeMillis();
            context.setRequestTime(startTime);
            
            try {
                response = httpClient.execute(request);
                
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity, context.getResponseEncodingSet());
                
                if (logLevel == LogLevel.INFO) {
                    log.info("------------------------------------------------------------------------------------------------------------------------------------------");
                    log.info("request Time={}", dateFormat.format(startTime));
                    log.info("response Time={}", dateFormat.format(System.currentTimeMillis()));
                    log.info("{} : {}", request.getMethod(), request.getURI());
                    if (request instanceof HttpEntityEnclosingRequestBase) {
                    	HttpEntityEnclosingRequestBase r = (HttpEntityEnclosingRequestBase) request;
                        log.info("contentType  : " + r.getEntity().getContentType());
                        log.info("param : " + EntityUtils.toString(r.getEntity()));
                    }
                    log.info("status : {}", response.getStatusLine());
                    log.info("result : {}", result);
                    log.info("------------------------------------------------------------------------------------------------------------------------------------------");
                    
                } else  if (logLevel == LogLevel.DEBUG) {
                    log.debug("------------------------------------------------------------------------------------------------------------------------------------------");
                    log.debug("{} : {}", request.getMethod(), request.getURI());
                    if (request instanceof HttpEntityEnclosingRequestBase) {
                    	HttpEntityEnclosingRequestBase r = (HttpEntityEnclosingRequestBase) request;
                        log.debug("contentType  : " + r.getEntity().getContentType());
                        log.debug("param : " + EntityUtils.toString(r.getEntity()));
                    }
                    log.debug("status : {}", response.getStatusLine());
                    log.debug("result : {}", result);
                    log.debug("------------------------------------------------------------------------------------------------------------------------------------------");
                }

                
                if (!context.isIgnoreResponseCode() && response.getStatusLine().getStatusCode() >= 300) {
                    throw new HttpResponseException(response.getStatusLine().getStatusCode() , response.getStatusLine().getReasonPhrase());
                }
                return result;
            } catch (Exception e) {
                if (logLevel == LogLevel.INFO) {
                    log.info("------------------------------------------------------------------------------------------------------------------------------------------");
                    log.info("request Time={}", dateFormat.format(startTime));
                    log.info("response Time={}", dateFormat.format(System.currentTimeMillis()));
                    log.info("{} : {}", request.getMethod(), request.getURI());
                    if (request instanceof HttpEntityEnclosingRequestBase) {
                    	HttpEntityEnclosingRequestBase p = (HttpEntityEnclosingRequestBase) request;
                        log.info("contentType  : " + p.getEntity().getContentType());
                        log.info("param : " + EntityUtils.toString(p.getEntity()));
                    }
                    log.info("error : {}", e.getMessage());
                    log.info("------------------------------------------------------------------------------------------------------------------------------------------");
                    
                } else  if (logLevel == LogLevel.DEBUG) {
                    log.debug("------------------------------------------------------------------------------------------------------------------------------------------");
                    log.debug("{} : {}", request.getMethod(), request.getURI());
                    if (request instanceof HttpEntityEnclosingRequestBase) {
                    	HttpEntityEnclosingRequestBase p = (HttpEntityEnclosingRequestBase) request;
                        log.debug("contentType  : " + p.getEntity().getContentType());
                        log.debug("param : " + EntityUtils.toString(p.getEntity()));
                    }
                    log.info("error : {}", e.getMessage());
                    log.debug("------------------------------------------------------------------------------------------------------------------------------------------");
                }
                throw e;
            } finally {
                if (response != null) {
                    response.close();
                }
            }
    	} catch (IOException ioe) {
            throw new ApiProtocolException(ioe, context);
    	}
    }

    public RestTemplate restTemplate() {
        return restTemplate;
    }
    
    public CloseableHttpClient client() {
        return httpClient;
    }
    
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }
    
    
    private CloseableHttpClient createHttpClient(RequestConfig defaultRequestConfig) {
        return HttpClients.custom().
                setMaxConnTotal(MAX_CONNECTION).
                setMaxConnPerRoute(MAX_CONNECTIONS_PER_ROUTE).
                addInterceptorFirst(new CustomRequestInterceptor()).
                addInterceptorFirst(new CustomResponseInterceptor()).
                setDefaultRequestConfig(defaultRequestConfig).
                build();
    }
    
    /**
     * default Request Config 호출
     * @return
     */
    private RequestConfig createDefaultRequestConfig() {
        return RequestConfig.custom().
                setConnectTimeout(REQUEST_CONNECTION_TIME_OUT).
                setSocketTimeout(REQUEST_READ_TIME_OUT).
                setConnectionRequestTimeout(REQUEST_CONNECTION_REQUEST_TIME_OUT).
                build();
    }
    
    class CustomRequestInterceptor implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        		
        }
    }
    
    class CustomResponseInterceptor implements HttpResponseInterceptor {
        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            
        }
    }

}
