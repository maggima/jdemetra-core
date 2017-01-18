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
package ec.tss.html.implementation;

import ec.satoolkit.GenericSaResults;
import ec.satoolkit.seats.SeatsResults;
import ec.tss.html.*;
import ec.tss.sa.SaManager;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.algorithm.ProcessingInformation;
import ec.tstoolkit.arima.ArimaModel;
import ec.tstoolkit.arima.IArimaModel;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.modelling.arima.PreprocessingModel;
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.ucarima.UcarimaModel;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Kristof Bayens
 */
public class HtmlTramoSeatsSummary extends AbstractHtmlElement implements IHtmlElement {

    private final List<ProcessingInformation> infos_;
    private final PreprocessingModel preprocessing_;
    private final SeatsResults decomposition_;
    private final String[] names_;
    private final ArimaModel[] list_;
    private final InformationSet diags_;
    private final String title_;

    public HtmlTramoSeatsSummary(String title, CompositeResults results, String[] names, ArimaModel[] list, InformationSet diags) {
        title_ = title;
        preprocessing_ = GenericSaResults.getPreprocessingModel(results);
        decomposition_ = GenericSaResults.getDecomposition(results, SeatsResults.class);
        names_ = names;
        list_ = list;
        if (diags != null) {
            diags_ = diags;
        } else {
            diags_ = SaManager.createDiagnostics(results);
        }
        infos_ = results.getProcessingInformation();
    }

    @Override
    public void write(HtmlStream stream) throws IOException {
        writeTitle(stream);
        writeInformation(stream);
        if (preprocessing_ == null && decomposition_ == null)
            return;
        writePreprocessing(stream);
        writeDecomposition(stream);
        writeDiagnostics(stream);
    }

    private void writeTitle(HtmlStream stream) throws IOException {
        if (title_ != null) {
            stream.write(HtmlTag.HEADER1, title_);
        }
    }

    private void writeInformation(HtmlStream stream) throws IOException {
        stream.write(new HtmlProcessingInformation(infos_));
    }

    private void writePreprocessing(HtmlStream stream) throws IOException {
        if (preprocessing_ == null)
            return;
        stream.write(HtmlTag.HEADER2, "Pre-processing (Tramo)");
        stream.write(new HtmlRegArima(preprocessing_, true));
    }

    private void writeDecomposition(HtmlStream stream) throws IOException {
        if (decomposition_ == null)
            return;
        stream.write(HtmlTag.HEADER2, "Decomposition (Seats)");
        SarimaModel tmodel = preprocessing_.estimation.getArima();
        IArimaModel smodel = decomposition_.getModel().getArima();
        if (tmodel == null || smodel == null) {
            stream.write("No decomposition", HtmlStyle.Bold, HtmlStyle.Underline);
        } else {
            boolean changed = !ArimaModel.same(tmodel, smodel, 1e-4);
            if (changed) {
                stream.write("Model changed by Seats", HtmlStyle.Bold, HtmlStyle.Underline).newLine();
            }

            UcarimaModel ucm = decomposition_.getUcarimaModel();
            HtmlUcarima arima = new HtmlUcarima(ucm.getModel(), list_, names_);
            arima.writeSummary(stream);
        }
    }

    private void writeDiagnostics(HtmlStream stream) throws IOException {
        stream.write(HtmlTag.HEADER2, "Diagnostics");
        stream.write(new HtmlDiagnosticSummary(diags_));
    }
}
