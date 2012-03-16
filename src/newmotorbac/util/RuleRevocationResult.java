/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.util;

/**
 *
 * @author fabien
 */
public class RuleRevocationResult
{
    public String ruleName;
    public boolean cascade;
    public boolean strong;
    public boolean obligation;

    public String toString()
    {
            return "rule name: " + ruleName +
                   ", obligation: " + obligation;
    }
}
