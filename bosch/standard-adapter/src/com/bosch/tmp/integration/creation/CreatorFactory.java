package com.bosch.tmp.integration.creation;

import com.bosch.tmp.integration.validation.CustomerId;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the factory to get/create a message creator based on the customer id.
 *
 * @author gtk1pal
 */
public class CreatorFactory
{

    public static Map<CustomerId, Creator> creators = new HashMap();

    public static Creator getCreator(CustomerId customerId)
    {
        Creator creator = creators.get(customerId);
        if (creator == null)
        {
            switch (customerId)
            {
                case SA:
                    creator = new DefaultCreator();
                    break;
                default:
                    creator = new DefaultCreator();
                    break;
            }
            creators.put(customerId, creator);
        }
        return creator;
    }
}
