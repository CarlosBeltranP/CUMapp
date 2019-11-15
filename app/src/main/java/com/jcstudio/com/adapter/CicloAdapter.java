package com.jcstudio.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jcstudio.com.activity.DialogActivity;
import com.jcstudio.com.activity.MateriaListActivity;
import com.jcstudio.com.cumapp.R;
import com.jcstudio.com.model.Ciclo;

import java.text.DecimalFormat;
import java.util.List;


public class CicloAdapter extends RecyclerView.Adapter<CicloAdapter.MyViewHolder> {

    private DecimalFormat precision = new DecimalFormat("0.00");
    private List<Ciclo> cicloList;
    private LayoutInflater inflater;
    private Context context;
    private DialogActivity dialogActivity;
    public CicloAdapter(Context context, List<Ciclo> cicloList){
        this.cicloList = cicloList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        dialogActivity = new DialogActivity(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_semester, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mCicloNombre.setText(cicloList.get(holder.getAdapterPosition()).getCiclo_nombre());
        holder.mCicloCum.setText(String.valueOf(precision.format(cicloList.get(holder.getAdapterPosition()).getTotal_nota())));
        String uv = "Total UV: "+String.valueOf(cicloList.get(holder.getAdapterPosition()).getTotal_uv());
        holder.mCicloUv.setText(uv);
        String nota = "Total materias: "+String.valueOf(cicloList.get(holder.getAdapterPosition()).getTotal_materia());
        holder.mCicloMateria.setText(nota);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ciclo ciclo = cicloList.get(holder.getAdapterPosition());
                long mId = ciclo.getmId();
                //Toast.makeText(context,"Clicked"+position+" "+mId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MateriaListActivity.class);
                intent.putExtra("mId", mId);
                context.startActivity(intent);
            }
        });
        holder.btn_delete_ciclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Ciclo ciclo = cicloList.get(holder.getAdapterPosition());
                dialogActivity.showDialogDeleteCicloForResultBookActivity(ciclo, holder.getAdapterPosition());*/
                PopupMenu popupMenu = new PopupMenu(context, holder.btn_delete_ciclo);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.rename){
                            Toast.makeText(context,"Renombrar",Toast.LENGTH_SHORT).show();
                        }
                        else if(menuItem.getItemId() == R.id.delete){
                            Ciclo ciclo = cicloList.get(holder.getAdapterPosition());
                            dialogActivity.showDialogDeleteCicloParaResultBookActivity(ciclo, holder.getAdapterPosition());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Ciclo ciclo = cicloList.get(holder.getAdapterPosition());
                dialogActivity.showDialogDeleteCicloParaResultBookActivity(ciclo, holder.getAdapterPosition());
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return !cicloList.isEmpty() ? cicloList.size() : 0;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mCicloNombre, mCicloCum, mCicloUv, mCicloMateria, opt_btn;
        CardView cardView;
        ImageButton btn_delete_ciclo;

        private MyViewHolder(View itemView) {
            super(itemView);
            mCicloNombre = itemView.findViewById(R.id.semester_name);
            mCicloCum = itemView.findViewById(R.id.semester_cgpa);
            mCicloUv = itemView.findViewById(R.id.semester_credit);
            mCicloMateria = itemView.findViewById(R.id.semester_course);
            //opt_btn = itemView.findViewById(R.id.option_dot);
            cardView = itemView.findViewById(R.id.single_row_card_layout_semester);
            btn_delete_ciclo = itemView.findViewById(R.id.btn_delete_semester);
        }
    }
}
