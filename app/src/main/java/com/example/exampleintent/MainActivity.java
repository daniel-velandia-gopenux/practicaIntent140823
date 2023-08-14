package com.example.exampleintent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.exampleintent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private String numberPhone = "3208802219";

    private String declionedAccess = "You declined the access";
    private String enabledPermission = "please enabled the request permission";

    private static final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
    }

    private void initUI() {
        binding.clMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSystemVersion();
            }
        });
    }

    private void checkSystemVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            newerVersions();
        } else {
            olderVersions();
        }
    }

    private void newerVersions() {
        if(checkPermission(Manifest.permission.CALL_PHONE)) {
            makeCall();
        } else {
            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
            } else {
                showToast(enabledPermission);
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        }

        //requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PHONE_CALL_CODE:
                checkPhonePermission(permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void checkPhonePermission(String[] permissions, int[] grantResults) {
        String permission = permissions[0];
        int result = grantResults[0];
        if(permission.equals(Manifest.permission.CALL_PHONE)) {
            if(result == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                showToast(declionedAccess);
            }
        }
    }

    private void olderVersions() {
        if(checkPermission(Manifest.permission.CALL_PHONE)) {
            makeCall();
        } else {
            showToast(declionedAccess);
        }
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numberPhone));
        startActivity(intent);
    }

    private void showToast(String body) {
        Toast.makeText(this, body, Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermission(String permission) {
        int result = this.checkCallingPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}