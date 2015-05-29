package com.androidsensei.soladroid.pomodoro.tasks.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidsensei.soladroid.R;
import com.androidsensei.soladroid.trello.api.model.Card;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter that holds the data model to be displayed by the Trello task card recycler view.
 *
 * Created by mihai on 5/29/15.
 */
public class TrelloTaskCardsAdapter extends RecyclerView.Adapter<TrelloTaskCardsAdapter.ViewHolder> {
    /**
     * The list of Trello task cards that we want to display.
     */
    private List<Card> trelloCards;

    /**
     * Creates an instance of this adapter, making sure that it is properly initialized.
     */
    public TrelloTaskCardsAdapter() {
        super();
        trelloCards = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_status_screen_trello_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.cardName.setText(trelloCards.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trelloCards.size();
    }

    /**
     * Replaces the current items with the given list of Trello cards in this adapter.
     *
     * @param newTrelloCards the new Trello cards to display.
     */
    public void replaceItems(List<Card> newTrelloCards) {
        trelloCards.clear();
        trelloCards.addAll(newTrelloCards);
        notifyDataSetChanged();
    }

    /**
     * View holder class for caching recycler view item views.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;

        public ViewHolder(View card) {
            super(card);
            cardName = (TextView) card.findViewById(R.id.task_status_trello_card_name);
        }
    }

}
