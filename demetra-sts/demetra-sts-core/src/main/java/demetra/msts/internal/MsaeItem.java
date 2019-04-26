/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demetra.msts.internal;

import demetra.maths.matrices.MatrixType;
import demetra.msts.ArInterpreter;
import demetra.msts.MstsMapping;
import demetra.msts.survey.WaveSpecificSurveyErrors;
import demetra.ssf.StateComponent;
import java.util.Arrays;
import java.util.List;
import demetra.msts.ParameterInterpreter;
import demetra.data.DoubleSeq;

/**
 *
 * @author palatej
 */
public class MsaeItem extends AbstractModelItem {
    
    private final int nwaves;
    private final int lag;
    private final int[] lar;
    private final ArInterpreter[] par;
    
    public MsaeItem(String name, int nwaves, MatrixType ar, boolean fixedar, int lag) {
        super(name);
        this.nwaves = nwaves;
        this.lag = lag;
        final int nar = ar.getColumnsCount();
        lar = new int[nar];
        par = new ArInterpreter[nar];
        for (int i = 0; i < nar; ++i) {
            int j = 0;
            for (; j <= i && j < ar.getRowsCount(); ++j) {
                double c = ar.get(j, i);
                if (Double.isNaN(c)) {
                    break;
                }
            }
            lar[i] = j;
            double[] car = ar.column(i).extract(0, j).toArray();
            par[i] = new ArInterpreter(name + ".wae" + (i + 1), car, fixedar);
        }
    }
    
    @Override
    public void addTo(MstsMapping mapping) {
        for (int i = 0; i < par.length; ++i) {
            mapping.add(par[i]);
        }
        mapping.add((p, builder) -> {
            double[][] w = new double[nwaves][];
            w[0] = DoubleSeq.EMPTYARRAY;
            int pos = 0;
            int nar = lar.length;
            for (int i = 0; i < nar; ++i) {
                w[i + 1] = p.extract(pos, lar[i]).toArray();
                pos += lar[i];
            }
            // same coefficients for the last waves, if any
            for (int i = nar + 1; i < nwaves; ++i) {
                w[i] = w[i - 1];
            }
            StateComponent cmp = WaveSpecificSurveyErrors.of(w, lag);
            builder.add(name, cmp, null);
            return pos;
        });
    }
    
    @Override
    public List<ParameterInterpreter> parameters() {
        return Arrays.asList(par);
    }
    
}
