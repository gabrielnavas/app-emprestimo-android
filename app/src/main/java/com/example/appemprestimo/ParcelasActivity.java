package com.example.appemprestimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ParcelasActivity extends AppCompatActivity {

    private TextView tvValorp;
    private TextView tvJurosp;

    private ListView listView;

    private void loadComponents() {
        tvValorp = findViewById(R.id.tvValorp);
        tvJurosp = findViewById(R.id.tvJurosp);
        listView = findViewById(R.id.listView);
    }

    private void loadDataFromMain() {
        Intent intent = getIntent();

        String parcelas = intent.getStringExtra("parcelas");
        String valor = intent.getStringExtra("valor");
        String juros = intent.getStringExtra("juros");

        String valorFormated = String.format("Valor\n%s", valor);
        String jurosFormated = String.format("Juros\n%s%%", juros);

        tvValorp.setText(valorFormated);
        tvJurosp.setText(jurosFormated);

        calcularItemListView(Integer.parseInt(parcelas), Double.parseDouble(valor), Double.parseDouble(juros));
    }

    private void calcularItemListView(int parcelas, double valor, double juros) {
        System.out.printf("%d, %.2f, %.2f", parcelas, valor, juros);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelas);
        loadComponents();
        loadDataFromMain();
    }
}