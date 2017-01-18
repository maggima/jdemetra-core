/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.tss.sa.documents;

import data.Data;
import ec.satoolkit.seats.SeatsResults;
import static ec.satoolkit.seats.SeatsResults.getComponents;
import static ec.satoolkit.seats.SeatsResults.getComponentsName;
import ec.satoolkit.tramoseats.TramoSeatsSpecification;
import ec.tss.Ts;
import ec.tss.TsFactory;
import ec.tss.html.HtmlStream;
import ec.tss.html.implementation.HtmlTramoSeatsSummary;
import ec.tss.sa.SaManager;
import ec.tss.sa.diagnostics.CoherenceDiagnosticsFactory;
import ec.tss.sa.diagnostics.MDiagnosticsFactory;
import ec.tss.sa.diagnostics.OutOfSampleDiagnosticsFactory;
import ec.tss.sa.diagnostics.OutliersDiagnosticsFactory;
import ec.tss.sa.diagnostics.ResidualsDiagnosticsFactory;
import ec.tss.sa.diagnostics.SeatsDiagnosticsFactory;
import ec.tss.sa.processors.TramoSeatsProcessor;
import ec.tss.tsproviders.utils.MultiLineNameUtil;
import ec.tstoolkit.ucarima.UcarimaModel;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Mats Maggi
 */
public class HtmlTramoSeatsSummaryTest {

    private TramoSeatsDocument doc;

    public HtmlTramoSeatsSummaryTest() {
    }

    @Before
    public void setUp() {
        SaManager.instance.add(new TramoSeatsProcessor());
        SaManager.instance.add(new OutliersDiagnosticsFactory());
        SaManager.instance.add(new SeatsDiagnosticsFactory());
        SaManager.instance.add(new ResidualsDiagnosticsFactory());
        SaManager.instance.add(new OutOfSampleDiagnosticsFactory());
        SaManager.instance.add(new MDiagnosticsFactory());
        SaManager.instance.add(new CoherenceDiagnosticsFactory());
        doc = new TramoSeatsDocument();
        Ts ts = TsFactory.instance.createTs("g_exports", null, Data.X);
        doc.setInput(ts);
        doc.setSpecification(TramoSeatsSpecification.RSAfull);
    }

    @Test
    public void testHtmlOutput() {
        SeatsResults seats = doc.getDecompositionPart();
        HtmlTramoSeatsSummary document;
        if (seats == null) {
            document = new HtmlTramoSeatsSummary(MultiLineNameUtil.join(doc.getInput().getName()), doc.getResults(), null, null, null);
        } else {
            UcarimaModel ucm = seats.getUcarimaModel();
            document = new HtmlTramoSeatsSummary(MultiLineNameUtil.join(doc.getInput().getName()), doc.getResults(), getComponentsName(ucm), getComponents(ucm), null);
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("\\\\nbb.local\\usr\\MAGGIMA\\Desktop\\Garbage\\tramoseats.html"), "utf-8"));
                HtmlStream stream = new HtmlStream(writer)) {
            stream.open();
            document.write(stream);
        } catch (IOException ex) {
        }
    }
}
