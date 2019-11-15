package com.jcstudio.com.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jcstudio.com.calculation.CumCalculator;
import com.jcstudio.com.cumapp.R;
import com.jcstudio.com.database.CicloOperation;
import com.jcstudio.com.database.MateriaOperation;
import com.jcstudio.com.model.Ciclo;
import com.jcstudio.com.model.Materia;

import java.util.List;
import java.util.Vector;

public class DialogActivity {

    private Context context;
    private EditText materiaNombreDialog, uvDialog, gpaDialog, saveCiclo;
    private String mMateriaNombre;
    private double mUv, mNota;
    private Button btn_add_dialog;
    private int itemPosition;
    private CumCalculator cumCalculator;
    private MateriaOperation materiaOperation;
    private CicloOperation cicloOperation;
    private static ClickListenerParaCalcularCumActivity clickListenerParaCalcularCumActivity;
    private static ClickListenerParaMateriaListActivity clickListenerParaMateriaListActivity;
    private static ClickListenerParaResultBookActivity clickListenerParaResultBookActivity;

    public DialogActivity(Context context) {
        this.context = context;
        cicloOperation = new CicloOperation(context);
        materiaOperation = new MateriaOperation(context);
    }

    //------------------------------DialogForCalculateCGPAActivity-----------------------------------//
    public void showDialogUpdateDeleteParaCalcularCumActivity(List<Materia> materiaList, int position){
        itemPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_update_delete,null);
        builder.setView(view);

