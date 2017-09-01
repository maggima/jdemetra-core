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
package demetra.timeseries.regression;

import demetra.data.DataBlock;
import demetra.maths.linearfilters.BackFilter;
import demetra.maths.linearfilters.RationalBackFilter;
import demetra.timeseries.TsDomain;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Jean Palate
 * @param <D>
 */
public class AdditiveOutlier <D extends TsDomain<?>> extends BaseOutlier implements IOutlier<D>{

    public static final String CODE = "AO";

    public AdditiveOutlier(LocalDateTime pos) {
        super(pos, defaultName(CODE, pos, null));
    }
    public AdditiveOutlier(LocalDateTime pos, String name) {
        super(pos, name);
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public LocalDateTime getPosition() {
        return position;
    }

    @Override
    public FilterRepresentation getFilterRepresentation() {
        return new FilterRepresentation(new RationalBackFilter(
                BackFilter.ONE, BackFilter.ONE, 0), 0);
    }

    @Override
    public void data(D domain, List<DataBlock> data) {
        long pos=domain.indexOf(position);
        if (pos >= 0){
            data.get(0).set((int)pos, 1);
        }
    }

    @Override
    public String getDescription(D context) {
        return defaultName(CODE, position, context);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ITsVariable<D> rename(String nname) {
        return new AdditiveOutlier(position, nname);
    }
    
}
