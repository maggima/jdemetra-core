/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demetra.linearmodel;

/**
 *
 * @author Jean Palate <jean.palate@nbb.be>
 */
public interface IOls {
    LeastSquaresResults compute(LinearModel model);
}
