package com.eventapp.pitch.Activities;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.pitch;
import com.eventapp.pitch.template;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;


import com.google.api.services.drive.model.Permission;


import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



import static com.eventapp.pitch.Utils.pitch.currentProjectIndex;
import static com.eventapp.pitch.Utils.pitch.recents;

public class uploadToDrive extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    String apkPath="";
    TextView statusView;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_drive);
        statusView=(TextView)findViewById(R.id.export_status);
        pb=(ProgressBar)findViewById(R.id.export_progress);
        String informationJson=getIntent().getStringExtra("informationJson");
        template information = (new Gson().fromJson(informationJson,new TypeToken<template>(){}.getType()));
        new manipulate(information).execute();
    }

    void setClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback); //SEND DATA TO DRIVE
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();


    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("Error","Could not connect");
    }

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.d("Error","Error while trying to create new file contents");
                        return;
                    }
                    final DriveContents driveContents = result.getDriveContents();


                            // write content to DriveContents
                            InputStream in = null;
                            try {
                                in = new FileInputStream(new File(apkPath));
                            }
                            catch (Exception e){
                                Log.d("Error","Apk file wasnt generated");
                                return;
                            }
                            OutputStream outputStream = driveContents.getOutputStream();
                            byte[] buffer = new byte[1024];
                            int read;
                            try {
                                while ((read = in.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, read);
                                }
                            }
                            catch (Exception e){
                                Log.d("Error","File Writing Failed");
                            }

                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                    .setTitle((recents.get(currentProjectIndex)).name+".apk")
                                    .setStarred(true).build();

                            // create a file on root folder
                            Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                    .createFile(mGoogleApiClient, changeSet, driveContents)
                                    .setResultCallback(fileCallback);
                        }

                };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.d("Error","Error while trying to create the file");
                        return;
                    }
                    String fieldId= result.getDriveFile().getDriveId().toString();
                    Log.d("Created","Created a file with content: "+fieldId);
                    pb.setVisibility(View.GONE);
                    statusView.setText("Uploaded to Drive :)");

                }
            };
    private Permission insertPermission(com.google.api.services.drive.Drive service, String fileId) {
        Permission newPermission = new Permission();
        newPermission.setType("anyone");
        newPermission.setRole("reader");
        try {

            return service.permissions().create(fileId,newPermission).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }

    class manipulate extends AsyncTask<Void,Integer,Void>{
        template information;
        public manipulate(template informationObject){
            information=informationObject;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress(0);
            pitch.extractToAccumulator();
            publishProgress(1);
            pitch.manipulate(information);
            publishProgress(2);
            apkPath=pitch.zipAndSign();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            statusView.setText("Uploading to Drive...");
            setClient();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]){
                case 0:
                    statusView.setText("Preparing the template...");
                    break;
                case  1:
                    statusView.setText("Changing the fields...");
                    break;
                case 2:
                    statusView.setText("Signing the app...");
                    break;
            }
            super.onProgressUpdate(values);
        }
    }

    public com.google.api.services.drive.Drive getDriveServices(){
        new Thread(){
            @Override
            public void run() {
                super.run();
            }
        };
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);
        credential.setSelectedAccountName("");
        return new com.google.api.services.drive.Drive.Builder( new NetHttpTransport(),new JacksonFactory(), credential).build();
    }
}