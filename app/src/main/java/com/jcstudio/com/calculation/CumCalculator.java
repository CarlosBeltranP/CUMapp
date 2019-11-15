package com.jcstudio.com.calculation;
import com.jcstudio.com.model.Materia;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class CumCalculator {

    private DecimalFormat precision = new DecimalFormat("0.00");

    public String cumCalculado(List<Materia> courseList){
        double sum = 0;
        double total_nota = 0;
        double cum_calculado;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getMateria_uv()*courseList.get(i).getNota_obtenida();
            total_nota += courseList.get(i).getMateria_uv();
        }
        cum_calculado = sum / total_nota;
        return courseList.isEmpty() ? "0.00000000":String.valueOf(precision.format(cum_calculado));
    }
    public Vector<Double> calculation(List<Materia> courseList){
        Vector<Double> ret = new Vector<>();
        double sum = 0;
        double total_nota = 0;
        double cum_calculado;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getMateria_uv()*courseList.get(i).getNota_obtenida();
            total_nota += courseList.get(i).getMateria_uv();
        }
        cum_calculado = sum / total_nota;
        ret.add(cum_calculado);
        ret.add(total_nota);
        return ret;
    }
}
