/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.notnegativeidvalidator;

import com.netbeansrcp.taskidgenerator.api.IdValidator;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nbuser
 */
@ServiceProvider(service = IdValidator.class)
public class NotNegativeIdValidator implements IdValidator {
    public boolean validate(String id) {
        System.out.println("NotNegativeIdValidator.validate(" + id
                + ")");
        return !id.startsWith("-");
    }
}
