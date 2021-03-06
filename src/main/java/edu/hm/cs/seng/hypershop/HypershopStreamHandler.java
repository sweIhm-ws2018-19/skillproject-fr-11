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
import edu.hm.cs.seng.hypershop.handlers.*;

@SuppressWarnings("unused")
public class HypershopStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new BackIntentHandler(),
                        new AddIngredientIntentHandler(),
                        new RemoveIngredientIntentHandler(),
                        new ListIngredientsIntentHandler(),
                        new CreateRecipeIntentHandler(),
                        new AddRecipesIntentHandler(),
                        new ListRecipesIntentHandler(),
                        new EditRecipeIntentHandler(),
                        new RemoveRecipeIntentHandler(),
                        new ClearListIntentHandler(),
                        new DeleteRecipeIntentHandler(),
                        new ListStepIngredientsIntentHandler(),
                        new RepeatIntentHandler(),
                        new NextIngredientIntentHandler(),
                        new CheckIngredientIntentHandler(),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new YesIntentFallbackHandler(),
                        new NoIntentFallbackHandler(),
                        new FallbackIntentHandler())
                .withTableName(Constants.DYNAMO_TABLE_NAME)
                .withSkillId(Constants.SKILL_ID)
                .build();
    }

    public HypershopStreamHandler() {
        super(getSkill());
    }

}
