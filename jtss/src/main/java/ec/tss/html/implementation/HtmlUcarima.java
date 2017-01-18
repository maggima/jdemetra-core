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

import ec.tss.html.HtmlStream;
import ec.tss.html.HtmlStyle;
import ec.tss.html.HtmlTag;
import ec.tss.html.IHtmlElement;
import ec.tstoolkit.arima.IArimaModel;
import java.io.IOException;

/**
 *
 * @author Jean Palate & BAYENSK
 */
public class HtmlUcarima implements IHtmlElement {

    private IArimaModel model_;
    private String[] names_;
    private IArimaModel[] cmps_;

    /**
     *
     * @param model
     * @param cmps
     * @param names
     */
    public HtmlUcarima(IArimaModel model, IArimaModel[] cmps, String[] names) {
        model_ = model;
        cmps_ = cmps;
        names_ = names;
    }

    /**
     *
     * @param stream
     * @throws IOException
     */
    @Override
    public void write(HtmlStream stream) throws IOException {
        writeDecomposition(stream);
    }

    public void writeDecomposition(HtmlStream stream) throws IOException {
        // HTMLFont mfont=new HTMLFont("arial", "blue", null, 3, false);
        // HTMLFont font=new HTMLFont("arial", null, null, 3, false);
        // stream.open(font);
        if (model_ != null) {
            stream.write("Model", HtmlStyle.Bold, HtmlStyle.Info).newLine()
                    .write(new HtmlArima(model_)).newLine();
        }
        for (int i = 0; i < cmps_.length; ++i) {
            if (cmps_[i] == null || cmps_[i].isNull()) {
                continue;
            }
            String name = names_ != null ? names_[i] : "Cmp_"
                    + Integer.toString(i + 1);
            stream.write(name, HtmlStyle.Bold, HtmlStyle.Info).newLine().write(new HtmlArima(cmps_[i]));
        }
    }

    public void writeSummary(HtmlStream stream) throws IOException {
        if (names_ != null) {
            stream.open(HtmlTag.UNORDERED_LIST);
            for (int i = 0; i < names_.length; ++i) {
                HtmlArima arima = new HtmlArima(cmps_[i]);
                stream.open(HtmlTag.LIST_ELEMENT);
                stream.write(HtmlTag.CODE, names_[i]);
                arima.writeShortModel(stream);
                stream.close(HtmlTag.LIST_ELEMENT);

            }
            stream.close(HtmlTag.UNORDERED_LIST);
        }
    }
}
