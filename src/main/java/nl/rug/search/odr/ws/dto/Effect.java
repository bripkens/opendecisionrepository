/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.odr.ws.dto;

import javax.xml.bind.annotation.XmlEnum;
import nl.rug.search.odr.ws.DTO;

/**
 *
 * @author fragger
 */
@XmlEnum
@DTO
public enum Effect {
        
    EXCLUDED, VERYPOSITIVE,POSITIVE,NEUTRAL,NEGATIVE,VERYNEGATIVE, REQUIRED;
}
