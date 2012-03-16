/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.util;

/**
 *
 * @author fabien
 */
public class RoleDelegationResult
{
    public String roleAssignmentName;
    public String grantee;
    public boolean transfert;

    public String toString()
    {
        return 	"role assignment: " + roleAssignmentName +
                ", grantee: " + grantee +
                ", transfert: " + transfert;
    }
}
