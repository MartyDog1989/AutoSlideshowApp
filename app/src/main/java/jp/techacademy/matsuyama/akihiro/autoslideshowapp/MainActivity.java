package jp.techacademy.matsuyama.akihiro.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Button next;
    Button back;
    Button resumeStop;
    ContentResolver resolver;
    Cursor cursor;
    int fieldIndex;
    Long id;
    Uri imageUri;
    ImageView imageView;
    Timer mTimer;
    Handler mHandler;
    boolean b = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("test", "許可されている");
                next = (Button) findViewById(R.id.next);
                back = (Button) findViewById(R.id.back);
                resumeStop = (Button) findViewById(R.id.resumeStop);
                imageView = (ImageView) findViewById(R.id.imageView);
                resolver = getContentResolver();
                cursor = resolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );
                mHandler = new Handler();

                getContentsInfo();

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContentsInfo();
                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContentsInfo2();
                    }
                });

                resumeStop.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (b) {
                            mTimer = new Timer();
                            mTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            getContentsInfo();
                                        }
                                    });
                                }
                            }, 2000, 2000);
                            next.setEnabled(false);
                            back.setEnabled(false);
                            resumeStop.setText("停止");
                            b = false;
                        } else {
                            mTimer.cancel();
                            resumeStop.setText("再生");
                            next.setEnabled(true);
                            back.setEnabled(true);
                            b = true;
                        }
                    }
                });
            } else {
                Log.d("test", "許可されていない");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test", "許可された");
                    next = (Button) findViewById(R.id.next);
                    back = (Button) findViewById(R.id.back);
                    resumeStop = (Button) findViewById(R.id.resumeStop);
                    imageView = (ImageView) findViewById(R.id.imageView);
                    resolver = getContentResolver();
                    cursor = resolver.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                    mHandler = new Handler();

                    getContentsInfo();

                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContentsInfo();
                        }
                    });

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getContentsInfo2();
                        }
                    });

                    resumeStop.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            if (b) {
                                mTimer = new Timer();
                                mTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                getContentsInfo();
                                            }
                                        });
                                    }
                                }, 2000, 2000);
                                next.setEnabled(false);
                                back.setEnabled(false);
                                resumeStop.setText("停止");
                                b = false;
                            } else {
                                mTimer.cancel();
                                resumeStop.setText("再生");
                                next.setEnabled(true);
                                back.setEnabled(true);
                                b = true;
                            }
                        }
                    });
                } else {
                    Log.d("test", "許可されなかった");
                    Toast toast = Toast.makeText(this, "許可されなかったため終了します", Toast.LENGTH_SHORT);
                    toast.show();
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        if(cursor.moveToNext()) {
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            imageView.setImageURI(imageUri);
        } else {
            cursor.moveToFirst();
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            imageView.setImageURI(imageUri);
        }

    }
    private void getContentsInfo2() {

        if(cursor.moveToPrevious()) {
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            imageView.setImageURI(imageUri);
        }else {
            cursor.moveToLast();
            fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            id = cursor.getLong(fieldIndex);
            imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            imageView.setImageURI(imageUri);
        }
    }
}
