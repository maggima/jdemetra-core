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

import data.Data;
import ec.tss.TsFactory;
import ec.tss.TsInformation;
import ec.tss.TsInformationType;
import ec.tstoolkit.MetaData;
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
public class XmlTsTest {

    private static final String FILE = "c:\\localdata\\xml\\ts.xml";

    @Test
    public void testMarshal() throws FileNotFoundException, JAXBException, IOException {
        JAXBContext jaxb = JAXBContext.newInstance(XmlTs.class);
        XmlTs xTs = new XmlTs();
        MetaData meta = new MetaData();
        meta.put("version", "2.2.0");
        TsInformation info = new TsInformation(TsFactory.instance.createTs("TsTest", meta, Data.P), TsInformationType.All);
        xTs.copy(info);
        FileOutputStream ostream = new FileOutputStream(FILE);
        try (OutputStreamWriter writer = new OutputStreamWriter(ostream, StandardCharsets.UTF_8)) {
            Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(xTs, writer);
            writer.flush();
        }

        XmlTs rslt = null;
        FileInputStream istream = new FileInputStream(FILE);
        try (InputStreamReader reader = new InputStreamReader(istream, StandardCharsets.UTF_8)) {
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            rslt = (XmlTs) unmarshaller.unmarshal(reader);
            TsInformation info2 = rslt.create();
            assertTrue(info2.data.equals(info.data)
                    && info2.metaData.size() == info.metaData.size()
                    && info2.metaData.get("version").equals(info.metaData.get("version"))
                    && info2.name.equals(info.name));
        }
    }

    @Test
    public void testValidation() throws FileNotFoundException, JAXBException, IOException, SAXException {
        JAXBContext jaxb = JAXBContext.newInstance(XmlTs.class);
        XmlTs xTs = new XmlTs();
        MetaData meta = new MetaData();
        meta.put("version", "2.2.0");
        TsInformation info = new TsInformation(TsFactory.instance.createTs("TsTest", meta, Data.P), TsInformationType.All);
        xTs.copy(info);
        JAXBSource source = new JAXBSource(jaxb, xTs);
        Validator validator = Schemas.Core.newValidator();
        //validator.setErrorHandler(new TestErrorHandler());
        validator.validate(source);
    }
}
