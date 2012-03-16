/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newmotorbac.util;

import newmotorbac.NewMotorbacView;
import newmotorbac.PanelAbstractConflicts;
import newmotorbac.PanelAbstractEntities;
import newmotorbac.PanelAbstractEntity;
import newmotorbac.PanelAbstractRuleList;
import newmotorbac.PanelAbstractRules;
import newmotorbac.PanelConcreteConflicts;
import newmotorbac.PanelConcreteEntity;
import newmotorbac.PanelConcreteRules;
import newmotorbac.PanelConflicts;
import newmotorbac.PanelContextState;
import newmotorbac.PanelContexts;
import newmotorbac.PanelEntityDefinitions;
import newmotorbac.PanelPolicy;
import newmotorbac.PanelRulesPriorities;
import newmotorbac.PanelSeparationConstraints;
import newmotorbac.PanelSimulation;
import orbac.AbstractOrbacPolicy;

/**
 *
 * @author fabien
 */
public class OrbacPolicyContext {
    // the associated policy
    public AbstractOrbacPolicy thePolicy;
    // the current organization selected in the organization list
    public String currentOrganization;
    // true if AdOrBAC objects must be displayed
    public boolean adorbacViewActive = false;
    // path to the policy file
    public String path;

    // instances of all parts of the GUI
    public PanelPolicy                 panelPolicy;
    public PanelAbstractEntities       panelAbstractEntities;
    public PanelAbstractEntity         panelRoles, panelActivities, panelViews;
    public PanelContexts               panelContexts;
    public PanelAbstractRules          panelAbstractRules;
    public PanelAbstractRuleList       panelPermissions, panelProhibitions, panelObligations;
    public PanelConflicts              panelConflicts;
    public PanelAbstractConflicts      panelAbstractConflicts;
    public PanelConcreteConflicts      panelConcreteConflicts;
    public PanelSeparationConstraints  panelSeparationConstraints;
    public PanelRulesPriorities        panelRulesPriorities;
    public PanelEntityDefinitions      panelEntityDefinitions;
    public PanelSimulation             panelSimulation;
    public PanelConcreteRules          panelConcreteRules;
    public PanelContextState           panelContextState;
    public PanelConcreteEntity         panelSubjects, panelActions, panelObjects;
    // parent GUI
    public NewMotorbacView             parentGui;
}
