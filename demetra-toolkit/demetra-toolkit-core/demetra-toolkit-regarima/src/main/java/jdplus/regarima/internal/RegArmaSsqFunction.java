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
package jdplus.regarima.internal;

import jdplus.regarima.RegArmaModel;
import jdplus.arima.IArimaModel;
import demetra.design.BuilderPattern;
import demetra.design.Development;
import demetra.likelihood.ConcentratedLikelihoodWithMissing;
import jdplus.likelihood.DefaultLikelihoodEvaluation;
import jdplus.maths.functions.IParametersDomain;
import jdplus.maths.functions.IParametricMapping;
import jdplus.maths.functions.ssq.ISsqFunction;
import jdplus.maths.functions.ssq.ISsqFunctionPoint;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import jdplus.arima.estimation.IArimaMapping;
import demetra.data.DoubleSeq;
import demetra.likelihood.Likelihood;
import demetra.maths.matrices.Matrix;

/**
 *
 * @author Jean Palate
 * @param <S>
 */
@Development(status=Development.Status.Alpha)
class RegArmaSsqFunction<S extends IArimaModel> implements ISsqFunction {

    @BuilderPattern(RegArmaSsqFunction.class)
    static class SsqBuilder<S extends IArimaModel> {

        // algorithms
        private boolean ml = true;
        private ConcentratedLikelihoodComputer cll = ConcentratedLikelihoodComputer.DEFAULT_COMPUTER;
        private boolean mt = false;
        // model
        private final DoubleSeq dy;
        private Matrix x;
        private int nmissing;
        // mapping
        private IArimaMapping<S> mapping;

        private SsqBuilder(final DoubleSeq dy) {
            this.dy = dy;
        }

        SsqBuilder variables(Matrix x) {
            this.x = x;
            return this;
        }

        SsqBuilder missingCount(int nm) {
            this.nmissing = nm;
            return this;
        }

        SsqBuilder mapping(IArimaMapping<S> mapping) {
            this.mapping=mapping;
            return this;
        }

        SsqBuilder parallelProcessing(boolean parallel) {
            this.mt = parallel;
            return this;
        }

        SsqBuilder maximumLikelihood(boolean ml) {
            this.ml = ml;
            return this;
        }

        SsqBuilder likelihoodComputer(ConcentratedLikelihoodComputer computer) {
            this.cll = computer;
            return this;
        }

        public RegArmaSsqFunction<S> build() {
            return new RegArmaSsqFunction<>(dy, x, nmissing, mapping, cll,
                    ml ? DefaultLikelihoodEvaluation.v() : DefaultLikelihoodEvaluation.errors(),
                    ml ? DefaultLikelihoodEvaluation.deviance() : DefaultLikelihoodEvaluation.ssq(), mt);
        }
    }

    public static <S extends IArimaModel> SsqBuilder<S> builder(DoubleSeq y){
        return new SsqBuilder<>(y);
    }
    
    // model
    final DoubleSeq dy;
    final Matrix x;
    final int nmissing;
    // mapping
    final IParametricMapping<S> mapping;
    // algorithms
    final ConcentratedLikelihoodComputer cll;
    final ToDoubleFunction<Likelihood> ssqll;
    final Function<Likelihood, DoubleSeq> errors;
    final boolean mt;

    private RegArmaSsqFunction(final DoubleSeq dy,
            final Matrix x,
            final int nm,
            final IParametricMapping<S> mapping,
            final ConcentratedLikelihoodComputer cll,
            final Function<Likelihood, DoubleSeq> errors,
            final ToDoubleFunction<Likelihood> ssqll,
            final boolean mt) {
        this.dy = dy;
        this.x = x;
        this.nmissing = nm;
        this.mapping = mapping;
        this.cll = cll;
        this.ssqll = ssqll;
        this.errors = errors;
        this.mt = mt;
    }

    @Override
    public IParametersDomain getDomain() {
        return mapping;
    }

    @Override
    public Evaluation<S> ssqEvaluate(DoubleSeq parameters) {
        return new Evaluation<>(this, parameters);
    }

    static class Evaluation<S extends IArimaModel> implements ISsqFunctionPoint {

        final RegArmaSsqFunction<S> fn;
        final DoubleSeq p;
        final S arma;
        final ConcentratedLikelihoodWithMissing ll;

        public Evaluation(RegArmaSsqFunction<S> fn, DoubleSeq p) {
            this.fn = fn;
            this.p = p;
            this.arma = fn.mapping.map(p);
            RegArmaModel<S> regarma = new RegArmaModel<>(fn.dy, arma, fn.x, fn.nmissing);
            ll = fn.cll.compute(regarma);
        }

        @Override
        public DoubleSeq getE() {
            return fn.errors.apply(ll);
        }

        public ConcentratedLikelihoodWithMissing getLikelihood() {
            return ll;
        }

        @Override
        public DoubleSeq getParameters() {
            return p;
        }

        @Override
        public double getSsqE() {
            return fn.ssqll.applyAsDouble(ll);
        }

        @Override
        public ISsqFunction getSsqFunction() {
            return fn;
        }

    }

}