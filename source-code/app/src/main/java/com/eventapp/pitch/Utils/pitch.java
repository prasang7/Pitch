package com.eventapp.pitch.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.eventapp.pitch.R;
import com.eventapp.pitch.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Sid on 1/15/17.
 */

public class pitch {

    public static void setStatusBar(Window window, Context context, int color){
        if(Build.VERSION.SDK_INT>=21){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(context, color));
        }
    }

    public static List<project> recents;

    public static int templateCount=1;

    public static int[] templateSreenShotIds = new int[]{R.drawable.screenshot_template_1};

    public static int[] templatePreviewIds = new int[]{R.layout.preview_template_1};

    public static String[] templateNames= new String[]{"Pitch First"};

    public static String[] templateApks = new String[]{"template_1.apk"};

    public static int currentTemplateIndex=-1;

    public static void copyAssets(Context context) {
        String packageDir = Environment.getExternalStorageDirectory()+"/Android/data/"+context.getPackageName()+"/";
        AssetManager assetManager = context.getAssets();
        for (String filename : templateApks) {
            String assetsDir = "templates/"+filename;
            InputStream in = null;
            OutputStream out = null;
            File newFile = null;
            try {
                in = assetManager.open(assetsDir);
                File newFileDir = new File(packageDir + "/templates/");
                newFileDir.mkdirs();
                newFile = new File(newFileDir.getAbsolutePath() + "/" + filename);
                out = new FileOutputStream(newFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Log.e("Error", e.toString());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        //printLog(TAG, "Exception while closing input stream",e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        //printLog(TAG, "Exception while closing output stream",e);
                    }
                }
            }
        }
    }
}