        materiaNombreDialog = view.findViewById(R.id.autoCompleteTextViewDialog);
        uvDialog = view.findViewById(R.id.creditDialog);
        gpaDialog = view.findViewById(R.id.gpaDialog);
        materiaNombreDialog.setText(materiaList.get(position).getMateria_nombre());
        uvDialog.setText(String.valueOf(materiaList.get(position).getMateria_uv()));
        gpaDialog.setText(String.valueOf(materiaList.get(position).getNota_obtenida()));

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Actualizar o eliminar información");
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mMateriaNombre = materiaNombreDialog.getText().toString();
                    mUv = Double.parseDouble(uvDialog.getText().toString());
                    mNota = Double.parseDouble(gpaDialog.getText().toString());
                    clickListenerParaCalcularCumActivity.updateList(itemPosition, mMateriaNombre,mUv, mNota);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.d("Anik", ""+e);
                }
            }
        });
        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickListenerParaCalcularCumActivity.removeItem(itemPosition);
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showDialogSaveCicloParaCalcularCumActivity(final List<Materia> materiaList){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_layout_save_semester,null);
        saveCiclo = view.findViewById(R.id.semesterName);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Nombre del ciclo");
        cumCalculator = new CumCalculator();
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Vector<Double> values = cumCalculator.calculation(materiaList);
                String ciclo_nombre = saveCiclo.getText().toString();
                double calculated_cgpa = values.elementAt(0);
                double total_uv = values.elementAt(1);
                if(!ciclo_nombre.isEmpty()){
                    Ciclo created_ciclo =
                            cicloOperation.createCiclo(ciclo_nombre,calculated_cgpa, materiaList.size(), total_uv);
                    //calculated_cgpa, materiaList.size(),total_uv
                    Log.d("Anik", ""+calculated_cgpa+" "+materiaList.size()+" "+total_uv);
                    Long cicloId = created_ciclo.getmId();
                    for(int i = 0; i< materiaList.size(); i++){
                        String materia_nombre = materiaList.get(i).getMateria_nombre();
                        double materia_uv = materiaList.get(i).getMateria_uv();
                        double gpa = materiaList.get(i).getNota_obtenida();
                        materiaOperation.createMateria(materia_nombre,materia_uv,gpa,cicloId);
                    }
                    clickListenerParaCalcularCumActivity.clearDisplay();
                    Toast.makeText(context,"Guardado",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context,"Debe llenar campo nombre de ciclo",Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //------------------------------DialogForResultBookActivity-----------------------------------//
    public void showDialogSveCicloParaResultBookActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_save_semester,null);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Nuevo nombre de ciclo");
        saveCiclo = view.findViewById(R.id.semesterName);
        cumCalculator = new CumCalculator();
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ciclo_nombre = saveCiclo.getText().toString();
                if(!ciclo_nombre.isEmpty()){
                    Ciclo newCiclo = cicloOperation.createCiclo(ciclo_nombre,0.00,0,0.00);
                    clickListenerParaResultBookActivity.saveNewCiclo(newCiclo);
                    }
                }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
    public void showDialogDeleteCicloParaResultBookActivity(final Ciclo ciclo, final int itemPosition){
        String ciclo_nombre = ciclo.getCiclo_nombre();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setTitle("Eliminar");

        builder.setMessage("Quiere eliminar "+ciclo_nombre+"?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cicloOperation.deleteCiclo(ciclo);
                clickListenerParaResultBookActivity.refreshListAfterDeleteCiclo(ciclo, itemPosition);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }
    //------------------------------DialogForMateriaListActivity-----------------------------------//
    public void showDialogUpdateDeleteParaMateriaListActivity(List<Materia> materiaList, int position){
        itemPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_update_delete,null);
        builder.setView(view);

        materiaNombreDialog = view.findViewById(R.id.autoCompleteTextViewDialog);
        uvDialog = view.findViewById(R.id.creditDialog);
        gpaDialog = view.findViewById(R.id.gpaDialog);
        materiaNombreDialog.setText(materiaList.get(position).getMateria_nombre());
        uvDialog.setText(String.valueOf(materiaList.get(position).getMateria_uv()));
        gpaDialog.setText(String.valueOf(materiaList.get(position).getNota_obtenida()));
        final Materia materia = materiaList.get(position);

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Actualizar o Eliminar");
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMateriaNombre = materiaNombreDialog.getText().toString();
                mUv = Double.parseDouble(uvDialog.getText().toString());
                mNota = Double.parseDouble(gpaDialog.getText().toString());
                materiaOperation.updateMateria(materia, mMateriaNombre,mUv, mNota);
                clickListenerParaMateriaListActivity.updateList(itemPosition, mMateriaNombre,mUv, mNota);
            }
        });
        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                materiaOperation.deleteMateria(materia);
                clickListenerParaMateriaListActivity.removeItem(itemPosition);
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showDialogParaAddNewCicloMateriaListActivity(final Long mId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_input_module,null);
        builder.setView(view);
        materiaNombreDialog = view.findViewById(R.id.dialog_autoCompleteTextView_courseName);
        uvDialog = view.findViewById(R.id.dialog_credit);
        gpaDialog = view.findViewById(R.id.dialog_gpa);
        btn_add_dialog = view.findViewById(R.id.dialog_btnAdd);

        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Agregar nueva materia");
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn_add_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String materia_nombre = materiaNombreDialog.getText().toString();
                if(materia_nombre.isEmpty()){
                    materia_nombre = "Materia Nombre?";
                }
                try {
                    double uv = Double.parseDouble(uvDialog.getText().toString());
                    double gpa = Double.parseDouble(gpaDialog.getText().toString());
                    Materia materia = materiaOperation.createMateria(materia_nombre, uv, gpa, mId);
                    Log.d("Anik", ""+gpa);
                    clickListenerParaMateriaListActivity.addNewMateria(materia);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    Toast.makeText(context,"Por favor llene los campos cuidadosamente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //---------------------------------------interface----------------------------------------------
    public interface ClickListenerParaCalcularCumActivity {
        void updateList(int position, String materiaNombre, double uv, double gpa);
        void removeItem(int position);
        void clearDisplay();
    }
    public interface ClickListenerParaResultBookActivity {
        void saveNewCiclo(Ciclo ciclo);
        void refreshListAfterDeleteCiclo(Ciclo ciclo, int position);
    }
    public interface ClickListenerParaMateriaListActivity {
        void addNewMateria(Materia materia);
        void updateList(int position, String materiaNombre, double uv, double gpa);
        void removeItem(int position);
    }

    //setListener
    public static  void setListenerParaCalcularCumActivity(ClickListenerParaCalcularCumActivity clickListener){
        clickListenerParaCalcularCumActivity = clickListener;
    }
    public static void setListenerParaResultBookActivity(ClickListenerParaResultBookActivity clickListener){
        clickListenerParaResultBookActivity = clickListener;
    }
    public static  void setListenerParaMateriaListActivity(ClickListenerParaMateriaListActivity clickListener){
        clickListenerParaMateriaListActivity = clickListener;
    }
}
