package agis.guillaume.argent.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for recyclerview to check if there are some  difference between 2 lists, containing in the recyclerview, to improve
 * the recyclerview performance, as it won't rebuild the view from scratch
 */
public class DiffCallback extends DiffUtil.Callback {

    private List oldList = new ArrayList();
    private List newList = new ArrayList();

    public void compareLists(List oldList, List newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    /**
     * Simplified for now, would probably want to compare against an ID.
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return !(oldItemPosition >= oldList.size() || newItemPosition >= newList.size())
                && oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return !(oldItemPosition >= oldList.size() || newItemPosition >= newList.size())
                && oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}