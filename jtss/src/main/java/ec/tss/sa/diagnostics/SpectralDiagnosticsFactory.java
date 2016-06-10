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


package ec.tss.sa.diagnostics;

import ec.tss.sa.ISaDiagnosticsFactory;
import ec.tstoolkit.algorithm.CompositeResults;
import ec.tstoolkit.algorithm.IDiagnostics;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Kristof Bayens
 */
@ServiceProvider(service = ISaDiagnosticsFactory.class)
public class SpectralDiagnosticsFactory implements ISaDiagnosticsFactory {

    //public static final SpectralDiagnosticsFactory Default = new SpectralDiagnosticsFactory();
    private SpectralDiagnosticsConfiguration config_;

    public SpectralDiagnosticsFactory() {
        config_ = new SpectralDiagnosticsConfiguration();
    }

    public SpectralDiagnosticsFactory(SpectralDiagnosticsConfiguration config) {
        config_ = config;
    }

    public SpectralDiagnosticsConfiguration getConfiguration() {
        return config_;
    }

    @Override
    public void dispose() {
     }

    @Override
    public String getName() {
        return "Visual spectral analysis";
    }

    @Override
    public String getDescription() {
        return "Visual spectral analysis";
    }

    @Override
    public boolean isEnabled() {
        return config_.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        config_.setEnabled(enabled);
    }

    @Override
    public Object getProperties() {
        return config_.clone();
    }

    @Override
    public void setProperties(Object obj) {
        SpectralDiagnosticsConfiguration config = (SpectralDiagnosticsConfiguration) obj;
        if (config != null) {
            config.check();
            config_ = config.clone();
        }
    }

    @Override
    public IDiagnostics create(CompositeResults rslts) {
        return SpectralDiagnostics.create(config_, rslts);
    }

    @Override
    public Scope getScope() {
        return Scope.Preliminary;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
