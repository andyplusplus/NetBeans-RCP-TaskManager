/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskidgenerator;

import com.netbeansrcp.taskidgenerator.api.IdValidator;
import com.netbeansrcp.taskidgenerator.api.TaskIdGenerator;
import java.util.Random;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nbuser
 */
@ServiceProvider(service = TaskIdGenerator.class)
public class TaskIdGeneratorImpl implements TaskIdGenerator {
//    private Random random = new Random();
//    public String generateID() {
//        String id = "000000" + this.random.nextInt();
//        id = id.substring(id.length() - 6);
//        return id;
//    }

    private Random random = new Random();

    public String generateID() {
        Lookup.Result<IdValidator> rslt =
                Lookup.getDefault().lookupResult(IdValidator.class);
        String id = null;
        boolean valid = false;
        while (!valid) {
            id = this.getId();
            valid = true;
            for (IdValidator validator : rslt.allInstances()) {
                valid = valid & validator.validate(id);
            }
        }
        return id;
    }

    private String getId() {
        String id = "000000" + this.random.nextInt();
        return id.substring(id.length() - 6);
    }
    
}
