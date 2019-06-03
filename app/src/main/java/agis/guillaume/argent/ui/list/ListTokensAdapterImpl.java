package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.R;
import agis.guillaume.argent.models.ERC20TokenUser;
import agis.guillaume.argent.utils.DiffCallback;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the recyclerview that display the list of ERc 20 tokens associated to this account,
 * with their balance, in ETH
 */
public class ListTokensAdapterImpl extends ListTokensAdapter {


    private DiffCallback callback;
    private List<ERC20TokenUser> tokens = new ArrayList<>();
    private LayoutInflater mInflater;

    /**
     * Default constructor, the dependencies are provided by dagger
     *
     * @param context      current context
     * @param diffCallback DiffUtils tools, used to improve the performance of the recyclerview
     */
    @Inject
    ListTokensAdapterImpl(Context context, DiffCallback diffCallback) {
        this.mInflater = LayoutInflater.from(context);
        this.callback = diffCallback;
    }


    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_token, parent, false);
        return new TokenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {
        ERC20TokenUser token = tokens.get(position);
        holder.update(token);
    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    /**
     * Update only the rows that has been modified, to avoid updating all the list. Improve the performance
     *
     * @param updatedTokenList latest version of the tokens list
     */
    @Override
    void update(List<ERC20TokenUser> updatedTokenList) {
        callback.compareLists(this.tokens, updatedTokenList);
        DiffUtil.DiffResult differenceFound = DiffUtil.calculateDiff(callback);
        this.tokens.clear();
        this.tokens.addAll(updatedTokenList);
        differenceFound.dispatchUpdatesTo(this);
    }


    // stores and recycles views as they are scrolled off screen
    class TokenViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTokenName)
        TextView tvTokenName;

        @BindView(R.id.tvAmount)
        TextView tvAmount;

        @BindView(R.id.tvBalance)
        TextView tvBalance;

        @BindView(R.id.ivInfo)
        ImageView ivInfo;

        TokenViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull ERC20TokenUser token) {

            tvTokenName.setText(String.format(tvTokenName.getContext().getString(R.string.item_token_title), token.getName(), token.getSymbol()));

            displayAmount(token.getAmount());
            if (token.isAvailableForTrading()) {
                displayBalanceETH(token);
            } else {
                displayInfoBubble();
            }
        }

        private void displayAmount(BigDecimal amount) {
            if (amount.doubleValue() == 0.0)
                tvAmount.setText(R.string.item_no_token);
            else
                tvAmount.setText(String.format(tvAmount.getContext().getString(R.string.item_token_nb), amount));
        }

        private void displayBalanceETH(@NonNull ERC20TokenUser token) {
            tvBalance.setVisibility(View.VISIBLE);
            ivInfo.setVisibility(View.GONE);
            tvBalance.setText(String.format(tvBalance.getResources().getString(R.string.account_amount_eth), token.getBalance()));
        }

        private void displayInfoBubble() {
            tvBalance.setVisibility(View.GONE);
            ivInfo.setVisibility(View.VISIBLE);
            ivInfo.setOnClickListener(v -> Toast.makeText(v.getContext(), v.getContext().getString(R.string.item_token_error), Toast.LENGTH_LONG).show());
        }

    }


}
