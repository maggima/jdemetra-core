/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demetra.tramoseats.spi;

import demetra.design.Algorithm;
import demetra.design.ServiceDefinition;
import demetra.processing.ProcResults;
import demetra.modelling.regsarima.SarimaSpec;
import demetra.timeseries.TsData;
import demetra.seats.SeatsSpec;
import javax.annotation.Nonnull;

/**
 *
 * @author palatej
 */
@Algorithm
@ServiceDefinition
public interface SeatsProcessor {

    ProcResults process(@Nonnull TsData series, @Nonnull SarimaSpec arima, @Nonnull SeatsSpec spec);
}
