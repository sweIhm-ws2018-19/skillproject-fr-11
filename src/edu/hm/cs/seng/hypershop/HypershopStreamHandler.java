/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package edu.hm.cs.seng.hypershop;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import edu.hm.cs.seng.hypershop.handlers.HelpIntentHandler;
import edu.hm.cs.seng.hypershop.handlers.FallbackIntentHandler;
import edu.hm.cs.seng.hypershop.handlers.LaunchRequestHandler;
import edu.hm.cs.seng.hypershop.handlers.SessionEndedRequestHandler;
import edu.hm.cs.seng.hypershop.handlers.CancelandStopIntentHandler;
import edu.hm.cs.seng.hypershop.handlers.AddIngredientIntentHandler;

@SuppressWarnings("unused")
public class HypershopStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new AddIngredientIntentHandler(),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new FallbackIntentHandler())
                .withSkillId("amzn1.ask.skill.83f45138-33db-4af6-874d-c78af275fa85")
                .build();
    }

    public HypershopStreamHandler() {
        super(getSkill());
    }

}
