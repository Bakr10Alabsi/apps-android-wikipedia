package org.wikipedia.feed.onboarding;

import org.wikipedia.feed.announcement.Announcement;
import org.wikipedia.feed.announcement.AnnouncementCard;
import org.wikipedia.settings.PrefsIoUtil;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public abstract class OnboardingCard extends AnnouncementCard {
    public OnboardingCard(@NonNull Announcement announcement) {
        super(announcement);
    }

    @StringRes public abstract int prefKey();

    public boolean shouldShow() {
        return PrefsIoUtil.getBoolean(prefKey(), true);
    }

    @Override public void onDismiss() {
        PrefsIoUtil.setBoolean(prefKey(), false);
    }

    @Override public void onRestore() {
        PrefsIoUtil.setBoolean(prefKey(), true);
    }
}
