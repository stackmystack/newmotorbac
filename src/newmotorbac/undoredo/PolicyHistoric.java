/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.undoredo;

import java.util.ArrayList;

/**
 *
 * @author fabien
 */
public class PolicyHistoric {
    // maximum size for the historic
    private int maximumHistoricSize = 10;
    // the historic as a list of policies represented as strings
    private ArrayList<String> historic = new ArrayList<String>();
    // position in historic for the associated policy
    private int posInHistoric = -1;
    // position in history when the policy was saved for the last time
    private int posWhenSaved = -1;

    public PolicyHistoric(int maximumHistoricSize)
    {
        this.maximumHistoricSize = maximumHistoricSize;
    }
    public PolicyHistoric()
    {
    }

    // called to initialize the stack, store the first created policy
    public void InitializeStack(String policyAsString)
    {
        // store policy
        historic.add(policyAsString);
        // initialize state variables
        posInHistoric = 0;
        posWhenSaved = 0;
    }

    // return true if the previous policy exists, i.e if we can undo last modification
    public boolean CanUndo()
    {
        if ( posInHistoric > 0 ) return true;
        return false;
    }
    // return true if the next policy exists, i.e if we can redo last modification
    public boolean CanRedo()
    {
        if ( posInHistoric < (historic.size() - 1) ) return true;
        return false;
    }

    // add a new version of the policy
    public void PushPolicy(String policyAsString)
    {
        // the policy may be added after a policy which is not the newest
        // in the history
        // this occurs when the user modifies a policy older than the last stored in the historic
        if ( posInHistoric != (historic.size() - 1) )
        {
            // remove all element after the current active policy
            while ( posInHistoric != (historic.size() - 1) )
                historic.remove(posInHistoric + 1);
        }
        // store policy
        historic.add(policyAsString);
        // if historic is getting too big we remove the first element
        // and modify the position of last saved policy
        if ( historic.size() > maximumHistoricSize )
        {
            historic.remove(0);
            // we erased one policy, the last saved position is shifted
            posWhenSaved--;
            if ( posWhenSaved < 0 ) posWhenSaved = -1;
        }

        // store position in historic of current active policy
        posInHistoric = historic.indexOf(policyAsString);
    }
    // retrieve the policy stored before the current active policy
    // i.e undo last modification
    public String RetrievePreviousPolicy()
    {
        if ( CanUndo() )
            posInHistoric--;
        return historic.get(posInHistoric);
    }
    // retrieve the policy stored after the current active policy
    // i.e redo last modification
    public String RetrieveNextPolicy()
    {
        if ( CanRedo() )
            posInHistoric++;
        return historic.get(posInHistoric);
    }
    // called when the associated policy as been saved
    public void AssociatedPolicyHasBeenSaved()
    {
        posWhenSaved = posInHistoric;
    }
    // return true if the current selected policy in historic is not the last
    // one which was saved
    // i.e check if the associated policy has been modified
    public boolean IsModified()
    {
        return ( posInHistoric != posWhenSaved );
    }
    // return true if the historic is empty
    public boolean IsEmpty()
    {
        return posInHistoric == -1;
    }
}
