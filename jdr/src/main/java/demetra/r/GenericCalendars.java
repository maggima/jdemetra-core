/*
 * Copyright 2017 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package demetra.r;

import demetra.maths.MatrixType;
import demetra.maths.matrices.Matrix;
import demetra.timeseries.TsDomain;
import demetra.timeseries.calendars.DayClustering;
import demetra.timeseries.calendars.GenericTradingDays;

/**
 *
 * @author Jean Palate
 */
@lombok.experimental.UtilityClass
public class GenericCalendars {

    public MatrixType td(TsDomain domain, int[] groups, boolean contrasts) {
        DayClustering dc = DayClustering.of(groups);
        if (contrasts) {
            GenericTradingDays gtd = GenericTradingDays.contrasts(dc);
            Matrix m = Matrix.make(domain.getLength(), dc.getGroupsCount() - 1);
            gtd.data(domain, m.columnList());
            return m.unmodifiable();
        } else {
            GenericTradingDays gtd = GenericTradingDays.of(dc);
            Matrix m = Matrix.make(domain.getLength(), dc.getGroupsCount());
            gtd.data(domain, m.columnList());
            return m.unmodifiable();
        }
    }
}
