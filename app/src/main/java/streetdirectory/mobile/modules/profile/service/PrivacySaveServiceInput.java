

package streetdirectory.mobile.modules.profile.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class PrivacySaveServiceInput extends SDHttpServiceInput
{

    public PrivacySaveServiceInput()
    {
    }

    public PrivacySaveServiceInput(String s, String s1, int i, int j, int k, int l, int i1)
    {
        super(s);
        uid = s1;
        likeBusiness = i;
        favoritePlace = j;
        favoriteRoute = k;
        shout = l;
        friends = i1;
    }

    public String getURL()
    {
        return URLFactory.createURLPrivacySave(uid, likeBusiness, favoritePlace, favoriteRoute, shout, friends, apiVersion);
    }

    public int favoritePlace;
    public int favoriteRoute;
    public int friends;
    public int likeBusiness;
    public int shout;
    public String uid;
}
