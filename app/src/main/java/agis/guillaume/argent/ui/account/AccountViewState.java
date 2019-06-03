package agis.guillaume.argent.ui.account;

import java.math.BigDecimal;

/**
 * MVI architecture : ViewState associated to the account
 * (Looks better in kotlin, using sealed class as the Java boilerplate is not needed ... )
 */
class AccountViewState {

    final static class DisplayAccountBalance extends AccountViewState {

        private BigDecimal value;

        DisplayAccountBalance(BigDecimal value) {
            this.value = value;
        }

        BigDecimal getValue() {
            return value;
        }
    }

    final static class DisplayERC20Balance extends AccountViewState {

        private BigDecimal value;

        DisplayERC20Balance(BigDecimal value) {
            this.value = value;
        }

        BigDecimal getValue() {
            return value;
        }
    }

    public final static class Error extends AccountViewState {
        private String error;

        Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    final static class NoInternet extends AccountViewState { }
    final static class ShowLoading extends AccountViewState { }
    final static class ApiKeyNotProvided extends AccountViewState { }
    final static class AccountAddrProvided extends AccountViewState { }
    final static class ViewMore extends AccountViewState { }
}


