package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.models.Coin;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

abstract class ListTokensAdapter extends RecyclerView.Adapter<ListTokensAdapterImpl.TokenViewHolder> {
    abstract void update(List<Coin> list);

}
