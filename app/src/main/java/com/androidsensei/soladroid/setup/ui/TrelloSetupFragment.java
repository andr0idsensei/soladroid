package com.androidsensei.soladroid.setup.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidsensei.soladroid.R;
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

        new LoadBoardsTask().execute(TrelloConstants.TRELLO_APP_KEY, SharedPrefsUtil.loadPreferenceString(
                TrelloConstants.TRELLO_APP_AUTH_TOKEN_KEY, getActivity()));
    }

    private static class LoadBoardsTask extends AsyncTask<String, Void, List<Board>> {

        @Override
        protected List<Board> doInBackground(String... params) {
            Log.d("r1k0", "doInBackground params: " + params[0] + " - " + params[1]);
            //todo remove logging - move this in a service - use some notification system
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
        }
    }
}
