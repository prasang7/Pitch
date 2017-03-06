package com.eventapp.pitch.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.eventapp.pitch.R;
import com.eventapp.pitch.project;
import com.eventapp.pitch.template;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import kellinwood.security.zipsigner.ZipSigner;

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

    public static int currentProjectIndex=-1;

    public static String PACKAGE_DIR;

    public static String TEMPLATE_DIR;

    public static String CONFIG_FILE_PATH;

    public static String MANIFEST_FILE_PATH;

    public static String ACCUMULATOR_PATH;

    public  static String OUTPUT_PATH;

    public static void copyAssetsAsync(final Context context) {
        (new Thread(){
            @Override
            public void run() {
                AssetManager assetManager = context.getAssets();
                for (String filename : templateApks) {
                    String assetsDir = "templates/"+filename;
                    InputStream in = null;
                    OutputStream out = null;
                    File newFile;
                    try {
                        in = assetManager.open(assetsDir);
                        File newFileDir = new File(TEMPLATE_DIR);
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
        }).start();
    }

    public static void extractToAccumulator(){
        File accumulator = new File(ACCUMULATOR_PATH);
        accumulator.mkdirs();
        try{
            ZipFile extractor = new ZipFile(TEMPLATE_DIR+templateApks[currentTemplateIndex]);
            extractor.extractAll(accumulator.getAbsolutePath());
        }
        catch (ZipException e){
            e.printStackTrace();
            Log.e("Extraction Error",e.getMessage());
        }
    }

    public static void manipulate(template information){
        try{
            FileWriter fout= new FileWriter(CONFIG_FILE_PATH);
            BufferedWriter out = new BufferedWriter(fout);
            out.write(information.toJson());
            out.close();
        }
        catch(IOException e){
            Log.e("Output Stream Failed",e.toString());
        }
        //readManifest();
    }


    public static String zipAndSign(){
        File output= new File(OUTPUT_PATH);
        output.mkdirs();
        try{
            ZipFile outApk= new ZipFile(output.getAbsolutePath()+"/edited_"+templateApks[currentTemplateIndex]);
            ZipParameters params= new ZipParameters();
            params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            outApk.addFile(new File(ACCUMULATOR_PATH+"AndroidManifest.xml"),params);
            outApk.addFile(new File(ACCUMULATOR_PATH+"classes.dex"),params);
            outApk.addFile(new File(ACCUMULATOR_PATH+"resources.arsc"),params);
            outApk.addFolder(new File(ACCUMULATOR_PATH+"res"),params);
            outApk.addFolder(new File(ACCUMULATOR_PATH+"META-INF"),params);
            outApk.addFolder(new File(ACCUMULATOR_PATH+"assets"),params);

            appSigner zipSigner= new appSigner();
            zipSigner.setKeymode(ZipSigner.MODE_AUTO_TESTKEY);
            String signedApkPath=output.getAbsolutePath()+"/signedApk_"+templateApks[currentTemplateIndex];
            zipSigner.signZip(output.getAbsolutePath()+"/edited_"+templateApks[currentTemplateIndex],signedApkPath);
            return signedApkPath;
        }
        catch (Exception e){
            Log.e("Compression failed",e.toString());
        }
        return null;
    }
}
