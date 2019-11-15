package com.jcstudio.com.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jcstudio.com.adapter.CicloAdapter;
import com.jcstudio.com.calculation.CumCalculator;
import com.jcstudio.com.cumapp.R;
import com.jcstudio.com.database.CicloOperation;
import com.jcstudio.com.database.MateriaOperation;
import com.jcstudio.com.model.Ciclo;
import com.jcstudio.com.model.Materia;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultBookActivity extends AppCompatActivity implements DialogActivity.ClickListenerParaResultBookActivity{
    CicloOperation cicloOperation;
    MateriaOperation materiaOperation;
    RecyclerView recyclerView_ciclo_list;
    LinearLayoutManager linearLayoutManager;
    CumCalculator cumCalculator;
    List<Ciclo> cicloList;
    List<Materia> materiaList;
    CicloAdapter cicloAdapter;
    DialogActivity dialogActivity;
    TextView cgpa_header;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_book);
        initialize_view();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(cicloList.isEmpty())
        {
            Toast.makeText(this, "Empty List", Toast.LENGTH_LONG).show();
        }
    }
    private void initialize_view(){
        cicloList = new ArrayList<>();
        materiaList = new ArrayList<>();
        cgpa_header = findViewById(R.id.show_cgpa_result_book);
        cicloOperation = new CicloOperation(this);
        materiaOperation = new MateriaOperation(this);
        materiaList = materiaOperation.getAllMaterias();
        cumCalculator = new CumCalculator();
        String cgpa_total = cumCalculator.cumCalculado(materiaList);
        cgpa_header.setText(cgpa_total);
        cicloList = cicloOperation.getAllCiclo();
        recyclerView_ciclo_list = findViewById(R.id.recyclerview_semester);
        linearLayoutManager = new LinearLayoutManager(ResultBookActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_ciclo_list.setLayoutManager(linearLayoutManager);
        recyclerView_ciclo_list.setHasFixedSize(true);
        cicloAdapter = new CicloAdapter(ResultBookActivity.this, cicloList);
        recyclerView_ciclo_list.setAdapter(cicloAdapter);
        dialogActivity = new DialogActivity(ResultBookActivity.this);
        DialogActivity.setListenerParaResultBookActivity(this);
    }
    public void addNewCiclo(View view) {
        dialogActivity.showDialogSveCicloParaResultBookActivity();
    }
    @Override
    public void saveNewCiclo(Ciclo newCiclo) {
        cicloList.add(newCiclo);
        cicloAdapter.notifyDataSetChanged();
        Log.d("Anik", "Data saved");
    }
    @Override
    public void refreshListAfterDeleteCiclo(Ciclo ciclo, int position) {
        cicloList.remove(ciclo);
        cicloAdapter.notifyItemRemoved(position);
        materiaList.clear();;
        materiaList = materiaOperation.getAllMaterias();
        String cgpa_total = cumCalculator.cumCalculado(materiaList);
        cgpa_header.setText(cgpa_total);
        Toast.makeText(this,"Eliminar",Toast.LENGTH_SHORT).show();
        Log.d("Anik", "Datos eliminados");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        initialize_view();
    }
}

