package org.wikipedia.feed.mostread;

import android.content.Context;
import android.content.Intent;

import org.wikipedia.activity.SingleFragmentActivity;
import org.wikipedia.json.GsonMarshaller;
import org.wikipedia.json.GsonUnmarshaller;

import androidx.annotation.NonNull;

public class MostReadArticlesActivity extends SingleFragmentActivity<MostReadFragment> {
    protected static final String MOST_READ_CARD = "item";

    public static Intent newIntent(@NonNull Context context, @NonNull MostReadListCard card) {
        return new Intent(context, MostReadArticlesActivity.class)
                .putExtra(MOST_READ_CARD, GsonMarshaller.marshal(card));
    }

    @Override
    public MostReadFragment createFragment() {
        return MostReadFragment.newInstance(GsonUnmarshaller.unmarshal(MostReadItemCard.class, getIntent().getStringExtra(MOST_READ_CARD)));
    }
}
