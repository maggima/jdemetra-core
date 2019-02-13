/*
 * Copyright 2016 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
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
package internal.timeseries.util;

import demetra.data.DoubleSequence;
import demetra.timeseries.TsUnit;
import java.time.LocalDateTime;
import java.util.function.IntUnaryOperator;

/**
 * @author Philippe Charles
 */
interface ObsList {

    int size();

    void sortByPeriod();

    IntUnaryOperator getPeriodIdFunc(TsUnit unit, LocalDateTime reference);

    double getValue(int index) throws IndexOutOfBoundsException;

    default DoubleSequence getValues() {
        double[] result = new double[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = getValue(i);
        }
        return DoubleSequence.ofInternal(result);
    }
}