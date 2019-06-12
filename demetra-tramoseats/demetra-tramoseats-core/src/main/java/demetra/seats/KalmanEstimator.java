/*
 * Copyright 2013 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package demetra.seats;

import demetra.design.Development;
import demetra.modelling.ComponentInformation;
import demetra.sa.ComponentType;
import demetra.sa.DecompositionMode;
import demetra.sa.SeriesDecomposition;
import jdplus.ssf.dk.DkToolkit;
import jdplus.ssf.implementations.CompositeSsf;
import jdplus.ssf.univariate.DefaultSmoothingResults;
import jdplus.ssf.univariate.ExtendedSsfData;
import jdplus.ssf.univariate.ISsfData;
import jdplus.ssf.univariate.SsfData;
import jdplus.ucarima.UcarimaModel;
import jdplus.ucarima.ssf.SsfUcarima;
import demetra.data.DoubleSeq;

/**
 * @author Jean Palate
 */
@Development(status = Development.Status.Alpha)
public class KalmanEstimator implements IComponentsEstimator {

    /**
     *
     * @param model
     * @return
     */
    @Override
    public SeriesDecomposition decompose(SeatsModel model) {
        SeriesDecomposition.Builder builder = SeriesDecomposition.builder(DecompositionMode.Additive);
        DoubleSeq s = model.getSeries();
        int n = s.length(), nf = model.getForecastsCount(), nb = model.getBackcastsCount();

        CompositeSsf ssf = SsfUcarima.of(model.getUcarimaModel());
        // compute KS
        ISsfData data = new ExtendedSsfData(new SsfData(s), nb, nf);
        DefaultSmoothingResults srslts = DkToolkit.sqrtSmooth(ssf, data, true, true);
        // for using the same standard error (unbiased stdandard error, not ml)
        srslts.rescaleVariances(model.getInnovationVariance());

        UcarimaModel ucm = model.getUcarimaModel();
        int[] pos = ssf.componentsPosition();
        for (int i = 0; i < ucm.getComponentsCount(); ++i) {
            ComponentType type = model.getTypes()[i];
            DoubleSeq cmp = srslts.getComponent(pos[i]);
            if (nb > 0) {
                builder.add(cmp.range(0, nb), type, ComponentInformation.Backcast);
            }
            if (nf > 0) {
                builder.add(cmp.extract(nb + n, nf), type, ComponentInformation.Forecast);
            }
            builder.add(cmp.extract(nb, n), type);
            cmp = srslts.getComponentVariance(pos[i]);
            if (nb > 0) {
                builder.add(cmp.range(0, nb), type, ComponentInformation.StdevBackcast);
            }
            if (nf > 0) {
                builder.add(cmp.extract(nb + n, nf), type, ComponentInformation.StdevForecast);
            }
            builder.add(cmp.extract(nb, n), type, ComponentInformation.Stdev);
        }
        return builder.build();
    }
}
