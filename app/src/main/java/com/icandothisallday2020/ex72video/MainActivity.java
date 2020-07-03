package com.icandothisallday2020.ex72video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv=findViewById(R.id.vv);

        //동적퍼미션
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 200:
                if (grantResults[0]==PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "앱 사용 불가", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    public void clickBtn(View view) {
        //비디오촬영 화면 Camera 앱 실행
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,50);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //비디오는 용량이 커서 무조건 File 저장 방식 (무조건 Uri 경로로 리턴됨)
        switch (requestCode){
            case 50:
                if(resultCode==RESULT_OK){//비디오촬영히 무사히 끝나면
                    Uri uri=data.getData();
                    vv.setVideoURI(uri);
                    //비디오뷰를 클릭했을때 아래쪽에 컨트롤바(컨트롤러뷰) 세팅
                    MediaController controller=new MediaController(this);
                    controller.setAnchorView(vv);//놓여지는 위치 설정
                    vv.setMediaController(controller);//비디오뷰에 컨트롤러 설정
                    //동영상은 용량이 커서 로딩에 시간이 소요되므로
                    // 로딩이 완료되는 것을 듣고 시작하는 것을 권장
                    vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {//동영상이 준비되면
                            vv.start();
                        }
                    });

                }else Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
