package bpj.com.update_ray;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView version = findViewById(R.id.version);
        //
        version.setText(BuildConfig.VERSION_NAME);

    }

    public void update(View view) {
//1、合成 apk
        //先从服务器下载到差分包
        new AsyncTask<Void,Void,File>(){

            @Override
            protected File doInBackground(Void... voids) {
                String old = getApplication().getApplicationInfo().sourceDir;
                bspatch(old,"/sdcard/patch","/sdcard/new.apk");
                return new File("/sdcard/new.apk");
            }

            @Override
            protected void onPostExecute(File file) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    // 声明需要的临时权限
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // 第二个参数，即第一步中配置的authorities
                    String packageName = getApplication().getPackageName();
                    Uri contentUri = FileProvider.getUriForFile(MainActivity.this, packageName + ".fileProvider", file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                }
                startActivity(intent);
            }
        }.execute();
    }

    /**
     *
     * @param oldapk 当前运行的apk
     * @param patch  差分包
     * @param output 合成后的新的apk输出到
     */
    native void bspatch(String oldapk,String patch,String output);
}
