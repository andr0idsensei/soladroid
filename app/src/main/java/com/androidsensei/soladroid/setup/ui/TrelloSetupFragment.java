package com.androidsensei.soladroid.setup.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.trello.api.TrelloResultsManager;
import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.MemberToken;
import com.androidsensei.soladroid.trello.api.TrelloService;
import com.androidsensei.soladroid.trello.api.model.TrelloList;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;
import com.androidsensei.soladroid.utils.trello.TrelloServiceFactory;

import java.util.List;

/**
 * The setup fragment will display the user's Trello boards and will allow her to choose which board to use, as well
 * as match lists in the board to the to do, doing, done lists in the application.
 *
 * TODO save the user selections and complete the setup
 * TODO re-iterate the UI and polish it
 */
public class TrelloSetupFragment extends SolaDroidBaseFragment {
    /**
     * The adapter for showing the boards.
     */
    private SetupSpinnerAdapter boardNamesAdapter;

    /**
     * The adapter for selecting the to do lists.
     */
    private SetupSpinnerAdapter todoListsAdapter;

    /**
     * The adapter for selecting the doing lists.
     */
    private SetupSpinnerAdapter doingListsAdapter;

    /**
     * The adapter for selecting the done lists.
     */
    private SetupSpinnerAdapter doneListsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_setup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupTaskLists();
        setupBoardsSpinner();
    }

    /**
     * Sets up the boards spinner view.
     */
    private void setupBoardsSpinner() {
        Spinner boardsSpinner = (Spinner) getView().findViewById(R.id.trello_setup_boards_section_spinner);
        boardNamesAdapter = new SetupSpinnerAdapter(getActivity().getLayoutInflater());
        boardsSpinner.setAdapter(boardNamesAdapter);

        final TrelloResultsManager resultsManager = TrelloResultsManager.getInstance();
        if (resultsManager.hasBoards()) {
            boardNamesAdapter.addItems(resultsManager.getTrelloBoards());
        } else {
            new LoadBoardsTask(boardNamesAdapter).execute(TrelloConstants.TRELLO_APP_KEY, SharedPrefsUtil.loadPreferenceString(
                    TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getActivity()));
        }

        boardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Board selectedBoard = (Board) boardNamesAdapter.getItem(position);
                if (resultsManager.hasLists(selectedBoard.getId())) {
                    List<TrelloList> taskList = resultsManager.getTrelloList(selectedBoard.getId());
                    todoListsAdapter.replaceItems(taskList);
                    doingListsAdapter.replaceItems(taskList);
                    doneListsAdapter.replaceItems(taskList);
                } else {
                    new LoadTaskListsTask(todoListsAdapter, doingListsAdapter, doneListsAdapter).execute(selectedBoard.getId(),
                            TrelloConstants.TRELLO_APP_KEY, SharedPrefsUtil.loadPreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY,
                                    getActivity()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Sets up the task lists spinner views.
     */
    private void setupTaskLists() {
        Spinner todoSpinner = (Spinner) getView().findViewById(R.id.trello_setup_todo_section_spinner);
        Spinner doingSpinner = (Spinner) getView().findViewById(R.id.trello_setup_doing_section_spinner);
        Spinner doneSpinner = (Spinner) getView().findViewById(R.id.trello_setup_done_section_spinner);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        todoListsAdapter = new SetupSpinnerAdapter(inflater);
        doingListsAdapter = new SetupSpinnerAdapter(inflater);
        doneListsAdapter = new SetupSpinnerAdapter(inflater);

        todoSpinner.setAdapter(todoListsAdapter);
        doingSpinner.setAdapter(doingListsAdapter);
        doneSpinner.setAdapter(doneListsAdapter);
    }

    /**
     * AsyncTask for loading the Trello open boards in the spinner.
     */
    private static class LoadBoardsTask extends AsyncTask<String, Void, List<Board>> {
        private SetupSpinnerAdapter boardNamesAdapter;

        public LoadBoardsTask(SetupSpinnerAdapter boardNamesAdapter) {
            this.boardNamesAdapter = boardNamesAdapter;
        }

        @Override
        protected List<Board> doInBackground(String... params) {
            Log.d("r1k0", "doInBackground params: " + params[0] + " - " + params[1]);
            TrelloService service = TrelloServiceFactory.createService();
            MemberToken token = service.getMemberToken(params[1], params[0]);
            Log.d("r1k0", "token: " + token);

            List<Board> boards = service.loadOpenBoards(token.getIdMember(), params[0], params[1]);

            return boards;
        }

        @Override
        protected void onPostExecute(List<Board> boards) {
            Log.d("r1k0", "trello boards: " + boards);
            if (boards != null) {
                TrelloResultsManager.getInstance().putTrelloBoards(boards);
                boardNamesAdapter.addItems(boards);
            }
        }
    }

    /**
     * AsyncTask for loading the Trello task lists that belong to a given board.
     */
    private static class LoadTaskListsTask extends AsyncTask<String, Void, List<TrelloList>> {
        /**
         * The spinner adapter for the to do spinner.
         */
        private SetupSpinnerAdapter todoSpinner;

        /**
         * The spinner adapter for the doing spinner.
         */
        private SetupSpinnerAdapter doingSpinner;

        /**
         * The spinner adapter for the done spinner.
         */
        private SetupSpinnerAdapter doneSpinner;

        /**
         * The board id for which we want to load the lists.
         */
        private String boardId;

        public LoadTaskListsTask(SetupSpinnerAdapter todoSpinner, SetupSpinnerAdapter doingSpinner,
                                 SetupSpinnerAdapter doneSpinner) {
            this.todoSpinner = todoSpinner;
            this.doingSpinner = doingSpinner;
            this.doneSpinner = doneSpinner;
        }

        @Override
        protected List<TrelloList> doInBackground(String... params) {
            boardId = params[0];
            TrelloService service = TrelloServiceFactory.createService();
            List<TrelloList> trelloLists = service.loadTaskListsForBoard(boardId, params[1], params[2]);

            return trelloLists;
        }

        @Override
        protected void onPostExecute(List<TrelloList> trelloLists) {
            if (trelloLists != null) {
                TrelloResultsManager.getInstance().putTrelloLists(boardId, trelloLists);
                todoSpinner.replaceItems(trelloLists);
                doingSpinner.replaceItems(trelloLists);
                doneSpinner.replaceItems(trelloLists);
            }
        }
    }
}
