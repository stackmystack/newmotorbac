/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.util;

/**
 *
 * @author fabien
 */
public class RoleRevocationResult
{
    public String roleAssignmentName;
    public boolean cascade;
    public boolean strong;
    public String grantee;

    public String toString()
    {
        return 	"role assignment: " + roleAssignmentName +
                ", grantee: " + grantee;
    }
}
