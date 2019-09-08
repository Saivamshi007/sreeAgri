package com.MithraAandS.projectsa2;



import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class FragmentKey {

    @Exclude
    public String FragmentKey;


    public <T extends FragmentKey> T withkey(@NonNull final String id) {
        this.FragmentKey = id;

        return (T) this;
    }

}

