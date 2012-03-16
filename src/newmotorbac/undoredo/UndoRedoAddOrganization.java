/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.undoredo;

import java.util.ArrayList;
import orbac.AbstractOrbacPolicy;

/**
 *
 * @author fabien
 */
public class UndoRedoAddOrganization extends OrbacUndoRedoOperation {

    public UndoRedoAddOrganization(AbstractOrbacPolicy policy, ArrayList<Object> args)
    {
        super(policy, args);
    }
    
    @Override
    public void redo() throws Exception {
        // do the organization addition if not already done
        if ( hasBeenExecuted == false )
        {
            policy.CreateOrganization((String)args.get(0));
        }
        else throw new UnsupportedOperationException("organization ");
    }

    @Override
    public void undo() throws Exception {
    }
}
