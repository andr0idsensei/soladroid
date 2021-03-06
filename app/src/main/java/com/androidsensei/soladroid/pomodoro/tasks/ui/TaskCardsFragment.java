package com.androidsensei.soladroid.pomodoro.tasks.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.pomodoro.timer.ui.PomodoroActivity;
import com.androidsensei.soladroid.trello.api.TrelloApiService;
import com.androidsensei.soladroid.trello.api.TrelloResultsManager;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.NetworkUtil;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;
import com.androidsensei.soladroid.utils.trello.TrelloServiceFactory;

import java.util.List;

import retrofit.RetrofitError;

/**
 *  Displays the Trello task cards for each of the to do, doing and done lists.
 */
public class TaskCardsFragment extends Fragment {

    /**
     * Argument bundle key for passing the Trello task list id to this fragment.
     */
    private static final String ARG_TRELLO_TASK_LIST_ID = "task_list_id";

    /**
     * Tell the fragment if it needs to reload data from Trello.
     */
    private static final String ARG_REFRESH_DATA = "refresh_data";

    /**
     * The Trello task list id for which we show the tasks in this fragment.
     */
    private String taskListId;

    /**
     * Boolean flag telling the fragment to refresh data from Trello.
     */
    private boolean refreshData;

    /**
     * The adapter for the recycler view containing the Trello task cards.
     */
    private  TrelloTaskCardsAdapter taskCardsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskListId = getArguments().getString(ARG_TRELLO_TASK_LIST_ID);
        refreshData = getArguments().getBoolean(ARG_REFRESH_DATA, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTrelloCardList();
    }

    /**
     * @return true if the current task list in the fragment is the TO DO list.
     */
    private boolean isTodoList() {
        return SharedPrefsUtil.loadPreferenceString(AppConstants.TODO_LIST_KEY, getActivity()).equals(taskListId);
    }

    /**
     * Sets up the Trello task card recycler view.
     */
    private void setupTrelloCardList() {
        RecyclerView trelloCards = (RecyclerView) getView().findViewById(R.id.task_status_card_list);
        trelloCards.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (isTodoList()) {
            taskCardsAdapter = new TrelloTaskCardsAdapter(new TrelloTaskCardsAdapter.CardActionCallback() {
                @Override
                public void onCardTap(Card cardData) {
                    startActivity(PomodoroActivity.getPomodoroActivityIntent(getActivity(), cardData));
                }
            });
        } else {
            taskCardsAdapter = new TrelloTaskCardsAdapter(null);
        }
        trelloCards.setAdapter(taskCardsAdapter);

        TrelloResultsManager resultsManager = TrelloResultsManager.getInstance();
        if (refreshData || !resultsManager.hasCards(taskListId)) {
            new LoadTrelloTaskCards(taskCardsAdapter, getFragmentManager()).execute(taskListId, SharedPrefsUtil.loadPreferenceString(
                    TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getActivity()));
        } else {
            taskCardsAdapter.replaceItems(resultsManager.getTrelloCards(taskListId));
        }
    }

    /**
     * Creates a TaskCardsFragment instance and sets the given task list id as argument.
     *
     * @param taskListId the Trello task list id for which we'll load the tasks in this fragment.
     * @return an instance of TaskCardsFragment
     */
    public static TaskCardsFragment create(String taskListId, boolean refreshData) {
        TaskCardsFragment fragment = new TaskCardsFragment();
        Bundle args = new Bundle();

        args.putString(ARG_TRELLO_TASK_LIST_ID, taskListId);
        args.putBoolean(ARG_REFRESH_DATA, refreshData);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * No-arg constructor.
     */
    public TaskCardsFragment() {
    }

    /**
     * Async task for loading the Trello task cards.
     */
    private static class LoadTrelloTaskCards extends AsyncTask<String, Void, List<Card>> {
        /**
         * The adapter reference for the recycler view containing the Trello task cards.
         */
        private  TrelloTaskCardsAdapter taskCardsAdapter;

        /**
         * The id of the Trello list for which we want to load the cards.
         */
        private String listId;

        /**
         * The fragment manager for displaying the error dialogs.
         */
        private FragmentManager fragmentManager;

        /**
         * We need to handle Retrofit errors.
         */
        private RetrofitError retrofitError;

        /**
         * Constructor taking the task cards adapter as argument.
         *
         * @param taskCardsAdapter the Trello task card adapter.
         */
        public LoadTrelloTaskCards(TrelloTaskCardsAdapter taskCardsAdapter, FragmentManager fragmentManager) {
            this.taskCardsAdapter = taskCardsAdapter;
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected List<Card> doInBackground(String... params) {
            listId = params[0];
            List<Card> cards = null;

            try {
                TrelloApiService service = TrelloServiceFactory.createService();
                cards = service.loadTrelloCardsForList(listId, TrelloConstants.TRELLO_APP_KEY, params[1]);
            } catch (RetrofitError error) {
                retrofitError = error;
            }

            return cards;
        }

        @Override
        protected void onPostExecute(List<Card> cards) {
            if (retrofitError != null) {
                NetworkUtil.showNetworkExceptionDialog(fragmentManager, retrofitError);
            } else {
                if (cards != null) {
                    taskCardsAdapter.replaceItems(cards);
                    TrelloResultsManager.getInstance().putCardList(listId, cards);
                }
            }
        }
    }
}
