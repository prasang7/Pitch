package com.eventapp.pitch.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eventapp.pitch.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.eventapp.pitch.Utils.pitch.*;

public class templateChooser extends AppCompatActivity {

    LinearLayout templateGridLeft, templateGridRight;
    RelativeLayout previewLayout;
    View selectedCardView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView();
    }

    void setLayoutView() {
        setContentView(R.layout.activity_template_chooser);
        setStatusBar(getWindow(), getApplicationContext(), R.color.colorOrangeDark);
        getDimensions();
        templateGridLeft = (LinearLayout) findViewById(R.id.templateGridLeft);
        templateGridRight = (LinearLayout) findViewById(R.id.templateGridRight);
        previewLayout = (RelativeLayout) findViewById(R.id.view_template);
        layTemplateGrid();
        (findViewById(R.id.tv_layName_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTemplateIndex != -1) {
                    startActivity(new Intent(templateChooser.this, BasicDataActivity.class));
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                } else {
                    Toast.makeText(templateChooser.this, "Select a template", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void layTemplateGrid() {
        for (int i = 0; i < templateCount; i++) {
            if (i % 2 == 0) {
                View view = createCard(i);
                templateGridLeft.addView(view);
            } else {
                View view = createCard(i);
                templateGridRight.addView(view);
            }
        }
        Log.d("Doing", "Dne");
    }

    View createCard(final int index) {
        final View cardView = getLayoutInflater().inflate(R.layout.tempate_card, null);
        cardView.setLayoutParams(new RelativeLayout.LayoutParams(templateWidth, 700));
        ImageView screenShot = (ImageView) cardView.findViewById(R.id.template_card_screenshot);
        Glide.with(getApplicationContext()).load(templateSreenShotIds[index]).into(screenShot);
        (cardView.findViewById(R.id.template_card_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreview(index);
            }
        });
        cardView.findViewById(R.id.template_card_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.findViewById(R.id.selected_template).setVisibility(View.VISIBLE);
                if (selectedCardView != null)
                    selectedCardView.findViewById(R.id.selected_template).setVisibility(View.INVISIBLE);
                selectedCardView = cardView;
                currentTemplateIndex = index;
            }
        });
        return cardView;
    }

    void showPreview(final int index) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View preview = getLayoutInflater().inflate(templatePreviewIds[index], null);
                previewLayout.removeAllViews();
                previewLayout.addView(preview);
                previewLayout.setVisibility(View.VISIBLE);
                preview.findViewById(R.id.back_to_chooser).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        previewLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }, 30);
    }

    int width;
    int height;
    int templateWidth;

    void getDimensions() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        templateWidth = (int) (width * 0.49);
    }
}
