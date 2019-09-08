package com.MithraAandS.projectsa2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView OrderRecycle;
    private List<OrdersSetter> Order_List;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private OrderRecyclerAdapter orderRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Order_List=new ArrayList<>();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        orderRecyclerAdapter=new OrderRecyclerAdapter(Order_List);
        OrderRecycle=(RecyclerView)findViewById(R.id.orderRecycleView);
        OrderRecycle.setLayoutManager(new LinearLayoutManager(this));
        OrderRecycle.setAdapter(orderRecyclerAdapter);


        String CurrentUser=firebaseAuth.getCurrentUser().getUid();

        Query firstQuery = firebaseFirestore.collection("SreeOrders").document("AllOrders").collection(CurrentUser).orderBy("adresstimestamp", Query.Direction.DESCENDING);

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {


                        String blogPostId = doc.getDocument().getId();
                        OrdersSetter ordersSetter=doc.getDocument().toObject(OrdersSetter.class).withId(blogPostId);
                        Order_List.add(ordersSetter);
                        orderRecyclerAdapter.notifyDataSetChanged();

                    }

                }

            }
        });


    }
}
