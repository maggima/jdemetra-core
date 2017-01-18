/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.tstoolkit.modelling.arima.tramo;

import ec.tstoolkit.Parameter;
import ec.tstoolkit.ParameterType;
import ec.tstoolkit.information.InformationSet;
import ec.tstoolkit.modelling.TsVariableDescriptor;
import ec.tstoolkit.timeseries.Day;
import ec.tstoolkit.timeseries.regression.IOutlierVariable;
import ec.tstoolkit.timeseries.regression.InterventionVariable;
import ec.tstoolkit.timeseries.regression.OutlierDefinition;
import ec.tstoolkit.timeseries.regression.OutlierType;
import ec.tstoolkit.timeseries.regression.Ramp;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author jadoull
 */
public class RegressionSpecTest {

    public RegressionSpecTest() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testInformationSet() {
        RegressionSpec expected = new RegressionSpec();
        RegressionSpec actual = new RegressionSpec();
        InformationSet info;

        assertTrue(expected.isDefault());
        assertTrue(actual.isDefault());
        assertEquals(expected, actual);

        CalendarSpec calspec = new CalendarSpec();
        EasterSpec eastspec = new EasterSpec();
        eastspec.setOption(EasterSpec.Type.IncludeEaster);
        eastspec.setDuration(3);
        calspec.setEaster(eastspec);
        expected.setCalendar(calspec);
        info = expected.write(true);
        actual.read(info);
        assertEquals(3, actual.getCalendar().getEaster().getDuration());

        OutlierDefinition outDef = new OutlierDefinition(Day.toDay(), OutlierType.AO);
        OutlierDefinition[] outliers_ = new OutlierDefinition[]{outDef};
        expected.setOutliers(outliers_);
        info = expected.write(true);
        actual.read(info);
        assertEquals(1, actual.getOutliers().length);
        assertEquals(OutlierType.AO, actual.getOutlier(0).getType());

        TsVariableDescriptor vardesc = new TsVariableDescriptor("test");
        vardesc.setEffect(TsVariableDescriptor.UserComponentType.Seasonal);
        TsVariableDescriptor[] tsvardesc_ = new TsVariableDescriptor[]{vardesc};
        expected.setUserDefinedVariables(tsvardesc_);
        info = expected.write(true);
        actual.read(info);
        assertEquals(1, actual.getUserDefinedVariablesCount());
        assertEquals(1, actual.getUserDefinedVariables().length);
        assertEquals(TsVariableDescriptor.UserComponentType.Seasonal, actual.getUserDefinedVariable(0).getEffect());

        InterventionVariable intvar = new InterventionVariable();
        intvar.setDelta(1.0);
        InterventionVariable[] intvars = new InterventionVariable[]{intvar};
        expected.setInterventionVariables(intvars);
        info = expected.write(true);
        actual.read(info);
        assertEquals(1, actual.getInterventionVariablesCount());
        assertEquals(1, actual.getInterventionVariables().length);
        assertEquals(1.0, actual.getInterventionVariable(0).getDelta(), 0.0);
        
        Ramp rampvar = new Ramp();
        Ramp[] rampvars = new Ramp[]{rampvar};
        expected.setRamps(rampvars);
        info = expected.write(true);
        actual.read(info);
        assertEquals(1, actual.getRampsCount());
        assertEquals(1, actual.getRamps().length);
        
        expected.setFixedCoefficients("ftest",new double[]{10});
        expected.setCoefficients("test",new double[]{10});
        info = expected.write(true);
        actual.read(info);
        assertEquals(actual, expected);
    }

