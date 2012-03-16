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
public abstract class OrbacUndoRedoOperation implements IUndoRedoOperation {

    // operation state
    protected boolean hasBeenExecuted = false;
    // operation arguments
    protected ArrayList<Object> args = new ArrayList<Object>();
    // the associated orbac policy
    protected AbstractOrbacPolicy policy;

    public OrbacUndoRedoOperation(AbstractOrbacPolicy policy, ArrayList<Object> args)
    {
        this.policy = policy;
        this.args = args;
    }
    
    @Override
    public void redo() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void undo() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasBeenExecuted() {
        return hasBeenExecuted;
    }

}
