package com.example.eyekidneystone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eyekidneystone.logic.Result;

public class ResultOverviewActivity extends AppCompatActivity {

    Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_overview);

        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        result = new Result();
        result.setType("Iris");

        TextView textView = findViewById(R.id.tvHasilKesimpulan);
        textView.setText(result.getStatus());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ResultScleraActivity.class));
        finish();
    }
}
