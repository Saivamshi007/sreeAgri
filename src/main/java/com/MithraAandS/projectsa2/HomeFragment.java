package com.MithraAandS.projectsa2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
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

import static com.MithraAandS.projectsa2.R.drawable.comingsoon;
import static com.MithraAandS.projectsa2.R.drawable.flowers;


public class HomeFragment extends Fragment {
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list,fraglist;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    private Boolean flag=false;
    Handler mHandler;
    private ProgressBar vProgress;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        Log.i("IsRefresh", "Yes");
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        blogRecyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //blogRecyclerAdapter.startListening();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        blog_list = new ArrayList<>();
        fraglist = new ArrayList<>();

        blog_list_view = view.findViewById(R.id.blog_listview);
        vProgress=view.findViewById(R.id.progressbar);
        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list,fraglist);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(blogRecyclerAdapter);


        if (firebaseAuth.getCurrentUser() != null) {


            firebaseFirestore = FirebaseFirestore.getInstance();

/*

            blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

            Query firstQuery = firebaseFirestore.collection("Posts").document("organic").collection("Product").orderBy("timestamp", Query.Direction.DESCENDING);
            showLoader(getActivity());

            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (isFirstPageFirstLoad) {

                        try {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            blog_list.clear();
                        }catch (Exception liste){
                           // Toast.makeText(getActivity(), "No Post To Show", Toast.LENGTH_SHORT).show();
                            blog_list_view.setBackgroundResource(comingsoon);
                            flag=true;

                        }
                    }

                    try {



                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();
                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            BlogPost frag = doc.getDocument().toObject(BlogPost.class).withkey("Home");
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
                    }catch (Exception al){

                    }


                    isFirstPageFirstLoad=false;
                }
            });
        }

        if (flag==true){

            blog_list_view.setBackgroundResource(comingsoon);
        }


        if (!(blogRecyclerAdapter ==null)){

            hideSpinner();

        }

        blogRecyclerAdapter.notifyDataSetChanged();
//        blogRecyclerAdapter.notify();

        vProgress.setVisibility(View.GONE);



        return view;

        }


   /* public void loadMorePost(){
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()!=null) {

            Query nextQuery = firebaseFirestore.collection("Posts").document("organic").collection("Product").orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastVisible).limit(3);


            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        try {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        }catch (Exception liste){
                            Toast.makeText(getActivity(), "No Post To Show", Toast.LENGTH_SHORT).show();

                        }
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                BlogPost frag = doc.getDocument().toObject(BlogPost.class).withkey("Home");
                                fraglist.add(frag);
                                blog_list.add(blogPost);
                                blogRecyclerAdapter.notifyDataSetChanged();



                            }
                        }

                    }
                }
            });
        }



    }*/
    protected Dialog loadDialog = null;

    protected void showLoader(Context context) {
        if (loadDialog != null) {
            if (loadDialog.isShowing()) {
                if (!((Activity) context).isFinishing()) {
                    loadDialog.dismiss();
                }
            }
        }
        loadDialog = new Dialog(context, R.style.TransparentDialogTheme);
        loadDialog.setContentView(R.layout.rotatespinner);
        loadDialog.setCanceledOnTouchOutside(false);

        loadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });


        if (loadDialog != null && !((Activity) context).isFinishing() && !loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    void hideSpinner() {
        if (loadDialog.isShowing()) {
            if (!(getActivity().isFinishing())) {
                loadDialog.dismiss();
            }

        }

    }

    private void setupProgress() {
        vProgress.setVisibility(View.VISIBLE);
        blogRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                vProgress.setVisibility(View.GONE);
                blogRecyclerAdapter.unregisterAdapterDataObserver(this);
            }
        });
    }


}
