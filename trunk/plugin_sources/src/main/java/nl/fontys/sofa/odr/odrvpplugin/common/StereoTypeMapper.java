/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import com.vp.plugin.action.VPContext;
import com.vp.plugin.diagram.IDiagramElement;
import com.vp.plugin.diagram.IShapeUIModel;
import com.vp.plugin.model.IActivityAction;
import com.vp.plugin.model.IControlFlow;
import java.awt.Color;

/**
 * The StereoTypeMapper is used to change the background color of the 
 * "decision" that is shown in the diagram. The StereoTypeMapper 
 * @author eigo
 */
public final class StereoTypeMapper {

    private StereoTypeMapper() {
    }

    /**
     * The setRelationshipType method provides functionality to set change
     * the relationship type of the relationship GUI component. 
     * The VP model component used for the relationships is IControlFlow
     * 
     * @param context the Visual Paradigm UI component
     * @param selectedItem The relationship type that was selected
     */
    public static void setRelationshipType(VPContext context, 
            String selectedItem) {
        IControlFlow myAction = (IControlFlow) context.getModelElement();
        String[] currentState = myAction.toStereotypeArray();
        for (String state : currentState) {
            myAction.removeStereotype(state);
        }
        myAction.addStereotype(selectedItem);
    }

    /**
     * The method setDecisionState is used to change the decision 
     * elements state. The VP model element to represent a decision is an
     * IActivityAction.
     * @param context the VP context element from which we get the model element
     * @param selectedItem the decision state which was selected by the user.
     */
    public static void setDecisionState(IDiagramElement context, 
            String selectedItem) {
        IActivityAction myAction = (IActivityAction) context.getModelElement();
        String[] currentState = myAction.toStereotypeArray();
        for (String state : currentState) {
            myAction.removeStereotype(state);
        }
        myAction.addStereotype(selectedItem);
        IShapeUIModel element = (IShapeUIModel) context;
        UserSettings userSettings = (UserSettings) 
                ValueRepository.getInstance().getValue(VPStrings.USERSETTINGS);
        element.getFillColor()
               .setColor1(userSettings.getDecisionStateColor(selectedItem));
    }

    /**
     * The setColorofDiagramElement method is used to set the color of a 
     * decision when its state has been changed. 
     * @param element the diagram element which color has to be changed.
     * @param color the color that is associated with the decision state in the
     * user settings.
     */
    public static void setColorOfDiagramElement(IDiagramElement element, 
                                                Color color) {
        IShapeUIModel model = (IShapeUIModel) element;
        /**
         * setColor1 is the method to use for setting the background color
         * of a decision GUI element.
         */
        model.getFillColor().setColor1(color);
    }
}
