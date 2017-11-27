package study.mmp.common.component.http;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.Getter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

@NotThreadSafe
public class ApiRequestContext {
    
    public static enum LogLevel {
        NONE, DEBUG, INFO
    }
    
    final FastDateFormat dateFormat = FastDateFormat.getInstance("HH:mm:ss.ZZZ");
    
    @Getter private final HttpMethod method;
    @Getter private final HttpUriRequest request;
    @Getter private final LogLevel logLevel;
    @Getter private ApiProtocolException exception;
    @Getter private String responseEncodingSet;
    @Getter private Date requestTime;
    @Getter private boolean ignoreResponseCode;
    
    protected ApiRequestContext(HttpMethod method, HttpUriRequest request, LogLevel logLevel, String responseEncodingSet, ApiProtocolException ape, boolean ignoreResponseCode) {
    	this.method = method;
        this.request = request;
        this.logLevel = logLevel;
        this.responseEncodingSet = responseEncodingSet;
        this.exception = ape;
        this.ignoreResponseCode = ignoreResponseCode;
    }

    protected void setRequestTime(long time) {
        requestTime = new Date(time);
    }
    
    public String requestToString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("RequestLine=").append(request.getRequestLine());
        if (requestTime != null) {
            sb.append(", before request time=").append(dateFormat.format(requestTime));
        }
        if (request instanceof HttpPost) {
            HttpPost post = (HttpPost) request;
            sb.append(", ContentType=").append(post.getEntity().getContentType());
            String body = StringUtils.EMPTY;
            try {
                body = EntityUtils.toString(post.getEntity());
            } catch (Exception e) {
                body = "cant read body";
            }
            sb.append(", Body=").append(body);
        }
        sb.append("}");
        return sb.toString();
    }
    

    public static class Builder {
    	private HttpMethod method = HttpMethod.GET;
        private String host;
        private String path = StringUtils.EMPTY;
        private List<NameValuePair> pathParams = new ArrayList<>();
        
        private List<NameValuePair> headers = new ArrayList<>();
        
        private HttpEntity httpEntity;
        
        private RequestConfig requestConfig;
        private LogLevel logLevel;
        private String responseEncodingSet;
        private boolean ignoreResponseCode = false;
        
        public Builder() {
        }
        
        public Builder(String host) {
            this.host = host;
        }
        
        public Builder host(String host) {
        	this.host = host;
        	return this;
        }
        
        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         * 사용 시, path에 ? 문자포함되면 비활성화 됩니다.
         **/
        public Builder pathValue(NameValuePair... nvps) {
            this.pathParams.addAll(Arrays.asList(nvps));
            return this;
        }
        
        public Builder headers(NameValuePair... headers) {
            this.headers.addAll(Arrays.asList(headers));
            return this;
        }
        
        public Builder config(RequestConfig config) {
            this.requestConfig = config;
            return this;
        }
        
        /**
         * Log Level을 지정하지 않을 경우, 기본값은 NONE입니다.
         * @param logLevel
         * @return
         */
        public Builder logWrapper(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }
        
        /**
         * HTTP method post 방식일 경우
         */
        public Builder post(HttpEntity entity) {
            this.httpEntity = entity;
            method = HttpMethod.POST;
            return this;
        }
        
        /**
         * HTTP method put 방식일 경우
         */
        public Builder put(HttpEntity entity) {
            this.httpEntity = entity;
            method = HttpMethod.PUT;
            return this;
        }
        
        public Builder setIgnoreResponseCode() {
        	this.ignoreResponseCode = true;
        	return this;
        }
        
        public Builder responseEncodingSet(String responseEncodingSet) {
            this.responseEncodingSet = responseEncodingSet;
            return this;
        }
        
        public ApiRequestContext build() {
            
            URIBuilder uriBuilder = null;
            HttpUriRequest request = null;
            
            try {
                uriBuilder = new URIBuilder(host + path);
                
                if (!CollectionUtils.isEmpty(pathParams) && !path.contains("?")) {
                    uriBuilder.addParameters(pathParams);
                }
                
                if (method == HttpMethod.POST) {
                	HttpPost post = new HttpPost(uriBuilder.build());
                	post.setConfig(requestConfig);
                	post.setEntity(httpEntity);
                	request = post;
                } else if (method == HttpMethod.PUT) {
                	HttpPut put = new HttpPut(uriBuilder.build());
                	put.setConfig(requestConfig);
                	put.setEntity(httpEntity);
                	request = put;
                } else {
                    HttpGet get = new HttpGet(uriBuilder.build());
                    get.setConfig(requestConfig);
                    request = get;
                }
                
                for (NameValuePair header : headers) {
                    request.addHeader(header.getName(), header.getValue());
                }
                
                if (logLevel == null) {
                    logLevel = LogLevel.NONE;
                }
                return new ApiRequestContext(method, request, logLevel, responseEncodingSet, null, ignoreResponseCode);
                
            } catch (URISyntaxException e) {
                ApiProtocolException exception = new ApiProtocolException("HelperRequestContext build exception", e);
                return new ApiRequestContext(method, request, logLevel, responseEncodingSet, exception, false);
            }
        }
    }
}
