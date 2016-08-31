package cn.domain.hello.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.domain.hello.R;
import cn.domain.hello.config.Config;
import cn.domain.hello.util.WebUtil;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private ViewGroup vsProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.etUsername = (EditText) this.findViewById(R.id.etUsername);
		this.etPassword = (EditText) this.findViewById(R.id.etPassword);
		this.btnLogin = (Button) this.findViewById(R.id.btnLogin);
		this.btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = MainActivity.this.etUsername.getText()
						.toString().trim();
				String password = MainActivity.this.etPassword.getText()
						.toString().trim();
				if ("".equals(username)) {
					Toast.makeText(MainActivity.this, "请填写用户名",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("".equals(password)) {
					Toast.makeText(MainActivity.this, "请填写密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				//如果已经填写了用户名和密码，执行登录操作
				executeLogin(username, password);
			}
		});

	}

	private void executeLogin(String username, String password) {
		new LoginTask().execute(username, password);
	}

	private void onLoginComplete(Integer userId) {
		if (userId == null || userId == 0) {//如果没有获取到用户ID，说明登录失败
			Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT)
					.show();
			if (vsProgress != null) {
				vsProgress.setVisibility(View.INVISIBLE);
			}
			return;
		}
		if (vsProgress != null) {
			vsProgress.setVisibility(View.INVISIBLE);
		}
		//如果成功获取到返回的用户ID，说明登录成功，跳转到HelloActivity
		Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
		HelloActivity.actionStart(MainActivity.this, userId, etUsername
				.getText().toString());
	}

	private class LoginTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//进行登录验证时，显示登录进度条
			if (vsProgress == null) {
				ViewStub vs = (ViewStub) findViewById(R.id.vsProgress);
				vsProgress = (ViewGroup) vs.inflate();
			} else {
				vsProgress.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Integer doInBackground(String... params) {
			Integer result = null;
			JSONArray reqValue;
			try {
				//将用户名和密码封装到JSONArray中，进行HTTP通信
				reqValue = new JSONArray().put(new JSONObject().put("username",
						params[0]).put("password", params[1]));
				JSONArray rec = WebUtil.getJSONArrayByWeb(Config.METHOD_LOGIN,
						reqValue);
				if (rec != null) {//如果成功获取用户ID
					JSONObject object = rec.getJSONObject(0);
					result = object.getInt("id");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			//回调
			onLoginComplete(result);
		}

	}
}
