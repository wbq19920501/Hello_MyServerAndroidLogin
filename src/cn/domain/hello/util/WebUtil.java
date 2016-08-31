package cn.domain.hello.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import cn.domain.hello.config.Config;

public class WebUtil {
	public static JSONArray getJSONArrayByWeb(String methodName,
			JSONArray params) {

		String returnValue = "";
		JSONArray result = null;
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter("charset", "UTF-8");
		HttpClient hc = new DefaultHttpClient(httpParams);
		HttpPost hp = new HttpPost(Config.SERVER_IP + "/DemoLogin/Servlet/"
				+ methodName);
		try {
			hp.setEntity(new StringEntity(params.toString(), "UTF-8"));
			HttpResponse hr = hc.execute(hp);
			if (hr.getStatusLine().getStatusCode() == 200) {
				returnValue = EntityUtils.toString(hr.getEntity(), "UTF-8");
				result = new JSONArray(returnValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (hc != null) {
			hc.getConnectionManager().shutdown();
		}
		return result;
	}
}
