package com.MithraAandS.projectsa2;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list,fraglist;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    private ProgressBar vProgrss;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        Log.i("IsRefresh", "Yes");
    }

    @Override
    public void onStart() {
        super.onStart();
        blogRecyclerAdapter.notifyDataSetChanged();
       // blogRecyclerAdapter.notify();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        blog_list = new ArrayList<>();
        fraglist = new ArrayList<>();

        blog_list_view = view.findViewById(R.id.InorganicListView);
        vProgrss=view.findViewById(R.id.progressbar);
        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list,fraglist);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(blogRecyclerAdapter);

        if (firebaseAuth.getCurrentUser() != null) {


            firebaseFirestore = FirebaseFirestore.getInstance();


          /*  blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {

                        loadMorePost();

                    }

                }
            });
*/

            Query firstQuery = firebaseFirestore.collection("Posts").document("In-organic").collection("Product").orderBy("timestamp", Query.Direction.DESCENDING);


            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (isFirstPageFirstLoad) {

                        try {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            blog_list.clear();
                        }catch (Exception liste){
                           // Toast.makeText(getActivity(), "No Post To Show", Toast.LENGTH_SHORT).show();

                        }
                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();
                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            BlogPost frag = doc.getDocument().toObject(BlogPost.class).withkey("Noti");
                            if (isFirstPageFirstLoad) {
                                blog_list.add(blogPost);
                                fraglist.add(frag);
                            }else {
                                blog_list.add(0,blogPost);
                                fraglist.add(frag);
                            }
                            blogRecyclerAdapter.notifyDataSetChanged();


                        }

                    }

                    isFirstPageFirstLoad=false;
                }
            });
        }
        blogRecyclerAdapter.notifyDataSetChanged();
        vProgrss.setVisibility(View.GONE);



        return view;
    }


  /*  public void loadMorePost(){
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()!=null) {

            Query nextQuery = firebaseFirestore.collection("Posts").document("In-organic").collection("Product").orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastVisible).limit(3);


            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        try {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        }catch (Exception liste){
                        //    Toast.makeText(getActivity(), "No Post To Show", Toast.LENGTH_SHORT).show();

                        }
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                BlogPost frag = doc.getDocument().toObject(BlogPost.class).withkey("Noti");
                                fraglist.add(frag);
                                blog_list.add(blogPost);
                                blogRecyclerAdapter.notifyDataSetChanged();



                            }
                        }

                    }
                }
            });
        }



    }
*/

}
