package com.androidsensei.soladroid.pomodoro.tasks.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.SolaDroidActivity;
import com.androidsensei.soladroid.trello.api.TrelloResultsManager;
import com.androidsensei.soladroid.trello.api.TrelloService;
import com.androidsensei.soladroid.trello.api.model.Card;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;
import com.androidsensei.soladroid.utils.trello.TrelloServiceFactory;

import java.util.List;

/**
 *  Displays the Trello task cards for each of the to do, doing and done lists.
 */
public class TaskCardsFragment extends Fragment {

    /**
     * Argument bundle key for passing the Trello task list id to this fragment.
     */
    private static final String ARG_TRELLO_TASK_LIST_ID = "task_list_id";

    /**
     * The Trello task list id for which we show the tasks in this fragment.
     */
    private String taskListId;

    /**
     * The adapter for the recycler view containing the Trello task cards.
     */
    private  TrelloTaskCardsAdapter taskCardsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskListId = getArguments().getString(ARG_TRELLO_TASK_LIST_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    Intent startTask = new Intent(getActivity(), SolaDroidActivity.class);
                    startTask.putExtra(AppConstants.START_TASK_CARD_KEY, cardData);
                    startActivity(startTask);
                }
            });
        } else {
            taskCardsAdapter = new TrelloTaskCardsAdapter(null);
        }
        trelloCards.setAdapter(taskCardsAdapter);

        TrelloResultsManager resultsManager = TrelloResultsManager.getInstance();
        if (resultsManager.hasCards(taskListId)) {
            taskCardsAdapter.replaceItems(resultsManager.getTrelloCards(taskListId));
        } else {
            new LoadTrelloTaskCards(taskCardsAdapter).execute(taskListId, SharedPrefsUtil.loadPreferenceString(
                    TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getActivity()));
        }
    }

    /**
     * Creates a TaskCardsFragment instance and sets the given task list id as argument.
     *
     * @param taskListId the Trello task list id for which we'll load the tasks in this fragment.
     * @return an instance of TaskCardsFragment
     */
    public static TaskCardsFragment create(String taskListId) {
        TaskCardsFragment fragment = new TaskCardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRELLO_TASK_LIST_ID, taskListId);
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
         * Constructor taking the task cards adapter as argument.
         *
         * @param taskCardsAdapter the Trello task card adapter.
         */
        public LoadTrelloTaskCards(TrelloTaskCardsAdapter taskCardsAdapter) {
            this.taskCardsAdapter = taskCardsAdapter;
        }

        @Override
        protected List<Card> doInBackground(String... params) {
            listId = params[0];
            TrelloService service = TrelloServiceFactory.createService();

            return service.loadTrelloCardsForList(listId, TrelloConstants.TRELLO_APP_KEY, params[1]);
        }

        @Override
        protected void onPostExecute(List<Card> cards) {
            if (cards != null) {
                taskCardsAdapter.replaceItems(cards);
                TrelloResultsManager.getInstance().putCardList(listId, cards);
            }
        }
    }
}
