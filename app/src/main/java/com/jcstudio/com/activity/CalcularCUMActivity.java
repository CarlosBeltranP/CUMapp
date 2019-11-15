package com.jcstudio.com.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jcstudio.com.adapter.MateriaAdapterParaCalcularCumActivity;
import com.jcstudio.com.calculation.CumCalculator;
import com.jcstudio.com.cumapp.R;
import com.jcstudio.com.model.Materia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CalcularCUMActivity extends AppCompatActivity implements DialogActivity.ClickListenerParaCalcularCumActivity {

    TextView cum_display;
    EditText materiaNombre, materiaUv, materiaGPA;
    Button add_materia_btn;
    RecyclerView recyclerView_materia_list;
    FloatingActionButton fab_save;
    MateriaAdapterParaCalcularCumActivity materiaAdapter;
    List<Materia> materiaList;
    LinearLayoutManager linearLayoutManager;
    CumCalculator cumCalculator;
    DialogActivity dialogActivity;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculatecgpa);
        initialize_view();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_materia_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String materia = materiaNombre.getText().toString();
                if(materia.isEmpty()){
                    materia = "Materia Nombre?";
                }
                try {
                    double mUv = Double.parseDouble(materiaUv.getText().toString());
                    double mCum = Double.parseDouble(materiaGPA.getText().toString());
                    materiaList.add(new Materia(materia,mUv,mCum));
                    materiaAdapter.notifyDataSetChanged();
                    setTotalCUM();
                    materiaGPA.setText("");
                    materiaNombre.setText("");
                } catch (NumberFormatException e) {
                    Toast.makeText(CalcularCUMActivity.this,"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogActivity = new DialogActivity(CalcularCUMActivity.this);
                dialogActivity.showDialogSaveCicloParaCalcularCumActivity(materiaList);
            }
        });
    }
    private void initialize_view(){
        cum_display = findViewById(R.id.show_cgpa_calculateCGPA);
        materiaNombre = findViewById(R.id.autoCompleteTextView_courseName);
        materiaUv = findViewById(R.id.credit_home);
        materiaGPA = findViewById(R.id.gpa_home);
        add_materia_btn = findViewById(R.id.btnAdd_home);
        recyclerView_materia_list = findViewById(R.id.recyclerView_courseList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_materia_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_materia_list.setHasFixedSize(true);
        fab_save = findViewById(R.id.floatingActionButton_save);
        materiaList = new ArrayList<>();
        cumCalculator = new CumCalculator();
        materiaAdapter = new MateriaAdapterParaCalcularCumActivity(this, materiaList);
        DialogActivity.setListenerParaCalcularCumActivity(CalcularCUMActivity.this);
        recyclerView_materia_list.setAdapter(materiaAdapter);
    }

    @Override
    public void updateList(int position, String materiaNombre, double uv, double gpa) {
        Log.d("Anik", ""+materiaNombre);
        materiaList.set(position, new Materia(materiaNombre, uv, gpa));
        materiaAdapter.notifyItemChanged(position);
        setTotalCUM();
    }
    @Override
    public void removeItem(int position) {
        materiaList.remove(position);
        materiaAdapter.notifyItemRemoved(position);
        setTotalCUM();
    }

    @Override
    public void clearDisplay() {
        cum_display.setText("0.00");
        materiaList.clear();
        materiaGPA.setText("");
        materiaNombre.setText("");
        materiaUv.setText("");
        materiaAdapter.notifyDataSetChanged();
        finish();
    }

    public void setTotalCUM(){
        String cum = cumCalculator.cumCalculado(materiaList);
        cum_display.setText(cum);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculatecgpa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        else if(id == R.id.clear_display){
            clear_display();
            Toast.makeText(this, "Limpiar", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void clear_display(){
        cum_display.setText("0.00");
        materiaList.clear();
        materiaGPA.setText("");
        materiaNombre.setText("");
        materiaUv.setText("");
        materiaAdapter.notifyDataSetChanged();
    }
}
