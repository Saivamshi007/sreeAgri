package com.MithraAandS.projectsa2;



import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class BlogPostId {

    @Exclude
    public String BlogPostId;
    public String fragkey;


    public <T extends BlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;

        return (T) this;
    }
    public <F extends BlogPostId> F withkey(@NonNull final String key) {
        this.fragkey = key;

        return (F) this;
    }



}
