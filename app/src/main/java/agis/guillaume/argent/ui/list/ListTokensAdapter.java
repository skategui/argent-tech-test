package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.models.ERC20TokenUser;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class ListTokensAdapter extends RecyclerView.Adapter<ListTokensAdapterImpl.TokenViewHolder> {
    abstract void update(List<ERC20TokenUser> list);

}
