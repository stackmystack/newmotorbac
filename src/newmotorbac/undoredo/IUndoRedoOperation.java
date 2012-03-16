/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.undoredo;

/**
 *  Interface to be implemented by reversible operations on the policy. Used to implement motorbac undo/redo feature.
 * @author fabien
 */
public interface IUndoRedoOperation {
    // do the operation represented by this object
    public void redo() throws Exception ;
    // undo the operation represented by this object
    public void undo() throws Exception ;
    // return true if this operation has been executed
    public boolean hasBeenExecuted();
}
