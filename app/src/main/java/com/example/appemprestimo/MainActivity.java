package com.example.appemprestimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btFinalizar;
    private EditText etValor;
    private SeekBar sbParcelas;
    private TextView tvParcelas;
    private SeekBar sbJuros;
    private TextView tvJuros;
    private TextView tvValorParcelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponents();
        loadEvents();
    }

    @Override
    protected void onStop() {
        // persistindo os parâmetros do emprestimo no SharedPreferences
        super.onStop();
        SharedPreferences prefs =
                getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("valor", Float.parseFloat(etValor.getText().toString()));
        editor.putInt("juros", sbJuros.getProgress());
        editor.putInt("parcelas", sbParcelas.getProgress());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs =
                getSharedPreferences("config",MODE_PRIVATE);


        Float etValor = prefs.getFloat("valor",10);
        Integer sbJuros = prefs.getInt("juros",1);
        Integer sbParcelas = prefs.getInt("parcelas",12);

        this.etValor.setText(etValor.toString());
        this.sbJuros.setProgress(sbJuros);
        this.sbParcelas.setProgress(sbParcelas);
    }

    private void loadComponents () {
        etValor = findViewById(R.id.etValor);
        sbParcelas = findViewById(R.id.sbParcelas);
        tvParcelas = findViewById(R.id.tvParcelas);
        sbJuros = findViewById(R.id.sbJuros);
        tvJuros = findViewById(R.id.tvJuros);
        tvValorParcelas = findViewById(R.id.tvValorParcelas);
        btFinalizar = findViewById(R.id.btFinalizar);
    }

    private void loadEvents() {
        tvValorParcelas.setOnClickListener(e -> trocarActivit());
        btFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onDestroy();
            }
        });
        sbParcelas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvParcelas.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                calcEmprestimo();
            }
        });
        sbJuros.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String value = String.valueOf(i / 10.) + "%";
                tvJuros.setText(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                calcEmprestimo();
            }
        });
        etValor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                etValor.selectAll();
            }
        });
        btFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcEmprestimo();
            }
        });
//        etValor.setKeyListener(new View);
    }

    private void trocarActivit() {
        Intent intent = new Intent(this, ParcelasActivity.class);

        String valorText = etValor.getText().toString();
        String jurosText = String.valueOf(sbJuros.getProgress()/10.);
        String parcelas = tvParcelas.getText().toString();

        intent.putExtra("valor", valorText);
        intent.putExtra("juros", jurosText);
        intent.putExtra("parcelas", parcelas);
        startActivity(intent);
    }


    private void calcEmprestimo() {
        double valorInicial = Double.parseDouble(etValor.getText().toString());
        double valorJuros = sbJuros.getProgress()/10.;
        int valorParcelas = sbParcelas.getProgress();
        Double valor = calcParcela(valorInicial, valorJuros, valorParcelas);
        String valorFinal = String.format("%.2f", valor);
        tvValorParcelas.setText(valorFinal);
    }

    private double calcParcela(double valor, double juros, int meses)
    {
        return valor*(juros/100/(1-Math.pow(1+juros/100,meses*-1)));
    }
}