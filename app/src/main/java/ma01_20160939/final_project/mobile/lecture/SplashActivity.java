package ma01_20160939.final_project.mobile.lecture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    //앱 시작 전 splash 화면을 잠깐 띄우는 액티비티
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(4000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}