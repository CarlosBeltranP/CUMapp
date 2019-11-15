package com.jcstudio.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcstudio.com.activity.DialogActivity;
import com.jcstudio.com.cumapp.R;
import com.jcstudio.com.model.Materia;

import java.text.DecimalFormat;
import java.util.List;

public class MateriaAdapterParaMateriaListActivity extends RecyclerView.Adapter<MateriaAdapterParaMateriaListActivity.MyViewHolder> {

    private DecimalFormat precision = new DecimalFormat("0.00");
    private List<Materia> materiaList;
    private LayoutInflater inflater;
    private DialogActivity dialogActivity;
    Context context;

    public MateriaAdapterParaMateriaListActivity(Context context, List<Materia> materias_list){
        inflater = LayoutInflater.from(context);
        materiaList = materias_list;
        dialogActivity = new DialogActivity(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singel_row_course, parent, false);
        return new MateriaAdapterParaMateriaListActivity.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Materia materia = materiaList.get(position);
        holder.mMateria.setText(materia.getMateria_nombre());
        String str = "UV: "+String.valueOf(materia.getMateria_uv());
        holder.mUv.setText(str);
        holder.mGpa.setText(String.valueOf(precision.format(materia.getNota_obtenida())));
    }

    @Override
    public int getItemCount() {
        return !materiaList.isEmpty() ? materiaList.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mMateria, mUv, mGpa;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mMateria = itemView.findViewById(R.id.coureseName);
            mUv = itemView.findViewById(R.id.courseCredit);
            mGpa = itemView.findViewById(R.id.courseCgpa);
            cardView = itemView.findViewById(R.id.single_row_card_layout);
        }

        @Override
        public void onClick(View view) {
            dialogActivity.showDialogUpdateDeleteParaMateriaListActivity(materiaList, getAdapterPosition());
            Log.d("Anik", context.getClass().getName());
        }
    }
}
