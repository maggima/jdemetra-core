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
package demetra.models.mapping;

import demetra.information.InformationMapping;
import demetra.maths.MatrixType;
import demetra.models.LinearModelType;

/**
 *
 * @author Jean Palate
 */
@lombok.experimental.UtilityClass
public class LinearModelInfo {
   private final String Y = "y", X = "x", MEAN="mean";

    private final InformationMapping<LinearModelType> MAPPING = new InformationMapping<>(LinearModelType.class);

    static {
        MAPPING.set(Y, double[].class, source -> source.getY().toArray());
        MAPPING.set(X, MatrixType.class, source -> source.getX());
        MAPPING.set(MEAN, Boolean.class, source -> source.isMeanCorrection());
    }
     
}