/*
 * Copyright 2016 National Bank of Belgium
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
package ec.demetra.xml.core;

import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Validator;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import xml.Schemas;

/**
 *
 * @author Jean Palate
 */
public class XmlTsDataTest {

    private static final String FILE = "c:\\localdata\\xml\\tsdata.xml";

    @Test
    public void testMarshal() throws FileNotFoundException, JAXBException, IOException {
        JAXBContext jaxb = JAXBContext.newInstance(XmlTsData.class);
        XmlTsData xTsData = new XmlTsData();
        TsData tsdata = TsData.random(TsFrequency.Monthly);
        xTsData.copy(tsdata);
        xTsData.name = "TestTsData";
        FileOutputStream ostream = new FileOutputStream(FILE);
        try (OutputStreamWriter writer = new OutputStreamWriter(ostream, StandardCharsets.UTF_8)) {
            Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(xTsData, writer);
            writer.flush();
        }

        XmlTsData rslt = null;
        FileInputStream istream = new FileInputStream(FILE);
        try (InputStreamReader reader = new InputStreamReader(istream, StandardCharsets.UTF_8)) {
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            rslt = (XmlTsData) unmarshaller.unmarshal(reader);
            TsData ncoll = rslt.create();
            assertTrue(ncoll.equals(tsdata));
        }
    }

    @Test
    public void testValidation() throws FileNotFoundException, JAXBException, IOException, SAXException {
        JAXBContext jaxb = JAXBContext.newInstance(XmlTsData.class);
        XmlTsData xTsData = new XmlTsData();
        TsData tsdata = TsData.random(TsFrequency.Monthly);
        xTsData.copy(tsdata);
        xTsData.name = "TestTsData";
        JAXBSource source = new JAXBSource(jaxb, xTsData);
        Validator validator = Schemas.Core.newValidator();
        //validator.setErrorHandler(new TestErrorHandler());
        validator.validate(source);
    }
}
