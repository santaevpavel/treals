package ru.nsu.fit.nsuschedule.model;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Pavel on 30.09.2016.
 */
public class GroupSuggestion implements SearchSuggestion {

    private Group group;
    private boolean mIsHistory = false;

    public GroupSuggestion(Group suggestion) {
        group = suggestion;
    }

    public GroupSuggestion(Parcel source) {
        this.group = new Group(source.readString(), source.readString());
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return group.getName();
    }

    public static final Creator<GroupSuggestion> CREATOR = new Creator<GroupSuggestion>() {
        @Override
        public GroupSuggestion createFromParcel(Parcel in) {
            return new GroupSuggestion(in);
        }

        @Override
        public GroupSuggestion[] newArray(int size) {
            return new GroupSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group.getId());
        dest.writeString(group.getName());
        dest.writeInt(mIsHistory ? 1 : 0);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean ismIsHistory() {
        return mIsHistory;
    }

    public void setmIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }
}