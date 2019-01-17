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
package demetra.regarima;

import demetra.likelihood.ConcentratedLikelihood;
import demetra.regarima.internal.ConcentratedLikelihoodComputer;
import demetra.sarima.SarimaModel;
import demetra.sarima.GlsSarimaProcessor;
import demetra.sarima.RegSarimaProcessor;

/**
 *
 * @author Jean Palate
 */
@lombok.experimental.UtilityClass
public class RegArimaToolkit {

    RegArimaEstimation<SarimaModel> robustEstimation(RegArimaModel<SarimaModel> regarima){
        RegSarimaProcessor processor = RegSarimaProcessor.builder().build();
        return processor.process(regarima);
    }

    RegArimaEstimation<SarimaModel> fastEstimation(RegArimaModel<SarimaModel> regarima){
        GlsSarimaProcessor processor = GlsSarimaProcessor.builder().build();
        return processor.process(regarima);
    }
    
    RegArimaEstimation<SarimaModel> concentratedLikelihood(RegArimaModel<SarimaModel> regarima, int nparams){
        ConcentratedLikelihood cl = ConcentratedLikelihoodComputer.DEFAULT_COMPUTER.compute(regarima);
        return new RegArimaEstimation(regarima, cl, nparams);
    }
}