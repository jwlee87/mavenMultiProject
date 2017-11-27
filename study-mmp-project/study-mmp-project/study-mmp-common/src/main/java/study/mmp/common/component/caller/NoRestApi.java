package study.mmp.common.component.caller;

import com.google.common.base.Charsets;

import net.sf.json.JSONObject;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import study.mmp.common.component.http.ApiHelper;
import study.mmp.common.component.http.ApiProtocolException;
import study.mmp.common.component.http.ApiRequestContext;
import study.mmp.common.component.http.ApiResultException;
import study.mmp.common.util.JacksonUtil;


@Component
public class NoRestApi {

	/**
    * 가입 처리
    */
    public void membershipAspJoin(String companyCd, long memberNo, String idNo, String gender, String birthday, String name, String userAgent) {
        
    	   ApiRequestContext context = new ApiRequestContext.Builder(url).
                   											 path("/membership/issue/membershipAspJoin").
                   											 pathValue(new BasicNameValuePair("companyCd", companyCd),
											                           new BasicNameValuePair("memberNo", String.valueOf(memberNo)),
											                           new BasicNameValuePair("idNo", idNo),
											                           new BasicNameValuePair("gender", gender),
											                           new BasicNameValuePair("birthday", birthday),
											                           new BasicNameValuePair("name", name),
											                           new BasicNameValuePair("userAgent", userAgent)).
											                 build();
           execute(context);
    }


    
    /************************************************************************************************************************************
     *  Config
     ************************************************************************************************************************************/

    @Autowired
    ApiHelper apiHelper;
    
    @Value("${api.url}") private String url;

    private final ContentType requestContentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), Charsets.UTF_8);
    
    /**
     * 
     */
    public JSONObject post(String path, Object param) {

        String jsonParam = JacksonUtil.toJson(param);
        ApiRequestContext context = new ApiRequestContext.Builder(url + path).
                                                                post(EntityBuilder.create().setText(jsonParam).setContentType(requestContentType).build()).
                                                                build();
        return execute(context);
    }

    /**
     * 공통, 응답부 처리
     * @throws ApiResultException
     * @throws ApiProtocolException ( IOException, ClientProtocolException, ParseException, HttpResponseException, JSONException )
     */
    private JSONObject execute(ApiRequestContext context) {

        String result = apiHelper.execute(context);
        JSONObject resultJson = JSONObject.fromObject(result);
        
        int code = -1;
        String message = "";
        if (resultJson.get("code") != null) {
            code = resultJson.getInt("code");
            message = resultJson.getString("message");
        }

        if (code != 0) {
            throw new ApiResultException(String.valueOf(code), message, context);
        }
        return resultJson;
    }
    
}
