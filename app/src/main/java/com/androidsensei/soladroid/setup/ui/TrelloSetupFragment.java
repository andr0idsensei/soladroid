package com.androidsensei.soladroid.setup.ui;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.setup.ui.logic.LoadTrelloListsCallback;
import com.androidsensei.soladroid.trello.api.TrelloApiService;
import com.androidsensei.soladroid.trello.api.TrelloResultsManager;
import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.MemberToken;
import com.androidsensei.soladroid.trello.api.model.TrelloList;
import com.androidsensei.soladroid.utils.AppConstants;
import com.androidsensei.soladroid.utils.NetworkUtil;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;
import com.androidsensei.soladroid.utils.trello.TrelloServiceFactory;
import com.androidsensei.soladroid.utils.ui.SolaDroidBaseFragment;

import java.util.List;

import retrofit.RetrofitError;

/**
 * The setup fragment will display the user's Trello boards and will allow her to choose which board to use, as well
 * as match lists in the board to the to do, doing, done lists in the application.
 * <p/>
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

    /**
     * The Trello list containing the to do tasks.
     */
    private TrelloList todoList;

    /**
     * The Trello list containing the doing tasks.
     */
    private TrelloList doingList;

    /**
     * The Trello list containing the done tasks.
     */
    private TrelloList doneList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_setup, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        setupTaskLists();
        setupBoardsSpinner();
        setupAllDoneButton();
    }

    /**
     * Sets up the save button for when the setup is complete.
     */
    private void setupAllDoneButton() {
        Button allDoneButton = (Button) getView().findViewById(R.id.trello_setup_all_done_button);

        allDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todoList == null || doingList == null || doneList == null) {
                    Toast.makeText(getActivity(), "Please make sure you selected lists for todo, doing and done.", Toast.LENGTH_LONG).show();
                } else {
                    SharedPrefsUtil.savePreferenceString(AppConstants.TODO_LIST_KEY, todoList.getId(), getActivity());
                    SharedPrefsUtil.savePreferenceString(AppConstants.DOING_LIST_KEY, doingList.getId(), getActivity());
                    SharedPrefsUtil.savePreferenceString(AppConstants.DONE_LIST_KEY, doneList.getId(), getActivity());
                    SharedPrefsUtil.savePreferenceBoolean(AppConstants.IS_APP_SETUP_KEY, true, getActivity());

                    contract.showTaskStatusActivity();
                }
            }
        });
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
            new LoadBoardsTask(boardNamesAdapter, getFragmentManager()).execute(TrelloConstants.TRELLO_APP_KEY,
                    SharedPrefsUtil.loadPreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getActivity()));
        }

        boardsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Board selectedBoard = (Board) boardNamesAdapter.getItem(position);
                if (resultsManager.hasLists(selectedBoard.getId())) {
                    List<TrelloList> taskList = resultsManager.getTrelloList(selectedBoard.getId());
                    trelloTasksLoaded(taskList);
                } else {
                    new LoadTaskListsTask(new LoadTrelloListsCallback() {
                        @Override
                        public void onTaskListsLoaded(List<TrelloList> trelloLists) {
                            trelloTasksLoaded(trelloLists);
                        }
                    }, getFragmentManager()).execute(selectedBoard.getId(),
                            TrelloConstants.TRELLO_APP_KEY, SharedPrefsUtil.loadPreferenceString(TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY,
                                    getActivity()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no implementation needed
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

        todoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todoList = (TrelloList) todoListsAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no implementation needed
            }
        });

        doingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doingList = (TrelloList) doingListsAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no implementation needed
            }
        });

        doneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doneList = (TrelloList) doneListsAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no implementation needed
            }
        });
    }

    private void trelloTasksLoaded(List<TrelloList> trelloLists) {
        todoListsAdapter.replaceItems(trelloLists);
        doingListsAdapter.replaceItems(trelloLists);
        doneListsAdapter.replaceItems(trelloLists);

        todoList = trelloLists.get(0);
        doingList = trelloLists.get(0);
        doneList = trelloLists.get(0);
    }

    /**
     * AsyncTask for loading the Trello open boards in the spinner.
     */
    private static class LoadBoardsTask extends AsyncTask<String, Void, List<Board>> {
        /**
         * Adapter containing the board names.
         */
        private SetupSpinnerAdapter boardNamesAdapter;

        /**
         * The fragment manager for displaying error dialogs.
         */
        private FragmentManager fragmentManager;

        /**
         * If Retrofit throws an error during the call, we should be able to handle it.
         */
        private RetrofitError retrofitError;

        public LoadBoardsTask(SetupSpinnerAdapter boardNamesAdapter, FragmentManager fragmentManager) {
            this.boardNamesAdapter = boardNamesAdapter;
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected List<Board> doInBackground(String... params) {
            List<Board> boards = null;

            try {
                TrelloApiService service = TrelloServiceFactory.createService();
                MemberToken token = service.getMemberToken(params[1], params[0]);
                boards = service.loadOpenBoards(token.getIdMember(), params[0], params[1]);
            } catch (RetrofitError error) {
                retrofitError = error;
            }

            return boards;
        }

        @Override
        protected void onPostExecute(List<Board> boards) {
            if (retrofitError != null) {
                NetworkUtil.showNetworkExceptionDialog(fragmentManager, retrofitError);
            } else {
                if (boards != null) {
                    TrelloResultsManager.getInstance().putTrelloBoards(boards);
                    boardNamesAdapter.addItems(boards);
                }
            }
        }
    }

    /**
     * AsyncTask for loading the Trello task lists that belong to a given board.
     */
    private static class LoadTaskListsTask extends AsyncTask<String, Void, List<TrelloList>> {
        /**
         * The callback for when the lists get loaded.
         */
        private LoadTrelloListsCallback callback;

        /**
         * The fragment manager for displaying dialogues.
         */
        private FragmentManager fragmentManager;

        /**
         * Store the error in case Retrofit throws something.
         */
        private RetrofitError retrofitError;

        /**
         * The board id for which we want to load the lists.
         */
        private String boardId;

        public LoadTaskListsTask(LoadTrelloListsCallback callback, FragmentManager fragmentManager) {
            this.callback = callback;
            this.fragmentManager = fragmentManager;
        }

        @Override
        protected List<TrelloList> doInBackground(String... params) {
            boardId = params[0];
            List<TrelloList> trelloLists = null;

            try {
                TrelloApiService service = TrelloServiceFactory.createService();
                trelloLists = service.loadTaskListsForBoard(boardId, params[1], params[2]);
            } catch (RetrofitError error) {
                retrofitError = error;
            }

            return trelloLists;
        }

        @Override
        protected void onPostExecute(List<TrelloList> trelloLists) {
            if (retrofitError != null) {
                NetworkUtil.showNetworkExceptionDialog(fragmentManager, retrofitError);
            } else {
                if (trelloLists != null) {
                    TrelloResultsManager.getInstance().putTrelloLists(boardId, trelloLists);
                    callback.onTaskListsLoaded(trelloLists);
                }
            }
        }
    }
}