    /**
     * Test of fillDictionary method, of class RegressionSpec.
     */
    @Test
    public void testFillDictionary() {
        System.out.println("fillDictionary");
        String prefix = "";
        Map<String, Class> dic = null;
        RegressionSpec.fillDictionary(prefix, dic);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class RegressionSpec.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        RegressionSpec instance = new RegressionSpec();
        instance.reset();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUsed method, of class RegressionSpec.
     */
    @Test
    public void testIsUsed() {
        System.out.println("isUsed");
        RegressionSpec instance = new RegressionSpec();
        boolean expResult = false;
        boolean result = instance.isUsed();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCalendar method, of class RegressionSpec.
     */
    @Test
    public void testGetCalendar() {
        System.out.println("getCalendar");
        RegressionSpec instance = new RegressionSpec();
        CalendarSpec expResult = null;
        CalendarSpec result = instance.getCalendar();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCalendar method, of class RegressionSpec.
     */
    @Test
    public void testSetCalendar() {
        System.out.println("setCalendar");
        CalendarSpec value = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setCalendar(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutliers method, of class RegressionSpec.
     */
    @Test
    public void testGetOutliers() {
        System.out.println("getOutliers");
        RegressionSpec instance = new RegressionSpec();
        OutlierDefinition[] expResult = null;
        OutlierDefinition[] result = instance.getOutliers();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutliers method, of class RegressionSpec.
     */
    @Test
    public void testSetOutliers() {
        System.out.println("setOutliers");
        OutlierDefinition[] value = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setOutliers(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserDefinedVariables method, of class RegressionSpec.
     */
    @Test
    public void testGetUserDefinedVariables() {
        System.out.println("getUserDefinedVariables");
        RegressionSpec instance = new RegressionSpec();
        TsVariableDescriptor[] expResult = null;
        TsVariableDescriptor[] result = instance.getUserDefinedVariables();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserDefinedVariables method, of class RegressionSpec.
     */
    @Test
    public void testSetUserDefinedVariables() {
        System.out.println("setUserDefinedVariables");
        TsVariableDescriptor[] value = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setUserDefinedVariables(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInterventionVariables method, of class RegressionSpec.
     */
    @Test
    public void testGetInterventionVariables() {
        System.out.println("getInterventionVariables");
        RegressionSpec instance = new RegressionSpec();
        InterventionVariable[] expResult = null;
        InterventionVariable[] result = instance.getInterventionVariables();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInterventionVariables method, of class RegressionSpec.
     */
    @Test
    public void testSetInterventionVariables() {
        System.out.println("setInterventionVariables");
        InterventionVariable[] value = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setInterventionVariables(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDefault method, of class RegressionSpec.
     */
    @Test
    public void testIsDefault() {
        System.out.println("isDefault");
        RegressionSpec instance = new RegressionSpec();
        boolean expResult = false;
        boolean result = instance.isDefault();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class RegressionSpec.
     */
    @Test
    public void testAdd_TsVariableDescriptor() {
        System.out.println("add");
        TsVariableDescriptor svar = null;
        RegressionSpec instance = new RegressionSpec();
        instance.add(svar);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class RegressionSpec.
     */
    @Test
    public void testAdd_OutlierDefinition() {
        System.out.println("add");
        OutlierDefinition outlier = null;
        RegressionSpec instance = new RegressionSpec();
        instance.add(outlier);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contains method, of class RegressionSpec.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        OutlierDefinition outlier = null;
        RegressionSpec instance = new RegressionSpec();
        boolean expResult = false;
        boolean result = instance.contains(outlier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class RegressionSpec.
     */
    @Test
    public void testAdd_IOutlierVariable() {
        System.out.println("add");
        IOutlierVariable outlier = null;
        RegressionSpec instance = new RegressionSpec();
        instance.add(outlier);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class RegressionSpec.
     */
    @Test
    public void testAdd_InterventionVariable() {
        System.out.println("add");
        InterventionVariable ivar = null;
        RegressionSpec instance = new RegressionSpec();
        instance.add(ivar);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearUserDefinedVariables method, of class RegressionSpec.
     */
    @Test
    public void testClearUserDefinedVariables() {
        System.out.println("clearUserDefinedVariables");
        RegressionSpec instance = new RegressionSpec();
        instance.clearUserDefinedVariables();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserDefinedVariablesCount method, of class RegressionSpec.
     */
    @Test
    public void testGetUserDefinedVariablesCount() {
        System.out.println("getUserDefinedVariablesCount");
        RegressionSpec instance = new RegressionSpec();
        int expResult = 0;
        int result = instance.getUserDefinedVariablesCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserDefinedVariable method, of class RegressionSpec.
     */
    @Test
    public void testGetUserDefinedVariable() {
        System.out.println("getUserDefinedVariable");
        int idx = 0;
        RegressionSpec instance = new RegressionSpec();
        TsVariableDescriptor expResult = null;
        TsVariableDescriptor result = instance.getUserDefinedVariable(idx);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearOutliers method, of class RegressionSpec.
     */
    @Test
    public void testClearOutliers() {
        System.out.println("clearOutliers");
        RegressionSpec instance = new RegressionSpec();
        instance.clearOutliers();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutliersCount method, of class RegressionSpec.
     */
    @Test
    public void testGetOutliersCount() {
        System.out.println("getOutliersCount");
        RegressionSpec instance = new RegressionSpec();
        int expResult = 0;
        int result = instance.getOutliersCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutlier method, of class RegressionSpec.
     */
    @Test
    public void testGetOutlier() {
        System.out.println("getOutlier");
        int idx = 0;
        RegressionSpec instance = new RegressionSpec();
        OutlierDefinition expResult = null;
        OutlierDefinition result = instance.getOutlier(idx);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearInterventionVariables method, of class RegressionSpec.
     */
    @Test
    public void testClearInterventionVariables() {
        System.out.println("clearInterventionVariables");
        RegressionSpec instance = new RegressionSpec();
        instance.clearInterventionVariables();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInterventionVariablesCount method, of class RegressionSpec.
     */
    @Test
    public void testGetInterventionVariablesCount() {
        System.out.println("getInterventionVariablesCount");
        RegressionSpec instance = new RegressionSpec();
        int expResult = 0;
        int result = instance.getInterventionVariablesCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInterventionVariable method, of class RegressionSpec.
     */
    @Test
    public void testGetInterventionVariable() {
        System.out.println("getInterventionVariable");
        int idx = 0;
        RegressionSpec instance = new RegressionSpec();
        InterventionVariable expResult = null;
        InterventionVariable result = instance.getInterventionVariable(idx);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegressionVariableNames method, of class RegressionSpec.
     */
    @Test
    public void testGetRegressionVariableNames() {
        System.out.println("getRegressionVariableNames");
        TsFrequency freq = null;
        RegressionSpec instance = new RegressionSpec();
        String[] expResult = null;
        String[] result = instance.getRegressionVariableNames(freq);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testGetCoefficients() {
        System.out.println("getCoefficients");
        String name = "";
        RegressionSpec instance = new RegressionSpec();
        double[] expResult = null;
        double[] result = instance.getCoefficients(name);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testSetCoefficients() {
        System.out.println("setCoefficients");
        String name = "";
        double[] c = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setCoefficients(name, c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearAllCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testClearAllCoefficients() {
        System.out.println("clearAllCoefficients");
        RegressionSpec instance = new RegressionSpec();
        instance.clearAllCoefficients();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testClearCoefficients() {
        System.out.println("clearCoefficients");
        String name = "";
        RegressionSpec instance = new RegressionSpec();
        instance.clearCoefficients(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFixedCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testGetFixedCoefficients() {
        System.out.println("getFixedCoefficients");
        String name = "";
        RegressionSpec instance = new RegressionSpec();
        double[] expResult = null;
        double[] result = instance.getFixedCoefficients(name);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllFixedCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testGetAllFixedCoefficients() {
        System.out.println("getAllFixedCoefficients");
        RegressionSpec instance = new RegressionSpec();
        Map expResult = null;
        Map result = instance.getAllFixedCoefficients();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testGetAllCoefficients() {
        System.out.println("getAllCoefficients");
        RegressionSpec instance = new RegressionSpec();
        Map expResult = null;
        Map result = instance.getAllCoefficients();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setFixedCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testSetFixedCoefficients() {
        System.out.println("setFixedCoefficients");
        String name = "";
        double[] c = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setFixedCoefficients(name, c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearAllFixedCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testClearAllFixedCoefficients() {
        System.out.println("clearAllFixedCoefficients");
        RegressionSpec instance = new RegressionSpec();
        instance.clearAllFixedCoefficients();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearFixedCoefficients method, of class RegressionSpec.
     */
    @Test
    public void testClearFixedCoefficients() {
        System.out.println("clearFixedCoefficients");
        String name = "";
        RegressionSpec instance = new RegressionSpec();
        instance.clearFixedCoefficients(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearRamps method, of class RegressionSpec.
     */
    @Test
    public void testClearRamps() {
        System.out.println("clearRamps");
        RegressionSpec instance = new RegressionSpec();
        instance.clearRamps();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRampsCount method, of class RegressionSpec.
     */
    @Test
    public void testGetRampsCount() {
        System.out.println("getRampsCount");
        RegressionSpec instance = new RegressionSpec();
        int expResult = 0;
        int result = instance.getRampsCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRamp method, of class RegressionSpec.
     */
    @Test
    public void testGetRamp() {
        System.out.println("getRamp");
        int idx = 0;
        RegressionSpec instance = new RegressionSpec();
        Ramp expResult = null;
        Ramp result = instance.getRamp(idx);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class RegressionSpec.
     */
    @Test
    public void testAdd_Ramp() {
        System.out.println("add");
        Ramp rp = null;
        RegressionSpec instance = new RegressionSpec();
        instance.add(rp);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRamps method, of class RegressionSpec.
     */
    @Test
    public void testGetRamps() {
        System.out.println("getRamps");
        RegressionSpec instance = new RegressionSpec();
        Ramp[] expResult = null;
        Ramp[] result = instance.getRamps();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRamps method, of class RegressionSpec.
     */
    @Test
    public void testSetRamps() {
        System.out.println("setRamps");
        Ramp[] value = null;
        RegressionSpec instance = new RegressionSpec();
        instance.setRamps(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class RegressionSpec.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        RegressionSpec instance = new RegressionSpec();
        RegressionSpec expResult = null;
        RegressionSpec result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class RegressionSpec.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        RegressionSpec instance = new RegressionSpec();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class RegressionSpec.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        RegressionSpec instance = new RegressionSpec();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write method, of class RegressionSpec.
     */
    @Test
    public void testWrite() {
        System.out.println("write");
        boolean verbose = false;
        RegressionSpec instance = new RegressionSpec();
        InformationSet expResult = null;
        InformationSet result = instance.write(verbose);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of read method, of class RegressionSpec.
     */
    @Test
    public void testRead() {
        System.out.println("read");
        InformationSet info = null;
        RegressionSpec instance = new RegressionSpec();
        boolean expResult = false;
        boolean result = instance.read(info);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
