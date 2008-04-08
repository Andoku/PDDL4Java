/*
 * Copyright Dept. of Mathematics & Computer Science Univ. Paris-Descartes
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package pddl4j.exp.fcomp;

import java.util.HashMap;
import java.util.Map;

import pddl4j.EvaluationException;
import pddl4j.exp.InitEl;
import pddl4j.exp.fexp.Number;
import pddl4j.exp.fexp.OpExp;
import pddl4j.exp.term.Constant;
import pddl4j.exp.term.Substitution;
import pddl4j.exp.term.Term;
import pddl4j.exp.term.TermID;

/**
 * This class implements the equality comparaison function add of the pddl
 * language.
 * 
 * @author Damien Pellier
 * @version 1.0
 */
public class EqualComp extends FCompExp implements InitEl {

    /**
     * The serial version id of the class.
     */
    private static final long serialVersionUID = -8369304517135857251L;

    /**
     * Creates a new empty equality compared function with two arguments.
     * 
     * @param arg1 the first argument of the equality compared function.
     * @param arg2 the second argument of the equality compared function.
     * @throws NullPointerException if <code>arg1 == null</code> or
     *             <code>arg2 == null</code>.
     */
    public EqualComp(Term arg1, Term arg2) {
        super(Comp.EQUAL, arg1, arg2);
    }
    
    /**
     * Substitutes all occurrences of the variables that occur in this
     * expression and that are mapped in the substitution by its binding term.
     * 
     * @param sigma the substitution.
     * @return a substituted copy of this expression.
     * @throws NullPointerException if <code>sigma == null</code>.
     */
    public EqualComp apply(Substitution sigma) {
        return (EqualComp) super.apply(sigma);
    }
    
    /**
     * Standardizes all occurrences of the variables that occur in this
     * expression. Remember that free variables are existentially quantified.
     * 
     * @return a standardized copy of this expression.
     */
    public EqualComp standardize() {
        return this.standardize(new HashMap<String, String>());
    }

    /**
     * Standardizes all occurrences of the variables that occur in this
     * expression. The map argument is used to store the variable already
     * standardized. The bound argument is used to store the bound variables. a
     * standardized copy of this expression. Remember that free variables are
     * existentially quantified.
     * 
     * @param images the object that maps old variable images to the standardize
     *            image.
     * @return a standardized copy of this expression.
     * @throws NullPointerException if
     *             <code>images == null || bound == null</code>.
     */
    public EqualComp standardize(Map<String, String> images) {
        return (EqualComp) super.standardize(images);
    }
    
    /**
     * Creates and returns a copy of this expression.
     * 
     * @return a clone of this expression instance.
     * @see pddl4j.exp.Exp#clone()
     */
    public EqualComp clone() {
        return (EqualComp) super.clone();
    }
    
    /**
     * Returns <code>true</code> if this compared expression is evaluable.
     * In the general case, an compared expression is evaluable if it is
     * ground and its arguments are recursively either a number either a
     * aritmetic function. In the case of the equality, a compared expression
     * is also evaluable if the arguments of the expression are constant
     * terms.
     * 
     * @return <code>true</code> if this compared expression is evaluable;
     *         <code>false</code> otherwise.
     */
    public boolean isEvaluable() {
        return super.isEvaluable() ? true :
            this.getArg1().getTermID().equals(TermID.CONSTANT)
                && this.getArg2().getTermID().equals(TermID.CONSTANT);
    }
    
    /**
     * Evaluates this compared expression.
     * 
     * @return the result of the evaluation.
     * @throws EvaluationException if the compared expression is not ground or
     *             if an numeric error occurs during the evaluation of an
     *             numeric error.
     * @see pddl4j.exp.fcomp.FCompExp#isEvaluable()            
     */
    public boolean evaluate() throws EvaluationException {
        if (super.isEvaluable()) {
            Number op1 = null;
            if (this.getArg1().equals(TermID.ARITHMETIC_FUNCTION)) {
                OpExp func = (OpExp) this.getArg1();
                op1 = func.evaluate();
            } else {
                op1 = (Number) this.getArg1();
            }
            Number op2 = null;
            if (this.getArg2().equals(TermID.ARITHMETIC_FUNCTION)) {
                OpExp func = (OpExp) this.getArg2();
                op2 = func.evaluate();
            } else {
                op2 = (Number) this.getArg1();
            }
            return op1.equals(op2);
        } else if (this.getArg1().getTermID().equals(TermID.CONSTANT)
                    && this.getArg2().getTermID().equals(TermID.CONSTANT)) {
            Constant op1 = (Constant) this.getArg1();
            Constant op2 = (Constant) this.getArg2();
            return op1.equals(op2);
        } else {
            throw new EvaluationException("comparaison function "
                        + this.toString() + ": is not evaluable");
        }
    }   
}
