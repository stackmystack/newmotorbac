/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.util;

/**
 *
 * @author fabien
 */
public class RuleDelegationResult {

    public String ruleName;
    public String grantee;
    public boolean transfert;
    public boolean grantOption;
    public String contextOption;
    public int steps;
    public boolean obligation;

    public String toString()
    {
        return "rule name: " + ruleName +
                ", grantee: " + grantee +
                ", transfert: " + transfert +
                ", grantOption: " + grantOption +
                ", contextOption: " + contextOption +
                ", steps: " + steps;
    }
}
