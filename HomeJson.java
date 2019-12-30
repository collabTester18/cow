package com.smartbear.collaborator.qa.json;

+import com.smartbear.collaborator.qa.entities.ActionItem;
import com.smartbear.collaborator.qa.json.api.JsonAction;
import com.smartbear.collaborator.qa.json.api.commands.user.GetActionItemsCommand;
import com.smartbear.collaborator.qa.json.api.commands.user.data.ActionItemData;

import java.util.ArrayList;

public class HomeJson {
    public static ArrayList<ActionItem> getActionItems() throws Exception {
        ArrayList<ActionItemData> rawList = (ArrayList<ActionItemData>) JsonAction.execute(new GetActionItemsCommand());
        ArrayList<ActionItem> list = new ArrayList<ActionItem>();
        for (ActionItemData item : rawList) {
            list.add(new ActionItem(item));
        }
        return list;
    }
}
