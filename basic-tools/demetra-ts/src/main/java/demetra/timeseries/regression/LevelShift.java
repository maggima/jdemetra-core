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
import static demetra.timeseries.regression.BaseOutlier.defaultName;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Jean Palate
 * @param <D>
 */
public class LevelShift<D extends TsDomain<?>> extends BaseOutlier implements IOutlier<D> {

    public static final String CODE = "LS";

    private final boolean zeroEnded;

    public LevelShift(LocalDateTime pos, boolean zeroEnded) {
        super(pos, defaultName(CODE, pos, null));
        this.zeroEnded = zeroEnded;
    }

    public LevelShift(LocalDateTime pos, boolean zeroEnded, String name) {
        super(pos, name);
        this.zeroEnded = zeroEnded;
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
                BackFilter.ONE, BackFilter.D1, 0), zeroEnded ? -1 : 0);
    }

    @Override
    public void data(D domain, List<DataBlock> data) {
        DataBlock buffer = data.get(0);
        int n = buffer.length();
        double Zero = zeroEnded ? -1 : 0, One = zeroEnded ? 0 : 1;
        int xpos = domain.indexOf(position);
        if (xpos == -1) {
            buffer.set(One);
        } else {
            int lpos = xpos >= 0 ? xpos : -xpos;
            if (lpos >= n) {
                buffer.set(Zero);
            } else {
                buffer.range(0, lpos).set(Zero);
                buffer.range(lpos, n).set(One);
            }
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
        return new LevelShift(position, zeroEnded, nname);
    }

}
