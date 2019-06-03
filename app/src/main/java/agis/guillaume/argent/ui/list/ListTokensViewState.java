package agis.guillaume.argent.ui.list;

import agis.guillaume.argent.models.Coin;

import java.util.List;

/**
 * MVI architecture : ViewState associated to the list tokens
 * (Looks better in kotlin, using sealed class as the Java boilerplate is not needed ... )
 */
class ListTokensViewState {

    public final static class DisplayTokensList extends ListTokensViewState {
        private List<Coin> list;

        DisplayTokensList(List<Coin> list) {
            this.list = list;
        }

        public List<Coin> getList() {
            return list;
        }
    }

    public final static class Error extends ListTokensViewState {
        private String error;

        Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    final static class NoInternet extends ListTokensViewState { }

    final static class ShowLoading extends ListTokensViewState { }
    final static class DisplayEmptyListMessage extends ListTokensViewState { }
}


