package com.jcstudio.com.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jcstudio.com.adapter.MateriaAdapterParaMateriaListActivity;
import com.jcstudio.com.calculation.CumCalculator;
import com.jcstudio.com.cumapp.R;
import com.jcstudio.com.database.CicloOperation;
import com.jcstudio.com.database.MateriaOperation;
import com.jcstudio.com.model.Materia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class MateriaListActivity extends AppCompatActivity implements DialogActivity.ClickListenerParaMateriaListActivity{

    TextView cum_display;
    List<Materia> materiaList;
    CicloOperation cicloOperation;
    MateriaOperation materiaOperation;
    RecyclerView recyclerView_materia_list;
    LinearLayoutManager linearLayoutManager;
    Long mId;
    MateriaAdapterParaMateriaListActivity materiaAdapter;
    CumCalculator cumCalculator;
    DialogActivity dialogActivity;
    Button btn_addMateria;
    CardView input_module;
    String cum_total;
    Vector<Double> values;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Intent intent = getIntent();
        mId = intent.getLongExtra("mId", -1);
        initialize();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void initialize(){
        cum_display = findViewById(R.id.show_cgpa_course_list);
        btn_addMateria = findViewById(R.id.dialog_btnAdd);
        input_module = findViewById(R.id.cardView_inputModule);
        materiaList = new ArrayList<>();
        materiaOperation = new MateriaOperation(this);
        cicloOperation = new CicloOperation(this);
        recyclerView_materia_list = findViewById(R.id.recyclerView_courseList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_materia_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_materia_list.setHasFixedSize(true);
        materiaList = materiaOperation.getMateriasPorCicloId(mId);
        materiaAdapter = new MateriaAdapterParaMateriaListActivity(this, materiaList);
        recyclerView_materia_list.setAdapter(materiaAdapter);
        cumCalculator = new CumCalculator();
        dialogActivity = new DialogActivity(MateriaListActivity.this);
        DialogActivity.setListenerParaMateriaListActivity(MateriaListActivity.this);
        setTotalCUM();
    }

    @Override
    public void addNewMateria(Materia materia) {
        materiaList.add(materia);
        Toast.makeText(this, "Materia added at the bottom", Toast.LENGTH_SHORT).show();
        materiaAdapter.notifyDataSetChanged();
        Vector<Double> values = cumCalculator.calculation(materiaList);
        double calculated_cum = values.elementAt(0);
        double total_uv = values.elementAt(1);
        setTotalCUM();
        cicloOperation.updateCiclo(mId, calculated_cum,total_uv,materiaList.size());
    }

    @Override
    public void updateList(int position, String materiaName, double uv, double nota) {
        Log.d("Anik", ""+materiaName);
        materiaList.set(position, new Materia(materiaName, uv, nota));
        materiaAdapter.notifyItemChanged(position);
        Vector<Double> values = cumCalculator.calculation(materiaList);
        double calculated_cum = values.elementAt(0);
        double total_uv = values.elementAt(1);
        setTotalCUM();
        cicloOperation.updateCiclo(mId, calculated_cum,total_uv,materiaList.size());

    }

    @Override
    public void removeItem(int position) {
        try {
            materiaList.remove(position);
            materiaAdapter.notifyItemRemoved(position);
            setTotalCUM();
            Vector<Double> values = cumCalculator.calculation(materiaList);
            double calculated_cum = values.elementAt(0);
            double total_uv = values.elementAt(1);
            setTotalCUM();
            if(!materiaList.isEmpty())
                cicloOperation.updateCiclo(mId, calculated_cum,total_uv,materiaList.size());
            else
                cicloOperation.updateCiclo(mId, 0,0,0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Anik", ""+e);
        }
    }
    public void setTotalCUM(){
        cum_total = cumCalculator.cumCalculado(materiaList);
        cum_display.setText(cum_total);
    }

    public void addNewMateriaFromActivity(View view) {
        dialogActivity.showDialogParaAddNewCicloMateriaListActivity(mId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }

}
