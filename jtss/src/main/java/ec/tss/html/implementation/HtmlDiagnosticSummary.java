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

import com.sun.xml.internal.ws.util.StringUtils;
import ec.tss.html.AbstractHtmlElement;
import ec.tss.html.HtmlStream;
import ec.tss.html.HtmlStyle;
import ec.tss.html.HtmlTag;
import ec.tss.html.IHtmlElement;
import ec.tstoolkit.algorithm.ProcDiagnostic;
import ec.tstoolkit.algorithm.ProcQuality;
import ec.tstoolkit.information.Information;
import ec.tstoolkit.information.InformationSet;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Kristof Bayens
 */
public class HtmlDiagnosticSummary extends AbstractHtmlElement implements IHtmlElement {

    private InformationSet diags_;

    public HtmlDiagnosticSummary(InformationSet diags) {
        diags_ = diags;
    }

    @Override
    public void write(HtmlStream stream) throws IOException {
        if (diags_ != null) {
            writeSummary(stream);
        }
    }

    public void writeSummary(HtmlStream stream) throws IOException {
        stream.write(HtmlTag.HEADER3, "Summary");
        writeQuality(stream, null, ProcDiagnostic.summary(diags_), Double.NaN);
        List<Information<InformationSet>> subsets = diags_.select(InformationSet.class);
        for (Information<InformationSet> subset : subsets) {
            writeDiagnostic(stream, subset.name, subset.value);
        }
    }

    private void writeDiagnostic(HtmlStream stream, String name, InformationSet diags) throws IOException {
        stream.write(HtmlTag.HEADER4, StringUtils.capitalize(name));
        List<Information<ProcDiagnostic>> items = diags.select(ProcDiagnostic.class);
        stream.open(HtmlTag.UNORDERED_LIST);
        for (Information<ProcDiagnostic> item : items) {
            ProcDiagnostic diag = item.value;
            stream.open(HtmlTag.LIST_ELEMENT);
            writeQuality(stream, item.name, diag.quality, diag.value);
            stream.close(HtmlTag.LIST_ELEMENT);
        }
        stream.close(HtmlTag.UNORDERED_LIST);
    }

    private void writeQuality(HtmlStream stream, String test, ProcQuality q, double val) throws IOException {
        if (q != ProcQuality.Undefined) {
            if (test != null) {
                stream.write("   " + test + ": ");
            }
            switch (q) {
                case Error:
                    stream.write("Error" + (!Double.isNaN(val) ? " (" + df3.format(val) + ")" : ""), HtmlStyle.Bold, HtmlStyle.Info);
                    break;
                case Severe:
                    stream.write("Severe" + (!Double.isNaN(val) ? " (" + df3.format(val) + ")" : ""), HtmlStyle.Bold, HtmlStyle.Danger);
                    break;
                case Bad:
                    stream.write("Bad" + (!Double.isNaN(val) ? " (" + df3.format(val) + ")" : ""), HtmlStyle.Danger);
                    break;
                case Uncertain:
                    stream.write("Uncertain" + (!Double.isNaN(val) ? " (" + df3.format(val) + ")" : ""), HtmlStyle.Warning);
                    break;
                case Good:
                    stream.write("Good" + (!Double.isNaN(val) ? " (" + df3.format(val) + ")" : ""), HtmlStyle.Success);
                    break;
            }
        }
    }
}
