package com.androidsensei.soladroid.setup.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.trello.api.TrelloResultsManager;
import com.androidsensei.soladroid.trello.api.model.Board;
import com.androidsensei.soladroid.trello.api.model.MemberToken;
import com.androidsensei.soladroid.trello.api.TrelloService;
import com.androidsensei.soladroid.utils.SharedPrefsUtil;
import com.androidsensei.soladroid.utils.SolaDroidBaseFragment;
import com.androidsensei.soladroid.utils.trello.TrelloConstants;

import java.util.List;

import retrofit.RestAdapter;

/**
 * The setup fragment will display the user's Trello boards and will allow her to choose which board to use, as well
 * as match lists in the board to the to do, doing, done lists in the application.
 */
public class TrelloSetupFragment extends SolaDroidBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trello_setup, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupBoardsSpinner();
    }

    /**
     * Sets up the boards spinner view.
     */
    private void setupBoardsSpinner() {
        Spinner boardsSpinner = (Spinner) getView().findViewById(R.id.trello_setup_boards_section_spinner);
        SetupSpinnerAdapter boardNamesAdapter = new SetupSpinnerAdapter(getActivity().getLayoutInflater());
        boardsSpinner.setAdapter(boardNamesAdapter);

        TrelloResultsManager resultsManager = TrelloResultsManager.getInstance();
        if (resultsManager.hasBoards()) {
            boardNamesAdapter.addItems(resultsManager.getTrelloBoards());
        } else {
            new LoadBoardsTask(boardNamesAdapter).execute(TrelloConstants.TRELLO_APP_KEY, SharedPrefsUtil.loadPreferenceString(
                    TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getActivity()));
        }
    }

    private void setupTaskLists() {
        Spinner todoSpinner = (Spinner) getView().findViewById(R.id.trello_setup_todo_section_spinner);
        Spinner doingSpinner = (Spinner) getView().findViewById(R.id.trello_setup_doing_section_spinner);
        Spinner doneSpinner = (Spinner) getView().findViewById(R.id.trello_setup_done_section_spinner);

    }

    private static class LoadBoardsTask extends AsyncTask<String, Void, List<Board>> {
        private SetupSpinnerAdapter boardNamesAdapter;

        public LoadBoardsTask(SetupSpinnerAdapter boardNamesAdapter) {
            this.boardNamesAdapter = boardNamesAdapter;
        }

        @Override
        protected List<Board> doInBackground(String... params) {
            Log.d("r1k0", "doInBackground params: " + params[0] + " - " + params[1]);
            //todo remove logging - maybe move this in a service - use some notification system
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(TrelloService.BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            TrelloService service = restAdapter.create(TrelloService.class);
            MemberToken token = service.getMemberToken(params[1], params[0]);
            Log.d("r1k0", "token: " + token);

            List<Board> boards = service.loadOpenBoards(token.getIdMember(), params[0], params[1]);

            return boards;
        }

        @Override
        protected void onPostExecute(List<Board> boards) {
            Log.d("r1k0", "trello boards: " + boards);
            if (boards != null) {
                boardNamesAdapter.addItems(boards);
            }
        }
    }
}
