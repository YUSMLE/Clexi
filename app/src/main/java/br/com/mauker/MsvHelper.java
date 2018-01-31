package br.com.mauker;

import br.com.mauker.materialsearchview.MaterialSearchView;

/**
 * Created by Yousef on 1/28/2018.
 */

public class MsvHelper
{

    public static void clearHistory(MaterialSearchView msv)
    {
        msv.clearHistory();
    }

    public static void clearSuggestions(MaterialSearchView msv)
    {
        msv.clearSuggestions();
    }

    public static void clearAll(MaterialSearchView msv)
    {
        msv.clearAll();
    }
}
